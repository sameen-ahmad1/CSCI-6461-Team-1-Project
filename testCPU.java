public class testCPU {
    public static void main(String[] args) {
        
        // --- TEST 1: LDR SUCCESS ---
        System.out.println("--- Starting CPU Test (LDR) ---");
        CPU myCpu = new CPU();
        // LDR 3, 0, 10 -> Opcode 01, Reg 3, Addr 10
        int ldrInstruction = 0x070A; 
        
        myCpu.cycle(); // 1. FETCH_1 (Sets memoryCycles=1)
        myCpu.cycle(); // 2. STALL (Timer 1 -> 0)
        myCpu.setMBR(ldrInstruction);
        myCpu.cycle(); // 3. FETCH_2 (IR = MBR, PC++)
        myCpu.cycle(); // 4. DECODE (Calls decodeAndExecute)
        myCpu.cycle(); // 5. COMPUTE_EA (Sets effectiveAddress = 10)
        myCpu.cycle(); // 6. EXECUTE (Sets MAR=10, memoryCycles=1)
        
        myCpu.setMBR(55); // Data we want to load
        myCpu.cycle(); // 7. STALL (Timer 1 -> 0)
        myCpu.cycle(); // 8. LDR_FINISH (GPR[3] = MBR)

        System.out.println("Expected GPR[3]: 55, Actual: " + myCpu.getGPR(3));
        System.out.println("Expected PC: 1, Actual: " + myCpu.getPC());


        // --- TEST 2: MACHINE FAULT ---
        System.out.println("\n--- Starting Machine Fault Test ---");
        CPU faultCpu = new CPU();
        int illegalInstruction = (0x3F << 10); // Undefined Opcode 63
        
        faultCpu.cycle(); // 1. FETCH_1 (Sets memoryCycles=1)
        faultCpu.cycle(); // 2. STALL (Timer 1 -> 0)
        faultCpu.setMBR(illegalInstruction);
        faultCpu.cycle(); // 3. FETCH_2 (IR = MBR)
        faultCpu.cycle(); // 4. DECODE (Calls decodeAndExecute -> setMFR(1))

        System.out.println("Expected MFR: 1, Actual: " + faultCpu.getMFR());


        // --- TEST 3: STR SUCCESS ---
        System.out.println("\n--- Starting STR Test ---");
        CPU strCpu = new CPU();
        // STR 2, 0, 15 -> Opcode 02, Reg 2, Addr 15
        int strInstruction = 0x0A0F; 
        
        strCpu.cycle(); // 1. FETCH_1 (Sets memoryCycles=1)
        strCpu.cycle(); // 2. STALL
        strCpu.setMBR(strInstruction);
        strCpu.cycle(); // 3. FETCH_2
        strCpu.cycle(); // 4. DECODE
        strCpu.cycle(); // 5. COMPUTE_EA (effectiveAddress = 15)
        strCpu.cycle(); // 6. EXECUTE (MAR = 15, MBR = GPR[2])
        
        System.out.println("Expected MAR: 15, Actual: " + strCpu.getMAR());
        // Since GPR[2] is 0 by default, MBR should be 0 here.
        System.out.println("Expected MBR (GPR[2]): 0, Actual: " + strCpu.getMBR());
    }
}