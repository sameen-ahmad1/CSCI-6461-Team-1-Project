package memory;
import assembler.Isa;

/**
 * Executor: contains all instruction execution logic.
 *
 * CPU.handleExecute() calls Executor.execute() and applies the returned
 * ExecuteResult (next state + memoryCycles) to itself, keeping CPU.java
 * focused purely on the fetch/decode/execute cycle structure.
 *
 * Finish-state helpers (finishLoad, finishAMR, finishSMR) are called
 * directly from the CPU cycle switch once the memory read is complete.
 */
public final class Executor
{
    private Executor() {}

    // =========================================================================
    // CONSTANTS
    // =========================================================================
    private static final int MASK_12 = 0x7FF;
    private static final int MASK_16 = 0xFFFF;
   // private static StringBuilder logs = new StringBuilder();

    // CC bit positions (0-indexed from LSB)
    private static final int CC_OVERFLOW = 0;
    private static final int CC_DIVZERO  = 2;
    private static final int CC_EQUAL    = 3;

    // private static void addLog(String msg) 
    // {
    //     logs.insert(0, msg + "\n"); // Add new messages to the top
    // }

    
    // public String getLogs() 
    // {
    //     return logs.toString();
    // }
    // =========================================================================
    // EXECUTE RESULT
    // Returned by execute() so CPU can apply state changes without knowing
    // any instruction logic itself.
    // =========================================================================
    public static final class ExecuteResult
    {
        public final CPU.State nextState;
        public final int memoryCycles;

        public ExecuteResult(CPU.State nextState, int memoryCycles)
        {
            this.nextState    = nextState;
            this.memoryCycles = memoryCycles;
        }

        /** Convenience: no memory cycle needed, just advance to next state. */
        public static ExecuteResult done()
        {
            return new ExecuteResult(CPU.State.FETCH_1, 0);
        }

        /** Convenience: one memory cycle then move to a finish state. */
        public static ExecuteResult memRead(CPU.State finishState)
        {
            return new ExecuteResult(finishState, 1);
        }

        /** Convenience: one memory cycle then return to FETCH_1 (used by stores). */
        public static ExecuteResult memWrite()
        {
            return new ExecuteResult(CPU.State.FETCH_1, 1);
        }

        /** Convenience: fault - halt the machine. */
        public static ExecuteResult fault()
        {
            return new ExecuteResult(CPU.State.HALT, 0);
        }
    }

    // =========================================================================
    // EFFECTIVE ADDRESS
    // =========================================================================

    /**
     * Returns true for instructions whose EA calculation uses index registers.
     * LDX/STX and register-register ops do not index.
     */
    private static boolean indexAllowed(Isa.Instruction ins)
    {
        switch (ins)
        {
            case LDR: case STR: case LDA:
            case AMR: case SMR:
            case JZ:  case JNE: case JCC:
            case JMA: case JSR: case SOB: case JGE:
                return true;
            default:
                return false;
        }
    }

    public static int calculateEA(Decoder.Decoded decoded, CPU cpu)
    {
        int ea = decoded.addr & 0x1F;   // raw 5-bit address field

        if (indexAllowed(decoded.ins))
        {
            int x = decoded.x & 0x03;
            if (x > 0 && x < 4)
            {
                ea += cpu.getIX(x);
            }
        }

        return ea & MASK_12;
    }

