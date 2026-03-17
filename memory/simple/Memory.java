package memory.simple;
import java.util.Arrays;
import memory.CPU;
import memory.MachineFault;
import memory.MemoryBus;
import memory.Device;

public final class Memory implements MemoryBus 
{
    public static final int SIZE = 2048;

    private final short[] mem = new short[SIZE];

    private enum Op { NONE, READ, WRITE }
    private Op pendingOp = Op.NONE;
    private int pendingAddr = 0;
    private final Device device = new Device();

    @Override
    public void reset() {
        Arrays.fill(mem, (short) 0);
        pendingOp = Op.NONE;
        pendingAddr = 0;
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
            throw new MachineFault(
                    MachineFault.Code.MEMORY_BUSY,
                    pendingAddr,
                    "Memory busy: new request before previous completed"
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

    public int inputDevice(int devid) 
    {
        return device.read(devid);
    }   

    public void outputDevice(int devid, int value) 
    {
        device.write(devid, value);
    }
    
    @Override
    public String getCacheStatus() 
    {
        return "Cache is currently DISABLED\nDirect Memory Access active.";
    }
    @Override
    public Device getDevice() {
        return device;
    }

    private static void checkAddr(int addr) {
        if (addr < 0 || addr >= SIZE) {
            throw new MachineFault(
                    MachineFault.Code.ILLEGAL_MEMORY_ADDRESS,
                    addr,
                    "Illegal memory address: " + addr + " (valid 0.." + (SIZE - 1) + ")"
            );
        }
    }
}
