// Handles Fetch/Decode/Execute with 2-cycle memory timing
public class CPU 
{
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

    private void calculateEA() 
    {
        // have already been bit shifted in decoder, so just need to mask
        int address = decoded.addr; // 0..31 (5 bits)
        int ixBits  = decoded.x;    // 0..3
        int iBit    = decoded.i;    // 0/1

        //handle the indexing
        if (ixBits > 0 && ixBits < 4) 
        {
            effectiveAddress = (IX[ixBits] + address) & MASK_12;
        } 
        else 
        {
            effectiveAddress = address & MASK_12;
        }

        //handle indirect
        if (iBit == 1) 
        {
            MAR = effectiveAddress;
            curState = State.INDIRECT_1;
        } 
        else 
        {
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
                //start the 2-cycle red
                memoryCycles = 1; 
                curState = State.LDR_FINISH;
                break;
        
            case STR: 
                //store the register to memory
                MAR = effectiveAddress & MASK_12;
                //put the data in the register in MBR for memory
                MBR = GPR[r] & MASK_16; 
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
        //for memory timing
        if (memoryCycles > 0) 
        {
            memoryCycles--;
            return;
        }

        switch (curState) 
        {   
            case FETCH_1:
                //start the fethc and move the PC to MAR
                MAR = PC & MASK_12;
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
                //get the first 6 bits which is the opcde
                decodeAndExecute(); 
                break;

            //computer the effective address
            case COMPUTE_EA:
                calculateEA();
                break;

            //get the actual address from the memory 
            case INDIRECT_1:
                
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
                //cpu works becuase of HALT or Fault
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
        return this.GPR[i]; 
    }

    public int getPC() 
    {
        //returns 12 bit PC
        return this.PC; 
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
        return IX[i] & 0xFFFF;
    }

    public void setIX(int i, int value) 
    {
        IX[i] = value & 0xFFFF;
    }
}