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
        HALT
    }

    private State curState = State.FETCH_1;

    //bit masks
    private static final int MASK_4  = 0xF;
    private static final int MASK_12 = 0xFFF;
    private static final int MASK_16 = 0xFFFF;

    private void calculateEA() 
    {
        //5 bit address
        int address = IR & 0x1F;      // 5-bit address field
        //2 bit IX 
        int ixBits = (IR >> 6) & 0x03; // 2-bit IX field
        //1 bit indirect
        int iBit = (IR >> 5) & 0x01;   // 1-bit Indirect field

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
        int opcode = (IR >> 10) & 0x3F;
        int r = (IR >> 8) & 0x03;

        //load the register from memory
        if (opcode == 0x01) 
        { 
            //set the MAR to the effective address
            MAR = effectiveAddress & MASK_12;
            //start the 2-cycle red
            memoryCycles = 1; 
            curState = State.LDR_FINISH;
        } 
        else if (opcode == 0x02) 
        { 
            //store the register to memory
            MAR = effectiveAddress & MASK_12;
            //put the data in the register in MBR for memory
            MBR = GPR[r] & MASK_16; 
            //start teh 2-cycle write
            memoryCycles = 1;
            //the next cycle will be fetch
            curState = State.FETCH_1; 
        }
        else 
        {
            //if there are other instructions
            curState = State.FETCH_1;
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
                //last step, move the data from MBR to GPR
                int last_ldr = (IR >> 8) & 0x03;
                GPR[last_ldr] = MBR & MASK_16;

                //go back to the next instruction
                curState = State.FETCH_1; 
                break;

            case HALT:
                //cpu works becuase of HALT or Fault
                break;
        }
    }



    //helper function for invalid opcode
    private boolean isValidOpcode(int opcode) 
    {
        // make sure that the opcode is correct
        return (opcode >= 0 && opcode <= 2);
    }

    private void decodeAndExecute() 
    {
        int opcode = (IR >> 10) & 0x3F;
        //int r = (IR >> 8) & 0x03;
    
        // Debug print to see what is happening inside the CPU
        //System.out.println("DEBUG: Decoding Opcode " + opcode + " for Register " + r);

        if (!isValidOpcode(opcode)) 
        {
            setMFR(1); 
            curState = State.HALT;
        } 
        else if (opcode == 0) 
        { // HLT
            curState = State.HALT;
        } 
        else 
        {
            curState = State.COMPUTE_EA;
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