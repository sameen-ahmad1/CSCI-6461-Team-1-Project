package testFiles.part1.test1_0;
import memory.CPU;
import memory.simple.Memory;

public class testCPU {
    public static void main(String[] args) {
        
        Memory memory = new Memory();
        CPU cpu = new CPU(memory);

        System.out.println("--- Starting Hardware Integration Test ---");

        // Load Program into Memory (Directly)
        // test: LDR 3, 0, 10 (Opcode 1, Reg 3, Address 10)
        // In ISA, this instruction value is 0x070A
        memory.directWrite(0, 0x070A);
        
        // Put the data '99' at Address 10
        memory.directWrite(10, 99);

        // Run the Clock Cycles
        // We need about 8-10 cycles to finish one LDR instruction due to timing
        for (int i = 1; i <= 10; i++) {
            cpu.cycle();
            System.out.println("Cycle " + i + " complete.");
            
            // If the CPU hits HALT or FETCH_1 after finishing, we stop
            // But for this test, we'll just run 10 to see the result
        }

        // 4. Final Verification
        System.out.println("\n--- Final Results ---");
        cpu.listRegisters();

        // Verification Logic
        if (cpu.getGPR(3) == 99) {
            System.out.println("SUCCESS: GPR[3] contains 99!");
        } else {
            System.out.println("FAILURE: GPR[3] contains " + cpu.getGPR(3));
        }
    }
}
