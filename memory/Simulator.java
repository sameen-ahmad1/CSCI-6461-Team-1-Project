package memory;
import java.util.Scanner;

public class Simulator 
{
    public static void main(String[] args) 
    {
        // 1. Setup the hardware
        // Memory memory = new Memory();
        // CPU cpu = new CPU(memory);

        memory.simple.Memory hardware = new memory.simple.Memory();
        MemoryBus memoryBus = new memory.Cache(hardware); 
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
                cpu.cycle(); 
                while (cpu.getCurState() != CPU.State.FETCH_1 && cpu.getCurState() != CPU.State.HALT) 
                {
                    cpu.cycle(); 
                }
                cpu.listRegisters(); 

                
                int currentMAR = cpu.getMAR();
                
                System.out.printf("Memory at MAR [%04o]: %06o\n", currentMAR, memoryBus.readWord(currentMAR));
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
