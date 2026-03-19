package memory;
// Handles Fetch/Decode/Execute with 2-cycle memory timing

import assembler.Isa;

public class CPU 
{
    //memory 
    private final MemoryBus memory;
   // private StringBuilder logs = new StringBuilder();
    public CPU(MemoryBus memory) 
    {
        this.memory = memory;
    }

//     private void addLog(String msg) {
//     logs.insert(0, msg + "\n"); // Add new messages to the top
// }

    
//     public String getLogs() {
//         return logs.toString();
//     }
    //CPU registers
    //program counter (12 bit)
    private int PC;           

    //memory buffer register (16 bit)
    private int MBR;       

    //machine fault register (4 bit)
    private int MFR;        

    //condition code (4 bit)
    private int CC;     

    //instruction register (16 bit)
    private int IR;    

    //memory address register (12 bit)
    private int MAR;           

    //4 general purpose registers (16 bit)
    private int[] GPR = new int[4]; 

    // 3 Index Registers (1-3) 
    private int[] IX = new int[4]; 

    //control varibale for effective addres
    private int effectiveAddress;

    //tracks 2-cycle memory timing
    private int memoryCycles = 0; 
    
    //state machines
    public enum State 
    {
        FETCH_1, 
        FETCH_2, 
        DECODE, 
        COMPUTE_EA, 
        INDIRECT_1, 
        INDIRECT_2, 
        EXECUTE, 
        LDR_FINISH, 
        LDX_FINISH, 
        AMR_FINISH,
        SMR_FINISH,       
        HALT
    }

    private Decoder.Decoded decoded;

    private State curState = State.FETCH_1;

    //bit masks
    private static final int MASK_4  = 0xF;
    private static final int MASK_12 = 0x7FF;
    private static final int MASK_16 = 0xFFFF;

    private void calculateEA() 
    {

        // I/O instructions use decoded.addr as the raw devid — no EA computation needed
        if (decoded.ins == Isa.Instruction.IN || decoded.ins == Isa.Instruction.OUT) 
        {
            curState = State.EXECUTE;
            return;
        }
        this.effectiveAddress = Executor.calculateEA(decoded, this);

        if ((decoded.i & 0x01) == 1) 
        {
            MAR = effectiveAddress;
            curState = State.INDIRECT_1;
        } 
        else 
        {
            curState = State.EXECUTE;
        }
    }

    public void halt() {
        this.curState = State.HALT;
    }

    public boolean isHalted() {
        return curState == State.HALT;
    }

    // function to decode and execute the instruction in IR
    private void decode() 
    {
        try {
            decoded = Decoder.decode(IR, this.memory);
            if (decoded.ins == Isa.Instruction.HLT) 
            {
                curState = State.HALT;
            } 
            else 
            {
                curState = State.COMPUTE_EA;
            }
        } 
        catch (MachineFault mf) 
        {
            setMFR(mf.code().value);
            curState = State.HALT;
        }
    }

    private void handleExecute() 
    {
        // calls the executor to perform the actual operation and get the memory cycles needed
        Executor.ExecuteResult result = Executor.execute(decoded, this, effectiveAddress, memory);

        memoryCycles = result.memoryCycles;
        curState     = result.nextState;

        System.out.println("DEBUG: handleExecute finished. New State is: " + curState);
        //addLog("DEBUG: HandleExecute finished. New State is: " + curState);
    }

    //cpu tick
    public void cycle() 
    {
        //memory process any requests from the previous cycle
        memory.tick(this);
        //for memory timing
        if (memoryCycles > 0) 
        {
            memoryCycles--;
            if (memoryCycles > 0) 
            { 
                return;
            }
            //return;
        }

        switch (curState) 
        {   
            case FETCH_1:
                //start the fetch and move the PC to MAR
                MAR = PC & MASK_12;
                //tell memory to read
                memory.requestRead(MAR);
                //hold for the 2-cycle memory red
                memoryCycles = 1; 
                curState = State.FETCH_2;
                break;

            case FETCH_2:
                //data from MBR is now moved to IR
                IR = MBR & MASK_16;
                PC = (PC + 1) & MASK_12;
                curState = State.DECODE;
                break;

            case DECODE:
                //get the first 6 bits which is the opcode
                decode(); 
                break;

            //computer the effective address
            case COMPUTE_EA:
                calculateEA();
                break;

            //get the actual address from the memory 
            case INDIRECT_1:
                memory.requestRead(MAR);
                memoryCycles = 1; 
                curState = State.INDIRECT_2;
                break;

            case INDIRECT_2:
                // get the address from MBR
                effectiveAddress = MBR & MASK_12;
                curState = State.EXECUTE;
                break;

            case EXECUTE:
                handleExecute();
                break;

            // these four states need to wait for the memory operation to complete before writing back to the register, so we wait for the memory cycle in the main loop and then write back here
            case LDR_FINISH:
                //GPR[decoded.r & 0x03] = MBR & MASK_16;
                Executor.finishLoad(decoded, this);
                curState = State.FETCH_1;
                break;

            case LDX_FINISH:
                //IX[decoded.x & 0x03] = MBR & MASK_16;
                Executor.finishLoad(decoded, this);
                curState = State.FETCH_1;
                break;

            case AMR_FINISH:
                //GPR[decoded.r & 0x03] = (GPR[decoded.r & 0x03] + MBR) & MASK_16;
                Executor.finishAMR(decoded, this);
                curState = State.FETCH_1;
                break;

            case SMR_FINISH:
                //GPR[decoded.r & 0x03] = (GPR[decoded.r & 0x03] - MBR) & MASK_16;
                Executor.finishSMR(decoded, this);
                curState = State.FETCH_1;
                break;

            case HALT:
                //cpu works because of HALT or Fault
                break;
        }
    //System.out.println("Cycle End - State: " + curState + " | PC: " + Integer.toString(PC, 8));
        // 1. Build the cycle trace string
    String trace = String.format("System Cycle State: %s | PC: %06o", curState, PC);

    // 2. Post it to the bus so the GUI can "see" it
    if (memory != null) 
    {
        memory.postError(trace); 
    }

  
    System.out.println(trace);
    }



