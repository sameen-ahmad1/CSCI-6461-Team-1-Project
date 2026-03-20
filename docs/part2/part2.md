Group #1 Members: Sameen Ahmad, Zack Rahbar, Liza Mozolyuk, Wesam Abu Rabia

## Part 2: Design & Documentation

## Core Components & Functions

### CPU
- The CPU acts as the central state machine of the simulator, executing instructions through a multi-step cycle
- Instruction Cycle: Implements FETCH_1, FETCH_2, DECODE, COMPUTE_EA, EXECUTE, and other states.
- Registers: Maintains four General Purpose Registers (GPR0–GPR3), three Index Registers (X1–X3), a Program Counter (PC), and an Instruction Register (IR)
- State Transition Logic: The CPU.java uses a switch statement within the tick() function. This ensures that a single clock pulse only advances the CPU by one micro-operation (ex: MAR <- PC)
- I/O Handling: Implements IN and OUT instructions. The IN instruction is designed as a blocking operation, halting the CPU cycle until the GUI keyboard buffer provides data.
- Every instruction is broken down into micro-operations that occur over multiple "ticks":
    - Instruction Fetch (IF): The PC is placed on the MAR. The MemoryBus is signaled for a read. The data returns to the MBR and is moved to the IR
    - Decode (ID): The 16-bit word in the IR is bit-masked to extract:
        - Opcode (Bits 10-15): Determines the operation
        - GPR (Bits 8-9): Selects one of four general-purpose registers
        - IXR (Bits 6-7): Selects one of three index registers
        - I-Bit (Bit 5): Determines if Indirect Addressing is required
        - Address (Bits 0-4): The base memory displacement
    - Effective Address Calculation (EA): This is the core of the program1.asm array logic
    - Execute (EX): The ALU performs the operation (ex: SMR for subtraction or AIR for immediate addition)
    - Write-Back (WB): The result is stored back in a GPR or sent to the MemoryBus using a STR instruction

### Memory
- A simplified memory module representing a fixed address space
- Memory is accessed using the MemoryBus interface, allowing the Cache to sit between the CPU and the physical RAM without changing CPU logic
- Logical vs. Physical: The CPU sees "Logical Memory" (the MemoryBus interface). The Cache provides the physical implementation
- The IPL (Initial Program Load) button triggers a specialized "Direct Write" mode that bypasses CPU cycles to rapidly populate the memory from the .asm file


### Memory & Cache Interaction
- We implemented a Memory Architecture. The CPU communicates with a MemoryBus interface rather than the Memory class directly
- The CPU is "unaware" of the cache. When it requests a word, the Cache intercepts the request
- The "Hit" Path: If the address is in one of the 16 lines, data is returned in the same cycle
- The "Miss" Path: The Cache pauses the CPU, fetches a block from Main Memory, performs a FIFO eviction if necessary, and then resumes the CPU

### Cache
- 
- Write Policy: Write-Through. Every write to the cache is simultaneously written to main memory to ensure data integrity

### I/O Subsystem (The Keyboard & Printer)


### GUI


## Design Principles

### System Initialization Flow (IPL)
- File Parsing: The user selects a .asm or .txt file using the Program File field
- Memory Loading: The loader parses each line, writing instructions directly to memory addresses
- Cache Warm-up: Initial writes during IPL populate the cache lines, visible in the Idx table immediately after loading
- Register Reset: PC is set to the first instruction address, and all GPRs/IXRs are cleared

### Printer Logging (Newest at Top)
- To provide the best user experience, the system implements a Prepend Logic for logs
- Buffer Management: The MemoryBus collects cycle logs into a StringBuilder
- Ordering: When updateTexts() is called, the new block of logs is added to the front of the existing text string (newLogs + history)
- Internal Chronology: While the newest cycles are at the top, the lines within a cycle (Fetch -> Decode -> Execute) maintain their natural top-to-bottom reading order for clarity

### Error & Debug Messages
- Invalid Opcode Detection: If the IR receives a word that doesn't match a known instruction, the system triggers a "Machine Check" error displayed in the Printer
- Cache Detection: By using a Write-Through policy, we ensure that the "Total Hits/Misses" stats remain accurate even if the user manually changes memory values using the GUI
- Hardware Trace: Provides real-time feedback on CACHE HIT, CACHE MISS, and CACHE EVICT events in the terminal
- Error Trace: Any errors are printed to the GUI, such as any Assemble Errors or CPU Halts
- Execution States: Explicitly labels the current hardware state (ex: COMPUTE_EA) to aid in debugging assembly logic

### System Flow (Program 1 Execution)
- Load Phase: .asm file is parsed; addresses 0-31 are filled with constants/pointers, and address 60+ is filled with opcodes
- Input Phase: CPU loops 20 times, halting at each IN instruction for user data
- Processing Phase: CPU iterates through memory addresses 32-52, performing subtraction (SMR) and absolute value logic to find the minimum difference
- Output Phase: The closest_index is used to calculate the final memory address, and the result is pushed to the Printer