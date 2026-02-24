package assembler;
import java.io.*;
import java.util.*;

//FROM PAGE 24 of the instruction
// Building the Assembler
// The following hints are provided for building the assembler.
// Use two passes
// Eary assemblers read the program twice.
// Pass 1:
// 1. Set code location to 0
// 2. Read a line of the file
// 3. Use the split command to break the line into its splitByParts
// 4. Process the line, if it is a label, add the label to a dictionary with the code location. Process
// the rest of the line (it could be blank, if so no code is generated). Check for errors in the
// code.
// 5. If code or data was generated increment the code location and go to step 2 until
// termination.

// Pass 2:
// 1. Set code location to 0
// 2. Read a line of the file
// 3. Use the split command to break the line into it splitByParts
// 4. Convert the code according to the second field.
// 5. Add line to listing file and to load file.
// 6. If code or data generated, increment the code counter, and go to step2 until termination.

// A two pass assembler is a simpler form from the standpoint of tracing errors and separating
// functionality.



// Assembler for 16-bit ISA
// Format: Opcode(6) R(2) IX(2) I(1) Address(5)
public class Assembler 
{
    //store 
    private static final Map<String, Integer> table = new HashMap<>();

    //error checker make sure that the values stay within bit limits
    private static void rangeCheck(int val, int max, String name) 
    {
        if (val < 0 || val > max) 
        {
            throw new IllegalArgumentException(name + " value " + val + " is out of range (0-" + max + ")");
        }
    }

    //first pass
    private static void passOne(File file) throws IOException 
    {
        //location is set to 0, from his instructions 
        int locCounter = 0;

        //read file from the buffer
        try (BufferedReader readFile = new BufferedReader(new FileReader(file))) 
        {
            //read line by line
            String fileLine;
            //while it is not null
            while ((fileLine = readFile.readLine()) != null) 
            {
                //split by the semicolon
                String[] lineSplit = fileLine.split(";");
                fileLine = lineSplit[0].trim();
                
                //if the current line being read from the file is a blank line, just move to the next line
                if (fileLine.isEmpty()) 
                {
                    //just continue to the next one
                    continue; 
                }
                
                //if the line has a colon
                if (fileLine.contains(":")) 
                {
                    //splits the line into two splitByParts
                    //everything before the colon is the label name
                    //everything after the colon are the instruction
                    String[] labelSplit = fileLine.split(":");
                    //takes the first part of that split which is the label
                    String name = labelSplit[0].trim();


                    //ERROR CHECK: Duplicate labels
                    if (table.containsKey(name)) 
                    {
                        throw new IllegalArgumentException("Duplicate label found: " + name);
                    }

                    //put the label name and the location to the table
                    table.put(name, locCounter);

                    //if there is instruction after the colon
                    if (labelSplit.length > 1) 
                    {
                        //gets the instruction
                        fileLine = labelSplit[1].trim();
                    } 
                    //there is nothing after
                    else 
                    {
                        //just set to empty string
                        fileLine = "";
                    }
                }

                

                //if the instruction is LOC
                if (fileLine.toUpperCase().startsWith("LOC")) 
                {
                    //found \\s+ from Google, it says that split by one or more whitespace characters (\\s+)
                    String[] instructionParts = fileLine.split("\\s+");
                    //set the location counter to that value 
                    locCounter = Integer.parseInt(instructionParts[1]);
                } 
                else if (!fileLine.isEmpty()) 
                {
                    //increase by 1
                    locCounter += 1;
                }

            }
        }
    }



