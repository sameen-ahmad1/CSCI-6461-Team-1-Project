import java.util.Arrays;

public final class Memory {
    public static final int SIZE = 2048;

    private final short[] mem = new short[SIZE];

    private enum Op { NONE, READ, WRITE }
    private Op pendingOp = Op.NONE;
    private int pendingAddr = 0;

    public void reset() {
        Arrays.fill(mem, (short) 0);
        pendingOp = Op.NONE;
        pendingAddr = 0;
    }

    public void requestRead(int mar) {
        checkAddr(mar);
        checkNotBusy();
        pendingAddr = mar;
        pendingOp = Op.READ;
    }

    public void requestWrite(int mar) {
        checkAddr(mar);
        checkNotBusy();
        pendingAddr = mar;
        pendingOp = Op.WRITE;
    }

    
    public void tick(CPU cpu) {
        if (pendingOp == Op.NONE) return;

        int mar = cpu.getMAR();  
        checkAddr(mar);

        switch (pendingOp) {
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
