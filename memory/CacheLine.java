package memory;

/**
 * CacheLine represents a single line in the cache.
 *
 * Each line holds:
 * valid: whether this line contains real data (false when cache is first created)
 * tag:   the memory address this line is storing data for
 * data:  the actual 16-bit word fetched from memory
 * fifoOrder: a counter stamp recording when this line was inserted,
 *            used by the FIFO replacement policy (lower = older = evict first)
 * A line starts invalid (empty). Once data is loaded into it, valid becomes true.
 */
public class CacheLine {
    
    public boolean valid; //False means the line is empty/unused.

    public int tag; //The memory address (tag) this line is caching.

    
    public int data; //The actual 16-bit data word stored in this cache line.

    public int fifoOrder; //Lower number = inserted earlier = evicted first.

    
    // Constructor:creates an empty cache line, everything starts blank
    public CacheLine() {
        this.valid     = false;
        this.tag       = -1;    //no address assigned yet
        this.data      = 0;
        this.fifoOrder = 0;
    }

    //called on a cache miss, fills this line with new data from memory
    public void load(int tag, int data, int fifoOrder) {
        this.valid     = true;
        this.tag       = tag;
        this.data      = data & 0xFFFF;   // keep it 16-bit
        this.fifoOrder = fifoOrder;
    }

    // called on a write-hit, updates the data without changing the FIFO order
    public void updateData(int data) {
        this.data = data & 0xFFFF;
    }
    // clears this line back to empty, used when the cache resets on IPL
    public void invalidate() {
        this.valid     = false;
        this.tag       = -1;
        this.data      = 0;
        this.fifoOrder = 0;
    }

    // prints out the line info for trace logging
    @Override
    public String toString() {
        if (!valid) {
            return "[EMPTY]";
        }
        return String.format("[VALID] tag=%04o  data=%06o  fifoOrder=%d",
                tag, data, fifoOrder);
    }
}
