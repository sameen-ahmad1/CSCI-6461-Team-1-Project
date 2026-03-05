package memory.simple;

import memory.CPU;

public interface MemorySystem 
{
    void requestRead(int address);
    void requestWrite(int address);
    void tick(CPU cpu);
    void reset();
    
    void directWrite(int address, int value);
    int peek(int address);
    String getCacheStatus();
   
}