    //ALL GETTERS AND SETTERS
    public void setMFR(int faultID) 
    {
        this.MFR = faultID & MASK_4; 
    }

    public void setMAR(int value)
    {

        this.MAR = value & MASK_12;

    }

    //set the MBR
    public void setMBR(int value) 
    {
        if (value != 0) {
        System.out.printf(">>> CPU BUS: Data %06o just landed in MBR!\n", value);
    }
        this.MBR = value & MASK_16; 
    }

    public void setPC(int value){

        this.PC = value & MASK_12;

    }

    public int getMAR() 
    {
        return this.MAR & MASK_12;
    }

    //get teh MBR
    public int getMBR() 
    {
        
        return this.MBR & MASK_16;
    }


    public int getGPR(int i) 
    {
        //returns 16-bit value of the specified GPR
        return this.GPR[i] & MASK_16;
    }

    public int getPC() 
    {
        //returns 12 bit PC
        return PC & MASK_12;
    }

    public int getMFR() 
    {
        return MFR;
    }

    public void setGPR(int i, int value) 
    {
        GPR[i] = value & MASK_16;
    }

    public int getIX(int i) 
    {
        return IX[i] & MASK_16;
    }

    public void setIX(int i, int value) 
    {
        IX[i] = value & MASK_16;
    }

    public int getIR() 
    {
        return this.IR & MASK_16;
    }

    public void setCC(int cc2) {
        this.CC = cc2 & MASK_4;
    }

    public int getCC() 
    {
        return this.CC & MASK_4;
    }

    public void listRegisters() 
    {
        System.out.println("\n================ REGISTER LISTS ================");
        // PC and MAR are 12-bit, so we format for 4 octal digits
        System.out.printf("PC :  %04o (Oct) | %04X (Hex) | %d (Dec)\n", getPC(), getPC(), getPC());
        System.out.printf("MAR:  %04o (Oct) | %04X (Hex)\n", getMAR(), getMAR());
    
        // MBR and IR are 16-bit, so we format for 6 octal digits
        System.out.printf("MBR:  %06o (Oct) | %04X (Hex)\n", getMBR(), getMBR());
        System.out.printf("IR :  %06o (Oct) | %04X (Hex)\n", IR, IR);
    
        // Status registers
        System.out.printf("MFR:  %d (ID)    | CC: %d\n", getMFR(), CC);
        System.out.println("-----------------------------------------------");
    
        // General Purpose Registers
        for (int i = 0; i < 4; i++) 
        {
            System.out.printf("GPR[%d]: %06o (Oct) | %04X (Hex) | %d\n", i, getGPR(i), getGPR(i), getGPR(i));
        }
    
        // Index Registers (starting from 1 as IX[0] is usually not used for indexing)
        for (int i = 1; i < 4; i++) 
        {
            System.out.printf("IX [%d]: %06o (Oct) | %04X (Hex) | %d\n", i, getIX(i), getIX(i), getIX(i));
        }
        System.out.println("===============================================\n");
    }

    public MemoryBus getMemory() 
    {
        // This returns the MemoryBus (or Cache) the CPU is using
        return this.memory; 
    }

    public void ipl() 
    {
        memory.reset();

        // 2. Clear Registers (Crucial!)
        for (int i = 0; i < 4; i++) setGPR(i, 0);
        for (int i = 1; i < 4; i++) setIX(i, 0);
        this.PC = 010;
        this.MAR = 0;
        this.MBR = 0;
        this.IR = 0;
        this.MFR = 0; // Clear the fault code
        this.CC = 0;
        
        // 3. Load the instructions
        memory.writeWord(010, 002020); // Instruction 1
        memory.writeWord(011, 0102220);
        memory.writeWord(020, 000100); // Data


        this.curState = State.FETCH_1;
        System.out.println("IPL: Program loaded with correct opcodes.");
    }

    public State getCurState() 
    {
        return this.curState;
    }

}
