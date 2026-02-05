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
        PrintWriter writeToFile = new PrintWriter(new FileWriter("output.txt"));

        //read each line from the file
        String fileLine;
        while ((fileLine = readFile.readLine()) != null) 
        {
            //split by the ; so it reads each line
            String line = fileLine.split(";")[0].trim();
            
            
            //if its a blank line or just labels after continue over it
            if (line.isEmpty() || (line.contains(":") && line.split(":").length <= 1)) 
            {
                //write to the file, \t is for tabs for spacing purposes
                writeToFile.println("\t\t\t" + fileLine);
                continue;
            }

            //if the line starts with LOC
            if (line.toUpperCase().startsWith("LOC")) 
            {
                //split into parts by the spaces
                String[] splitByParts = line.split("\\s+");

                //set the current location to the value
                currentLoc = Integer.parseInt(splitByParts[1]);
                //write to the file to keep track
                writeToFile.println("\t\t\t" + fileLine);
                continue;
            }

            
            //this checks if the line contains a colon, which means there is a label
            if (line.contains(":")) 
            {
                // [1] is the actual instruction
                line = line.split(":")[1].trim();
            }

            

            //parse now for binary conversions
            String[] binaryParts = line.split("\\s+"); 
            //get the operation name
            String operationName = binaryParts[0].toUpperCase();

            //set the instruction for the conversion
            int instructions = 0;

            if (operationName.equals("DATA")) 
            {
                
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
                Isa.Instruction instr = Isa.Instruction.valueOf(operationName);


                //the ppcode must be the first 6 bits of the 16-bit word
                //in Java, an integer is 32 bits
                //to move the 6-bit opcode into the correct position for a 16-bit word, we need to shift it 10 spaces to the left
                //10 bits will be filled with Register (2 bits), Index Register (2 bits), Indirect bit (1 bit), and Address (5 bits)
                instructions = (instr.opcode << 10); 


                //if there are instructions
                if (binaryParts.length > 1) 
                {
                    //finds the operands and splits them 
                    String[] operands = binaryParts[1].split(",");
                    
                    //if it matches the format
                    if (instr.format == Isa.Format.R_X_ADDR_I) 
                    {
                        int memAddr;

                        // [Op:6][R:2][IX:2][I:1][memAddr:5] 
                        //gets the first operand
                        int r = Integer.parseInt(operands[0].trim());
                        //gets the second operand
                        int ix = Integer.parseInt(operands[1].trim());
                        
                        //memory address
                        //gets the memory address
                        String memaddstring = operands[2].trim();

                        
                        //if the table has it
                        if (table.containsKey(memaddstring)) 
                        {
                            //get the memory address
                            memAddr = table.get(memaddstring);
                        } 
                        else 
                        {
                            //parse to get the int
                            memAddr = Integer.parseInt(memaddstring);
                        }

                        

                        //get the indirect bit
                        int indirectBit = 0;
                        if (operands.length == 4) 
                        {
                            indirectBit = Integer.parseInt(operands[3].trim());
                        }

                        //everything has a Left-Shift 
                        //register is shifted by 8 since it is 2 bits
                        instructions = instructions | (r << 8);     
                        //index is shifted by 6 since it is 2 bits
                        instructions = instructions | (ix << 6);  
                        //indirect bit is shifted by 5 since it is only one bit
                        instructions = instructions | (indirectBit << 5); 
                        //memory address is remaining 5 bits
                        //0x1F (binary 11111) makes sure that the address doesn't go above the 5-bit limit
                        instructions = instructions | (memAddr & 0x1F);
                    }
                }
            }

            //convert them
            //%06o is for making it an octal number (o), make it 6 characters wide (6), fill any empty spaces with zeros
            String octaladdress = String.format("%06o", currentLoc);
            String octalvalue = String.format("%06o", instructions);

            //write this to the file 
            writeToFile.printf("%s\t%s\t%s%n", octaladdress, octalvalue, fileLine);

            //increase the location count
            currentLoc = currentLoc + 1;
        }

        //close the files
        writeToFile.close();
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
