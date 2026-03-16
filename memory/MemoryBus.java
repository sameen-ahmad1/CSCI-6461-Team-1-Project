package memory;

/*
 * MemoryBus is just an interface that the CPU uses to read and write memory.
 * instead of the CPU talking directly to Memory, it talks to this interface.
 * that way we can plug the cache in underneath without changing the CPU logic.
 * Cache implements this interface, so when the CPU calls readWord or writeWord
 * it goes through the cache first before hitting main memory.
 */
public interface MemoryBus {

    // read a word from the given address
    // returns the value either from cache (hit) or memory (miss)
    int readWord(int address);

    // write a value to the given address
    // always writes to memory, updates cache too if address is already there
    void writeWord(int address, int value);

    //added more to connect cache, memory and cpu all together
    String getCacheStatus();
    void reset();
    void tick(CPU cpu);
    void requestRead(int address);
    void requestWrite(int address);
}