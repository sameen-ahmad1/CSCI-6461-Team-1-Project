package memory.simple;
import java.util.Arrays;
import memory.CPU;
import memory.MachineFault;
import memory.MemoryBus;

public final class Memory implements MemoryBus 
{
    public static final int SIZE = 2048;
   // private static StringBuilder logs = new StringBuilder();
    private final short[] mem = new short[SIZE];

    private enum Op { NONE, READ, WRITE }
    private Op pendingOp = Op.NONE;
    private int pendingAddr = 0;

    @Override
    public void reset() {
        Arrays.fill(mem, (short) 0);
        pendingOp = Op.NONE;
        pendingAddr = 0;
    }

    

    private StringBuilder errorLog = new StringBuilder();

    @Override
    public void postError(String message) {
       
        System.err.println("SYSTEM FAULT: " + message);
        
      
        errorLog.append(">> ").append(message).append("\n");
    }

    @Override
    public String getErrors() 
    {
        // 3. Convert the buffer to a String
        String result = errorLog.toString();
        
        errorLog.setLength(0); 
        
        return result;
    }

    @Override
    public void requestRead(int mar) {
        checkAddr(mar);
        checkNotBusy();
        pendingAddr = mar;
        pendingOp = Op.READ;
    }

    @Override
    public void requestWrite(int mar) {
        checkAddr(mar);
        checkNotBusy();
        pendingAddr = mar;
        pendingOp = Op.WRITE;
    }

    @Override
    public void tick(CPU cpu) {
        if (pendingOp == Op.NONE) return;

        int mar = pendingAddr;  
        checkAddr(mar);

        switch (pendingOp) {
            case NONE -> throw new IllegalStateException("No pending memory operation");
            case READ -> cpu.setMBR(u16(mem[mar]));
            case WRITE -> mem[mar] = (short) (cpu.getMBR() & 0xFFFF);
        }

        pendingOp = Op.NONE;
    }

    public int peek(int addr) {
        checkAddr(addr);
        return u16(mem[addr]);
    }

    public void directWrite(int addr, int value16) {
        checkAddr(addr);
        mem[addr] = (short) (value16 & 0xFFFF);
    }

    private void checkNotBusy() {
        if (pendingOp != Op.NONE) {
            String msg = String.format("FAULT [%s]: Memory busy at addr %04o", 
                                    MachineFault.Code.MEMORY_BUSY, pendingAddr);
        
        // Log it so the GUI can see it
        postError(msg); 

        throw new MachineFault(
                MachineFault.Code.MEMORY_BUSY,
                pendingAddr,
                msg
        );
        }
    }

    private static int u16(short s) {
        return s & 0xFFFF;
    }

    @Override
    public int readWord(int address) 
    {
        return peek(address);
    }

    @Override
    public void writeWord(int address, int value) 
    {
        directWrite(address, value);
    }
    
    @Override
    public String getCacheStatus() 
    {
        return "Cache is currently DISABLED\nDirect Memory Access active.";
    }

    private void checkAddr(int addr) {
        if (addr < 0 || addr >= SIZE) {
            String msg = String.format("FAULT [%s]: Invalid addr %04o (Limit: %04o)", 
                                    MachineFault.Code.ILLEGAL_MEMORY_ADDRESS, addr, SIZE-1);
        
            // Log it so the GUI can see it
            postError(msg);

        throw new MachineFault(
                MachineFault.Code.ILLEGAL_MEMORY_ADDRESS,
                addr,
                msg
        );
        }
    }
}
