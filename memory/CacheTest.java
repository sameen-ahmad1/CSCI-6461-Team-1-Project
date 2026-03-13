package memory;
 
import memory.simple.Memory;
 
/*
 * testing the cache to make sure hits, misses, and evictions all work
 */
public class CacheTest {
 
    public static void main(String[] args) {
 
        Memory memory = new Memory();
        Cache cache = new Cache(memory);
 
        // test 1: first read should miss, second read to same address should hit
        System.out.println("===== TEST 1: Miss then Hit =====");
 
        memory.directWrite(010, 01234);
        int val1 = cache.read(010);     // miss
        int val2 = cache.read(010);     // hit
 
        System.out.println("read 1 (expect MISS): " + Integer.toOctalString(val1));
        System.out.println("read 2 (expect HIT):  " + Integer.toOctalString(val2));
        cache.dump();
 
        // test 2: write a new value then read it back, should be a hit with the updated value
        System.out.println("===== TEST 2: Write then Read =====");
 
        cache.write(010, 05677);
        int val3 = cache.read(010);     // should hit and return new value
 
        System.out.println("read after write (expect HIT + new value): " + Integer.toOctalString(val3));
        cache.dump();
 
        // test 3: fill up all 16 lines then read a new address, should evict the oldest one
        System.out.println("===== TEST 3: FIFO Eviction =====");
 
        cache.reset(); // clear everything so we start with 16 empty lines
 
        // load 16 different addresses to fill the cache
        for (int i = 0; i < 16; i++) {
            int addr = 020 + i;
            memory.directWrite(addr, addr * 2);
            cache.read(addr);   // all misses, fills up the cache
        }
 
        System.out.println("cache is full now:");
        cache.dump();
 
        // reading a 17th address should evict address 020 since it was loaded first
        memory.directWrite(040, 0777);
        System.out.println("reading new address 040 - should evict addr=020:");
        cache.read(040);
        cache.dump();
 
        // reading 020 again should be a miss now since it got evicted
        System.out.println("reading 020 again - should be a MISS since it was evicted:");
        cache.read(020);
        cache.dump();
    }
}