    // =========================================================================
    // MAIN EXECUTE DISPATCH
    // Called from CPU.handleExecute(). Returns an ExecuteResult describing
    // what state and memory timing the CPU should apply next.
    // =========================================================================
    public static ExecuteResult execute(Decoder.Decoded decoded,
                                        CPU cpu,
                                        int effectiveAddress,
                                        MemoryBus memory)
    {
        int mar = effectiveAddress & MASK_12;

        switch (decoded.ins)
        {
            // -----------------------------------------------------------------
            // LOAD / STORE
            // -----------------------------------------------------------------
            case LDR:
                memory.requestRead(mar);
                return ExecuteResult.memRead(CPU.State.LDR_FINISH);

            case LDX:
                if (decoded.x == 0)
                {
                    cpu.setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
                    return ExecuteResult.fault();
                }
                memory.requestRead(mar);
                return ExecuteResult.memRead(CPU.State.LDX_FINISH);

            case STR:
                cpu.setMBR(cpu.getGPR(decoded.r));
                memory.requestWrite(mar);
                return ExecuteResult.memWrite();

            case STX:
                if (decoded.x == 0)
                {
                    cpu.setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
                    return ExecuteResult.fault();
                }
                cpu.setMBR(cpu.getIX(decoded.x));
                memory.requestWrite(mar);
                return ExecuteResult.memWrite();

            case LDA:
                cpu.setGPR(decoded.r, effectiveAddress);
                return ExecuteResult.done();

            // -----------------------------------------------------------------
            // ARITHMETIC – MEMORY OPERAND
            // Both need a read cycle; finish state does the arithmetic once
            // MBR is populated.
            // -----------------------------------------------------------------
            case AMR:
                memory.requestRead(mar);
                return ExecuteResult.memRead(CPU.State.AMR_FINISH);

            case SMR:
                memory.requestRead(mar);
                return ExecuteResult.memRead(CPU.State.SMR_FINISH);

            // -----------------------------------------------------------------
            // ARITHMETIC – IMMEDIATE
            // IX and I fields are ignored per ISA spec.
            // decoded.addr holds the raw 5-bit immediate.
            // -----------------------------------------------------------------
            case AIR:
                return executeAIR(decoded, cpu);

            case SIR:
                return executeSIR(decoded, cpu);

            // -----------------------------------------------------------------
            // BRANCH / CONTROL
            // -----------------------------------------------------------------
            case JZ:
                if (toSigned16(cpu.getGPR(decoded.r)) == 0)
                {
                    cpu.setPC(effectiveAddress);
                }
                return ExecuteResult.done();

            case JNE:
                if (toSigned16(cpu.getGPR(decoded.r)) != 0)
                {
                    cpu.setPC(effectiveAddress);
                }
                return ExecuteResult.done();

            case JCC:
                return executeJCC(decoded, cpu, effectiveAddress);

            case JMA:
                // r field is ignored per ISA
                cpu.setPC(effectiveAddress);
                return ExecuteResult.done();

            case JSR:
                // PC was already incremented during FETCH_2, so it holds PC+1
                cpu.setGPR(3, cpu.getPC());
                cpu.setPC(effectiveAddress);
                return ExecuteResult.done();

            case RFS:
                // R0 ← Immed (addr field); PC ← c(R3). IX and I ignored.
                cpu.setGPR(0, decoded.addr & MASK_16);
                cpu.setPC(cpu.getGPR(3));
                return ExecuteResult.done();

            case SOB:
                return executeSOB(decoded, cpu, effectiveAddress);

            case JGE:
                if (toSigned16(cpu.getGPR(decoded.r)) >= 0)
                {
                    cpu.setPC(effectiveAddress);
                }
                return ExecuteResult.done();

            // -----------------------------------------------------------------
            // REGISTER-REGISTER
            // rx = decoded.r (bits 9-8), ry = decoded.x (bits 7-6)
            // -----------------------------------------------------------------
            case MLT:
                return executeMLT(decoded, cpu);

            case DVD:
                return executeDVD(decoded, cpu);

            case TRR:
                boolean equal = (cpu.getGPR(decoded.r) & MASK_16)
                             == (cpu.getGPR(decoded.x) & MASK_16);
                setCCBit(cpu, CC_EQUAL, equal);
                return ExecuteResult.done();

            case AND:
                cpu.setGPR(decoded.r,
                    (cpu.getGPR(decoded.r) & cpu.getGPR(decoded.x)) & MASK_16);
                return ExecuteResult.done();

            case ORR:
                cpu.setGPR(decoded.r,
                    (cpu.getGPR(decoded.r) | cpu.getGPR(decoded.x)) & MASK_16);
                return ExecuteResult.done();

            case NOT:
                cpu.setGPR(decoded.r, (~cpu.getGPR(decoded.r)) & MASK_16);
                return ExecuteResult.done();

            // -----------------------------------------------------------------
            // SHIFT / ROTATE
            // Decoder already extracted decoded.al, decoded.lr, decoded.count
            // -----------------------------------------------------------------
            case SRC:
                return executeSRC(decoded, cpu);

            case RRC:
                return executeRRC(decoded, cpu);

            // -----------------------------------------------------------------
            // I/O  (devid carried in decoded.addr)
            // -----------------------------------------------------------------
            // case IN:
            //     cpu.setGPR(decoded.r, memory.inputDevice(decoded.addr) & MASK_16);
            //     return ExecuteResult.done();

            // case OUT:
            //     memory.outputDevice(decoded.addr, cpu.getGPR(decoded.r) & MASK_16);
            //     return ExecuteResult.done();

            // -----------------------------------------------------------------
            // Unimplemented / illegal
            // -----------------------------------------------------------------
            default:
                System.out.printf("FAULT: Unhandled instruction %s (Opcode: %o) Raw: %06o\n",
                        decoded.ins, decoded.opcode, decoded.raw);
                String error3 = String.format("FAULT: Unhandled instruction %s Opcode: %o Raw: %06o\n", decoded.ins, decoded.opcode, decoded.raw);
                memory.postError(error3);
                cpu.setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
                return ExecuteResult.fault();
        }
    }

