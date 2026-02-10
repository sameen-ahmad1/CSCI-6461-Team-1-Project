## Part 0: Assembler Design & Documentation
Basic Design Notes
The assembler is designed as a Two-Pass Assembler to ensure all labels are resolved before the final machine code is generated.

1. Core Components & Functions
rangeCheck(int val, int max, String name)
     - Purpose: Validates that numerical inputs (registers, addresses, immediate values) fit within their designated bit-field limits
     - Logic: Compares the input val against the allowed max. If the value is out of bounds, it throws an IllegalArgumentException to prevent corrupted instructions

passOne(File file)
     - Purpose: Scans the source file to build a Symbol Table and calculate memory locations
     - Logic: Sets the initial locCounter to 0. Reads each line, strips comments (anything after a ;), and identifies labels (anything before a :).
     - Symbol Table: Stores unique labels and their current locCounter in a HashMap. It throws an error if a duplicate label is detected.
     - Location Counter: Increments the locCounter for every instruction or data line found. It also handles the LOC to jump the counter to a specific memory address.

passTwo(File file)
     - Purpose: 
     - Logic:


main(String[] args)
     - Purpose: Acts as the entry point for the application
     - Logic: Takes the assembly file path from the command line, creates a File object, and executes passOne followed by passTwo. It includes a global try-catch block to report any assembly errors to the user.


2. Design Principles
     - Separation: By splitting the process into two passes, the assembler can resolve "forward references" (where a jump instruction refers to a label that appears later in the file).
     - Validity: Extensive use of .trim(), .split(), and .replaceAll() ensures that the assembler is not sensitive to extra whitespace or varied formatting in the source file.
     - Standardized Output: All numerical values are converted to 6-digit octal strings using String.format("%06o", ...) to match the hardware's 16-bit architecture requirements.
     - Error Handling: The system performs range checks on all bit-fields (0-3 for registers, 0-31 for addresses) and prevents the use of duplicate or undefined labels.