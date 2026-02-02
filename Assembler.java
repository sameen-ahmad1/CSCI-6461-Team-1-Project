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
// 3. Use the split command to break the line into its parts
// 4. Process the line, if it is a label, add the label to a dictionary with the code location. Process
// the rest of the line (it could be blank, if so no code is generated). Check for errors in the
// code.
// 5. If code or data was generated increment the code location and go to step 2 until
// termination.

// Pass 2:
// 1. Set code location to 0
// 2. Read a line of the file
// 3. Use the split command to break the line into it parts
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
            String line;
            //while it is not null
            while ((line = readFile.readLine()) != null) 
            {
                //split by the semicolon
                String[] lineSplit = line.split(";");
                line = lineSplit[0].trim();
                
                //if the current line being read from the file is a blank line, just move to the next line
                if (line.isEmpty()) 
                {
                    //just continue to the next one
                    continue; 
                }
                
                //if the line has a colon
                if (line.contains(":")) 
                {
                    //splits the line into two parts
                    //everything before the colon is the label name
                    //everything after the colon are the instruction
                    String[] labelSplit = line.split(":");
                    //takes the first part of that split which is the label
                    String name = labelSplit[0].trim();

                    //put the label name and the location to the table
                    table.put(name, locationCounter);

                    //if there is instruction after the colon
                    if (labelSplit.length > 1) 
                    {
                        //gets the instruction
                        line = labelSplit[1].trim();
                    } 
                    //there is nothing after
                    else 
                    {
                        //just set to empty string
                        line = "";
                    }
                }

                

                //if the instruction is LOC
                if (line.toUpperCase().startsWith("LOC")) 
                {
                    //found \\s+ from Google, it says that split by one or more whitespace characters (\\s+)
                    String[] parts = line.split("\\s+");
                    //set the location counter to that value 
                    locCounter = Integer.parseInt(parts[1]);
                } 
                else if (!line.isEmpty()) 
                {
                    //increase by 1
                    locCounter += 1;
                }

            }
        }
    }



    //pass TWO will go here
    private static void passTwo(File file) throws IOException 
    {
        
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
            System.out.println("Assembler successful!! Created listing.txt");
        }
        //if there is an error, it'll output the error 
        catch (Exception e) 
        {
            System.err.println("Error: " + e.getMessage());
        }
    }




}
