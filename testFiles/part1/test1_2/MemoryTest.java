package testFiles.part1.test1_2;
import memory.MachineFault;
import memory.simple.Memory;

public class MemoryTest {
    public static void main(String[] args) {
        Memory mem = new Memory();

        // Test 1: reset sets memory to 0
        mem.reset();
        if (mem.peek(0) != 0) {
            System.out.println("FAIL: Reset test");
        }

        // Test 2: directWrite + peek
        mem.directWrite(100, 1234);
        if (mem.peek(100) != 1234) {
            System.out.println("FAIL: Write/Read test");
        }

        // Test 3: bounds check
        try {
            mem.peek(3000);
            System.out.println("FAIL: Bounds check failed");
        } catch (MachineFault e) {
            System.out.println("PASS: Bounds check works");
        }

        System.out.println("Memory tests complete.");
    }
}