    //pass TWO will go here, coverts into 16-bit octal
private static void passTwo(File file) throws IOException
{
    //current location counter
    int currentLoc = 0;

    //read the files
    BufferedReader readFile = new BufferedReader(new FileReader(file));

    //create the new file to write the output to
    PrintWriter listingWriter = new PrintWriter(new FileWriter("listing.txt"));
    PrintWriter loadWriter    = new PrintWriter(new FileWriter("load.txt"));


    //read each line from the file
    String fileLine;
    while ((fileLine = readFile.readLine()) != null)
    {
        //split by the ; so it reads each line
        String line = fileLine.split(";")[0].trim();

        //if its a blank line or just labels after continue over it
        if (line.isEmpty() || (line.contains(":") && line.split(":").length <= 1))
        {
            continue;
        }

        //if the line starts with LOC
        if (line.toUpperCase().startsWith("LOC"))
        {
            //split into parts by the spaces
            String[] splitByParts = line.split("\\s+");
            currentLoc = Integer.parseInt(splitByParts[1]);
            continue;
        }

            
        //this checks if the line contains a colon, which means there is a label
        if (line.contains(":"))
        {
            // [1] is the actual instruction
            line = line.split(":")[1].trim();
        }

        // split into whitespace tokens
        String[] binaryParts = line.split("\\s+");
        String operationName = binaryParts[0].toUpperCase();

        int instructions = 0;

        String operandText = "";
        if (binaryParts.length > 1) {
            operandText = String.join(" ", Arrays.copyOfRange(binaryParts, 1, binaryParts.length)).trim();
        }
        // remove all whitespace so "3, 0, 15" -> "3,0,15"
        operandText = operandText.replaceAll("\\s+", "");
        String[] operands = operandText.isEmpty() ? new String[0] : operandText.split(",");

        if (operationName.equals("DATA"))
        {
            if (operands.length < 1) {
                throw new IllegalArgumentException("DATA missing value on line: " + fileLine);
            }

                //if its the data, then get the value of it
                String val = binaryParts[1];

                //check to see if the value is a label thats already in the table
            if (table.containsKey(val))
            {
                //get the memory address if its already there
                instructions = table.get(val);
            }
            else
            {
                //convert it into the int
                instructions = Integer.parseInt(val);
            }
        }
        else
        {
                
            //map the opcode to octal, find it from Isa.java file
            //it gets all the information stored for that specific instruction

            // ERROR CHECK: Check if opcode exists in Isa.java
            Isa.Instruction instr;
            try 
            {
                instr = Isa.Instruction.valueOf(operationName);
            } 
            catch (IllegalArgumentException e) 
            {
                throw new IllegalArgumentException("Opcode NOT allowed '" + operationName + "' on line: " + fileLine);
            }
            


            //the oppcode must be the first 6 bits of the 16-bit word
            //in Java, an integer is 32 bits
            //to move the 6-bit opcode into the correct position for a 16-bit word, we need to shift it 10 spaces to the left
            instructions = (instr.opcode << 10);

            // encode line based on format defined in Isa.java
            switch (instr.format)
            {
                case NONE -> {
                    // HLT: no operands
                    if (operands.length != 0) 
                    {
                        throw new IllegalArgumentException(instr.mnemonic + " takes no operands: " + fileLine);
                    }
                }

                case TRAP_CODE -> {
                    // TRAP code
                    if (operands.length != 1) {
                        throw new IllegalArgumentException("TRAP requires 1 operand (code): " + fileLine);
                    }
                    int code = Integer.parseInt(operands[0]);
                    // table max 16 => 0..15
                    if (code < 0 || code > 15) {
                        throw new IllegalArgumentException("TRAP code out of range (0..15): " + fileLine);
                    }
                    rangeCheck(code, 15, "TRAP Code");
                    instructions |= (code & 0x1F);
                }

                case R_X_ADDR_I -> {
                    // r, x, address[, I]
                    if (!(operands.length == 3 || operands.length == 4)) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires r,x,address[,I]: " + fileLine);
                    }

                    int r  = Integer.parseInt(operands[0]);
                    int ix = Integer.parseInt(operands[1]);

                    String addrToken = operands[2];
                    int memAddr = table.containsKey(addrToken) ? table.get(addrToken) : Integer.parseInt(addrToken);

                    int indirectBit = (operands.length == 4) ? Integer.parseInt(operands[3]) : 0;

                    rangeCheck(r, 3, "Register/CC/FR");
                    rangeCheck(ix, 3, "Index Register");
                    rangeCheck(memAddr, 31, "Address");


                    instructions |= (r & 0x3) << 8;
                    instructions |= (ix & 0x3) << 6;
                    instructions |= (indirectBit & 0x1) << 5;
                    instructions |= (memAddr & 0x1F);
                }

                case CC_X_ADDR_I -> {
                    // cc, x, address[, I]
                    if (!(operands.length == 3 || operands.length == 4)) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires cc,x,address[,I]: " + fileLine);
                    }

                    int cc = Integer.parseInt(operands[0]);
                    int ix = Integer.parseInt(operands[1]);

                    String addrToken = operands[2];
                    int memAddr = table.containsKey(addrToken) ? table.get(addrToken) : Integer.parseInt(addrToken);

                    int indirectBit = (operands.length == 4) ? Integer.parseInt(operands[3]) : 0;


                    instructions |= (cc & 0x3) << 8;          // CC uses R field
                    instructions |= (ix & 0x3) << 6;
                    instructions |= (indirectBit & 0x1) << 5;
                    instructions |= (memAddr & 0x1F);
                }

                case X_ADDR_I -> {
                    // x, address[, I]
                    if (!(operands.length == 2 || operands.length == 3)) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires x,address[,I]: " + fileLine);
                    }

                    int ix = Integer.parseInt(operands[0]);

                    String addrToken = operands[1];
                    int memAddr = table.containsKey(addrToken) ? table.get(addrToken) : Integer.parseInt(addrToken);

                    int indirectBit = (operands.length == 3) ? Integer.parseInt(operands[2]) : 0;

                    rangeCheck(ix, 3, "Index Register");
                    rangeCheck(memAddr, 31, "Address");

                    // R field stays 0
                    instructions |= (ix & 0x3) << 6;
                    instructions |= (indirectBit & 0x1) << 5;
                    instructions |= (memAddr & 0x1F);
                }

                case R_IMM -> {
                    // r, immed  (AIR/SIR)
                    if (operands.length != 2) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires r,immed: " + fileLine);
                    }

                    int r = Integer.parseInt(operands[0]);
                    int imm = Integer.parseInt(operands[1]);

                    rangeCheck(r, 3, "Register");
                    rangeCheck(imm, 31, "Immediate Value");

                    instructions |= (r & 0x3) << 8;
                    instructions |= (imm & 0x1F); // 5-bit imm
                }

