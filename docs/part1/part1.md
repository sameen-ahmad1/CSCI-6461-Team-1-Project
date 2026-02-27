Group #1 Members: Sameen Ahmad, Zack Rahbar, Liza Mozolyuk, Wesam Abu Rabia

## Part 1: Design & Documentation

## Core Components & Functions
The system is designed as a cycle simulation of a 16-bit processor, emphasizing the separation between instruction decoding, state management, and memory.

### CPU
The CPU is implemented as a Cycle-Accurate State Machine. It manages the instruction lifecycle through discrete micro-steps, allowing for hardware-accurate timing and future GUI stepping.

- cycle() (System Clock): Triggers memory.tick().
    - Manages memoryCycles to simulate hardware latency (waiting 2 cycles for a read).
    - Advances the curState (FETCH, DECODE, EXECUTE, etc.).

- decode() (Control Unit): Uses the Decoder class to parse the 16-bit IR into Opcode, R, X, I, and Address fields.
    - Implements Fault Protection; if an illegal opcode is detected, it transitions the machine to HALT and sets the MFR.

- calculateEA() (Address Generation): Delegates logic to the Executor to determine the Effective Address.
    - Handles Indirect Bit (I): If set, the CPU transitions to INDIRECT_1 to fetch the actual address from memory.

- handleExecute(): The main part for instruction-specific logic.
    - Loads (LDR, LDX): Sets MAR and requests a memory read.
    - Stores (STR, STX): Moves register data to MBR and requests a memory write.
    - LDA: Performs an immediate move of the EA into a register.
    - listRegisters(): Provides a formatted snapshot of the internal state (PC, MAR, MBR, IR, GPRs, IXs) in Octal, Hex, and Decimal.

### Executor & Decoder
To maintain clean code, logic for bit-parsing and instruction execution has been separated from the main CPU class.

- Decoder.Decoded: A data structure holding the 16-bit instruction broken into its constituent fields (Opcode, Register, Index, Indirect, Address).
- Executor.calculateEA()
    - Applies a 12-bit mask (0x7FF) to ensure the address stays within memory bounds
- Executor.finishLoad(): 
    - Handles the writing back phase. Once memory provides data to the MBR, this method updates the target GPR or IX.

- Executor class:
    - Functionality:
    - Role:

### How they work together (The Flow)
- cycle() starts in FETCH_1
- memory.requestRead() is called using the PC
- cycle() stalls for one tick
- memory.tick() puts the instruction in the MBR
- decode() identifies the instruction
- calculateEA() finds the target address
- handleExecute() moves the data
- listRegisters() shows you the result


### Memory
- Memory Constructor: public Memory(int size)
    - Functionality: Initializes the memory array and prepares it for read/write operations.
    - Role: Acts as the main storage unit of the system, containing instructions, data, and reserved system locations.

- public void requestRead(int address)
    - Functionality: Begins a memory read operation.
    - Role: Stores the requested address and prepares the system to transfer the value during the next memory tick, simulating hardware latency.

- public void requestWrite(int address, int value)
    - Functionality: Begins a memory write operation.
    - Role: Stores the value temporarily and schedules it to be written during the next memory tick cycle.

- public void tick(CPU cpu)
    - Functionality: Finalizes pending memory operations.
    - Role: If a read was requested, transfers memory[address] into the CPU’s MBR. If a write was requested, updates memory[address] with the MBR value.

- Memory Protection
    - Functionality: Validates all memory addresses before access.
    - Role: Prevents illegal memory access outside the valid range (0 – SIZE). If an address exceeds bounds, a MachineFault is triggered.

- Reserved Memory
    - Functionality: Protects specific low memory addresses reserved for trap tables and fault routines.
    - Role: Prevents user programs from overwriting system routines. Any illegal write attempt triggers a MachineFault.


### Machine Fault
- MachineFault Exception
    - Functionality: Custom exception used to signal hardware-level faults.
    - Role: Allows the CPU to handle illegal operations safely instead of crashing.

- Machine Fault Register (MFR)
    - Functionality: Stores a numeric fault code identifying the type of failure.
    - Role: Provides state tracking and debugging information when a fault occurs.

- Fault Conditions
    - Illegal Memory Address
    - Memory Protection Violation
    - Illegal Opcode
    - Illegal Trap Code

- Fault Handling
    - Role:
        - CPU catches the exception
        - The fault ID is stored in the MFR
        - The CPU transitions to the HALT state to prevent further corruption


### ROM Loader
- ROM Loader Constructor: public RomLoader(Memory memory)
    - Functionality: Loads predefined system instructions into reserved memory during initialization.
    - Role: Simulates power-on behavior by preloading trap handlers and machine fault routines before user programs execute.

- Responsibilities
    - Populate reserved memory locations
    - Load trap table entries
    - Load machine fault handler routine
    - Ensure protected memory is initialized before user programs load

- System Initialization Flow
    - Memory is created
    - ROM Loader loads system routines
    - Program Loader loads user instructions
    - CPU begins execution at the program start address     

## Design Principles
### CPU
- State Machine: The CPU utilizes an enum State to break down instructions into discrete micro-steps (FETCH_1, DECODE, COMPUTE_EA, more.). This allows the simulation to pause (stall) without blocking the entire system thread, enabling future GUI integration and real-time stepping
- Addressing Mode Flexibility: The calculateEA() method centralizes the logic for:
     - Direct Addressing: Using the immediate address field
     - Indexed Addressing: Adding contents of Index Registers (EA = Addr + IX[x])
     - Indirect Addressing: A multi-cycle process where the CPU fetches an address from memory to find the final target data
- Hardware Masking: All register values are passed through bit-masks. This ensures that if a calculation exceeds the physical bit-width of the register, it "wraps around" exactly as physical transistors would, preventing Java's 32-bit integers from hiding logic errors
- Error Handling: The design uses MachineFault exception. If the Decoder finds an unknown opcode or the Memory receives an address beyond SIZE, the CPU catches the fault, logs the ID in the MFR, and enters a HALT state to prevent data corruption

### System Initialization Flow (IPL)
- Memory Reset: Clears all memory cells.
- Register Clear: PC, MAR, MBR, and all GPRs/IXs are zeroed.
- Bootstrap Load: directWrite is used to insert a small program starting at Octal 010.
- State Initialization: curState is set to FETCH_1.
