Group #1 Members: Sameen Ahmad, Zack Rahbar, Liza Mozolyuk, Wesam Abu Rabia

## Part 2: Design & Documentation

## Core Components & Functions

### CPU
- The CPU acts as the central state machine of the simulator, executing instructions through a multi-step cycle
- Instruction Cycle: Implements FETCH_1, FETCH_2, DECODE, COMPUTE_EA, EXECUTE, and other states
- Registers: Maintains four General Purpose Registers (GPR0–GPR3), three Index Registers (X1–X3), a Program Counter (PC), and an Instruction Register (IR)
- State Transition Logic: The CPU.java uses a switch statement within the tick() function. This ensures that a single clock moves only advances the CPU by one micro-operation (ex: MAR <- PC)
- I/O Handling: Implements IN and OUT instructions. The IN instruction is designed as a blocking operation, halting the CPU cycle until the GUI keyboard buffer provides data
- Every instruction is broken down into micro-operations that occur over multiple "ticks":
    - Instruction Fetch (IF): The PC is placed on the MAR. The MemoryBus is signaled for a read. The data returns to the MBR and is moved to the IR
    - Decode (ID): The 16-bit word in the IR is bit-masked to extract:
        - Opcode (Bits 10-15): Determines the operation
        - GPR (Bits 8-9): Selects one of four general-purpose registers
        - IXR (Bits 6-7): Selects one of three index registers
        - I-Bit (Bit 5): Determines if Indirect Addressing is required
        - Address (Bits 0-4): The base memory
    - Effective Address Calculation (EA): This is the core of the program1.asm array logic
    - Execute (EX): The ALU performs the operation (ex: SMR for subtraction or AIR for immediate addition)
    - Write-Back (WB): The result is stored back in a GPR or sent to the MemoryBus using a STR instruction

### Memory
- A memory module representing a fixed address space
- Memory is accessed using the MemoryBus interface, allowing the Cache to sit between the CPU and the physical RAM without changing CPU logic
- Internal Storage & Addressing
    - Capacity: 4096 words (Addressable from 0000 to 7777 in octal)
    - Word Size: 16-bits per address
    - Addressing Logic: The memory uses a 12-bit Memory Address Register (MAR) to select a location. Because the system is word-addressable, there is no need for byte-alignment logic, simplifying the data path between RAM and GPRs
- Key Memory Functions (Implementation)
    - The readWord(int address) Logic: When a read is requested, the memory must return the 16-bit integer at that index. If the address is out of bounds (above 4095), the system is designed to trigger a "Machine Check" error to prevent a simulation crash
    - The writeWord(int address, int value) Logic: This handles the STR (Store Register) and STX (Store Index Register) instructions
- Memory Design Principles
    - Instruction vs. Data Separation: Our design reserves addresses 0-5 for system pointers and 6-31 for constants, with code starting at 060
    - The IPL (Initial Program Load): A specific "Direct Access" method was created for the Loader. This allows the GUI to populate memory from a text file without the CPU needing to be "ON". This bypasses the Cache to ensure the initial state of the machine is clean.
    - Bus Interface: By implementing a MemoryBus interface, we successfully separated the CPU from the physical RAM. This allowed us to "plug in" the Cache module in Part 2 without modifying a single line of code in the CPU class. The CPU simply calls readWord(), and it doesn't care if that word comes from the Cache or the RAM



### Memory & Cache Interaction
- We implemented a Memory Architecture. The CPU communicates with a MemoryBus interface rather than the Memory class directly
- The CPU is "unaware" of the cache. When it requests a word, the Cache intercepts the request
- The "Hit" Path: If the address is in one of the 16 lines, data is returned in the same cycle
- The "Miss" Path: The Cache pauses the CPU, fetches a block from Main Memory, performs a FIFO eviction if necessary, and then resumes the CPU

### Cache
- The Cache subsystem sits between the CPU and main memory, storing recently accessed words so the CPU can retrieve them faster on subsequent accesses
- Built around three pieces: the `MemoryBus` interface that decouples the CPU from the memory layer, the `CacheLine` class representing a single cache slot, and the `Cache` class managing all 16 lines
- Write Policy: Write-Through. Every write to the cache is simultaneously written to main memory
- Eviction Policy: FIFO. When all 16 lines are full, the line that was inserted earliest is replaced first

### CacheLine
- CacheLine represents a single slot in the cache, holding a validity flag, a tag (the memory address being cached), the actual 16-bit data word, and a FIFO order counter for eviction priority
- Constructor CacheLine(): Initializes the line as empty (valid = false, tag = -1, data = 0, fifoOrder = 0), ensuring every slot starts clean before the cache is populated
- load(int tag, int data, int fifoOrder): Fills the line with a new address and data word on a cache miss. Marks the line valid and stamps it with the current FIFO counter. Data is masked to 16 bits to match hardware word width
- updateData(int data): Overwrites the stored data without touching the FIFO order, called on a write-hit to keep the cached copy consistent with memory
- invalidate(): Resets the line back to its initial empty state, called on IPL to ensure no stale data persists across program loads

### MemoryBus
- MemoryBus is the interface that allows the cache to be plugged in between the CPU and Memory without modifying either. The CPU only ever holds a MemoryBus reference so it remains unaware of whether it is talking to cache or memory directly
- readWord(int address) / writeWord(int address, int value): Core read and write operations. The CPU calls these without knowing if the result comes from cache or memory
- requestRead(int address) / requestWrite(int address): Hardware-level memory cycle triggers used by the CPU tick-based state machine for operations that resolve over multiple cycles
- tick(CPU cpu): Advances the memory subsystem by one clock cycle, allowing pending read/write operations to finalize and simulate hardware latency
- getCacheStatus(): Exposes cache line contents and hit/miss statistics to the GUI without the CPU needing direct knowledge of the cache implementation
- postError(String message) / getErrors(): Lightweight fault reporting channel through the bus so errors can be surfaced to the CPU or GUI without throwing exceptions

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