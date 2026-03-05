package memory.simple;

import memory.CPU;

public class CacheTest implements MemorySystem 
{
    private final Memory physicalMemory;
    //testing purposes
    private int accessCount = 0;
    private int lastAddr = 0;

    public CacheTest(Memory physicalMemory) 
    {
        this.physicalMemory = physicalMemory;
    }

    @Override
    public void requestRead(int address) 
    {
        accessCount++;
        this.lastAddr = address;
        System.out.println("[CACHE Test] READ at: " + Integer.toString(address, 8));
        physicalMemory.requestRead(address);
    }

    @Override
    public void requestWrite(int address) 
    {
        accessCount++;
        this.lastAddr = address;
        System.out.println("[CACHE Test] WRITE at: " + Integer.toString(address, 8));
        physicalMemory.requestWrite(address);
    }

    @Override
    public void tick(CPU cpu) 
    {
        physicalMemory.tick(cpu);
    }

    @Override
    public void reset() { physicalMemory.reset(); }

    @Override
    public void directWrite(int addr, int val) { physicalMemory.directWrite(addr, val); }

    @Override
    public int peek(int addr) { return physicalMemory.peek(addr); }


    @Override
    public String getCacheStatus() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("===== CACHE SUBSYSTEM =====\n");
        sb.append(String.format("Mode:        %s\n", "TEST"));
        sb.append(String.format("Accesses:    %d\n", accessCount));
        sb.append(String.format("Last Address Accessed:   %04o\n", lastAddr));
        sb.append("---------------------------\n");
        return sb.toString();
    }
}