Group #1 Members: Sameen Ahmad, Zack Rahbar, Liza Mozolyuk, Wesam Abu Rabia

## Part 1: Design & Documentation

## Core Components & Functions
The system is designed as a cycle simulation of a 16-bit processor, emphasizing the separation between instruction decoding, state management, and memory.

### CPU
- CPU Constructor: public CPU(Memory memory)
    -  Functionality: This is the initialization phase. It connects the CPU into the Memory unit
    - Role: It stores a reference to the Memory object and sets the initial state to FETCH_1. Without this, the CPU would have no "storage" to read instructions from

- public void cycle()
    - Functionality: This is the System Clock of the simulation
    - Role:
        - It calls memory.tick(this) to finalize any pending memory transfers
        - It manages the Timing Stall so if memoryCycles > 0, it decrements the counter and exits, effectively pausing the CPU to wait for the slower memory hardware
        - It contains the Main State Machine (switch (curState)), which moves the CPU through the small steps of an instruction

- private void calculateEA()
    - Functionality: This is the Address Generation Unit
    - Role: It looks at the bit-fields decoded from the instruction to determine exactly where in memory the data is
        - Indexing: It adds the value of an Index Register (IX) to the base address if required
        - Indirection: If the I bit is set, it redirects the CPU to the INDIRECT_1 state to fetch the real address from memory
        - Masking: It ensures the resulting address fits within the 11 or 12-bit memory limit using MASK_12

- private void decodeAndExecute()
    - Functionality: This is the Control Unit
    - Role: It passes the 16-bit IR to the Decoder to divide it into Opcode, Register, Index, and Address fields
        - It determines the Routing based on the Opcode, it decides if the CPU needs to go to COMPUTE_EA (for Loads/Stores) or HALT (for errors/end of program).
        - Fault Protection: It wraps the decoding in a try-catch to catch illegal opcodes immediately

- private void handleExecute()
    - Functionality: This is the Execution Stage for memory-access instructions
    - Role:
        - For Loads (LDR/LDX): It sets the MAR and calls memory.requestRead()
        - For Stores (STR/STX): It moves data from a register into the MBR and calls memory.requestWrite()
        - For LDA: It performs an immediate move of the address value into a register without touching memory

- public void listRegisters()
    - Functionality: This is the Debugging/Console Output
    - Role: It prints a snapshot of every register in Octal, Hex, and Decimal. This is essential for the User Console requirement, showing the user exactly what the machine did after a Single Step

### How they work together (The Flow)
- cycle() starts in FETCH_1
- memory.requestRead() is called using the PC
- cycle() stalls for one tick
- memory.tick() puts the instruction in the MBR
- decodeAndExecute() identifies the command
- calculateEA() finds the target address
- handleExecute() moves the data
- listRegisters() shows you the result


## Design Principles
### CPU
- State Machine: The CPU utilizes an enum State to break down instructions into discrete micro-steps (FETCH_1, DECODE, COMPUTE_EA, more.). This allows the simulation to pause (stall) without blocking the entire system thread, enabling future GUI integration and real-time stepping
- Addressing Mode Flexibility: The calculateEA() method centralizes the logic for:
     - Direct Addressing: Using the immediate address field
     - Indexed Addressing: Adding contents of Index Registers (EA = Addr + IX[x])
     - Indirect Addressing: A multi-cycle process where the CPU fetches an address from memory to find the final target data
- Hardware Masking: All register values are passed through bit-masks. This ensures that if a calculation exceeds the physical bit-width of the register, it "wraps around" exactly as physical transistors would, preventing Java's 32-bit integers from hiding logic errors
- Error Handling: The design uses MachineFault exception. If the Decoder finds an unknown opcode or the Memory receives an address beyond SIZE, the CPU catches the fault, logs the ID in the MFR, and enters a HALT state to prevent data corruption