                case IMM -> {
                    // immed (RFS)
                    if (operands.length != 1) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires immed: " + fileLine);
                    }

                    int imm = Integer.parseInt(operands[0]);
                    rangeCheck(imm, 31, "Immediate Value");
                    instructions |= (imm & 0x1F);
                }

                case RX_RY -> {
                    // rx, ry (MLT/DVD/TRR/AND/ORR)
                    if (operands.length != 2) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires rx,ry: " + fileLine);
                    }

                    int rx = Integer.parseInt(operands[0]);
                    int ry = Integer.parseInt(operands[1]);

                    rangeCheck(rx, 3, "Rx");
                    rangeCheck(ry, 3, "Ry");

                    // Use R slot for rx, IX slot for ry
                    instructions |= (rx & 0x3) << 8;
                    instructions |= (ry & 0x3) << 6;
                }

                case RX_ONLY -> {
                    // rx (NOT)
                    if (operands.length != 1) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires rx: " + fileLine);
                    }

                    int rx = Integer.parseInt(operands[0]);
                    rangeCheck(rx, 3, "Rx");
                    instructions |= (rx & 0x3) << 8;
                }

                case IO_R_DEVID -> {
                    // r, devid (IN/OUT/CHK)
                    if (operands.length != 2) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires r,devid: " + fileLine);
                    }

                    int r = Integer.parseInt(operands[0]);
                    int dev = Integer.parseInt(operands[1]);

                    rangeCheck(r, 3, "Register");
                    rangeCheck(dev, 31, "Device ID");
                    instructions |= (r & 0x3) << 8;
                    instructions |= (dev & 0x1F);
                }

                case FR_X_ADDR_I -> {
                    // fr, x, address[, I]  (FADD/FSUB/VADD/VSUB/LDFR/STFR)
                    if (!(operands.length == 3 || operands.length == 4)) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires fr,x,address[,I]: " + fileLine);
                    }

                    int fr = Integer.parseInt(operands[0]);
                    int ix = Integer.parseInt(operands[1]);

                    String addrToken = operands[2];
                    int memAddr = table.containsKey(addrToken) ? table.get(addrToken) : Integer.parseInt(addrToken);

                    int indirectBit = (operands.length == 4) ? Integer.parseInt(operands[3]) : 0;

                    instructions |= (fr & 0x3) << 8;          // FR uses R field
                    instructions |= (ix & 0x3) << 6;
                    instructions |= (indirectBit & 0x1) << 5;
                    instructions |= (memAddr & 0x1F);
                }

                case SHIFT_ROT -> {
                    // r, count, L/R, A/L (SRC/RRC)
                    // This assumes [OP:6][R:2][A/L:1][L/R:1][COUNT:4][00:2]
                    if (operands.length != 4) {
                        throw new IllegalArgumentException(instr.mnemonic + " requires r,count,L/R,A/L: " + fileLine);
                    }

                    int r     = Integer.parseInt(operands[0]);
                    int count = Integer.parseInt(operands[1]);
                    int lr    = Integer.parseInt(operands[2]); // 0/1
                    int al    = Integer.parseInt(operands[3]); // 0/1

                    rangeCheck(r, 3, "Register");
                    rangeCheck(count, 15, "Count");
                    // rebuild lower bits for this format
                    instructions = (instr.opcode << 10);
                    instructions |= (r & 0x3) << 8;
                    instructions |= (al & 0x1) << 7;
                    instructions |= (lr & 0x1) << 6;
                    instructions |= (count & 0xF) << 2; // 4-bit count into bits 5..2
                }
            }
        }

        //convert them, %06o is for making it an octal number (o), make it 6 characters wide (6), fill any empty spaces with zeros
        String octaladdress = String.format("%06o", currentLoc);
        String octalvalue   = String.format("%06o", (instructions & 0xFFFF));

        //write this to the file
        //writeToFile.printf("%s\t%s\t%s%n", octaladdress, octalvalue, fileLine);

        // %-8s creates a left-aligned column exactly 8 characters wide
        loadWriter.println(octaladdress + "\t" + octalvalue);
        listingWriter.println(octaladdress + "\t" + octalvalue + "\t" + fileLine);



        //increase the location count
        currentLoc = currentLoc + 1;
    }

    //close the files
    listingWriter.close();
    loadWriter.close();
    readFile.close();

}


    //take the file path from the terminal and run the two functions
    public static void main(String[] args) 
    {
        //sanity check, if there is nothing 
        if (args.length < 1) 
        {
            //return out
            return;
        }

        //get the file path
        String input = args[0];
        File file = new File(input);

        try 
        {
            //pass 1 so read the file and set
            passOne(file);
            //pass 2 so convert the instructions to octal
            passTwo(file);
            //if everything works as intended, it'll print that its successful 
            System.out.println("Assembler successful!!");
        }
        //if there is an error, it'll output the error 
        catch (Exception e) 
        {
            System.err.println("Error: " + e.getMessage());
        }
    }




}
