package memory;
import java.util.Scanner;
import memory.simple.MemorySystem;

public class Simulator 
{
    public static void main(String[] args) 
    {
        // 1. Create the base physical memory
        memory.simple.Memory hardware = new memory.simple.Memory();
    
        // 2. Wrap it in the Cache Layer (Sameen's Integration Part)
        // Swap 'new memory.simple.Memory()' for 'new memory.simple.CacheTest(hardware)'
        MemorySystem memoryBus = new memory.simple.CacheTest(hardware); 
    
        // 3. Plug the Cache-wrapped bus into the CPU
        CPU cpu = new CPU(memoryBus);

        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("--- GWU CS6461 Computer Simulator ---");
        System.out.println("Commands: 'ipl', 'step', 'dump', 'quit'");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().toLowerCase();

            if (input.equals("ipl")) 
            {
                cpu.ipl(); // Calls the method we just wrote
                cpu.listRegisters();
            } 
            else if (input.equals("step")) 
            {
                
                // 1. Force the first cycle to leave the starting FETCH_1 state
                        
                cpu.cycle(); // 1. Push out of FETCH_1
                while (cpu.getCurState() != CPU.State.FETCH_1 && cpu.getCurState() != CPU.State.HALT) 
                {
                    cpu.cycle(); // 2. Run until next instruction start
                }
                cpu.listRegisters(); // 3. Show the result

                
                // 3. NOW print the registers. The GPR[0] should be updated.
              
                System.out.println(memoryBus.getCacheStatus());
                
                // Extra requirement: Show memory contents at MAR
                int currentMAR = cpu.getMAR();
                System.out.printf("Memory at MAR [%04o]: %06o\n", currentMAR, memoryBus.peek(currentMAR));
            } 
            else if (input.equals("dump")) {
                cpu.listRegisters();
            } 
            else if (input.equals("quit")) {
                break;
            }
        }
        scanner.close();
    }
}
