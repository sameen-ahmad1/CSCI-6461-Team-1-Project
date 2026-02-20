import java.io.*;

public class test0 {

    public static String getBuffer(String filePath){

        StringBuilder buffer = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.lineSeparator()); // Add newline
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String content = buffer.toString();

        return content;

    }

    private static void test1(){

        //generate test data 
        String input = "./testFiles/test0_1/test.asm";

        //generate expected output
        String expectedLoadFile = getBuffer("./testFiles/test0_1/expectedLoad.txt");
        String expectedListingFile = getBuffer("./testFiles/test0_1/expectedListing.txt");

        //call the parser on the test data 
        Assembler.main(new String[]{input});

        //read in the outputs 
        String loadFile = getBuffer("load.txt");
        String listingFile = getBuffer("listing.txt");

        //compare outputs to expected outputs
        if(loadFile.equals(expectedLoadFile) && listingFile.equals(expectedListingFile)){

            System.out.println("Test 1 Passed");
            return;

        }

        System.out.println("Test 1 Failed");

    }

    private static void test2(){

        //generate test data 
        String input = "./testFiles/test0_1/test.asm";

        //generate expected output
        String expectedLoadFile = getBuffer("./testFiles/test0_2/expectedLoad.txt");
        String expectedListingFile = getBuffer("./testFiles/test0_2/expectedListing.txt");

        //call the parser on the test data 
        Assembler.main(new String[]{input});

        //read in the outputs 
        String loadFile = getBuffer("load.txt");
        String listingFile = getBuffer("listing.txt");

        //compare outputs to expected outputs
        if(!loadFile.equals(expectedLoadFile) && !listingFile.equals(expectedListingFile)){

            System.out.println("Test 1 Passed");
            return;

        }

        System.out.println("Test 1 Failed");

    }
    
    public static void main(String[] args) {

        test1();
        test2();

    }

}
