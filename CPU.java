// Handles Fetch/Decode/Execute with 2-cycle memory timing
public class CPU 
{
    //memory 
    private final Memory memory;
    public CPU(Memory memory) 
    {
        this.memory = memory;
    }
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
    private enum State 
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
        HALT
    }

    private Decoder.Decoded decoded;

    private State curState = State.FETCH_1;

    //bit masks
    private static final int MASK_4  = 0xF;
    private static final int MASK_12 = 0xFFF;
    private static final int MASK_16 = 0xFFFF;

    private void calculateEA() {
        // address is 5-bit field per assembler/spec
        int address = decoded.addr & 0x1F;
        // x field from instruction
        int xField = decoded.x & 0x03;
        // indirect bit
        int iBit = decoded.i & 0x01;
        // EA base is always the address field from the instruction
        int ea = address;

        // LDR/STR/LDA use indexing LDX/STX do NOT (per the spec you pasted)
        if (decoded.ins == Isa.Instruction.LDR ||
            decoded.ins == Isa.Instruction.STR ||
            decoded.ins == Isa.Instruction.LDA) {

            if (xField > 0 && xField < 4) {
                ea = (ea + IX[xField]) & MASK_12;
            } else {
                ea = ea & MASK_12;
            }

        } else {
            // do not add IX[x] for LDX/STX
            ea = ea & MASK_12;
        }

        effectiveAddress = ea;

        // indirect addressing
        if (iBit == 1) {
            MAR = effectiveAddress;
            curState = State.INDIRECT_1;
        } else {
            curState = State.EXECUTE;
        }
    }

    private void handleExecute() 
    {
        int r = decoded.r & 0x03;
        int x = decoded.x & 0x03;

        //load the register from memory
        switch (decoded.ins) {
            case LDR: 
                //set the MAR to the effective address
                MAR = effectiveAddress & MASK_12;
                memory.requestRead(MAR);
                //start the 2-cycle red
                memoryCycles = 1; 
                curState = State.LDR_FINISH;
                break;
        
            case STR: 
                //store the register to memory
                MAR = effectiveAddress & MASK_12;
                //put the data in the register in MBR for memory
                MBR = GPR[r] & MASK_16; 
                memory.requestWrite(MAR);
                //start teh 2-cycle write
                memoryCycles = 1;
                //the next cycle will be fetch
                curState = State.FETCH_1; 
                break;

            case LDA:
                //load the effective address into the register
                GPR[r] = effectiveAddress & MASK_16;
                curState = State.FETCH_1;
                break;

            case LDX:
                if (x == 0) 
                {
                    // IX=0 is not allowed for LDX
                    setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
                    curState = State.HALT;
                    return;
                }
                //set the MAR to the effective address
                MAR = effectiveAddress & MASK_12;
                memory.requestRead(MAR);
                memoryCycles = 1;                 // stall for memory read
                curState = State.LDX_FINISH;
                break;

            case STX:
                if (x == 0) 
                {
                    setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
                    curState = State.HALT;
                    return;
                }
                MAR = effectiveAddress & MASK_12;
                MBR = IX[x] & MASK_16;
                memory.requestWrite(MAR);
                memoryCycles = 1;                 // stall for memory write
                curState = State.FETCH_1;
                break;

            //Implement other instructions here (not implemented yet)
            default:
                curState = State.FETCH_1;
                break;
            
        }
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
            return;
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
                decodeAndExecute(); 
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

            case LDR_FINISH:
                GPR[decoded.r & 0x03] = MBR & MASK_16;
                curState = State.FETCH_1;
                break;
            case LDX_FINISH:
                IX[decoded.x & 0x03] = MBR & MASK_16;
                curState = State.FETCH_1;
                break;

            case HALT:
                //cpu works because of HALT or Fault
                break;
        }
    }


    // function to decode and execute the instruction in IR
    private void decodeAndExecute() {
        try {
            //using global decoded variable to store the decoded instruction for use in execute
            decoded = Decoder.decode(IR);
            //check for HALT first since it doesnt follow the normal execution path
            if (decoded.ins == Isa.Instruction.HLT) {
                curState = State.HALT;
                return;
            }

            // Load/store family routes into EA computation
            switch (decoded.ins) {
                case LDR, STR, LDA, LDX, STX -> curState = State.COMPUTE_EA;

                // TODO: Other instructions will have their own execution paths (not implemented yet)
                default -> {
                    // Not implemented yet - have a test instruction that triggers this for now
                    setMFR(MachineFault.Code.ILLEGAL_OPCODE.value);
                    curState = State.HALT;
                }
            }

        } catch (MachineFault mf) {
            setMFR(mf.code().value);
            curState = State.HALT;
        }
}


    //ALL GETTERS AND SETTERS ARE BELOW
    //set the MFR
    public void setMFR(int faultID) 
    {
        this.MFR = faultID & MASK_4; 
    }

    //set the MBR
    public void setMBR(int value) 
    {
        this.MBR = value & MASK_16; 
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
        return this.PC & MASK_12;
    }

    public int getMFR() 
    {
        return this.MFR; 
    }

    public void setGPR(int i, int value) 
    {
        GPR[i] = value & 0xFFFF;
    }

    public int getIX(int i) 
    {
        return this.IX[i] & MASK_16;
    }

    public void setIX(int i, int value) 
    {
        IX[i] = value & 0xFFFF;
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
    for (int i = 0; i < 4; i++) {
        System.out.printf("GPR[%d]: %06o (Oct) | %04X (Hex) | %d\n", i, getGPR(i), getGPR(i), getGPR(i));
    }
    
    // Index Registers (starting from 1 as IX[0] is usually not used for indexing)
    for (int i = 1; i < 4; i++) 
    {
        System.out.printf("IX [%d]: %06o (Oct) | %04X (Hex) | %d\n", i, getIX(i), getIX(i), getIX(i));
    }
    System.out.println("===============================================\n");
    }
}
