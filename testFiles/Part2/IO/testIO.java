package testFiles.part2.IO;
import assembler.Assembler;
import memory.Cache;
import memory.CPU;
import memory.simple.Memory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class testIO {

    private Memory rawMem;
    private Cache cache;
    private CPU cpu;

    private void setup() {
        rawMem = new Memory();
        cache  = new Cache(rawMem);
        cpu    = new CPU(cache);
    }

    private void loadProgram(String asmFile) throws Exception {
        Assembler.main(new String[]{ asmFile });

        BufferedReader reader = new BufferedReader(new FileReader("load.txt"));
        int firstAddr = -1;
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith(";")) continue;

            String[] parts = line.split("\\s+");
            if (parts.length < 2) continue;

            int addr = Integer.parseInt(parts[0], 8);
            int val  = Integer.parseInt(parts[1], 8) & 0xFFFF;
            cache.writeWord(addr, val);

            if (firstAddr == -1) firstAddr = addr;
        }
        reader.close();

        if (firstAddr != -1) cpu.setPC(firstAddr);
    }

    private void runUntilHalt() {
        while (!cpu.isHalted()) {
            cpu.cycle();
        }
    }

    private void assertEqual(String testName, int expected, int actual) {
        if (expected == actual) {
            System.out.println("PASS: " + testName);
        } else {
            System.out.println("FAIL: " + testName + " expected=" + expected + " actual=" + actual);
        }
    }

    private void testKeyboardEcho() throws Exception {
        setup();
        rawMem.getDevice().loadKeyboardInput("XA"); // GPR[0] gets 'X', GPR[1] gets 'A'
        loadProgram("test_io.asm");
        runUntilHalt();
        assertEqual("testKeyboardEcho GPR[1]", 65, cpu.getGPR(1));
    }

    private void testTwoKeyboardChars() throws Exception {
        setup();
        rawMem.getDevice().loadKeyboardInput("AB");
        loadProgram("test_io.asm");
        runUntilHalt();
        assertEqual("testTwoKeyboardChars GPR[0]", 65, cpu.getGPR(0));
        assertEqual("testTwoKeyboardChars GPR[1]", 66, cpu.getGPR(1));
    }

    private void testEmptyKeyboardBuffer() throws Exception {
        setup();
        // no input loaded — should return 0
        loadProgram("test_io.asm");
        runUntilHalt();
        assertEqual("testEmptyKeyboardBuffer GPR[1]", 0, cpu.getGPR(1));
    }

    public static void main(String[] args) throws Exception {
        testIO t = new testIO();

        System.out.println("=== TestIO ===");
        t.testKeyboardEcho();
        t.testTwoKeyboardChars();
        t.testEmptyKeyboardBuffer();
        System.out.println("=== Done ===");
    }
}