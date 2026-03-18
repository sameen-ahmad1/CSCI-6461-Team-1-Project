package memory;
 
import memory.simple.Memory;
 
/*
 * Cache class - sits between the CPU and main memory.
 * holds 16 lines, each one stores a word we already fetched from memory.
 *
 * on a read: check if we already have it (hit), if not, we go get it from memory (miss)
 * on a write: always write to memory, update the cache line too if its already there
 * when all 16 lines are full: kick out the oldest one using FIFO
 */
public class Cache implements MemoryBus {
    private static final int NUM_LINES = 16; //number of lines in the cache
    //private StringBuilder logs = new StringBuilder();
    private final CacheLine[] lines; //array of cache lines
 
    private final Memory memory; //reference to main memory for misses and writes
 
    private int fifoCounter; //goes up every time we add a line, used to track insertion order for FIFO
 
    private int totalHits; //just tracking stats
    private int totalMisses;
 
    // constructor - sets up 16 empty lines
    public Cache(Memory memory) {
        this.memory = memory;
        this.lines = new CacheLine[NUM_LINES];
        this.fifoCounter = 0;
        this.totalHits = 0;
        this.totalMisses = 0;
 
        for (int i = 0; i < NUM_LINES; i++) {
            lines[i] = new CacheLine();
        }
    }
 
    private StringBuilder errorLog = new StringBuilder();

    @Override
    public void postError(String message) {
        // Add new errors to the top with a newline
        errorLog.insert(0, message + "\n"); 
    }

    @Override
    public String getErrors() {
        return errorLog.toString();
    }

    @Override
    public void requestRead(int address) 
    {
        // Pass the hardware request through to the physical memory
        updateStatsAndCache(address);
        this.memory.requestRead(address); 
    }

    @Override
    public void requestWrite(int address) 
    {
        // Pass the hardware request through to the physical memory
        updateStatsAndCache(address);
        this.memory.requestWrite(address); 
    }

    // read from cache, if its not there go fetch it from memory
    @Override
    public int readWord(int address) {
        int hitIndex = findLine(address);
 
        if (hitIndex != -1) { //found it in cache, return it
            totalHits++;
            System.out.printf("[CACHE HIT]  addr=%04o  data=%06o  line=%d\n",
                    address, lines[hitIndex].data, hitIndex);
            //addLog(String.format("[CACHE HIT]  addr=%04o data=%06o  line=%d\n", address, lines[hitIndex].data, hitIndex));
            return lines[hitIndex].data;
        }
 
        totalMisses++; //not in cache, go get it from memory
        int value = memory.peek(address);
        System.out.printf("[CACHE MISS] addr=%04o  data=%06o  fetched from memory\n",
                address, value);
        //addLog(String.format("[CACHE MISS] addr=%04o  data=%06o  fetched from memory\n", address, value));
        loadLine(address, value);// store it in cache so next time is a hit
        return value;
    }

    @Override
    public void tick(CPU cpu) 
    {
        // Pass the tick through to the physical hardware
        this.memory.tick(cpu); 
    }
 
    // write always goes to memory, update cache too if the address is already there
    @Override
    public void writeWord(int address, int value) {
        memory.directWrite(address, value); //write through to memory
        System.out.printf("[CACHE WRITE] addr=%04o  data=%06o\n", address, value);
        //addLog(String.format("[CACHE WRITE] addr=%04o  data=%06o\n", address, value));
        int hitIndex = findLine(address); // if its in the cache update it so it doesnt go stale
        if (hitIndex != -1) {
            lines[hitIndex].updateData(value);
            System.out.printf("[CACHE UPDATE] line=%d updated with new data\n", hitIndex);
            //addLog(String.format("[CACHE UPDATE] line=%d updated with new data\n", hitIndex));
        }
    }
 
    // clear everything out, called on IPL reset
    @Override
    public void reset() 
    {
        for (int i = 0; i < NUM_LINES; i++) {
            lines[i].invalidate();
        }
        fifoCounter = 0;
        totalHits   = 0;
        totalMisses = 0;
        System.out.println("[CACHE RESET] all lines cleared");
    }

    private void updateStatsAndCache(int address) 
    {
        int hitIndex = findLine(address);
        if (hitIndex != -1) 
        {
            totalHits++;
        } 
        else 
        {
            totalMisses++;
            int value = memory.peek(address);
            loadLine(address, value);
        }
    }
 
    // search lines for a matching address, return index or -1 if not found
    private int findLine(int address) {
        for (int i = 0; i < NUM_LINES; i++) {
            if (lines[i].valid && lines[i].tag == address) {
                return i;
            }
        }
        return -1;
    }
 
    //load a new word into the cache, evict oldest line if all are full
    private void loadLine(int address, int value) {
        for (int i = 0; i < NUM_LINES; i++) { //look for an empty line first
            if (!lines[i].valid) {
                lines[i].load(address, value, fifoCounter++);
                System.out.printf("[CACHE LOAD]  addr=%04o loaded into empty line=%d  fifoOrder=%d\n",
                        address, i, lines[i].fifoOrder);
                return;
            }
        }
 
        int evictIndex = findOldestLine(); //all lines full, evict the oldest one
        System.out.printf("[CACHE EVICT] evicting line=%d  tag=%04o  fifoOrder=%d\n",
                evictIndex, lines[evictIndex].tag, lines[evictIndex].fifoOrder);
 
        lines[evictIndex].load(address, value, fifoCounter++);
        System.out.printf("[CACHE LOAD]  addr=%04o loaded into line=%d  fifoOrder=%d\n",
                address, evictIndex, lines[evictIndex].fifoOrder);
    }
 
    // find the line with the lowest fifoOrder, thats the oldest one to evict
    private int findOldestLine() {
        int oldestIndex = 0;
        int oldestOrder = lines[0].fifoOrder;
 
        for (int i = 1; i < NUM_LINES; i++) {
            if (lines[i].fifoOrder < oldestOrder) {
                oldestOrder = lines[i].fifoOrder;
                oldestIndex = i;
            }
        }
        return oldestIndex;
    }
 
    // prints all 16 lines and hit/miss stats (for debugging)
    public void dump() {
        System.out.println("\n========== CACHE DUMP ==========");
        for (int i = 0; i < NUM_LINES; i++) {
            System.out.printf("  Line %02d: %s\n", i, lines[i].toString());
        }
        System.out.printf("  Total Hits: %d  |  Total Misses: %d\n", totalHits, totalMisses);
        System.out.println("=================================\n");
    }
 
    // getters for hit/miss counts
    public int getTotalHits()   { return totalHits;   }
    public int getTotalMisses() { return totalMisses; }


    @Override
    public String getCacheStatus() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("===== CACHE  =====\n");
        sb.append(String.format("Mode:        %s\n", "FIFO (16 Lines)"));
        
        // Calculate how many lines are currently being used
        int activeLines = 0;
        for (CacheLine line : lines) {
            if (line.valid) activeLines++;
        }
        
        sb.append(String.format("Line Count:  %d / 16\n", activeLines));
        sb.append("---------------------------\n");
        sb.append(String.format("Total Hits:   %d\n", getTotalHits()));
        sb.append(String.format("Total Misses: %d\n", getTotalMisses()));
        
        // Add a quick visual of the lines if you want
        sb.append("---------------------------\n");
        sb.append("Status:      READY\n");
        
        return sb.toString();
    }
}