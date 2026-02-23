package memory;
import memory.simple.Memory;

/**
 * Executor Class: executes a subset of instructions on CPU+Memory state.
 * This is where we implement the core logic of load/store instructions.
 * 
 * For Part 1: Load/Store only (LDR/STR/LDA/LDX/STX).
 * 

 */

// TODO: This version of the exectuor uses Memory.peek and is not tied to 2 cycle memory yet

public final class Executor {
    private Executor() {}

    /**
     * Compute effective address for formats that use (x, i, addr).
     * Mirrors CPU EA logic and assembler's 5-bit addr field.
     *
     * If machine memory is 2048 words, consider masking EA with 0x7FF.
     */
    public static int computeEA(Decoder.Decoded decoded, CPU cpu, Memory mem) {
        int ea = decoded.addr; // 5-bit address field (0..31)

        // indexing: X0 means no indexing
        if (decoded.x != 0) {
            ea = (ea + (cpu.getIX(decoded.x) & 0xFFFF)) & 0xFFFF;
        }

        // If you want EA constrained to 2048 memory locations:
        // ea &= 0x7FF;

        // indirect: EA <- M[EA]
        if (decoded.i == 1) {
            ea = mem.peek(ea);
            // ea &= 0x7FF; // optional clamp again
        }

        return ea;
    }

    /**
     * Execute load/store instructions using already computed EA
     */
    public static void execLoadStore(Decoder.Decoded decoded, int ea, CPU cpu, Memory mem) {
        // uses the decoded instruction to determine which load/store to execute
        switch (decoded.ins) {
            case LDR -> {
                int value = mem.peek(ea);
                cpu.setGPR(decoded.r, value);
            }

            case STR -> {
                int value = cpu.getGPR(decoded.r);
                mem.directWrite(ea, value);
            }

            case LDA -> {
                cpu.setGPR(decoded.r, ea);
            }

            case LDX -> {
                if (decoded.x == 0) {
                    throw new MachineFault(
                            MachineFault.Code.ILLEGAL_OPCODE,
                            decoded.raw,
                            "LDX requires x != 0"
                    );
                }
                int value = mem.peek(ea);
                cpu.setIX(decoded.x, value);
            }

            case STX -> {
                if (decoded.x == 0) {
                    throw new MachineFault(MachineFault.Code.ILLEGAL_OPCODE, decoded.raw, "STX requires x != 0"
                    );
                }
                int value = cpu.getIX(decoded.x);
                mem.directWrite(ea, value);
            }

            default -> throw new IllegalArgumentException("Not a load/store instruction: " + decoded.ins);
        }
    }

    // convenience method compute EA then execute.

    public static void execLoadStore(Decoder.Decoded decoded, CPU cpu, Memory mem) {
        int ea = computeEA(decoded, cpu, mem);
        execLoadStore(decoded, ea, cpu, mem);
    }
}