    // =========================================================================
    // FINISH-STATE HELPERS
    // Called from CPU.cycle() once the memory read cycle is complete.
    // =========================================================================

    /** LDR / LDX: write MBR into the correct register once memory delivers data. */
    public static void finishLoad(Decoder.Decoded decoded, CPU cpu)
    {
        int value = cpu.getMBR();

        if (decoded.ins == Isa.Instruction.LDR)
        {
            System.out.printf("DEBUG: finishLoad LDR - MBR is %06o, writing to GPR[%d]\n",
                    value, decoded.r);
            //addLog(String.format("DEBUG: finishLoad LDR - MBR is %06o, writing to GPR[%d]\n", value, decoded.r));
            cpu.setGPR(decoded.r, value);
        }
        else if (decoded.ins == Isa.Instruction.LDX)
        {
            System.out.printf("DEBUG: finishLoad LDX - MBR is %06o, writing to IX[%d]\n",
                    value, decoded.x);
            //addLog(String.format("DEBUG: finishLoad LDX - MBR is %06o, writing to IX[%d]\n",value, decoded.x));
            cpu.setIX(decoded.x, value);
        }
    }

    /** AMR: r ← c(r) + c(EA)  (MBR now holds c(EA)) */
    public static void finishAMR(Decoder.Decoded decoded, CPU cpu)
    {
        int a      = toSigned16(cpu.getGPR(decoded.r));
        int b      = toSigned16(cpu.getMBR());
        int result = a + b;
        setOverflowIfNeeded(cpu, result);
        cpu.setGPR(decoded.r, result & MASK_16);
    }

    /** SMR: r ← c(r) – c(EA)  (MBR now holds c(EA)) */
    public static void finishSMR(Decoder.Decoded decoded, CPU cpu)
    {
        int a      = toSigned16(cpu.getGPR(decoded.r));
        int b      = toSigned16(cpu.getMBR());
        int result = a - b;
        setOverflowIfNeeded(cpu, result);
        cpu.setGPR(decoded.r, result & MASK_16);
    }

    // =========================================================================
    // PRIVATE INSTRUCTION HELPERS
    // =========================================================================

    private static ExecuteResult executeAIR(Decoder.Decoded decoded, CPU cpu)
    {
        int immed = decoded.addr;
        if (immed == 0)
        {
            return ExecuteResult.done();
        }

        int rVal = toSigned16(cpu.getGPR(decoded.r));
        int result;
        if (rVal == 0)
        {
            result = immed;
        }
        else
        {
            result = rVal + immed;
        }
        setOverflowIfNeeded(cpu, result);
        cpu.setGPR(decoded.r, result & MASK_16);
        return ExecuteResult.done();
    }

    private static ExecuteResult executeSIR(Decoder.Decoded decoded, CPU cpu)
    {
        int immed = decoded.addr;
        if (immed == 0)
        {
            return ExecuteResult.done();
        }

        int rVal = toSigned16(cpu.getGPR(decoded.r));
        int result;
        if (rVal == 0)
        {
            result = -immed;
        }
        else
        {
            result = rVal - immed;
        }
        setOverflowIfNeeded(cpu, result);
        cpu.setGPR(decoded.r, result & MASK_16);
        return ExecuteResult.done();
    }

    private static ExecuteResult executeJCC(Decoder.Decoded decoded,CPU cpu,int effectiveAddress)
    {
        // decoded.r carries the cc field (0..3)
        int cc = decoded.r;
        boolean bitSet = ((cpu.getCC() >> cc) & 1) == 1;
        if (bitSet)
        {
            cpu.setPC(effectiveAddress);
        }
        return ExecuteResult.done();
    }

