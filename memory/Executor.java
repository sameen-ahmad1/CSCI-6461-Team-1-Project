package memory;
import assembler.Isa;

/**
 * Executor Class: executes a subset of instructions on CPU+Memory state.
 * This is where we implement the core logic of load/store instructions.
 * 
 * For Part 1: Load/Store only (LDR/STR/LDA/LDX/STX).
 * 

 */


public final class Executor 
{
    private Executor() {}

    //to determine if an instruction should use Index Registers
    private static boolean indexAllowed(Isa.Instruction ins) 
    {
        return ins == Isa.Instruction.LDR || 
               ins == Isa.Instruction.STR || 
               ins == Isa.Instruction.LDA;
    }
    public static int calculateEA(Decoder.Decoded decoded, CPU cpu) 
    {
        int ea = decoded.addr & 0x1F; // 5-bit address field
        // LDX/STX do NOT use indexing for the EA calculation
        if (indexAllowed(decoded.ins)) 
        {
            int x = decoded.x & 0x03;
            if (x > 0 && x < 4) 
            {
                ea += cpu.getIX(x);
            }
        }
        return ea & 0x7FF;
    }


    // for finishing a Load operation once MBR has been populated by Memory
    public static void finishLoad(Decoder.Decoded decoded, CPU cpu) 
    {
        int value = cpu.getMBR();
        
        if (decoded.ins == Isa.Instruction.LDR) 
        {
            System.out.printf("DEBUG: finishLoad LDR - MBR is %06o, writing to GPR[%d]\n", value, decoded.r);
            cpu.setGPR(decoded.r, value);
        } 
        else if (decoded.ins == Isa.Instruction.LDX) 
        {
            System.out.printf("DEBUG: finishLoad LDX - MBR is %06o, writing to IX[%d]\n", value, decoded.x);
            cpu.setIX(decoded.x, value);
        }
    }

}
