import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RunLoadFileTest {
    // Default file 
    private static final String DEFAULT_LOAD_FILE = "load.txt";

    // Safety so it never runs forever
    private static final int DEFAULT_MAX_CYCLES = 3000;

    public static void main(String[] args) throws Exception {

        String loadName = DEFAULT_LOAD_FILE;
        int maxCycles = DEFAULT_MAX_CYCLES;
        DumpSpec dumpSpec = DumpSpec.auto();
        List<Expectation> expectations = new ArrayList<>();

        // Parse args
        if (args.length >= 1) {
            loadName = args[0];
        }

        for (int i = 1; i < args.length; i++) {
            String tok = args[i].trim();
            if (tok.isEmpty()) continue;

            if (tok.startsWith("max=")) {
                maxCycles = Integer.parseInt(tok.substring("max=".length()));
                continue;
            }

            if (tok.startsWith("dump=")) {
                dumpSpec = DumpSpec.parse(tok.substring("dump=".length()));
                continue;
            }

            // Expectations:
            //   mem:18=99   OR  18=99   (memory address)
            //   R0=5, R1=18, R2=99, R3=7
            //   IX1=3, IX2=3, IX3=0
            //   PC=17, MAR=16, MBR=..., MFR=0 (optional)
            expectations.add(Expectation.parse(tok));
        }

        // ----------------------------
        // 1) Load program into memory
        // ----------------------------
        Memory memory = new Memory();
        memory.reset();

        File loadFile = new File(loadName);
        RomLoader.Result res = RomLoader.loadIntoMemory(loadFile, memory);

        System.out.println("Loaded file: " + loadFile.getName());
        System.out.println("First address (decimal): " + res.firstAddress);
        System.out.println("Lines loaded: " + res.linesLoaded);
        System.out.println("--------------------------------------------------");

        // ----------------------------
        // 2) Create CPU and set PC
        // ----------------------------
        CPU cpu = new CPU(memory);

        // If CPU has no setPC(), we set it using reflection.
        forceSetPrivateIntField(cpu, "PC", res.firstAddress);

        // ----------------------------
        // 3) Run until HALT or maxCycles
        // ----------------------------
        int cyclesExecuted = 0;
        boolean halted = false;

        for (int cycle = 1; cycle <= maxCycles; cycle++) {
            cyclesExecuted = cycle;
            cpu.cycle();

            if (cpu.getMFR() != 0) {
                System.out.println("Stopped: Machine fault detected. MFR=" + cpu.getMFR());
                break;
            }

            if (isCpuHalted(cpu)) {
                halted = true;
                System.out.println("Stopped: HALT reached.");
                break;
            }

            if (cycle == maxCycles) {
                System.out.println("Stopped: Reached MAX_CYCLES (" + maxCycles + "). Possible infinite loop.");
            }
        }

        System.out.println("Cycles executed: " + cyclesExecuted);
        System.out.println("--------------------------------------------------");
        cpu.listRegisters();

        // ----------------------------
        // 4) Dump memory 
        // ----------------------------
        int dumpStart;
        int dumpEnd;

        if (dumpSpec.kind == DumpSpec.Kind.AUTO) {
            // Dump around loaded program + a little extra
            dumpStart = clamp(res.firstAddress - 8, 0, Memory.SIZE - 1);
            dumpEnd   = clamp(res.firstAddress + res.linesLoaded + 32, 0, Memory.SIZE - 1);
        } else if (dumpSpec.kind == DumpSpec.Kind.RANGE) {
            dumpStart = clamp(dumpSpec.start, 0, Memory.SIZE - 1);
            dumpEnd   = clamp(dumpSpec.end, 0, Memory.SIZE - 1);
        } else {
            // first..+N
            dumpStart = clamp(res.firstAddress, 0, Memory.SIZE - 1);
            dumpEnd   = clamp(res.firstAddress + dumpSpec.plusCount, 0, Memory.SIZE - 1);
        }

        System.out.println("============= MEMORY DUMP =============");
        System.out.println("Dump range (decimal): " + dumpStart + " .. " + dumpEnd);
        dumpMemory(memory, dumpStart, dumpEnd);
        System.out.println("======================================");

        // ----------------------------
        // 5) Evaluate expectations (PASS/FAIL)
        // ----------------------------
        if (!expectations.isEmpty()) {
            System.out.println("============= EXPECTED CHECKS =============");
            int pass = 0;
            int fail = 0;

            for (Expectation ex : expectations) {
                int actual = ex.readActual(cpu, memory);
                boolean ok = (actual == ex.expected);

                if (ok) pass++; else fail++;

                System.out.printf("%s  expected=%s actual=%s  => %s%n",
                        ex.label(),
                        formatValue(ex.expected),
                        formatValue(actual),
                        ok ? "PASS" : "FAIL");
            }

            System.out.println("-------------------------------------------");
            System.out.println("Summary: PASS=" + pass + " FAIL=" + fail
                    + (halted ? " (HALT reached)" : " (HALT not confirmed)"));
            System.out.println("===========================================");
        } else {
            System.out.println("No expected checks provided.");
            System.out.println("Tip: run like: java RunLoadFileTest " + loadName + " 18=99 R2=99 IX1=3");
        }
    }

    // -------- Helper: memory dump --------
    private static void dumpMemory(Memory memory, int start, int end) {
        for (int addr = start; addr <= end; addr++) {
            int val = memory.peek(addr) & 0xFFFF;
            // show decimal addr, octal addr, and word in dec/hex/oct
            System.out.printf("Mem[%4d | %06o] = %5d (dec) | %04X (hex) | %06o (oct)%n",
                    addr, addr, val, val, val);
        }
    }

    // -------- Helper: halt detection via reflection --------
    private static boolean isCpuHalted(CPU cpu) {
        try {
            Field f = CPU.class.getDeclaredField("curState");
            f.setAccessible(true);
            Object state = f.get(cpu);
            return state != null && state.toString().equals("HALT");
        } catch (Exception e) {
            // If we can't read it, we can't confirm HALT
            return false;
        }
    }

    // -------- Helper: reflection set private int field --------
    private static void forceSetPrivateIntField(Object obj, String fieldName, int value) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.setInt(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field '" + fieldName + "' via reflection: " + e.getMessage(), e);
        }
    }

    private static int clamp(int v, int lo, int hi) {
        if (v < lo) return lo;
        if (v > hi) return hi;
        return v;
    }

    private static String formatValue(int v) {
        int u16 = v & 0xFFFF;
        return u16 + " (dec) | " + String.format("%04X", u16) + " (hex) | " + String.format("%06o", u16) + " (oct)";
    }

    // =============================
    // Expectations
    // =============================
    private static final class Expectation {
        enum Kind { MEM, GPR, IX, PC, MAR, MBR, MFR }

        final Kind kind;
        final int index;      // register index or memory address
        final int expected;

        private Expectation(Kind kind, int index, int expected) {
            this.kind = kind;
            this.index = index;
            this.expected = expected & 0xFFFF;
        }

        static Expectation parse(String token) {
            // Accept:
            //  18=99           -> memory[18]=99
            //  mem:18=99       -> memory[18]=99
            //  R2=99           -> GPR[2]=99
            //  IX1=3           -> IX[1]=3
            //  PC=17, MAR=16, MBR=1234, MFR=0

            String t = token.trim();

            // memory explicit
            if (t.startsWith("mem:")) {
                String rest = t.substring(4);
                String[] parts = rest.split("=");
                if (parts.length != 2) throw new IllegalArgumentException("Bad expectation: " + token);
                int addr = Integer.parseInt(parts[0]);
                int val = Integer.parseInt(parts[1]);
                return new Expectation(Kind.MEM, addr, val);
            }

            // register expectations
            if (t.startsWith("R")) {
                String[] parts = t.split("=");
                if (parts.length != 2) throw new IllegalArgumentException("Bad expectation: " + token);
                int r = Integer.parseInt(parts[0].substring(1));
                int val = Integer.parseInt(parts[1]);
                return new Expectation(Kind.GPR, r, val);
            }

            if (t.startsWith("IX")) {
                String[] parts = t.split("=");
                if (parts.length != 2) throw new IllegalArgumentException("Bad expectation: " + token);
                int ix = Integer.parseInt(parts[0].substring(2));
                int val = Integer.parseInt(parts[1]);
                return new Expectation(Kind.IX, ix, val);
            }

            if (t.startsWith("PC=")) {
                int val = Integer.parseInt(t.substring(3));
                return new Expectation(Kind.PC, 0, val);
            }

            if (t.startsWith("MAR=")) {
                int val = Integer.parseInt(t.substring(4));
                return new Expectation(Kind.MAR, 0, val);
            }

            if (t.startsWith("MBR=")) {
                int val = Integer.parseInt(t.substring(4));
                return new Expectation(Kind.MBR, 0, val);
            }

            if (t.startsWith("MFR=")) {
                int val = Integer.parseInt(t.substring(4));
                return new Expectation(Kind.MFR, 0, val);
            }

            // default: addr=value means memory check
            if (t.contains("=")) {
                String[] parts = t.split("=");
                if (parts.length != 2) throw new IllegalArgumentException("Bad expectation: " + token);
                int addr = Integer.parseInt(parts[0]);
                int val = Integer.parseInt(parts[1]);
                return new Expectation(Kind.MEM, addr, val);
            }

            throw new IllegalArgumentException("Bad expectation token: " + token);
        }

        int readActual(CPU cpu, Memory memory) {
            return switch (kind) {
                case MEM -> memory.peek(index) & 0xFFFF;
                case GPR -> cpu.getGPR(index) & 0xFFFF;
                case IX  -> cpu.getIX(index) & 0xFFFF;
                case PC  -> cpu.getPC() & 0xFFFF;
                case MAR -> cpu.getMAR() & 0xFFFF;
                case MBR -> cpu.getMBR() & 0xFFFF;
                case MFR -> cpu.getMFR() & 0xFFFF;
            };
        }

        String label() {
            return switch (kind) {
                case MEM -> "mem[" + index + "]";
                case GPR -> "R" + index;
                case IX  -> "IX" + index;
                case PC  -> "PC";
                case MAR -> "MAR";
                case MBR -> "MBR";
                case MFR -> "MFR";
            };
        }
    }

    // =============================
    // Dump spec
    // =============================
    private static final class DumpSpec {
        enum Kind { AUTO, RANGE, FIRST_PLUS }

        final Kind kind;
        final int start;
        final int end;
        final int plusCount;

        private DumpSpec(Kind kind, int start, int end, int plusCount) {
            this.kind = kind;
            this.start = start;
            this.end = end;
            this.plusCount = plusCount;
        }

        static DumpSpec auto() {
            return new DumpSpec(Kind.AUTO, 0, 0, 0);
        }

        static DumpSpec parse(String s) {
            String t = s.trim().toLowerCase();

            if (t.equals("auto")) return auto();

            // "0..40"
            if (t.contains("..") && !t.startsWith("first")) {
                String[] parts = t.split("\\.\\.");
                if (parts.length != 2) throw new IllegalArgumentException("Bad dump spec: " + s);
                int a = Integer.parseInt(parts[0]);
                int b = Integer.parseInt(parts[1]);
                return new DumpSpec(Kind.RANGE, a, b, 0);
            }

            // "first..+64"
            if (t.startsWith("first..+")) {
                int n = Integer.parseInt(t.substring("first..+".length()));
                return new DumpSpec(Kind.FIRST_PLUS, 0, 0, n);
            }

            throw new IllegalArgumentException("Bad dump spec: " + s + " (try auto, 0..40, first..+64)");
        }
    }
}