    private static ExecuteResult executeSOB(Decoder.Decoded decoded,
                                             CPU cpu,
                                             int effectiveAddress)
    {
        int val = toSigned16(cpu.getGPR(decoded.r)) - 1;
        cpu.setGPR(decoded.r, val & MASK_16);
        if (toSigned16(cpu.getGPR(decoded.r)) > 0)
        {
            cpu.setPC(effectiveAddress);
        }
        return ExecuteResult.done();
    }

    private static ExecuteResult executeMLT(Decoder.Decoded decoded, CPU cpu)
    {
        int rx = decoded.r;
        int ry = decoded.x;

        if ((rx != 0 && rx != 2) || (ry != 0 && ry != 2))
        {
            cpu.setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
            return ExecuteResult.fault();
        }

        long product = (long) toSigned16(cpu.getGPR(rx))
                     * (long) toSigned16(cpu.getGPR(ry));
        int hi = (int) ((product >> 16) & MASK_16);
        int lo = (int) (product & MASK_16);
        cpu.setGPR(rx,     hi);
        cpu.setGPR(rx + 1, lo);

        if (product > 32767L || product < -32768L)
        {
            setCCBit(cpu, CC_OVERFLOW, true);
        }
        return ExecuteResult.done();
    }

    private static ExecuteResult executeDVD(Decoder.Decoded decoded, CPU cpu)
    {
        int rx = decoded.r;
        int ry = decoded.x;

        if ((rx != 0 && rx != 2) || (ry != 0 && ry != 2))
        {
            cpu.setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
            return ExecuteResult.fault();
        }

        int divisor = toSigned16(cpu.getGPR(ry));
        if (divisor == 0)
        {
            setCCBit(cpu, CC_DIVZERO, true);
            return ExecuteResult.done();
        }

        int dividend  = toSigned16(cpu.getGPR(rx));
        int quotient  = dividend / divisor;
        int remainder = dividend % divisor;
        cpu.setGPR(rx,     quotient  & MASK_16);
        cpu.setGPR(rx + 1, remainder & MASK_16);
        return ExecuteResult.done();
    }

    private static ExecuteResult executeSRC(Decoder.Decoded decoded, CPU cpu)
    {
        int count   = decoded.count;
        boolean left    = (decoded.lr == 1);
        boolean logical = (decoded.al == 1);

        if (count == 0)
        {
            return ExecuteResult.done();
        }

        int val = cpu.getGPR(decoded.r) & MASK_16;
        int result;

        if (left)
        {
            // Left shift: logical and arithmetic are identical
            result = (val << count) & MASK_16;
        }
        else
        {
            if (logical)
            {
                result = (val >>> count) & MASK_16;
            }
            else
            {
                // Arithmetic right: preserve sign bit
                result = (toSigned16(val) >> count) & MASK_16;
            }
        }

        cpu.setGPR(decoded.r, result);
        return ExecuteResult.done();
    }

    private static ExecuteResult executeRRC(Decoder.Decoded decoded, CPU cpu)
    {
        int count = decoded.count % 16;  // rotating by 16 is a no-op
        boolean left = (decoded.lr == 1);

        if (count == 0)
        {
            return ExecuteResult.done();
        }

        int val = cpu.getGPR(decoded.r) & MASK_16;
        int result;

        if (left)
        {
            result = ((val << count) | (val >>> (16 - count))) & MASK_16;
        }
        else
        {
            result = ((val >>> count) | (val << (16 - count))) & MASK_16;
        }

        cpu.setGPR(decoded.r, result);
        return ExecuteResult.done();
    }

    // =========================================================================
    // SHARED ARITHMETIC HELPERS
    // =========================================================================

    /** Convert unsigned 16-bit value to signed Java int. */
    static int toSigned16(int val)
    {
        val &= MASK_16;
        if (val >= 0x8000)
        {
            return val - 0x10000;
        }
        else
        {
            return val;
        }
    }

    /** Set the OVERFLOW CC bit if result falls outside signed 16-bit range. */
    private static void setOverflowIfNeeded(CPU cpu, int result)
    {
        if (result > 32767 || result < -32768)
        {
            setCCBit(cpu, CC_OVERFLOW, true);
        }
    }

    /** Set or clear a specific CC bit on the CPU. */
    private static void setCCBit(CPU cpu, int bit, boolean value)
    {
        int cc = cpu.getCC();
        if (value)
        {
            cc |= (1 << bit);
        }
        else
        {
            cc &= ~(1 << bit);
        }
        cpu.setCC(cc);
    }
}
