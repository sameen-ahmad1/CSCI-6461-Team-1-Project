import assembler.Assembler;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import memory.CPU;
import memory.simple.Memory;

public class gui extends JFrame{

    private boolean isRunning = false;
    private CPU cpu;
    private Memory memory = new Memory();

    // GPR displays
    private JTextField zeroTextGPR, oneTextGPR, twoTextGPR, threeTextGPR;

    // IXR displays
    private JTextField oneTextIXR, twoTextIXR, threeTextIXR;

    // Other register displays
    private JTextField pcText, marText, mbrText, irText, ccText, mfrText;

    // Misc
    private JTextField binary, octalInput, programFile;
    private JTextArea cacheContent, printer;
    private JTextField consoleInput;

    public gui(){

        JPanel outer = new JPanel(new BorderLayout(10,10));

        outer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel firstNorth = new JPanel(new BorderLayout(10,10));
        JPanel firstCenter = new JPanel(new BorderLayout(10,10));
        JPanel firstEast = new JPanel(new BorderLayout(10,10));

        outer.add(firstNorth, BorderLayout.NORTH);
        outer.add(firstCenter, BorderLayout.CENTER);
        outer.add(firstEast, BorderLayout.EAST);

        JLabel title = new JLabel("CSCI 6461 Machine Simulator");
        JLabel names = new JLabel("Group 1: Zack Rahbar, Liza Mozolyuk, Wesam Abu Rabia, Sameen Ahmad");

        firstNorth.add(title, BorderLayout.NORTH);
        firstNorth.add(names, BorderLayout.SOUTH);

        JPanel firstEastNorth = new JPanel(new BorderLayout(5,5));
        JPanel firstEastCenter = new JPanel(new BorderLayout(5,5));
        JPanel firstEastSouth = new JPanel(new BorderLayout(5,5));

        firstEast.add(firstEastNorth, BorderLayout.NORTH);
        firstEast.add(firstEastCenter, BorderLayout.CENTER);
        firstEast.add(firstEastSouth, BorderLayout.SOUTH);

        JLabel cacheContentLabel = new JLabel("Cache Content");
        cacheContent = new JTextArea("",20,30);
        cacheContent.setEditable(false);

        firstEastNorth.add(cacheContentLabel, BorderLayout.NORTH);
        firstEastNorth.add(cacheContent, BorderLayout.SOUTH);

        JLabel printerLabel = new JLabel("Printer");
        printer = new JTextArea("",10,20);
        printer.setEditable(false);

        firstEastCenter.add(printerLabel, BorderLayout.NORTH);
        firstEastCenter.add(printer, BorderLayout.SOUTH);

        JLabel consoleInputLabel = new JLabel("Console Input");
        consoleInput = new JTextField("", 20);

        firstEastSouth.add(consoleInputLabel, BorderLayout.NORTH);
        firstEastSouth.add(consoleInput, BorderLayout.SOUTH);

        JPanel firstCenterNorth = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenter = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterSouth = new JPanel(new FlowLayout());

        firstCenter.add(firstCenterNorth, BorderLayout.NORTH);
        firstCenter.add(firstCenterCenter, BorderLayout.CENTER);
        firstCenter.add(firstCenterSouth, BorderLayout.SOUTH);

        JLabel programFileLabel = new JLabel("Program File");
        programFile = new JTextField("", 50);

        firstCenterSouth.add(programFileLabel);
        firstCenterSouth.add(programFile);

        JPanel firstCenterCenterWest = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenterCenter = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel firstCenterCenterEast = new JPanel(new GridLayout(0, 1, 5, 5));

        firstCenterCenter.add(firstCenterCenterWest, BorderLayout.WEST);
        firstCenterCenter.add(firstCenterCenterCenter, BorderLayout.CENTER);
        firstCenterCenter.add(firstCenterCenterEast, BorderLayout.EAST);

        JPanel firstCenterCenterWestNorth = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenterWestSouth = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));

        firstCenterCenterWest.add(firstCenterCenterWestNorth, BorderLayout.NORTH);
        firstCenterCenterWest.add(firstCenterCenterWestSouth, BorderLayout.SOUTH);

        JLabel binaryLabel = new JLabel("Binary");
        binary = new JTextField("", 10);
        binary.setEditable(false);

        firstCenterCenterWestNorth.add(binaryLabel, BorderLayout.NORTH);
        firstCenterCenterWestNorth.add(binary, BorderLayout.SOUTH);

        JLabel octalInputLabel = new JLabel("Octal Input");
        octalInput = new JTextField("", 8);

        octalInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                String octal = octalInput.getText();

                if(!octal.isEmpty()) {

                    int decimalNum = Integer.parseInt(octal, 8);
                    String binaryString = Integer.toBinaryString(decimalNum);

                    binary.setText(binaryString);

                } 
                else{

                    binary.setText("");

                }
            }            
        });

        firstCenterCenterWestSouth.add(octalInputLabel);
        firstCenterCenterWestSouth.add(octalInput);

        JPanel loadRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton loadButton = new JButton();
        JLabel loadLabel = new JLabel("Load");

        loadButton.setPreferredSize(new Dimension(20, 18));

        loadRow.add(loadButton);
        loadRow.add(loadLabel);

        loadButton.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int octalVal = Integer.parseInt(octalInput.getText(), 8);
                int marAddr = cpu.getMAR();
                memory.directWrite(marAddr, octalVal);
                cpu.setMBR(octalVal);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel loadPlusRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton loadPlusButton = new JButton();
        JLabel loadPlusLabel = new JLabel("Load+");

        loadPlusButton.setPreferredSize(new Dimension(20, 18));

        loadPlusRow.add(loadPlusButton);
        loadPlusRow.add(loadPlusLabel);

        loadPlusButton.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int octalVal = Integer.parseInt(octalInput.getText(), 8);
                int marAddr = cpu.getMAR(); 
                memory.directWrite(marAddr, octalVal);
                cpu.setMBR(octalVal); 
                cpu.setMAR(marAddr + 1);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel storeRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton storeButton = new JButton();
        JLabel storeLabel = new JLabel("Store");

        storeButton.setPreferredSize(new Dimension(20, 18));

        storeRow.add(storeButton);
        storeRow.add(storeLabel);

        storeButton.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int marAddr = cpu.getMAR();
                int mbrVal = cpu.getMBR();
                memory.directWrite(marAddr, mbrVal);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid value in MBR or MAR");
            }
        });

        JPanel storePlusRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton storePlusButton = new JButton();
        JLabel storePlusLabel = new JLabel("Store+");

        storePlusButton.setPreferredSize(new Dimension(20, 18));

        storePlusRow.add(storePlusButton);
        storePlusRow.add(storePlusLabel);

        storePlusButton.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int marAddr = cpu.getMAR();
                int mbrVal = cpu.getMBR();
                memory.directWrite(marAddr, mbrVal);
                cpu.setMAR(marAddr + 1);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid value in MBR or MAR");
            }
        });

        firstCenterCenterCenter.add(loadRow);
        firstCenterCenterCenter.add(loadPlusRow);
        firstCenterCenterCenter.add(storeRow);
        firstCenterCenterCenter.add(storePlusRow);

        JPanel runRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton runButton = new JButton();
        JLabel runLabel = new JLabel("Run");

        runButton.setPreferredSize(new Dimension(20, 18));

        runRow.add(runButton);
        runRow.add(runLabel);

        runButton.addActionListener((e) -> {

            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "No program loaded. Press IPL first.");
                return;
            }
            if (isRunning == false){

                isRunning = true;

                new Thread(() -> {
                    while (isRunning) {

                        cpu.cycle();
                        updateDisplays();
                        if (cpu.isHalted() || cpu.getMFR() != 0) {
                            isRunning = false;
                            break;
                        }

                        try { 
                            Thread.sleep(2000); 
                        } 
                        catch (InterruptedException ex) {
                            break;
                        }
                    }

                    isRunning = false;

                }).start();
            }

        });

        JPanel stepRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton stepButton = new JButton();
        JLabel stepLabel = new JLabel("Step");

        stepButton.setPreferredSize(new Dimension(20, 18));

        stepRow.add(stepButton);
        stepRow.add(stepLabel);

        stepButton.addActionListener((e) -> {

            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "Press IPL first.");
                return;
            }

            cpu.cycle();
            cpu.listRegisters();
            updateDisplays();
            
        });

        JPanel haltRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton haltButton = new JButton();
        JLabel haltLabel = new JLabel("Halt");

        haltButton.setPreferredSize(new Dimension(20, 18));

        haltRow.add(haltButton);
        haltRow.add(haltLabel);

        haltButton.addActionListener((e) -> {

            isRunning = false;
            cpu.listRegisters();
            updateDisplays();
            
        });

        JPanel IPLRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton IPLButton = new JButton();
        JLabel IPLLabel = new JLabel("IPL");

        IPLButton.setPreferredSize(new Dimension(20, 18));

        IPLRow.add(IPLButton);
        IPLRow.add(IPLLabel);

        // IPLButton.addActionListener((e) -> {
        //     String filePath = programFile.getText().trim();
        //     if (filePath.isEmpty()) {
        //         JOptionPane.showMessageDialog(this, "Please enter a program file path.");
        //         return;
        //     }

        //     try {
        //         isRunning = false;
        //         memory.reset();
        //         cpu = new CPU(memory);

        //         // Decide: if it's a .asm file, assemble it first
        //         // If it's already a load/txt file, skip straight to loading
        //         String actualLoadFile;
        //         if (filePath.endsWith(".asm")) {
        //             Assembler.main(new String[]{filePath});
        //             actualLoadFile = "load.txt";
        //         } else {
        //             actualLoadFile = filePath;  // use it directly
        //         }

        //         StringBuilder fileContent = new StringBuilder();
        //         int startAddress = -1;

        //         try (BufferedReader reader = new BufferedReader(new FileReader(actualLoadFile))) {
        //             String line;
        //             while ((line = reader.readLine()) != null) {
        //                 if (line.isEmpty()) continue;
        //                 String[] parts = line.split("\\s+");
        //                 if (parts.length < 2) continue;

        //                 int addr = Integer.parseInt(parts[0], 8);
        //                 int val  = Integer.parseInt(parts[1], 8);
        //                 memory.directWrite(addr, val);
        //                 fileContent.append(line).append("\n");

        //                 System.out.printf("Loading Addr: %06o | Val: %06o | Decoded Op: %o\n",
        //                         addr, val, (val >>> 10) & 0x3F);

        //                 if (startAddress == -1) startAddress = addr;
        //             }
        //         }

        //         cacheContent.setText(fileContent.toString());

        //         if (startAddress != -1) {
        //             cpu.setPC(readStartAddress("start.txt", startAddress));
        //         }

        //         updateDisplays();

        //     } catch (java.io.FileNotFoundException ex) {
        //         JOptionPane.showMessageDialog(this, "File not found: " + filePath);
        //     } catch (NumberFormatException ex) {
        //         JOptionPane.showMessageDialog(this, "Invalid format in load file.");
        //     } catch (Exception ex) {
        //         JOptionPane.showMessageDialog(this, "Error loading program: " + ex.getMessage());
        //     }
        // });

        IPLButton.addActionListener((e) -> {
            String filePath = programFile.getText().trim();
            if (filePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a program file path.");
                return;
            }

            try {
                isRunning = false;
                memory.reset();
                cpu = new CPU(memory);

                // Decide input type
                boolean assembled = false;
                String actualLoadFile = filePath;

                if (filePath.toLowerCase().endsWith(".asm")) {
                    Assembler.main(new String[]{filePath});
                    // load.txt is written by your assembler (in current working dir)
                    actualLoadFile = "load.txt";
                    assembled = true;
                }

                StringBuilder fileContent = new StringBuilder();
                int firstAddrInFile = -1;

                try (BufferedReader reader = new BufferedReader(new FileReader(actualLoadFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty()) continue;
                        if (line.startsWith(";")) continue;

                        String[] parts = line.split("\\s+");
                        if (parts.length < 2) continue;

                        int addr = Integer.parseInt(parts[0], 8);
                        int val  = Integer.parseInt(parts[1], 8) & 0xFFFF;

                        memory.directWrite(addr, val);
                        fileContent.append(line).append("\n");

                        if (firstAddrInFile == -1) firstAddrInFile = addr;
                    }
                }

                cacheContent.setText(fileContent.toString());

                if (firstAddrInFile != -1) {
                    int pc = firstAddrInFile;

                    if (assembled) {
                        // Prefer start.txt if we assembled (fallback is first address)
                        pc = readStartAddressIfPresent("start.txt", firstAddrInFile);
                    }

                    cpu.setPC(pc);
                }

                updateDisplays();

            } catch (java.io.FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "File not found: " + filePath);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid format in load file.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading program: " + ex.getMessage());
            }
        });

        firstCenterCenterEast.add(runRow);
        firstCenterCenterEast.add(stepRow);
        firstCenterCenterEast.add(haltRow);
        firstCenterCenterEast.add(IPLRow);

        JPanel firstCenterNorthWest = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel firstCenterNorthCenter = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel firstCenterNorthEast = new JPanel(new GridLayout(0, 1, 5, 5));

        firstCenterNorth.add(firstCenterNorthWest, BorderLayout.WEST);
        firstCenterNorth.add(firstCenterNorthCenter, BorderLayout.CENTER);
        firstCenterNorth.add(firstCenterNorthEast, BorderLayout.EAST);

        JPanel gprLabelRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel gprLabel = new JLabel("GPR");

        gprLabelRow.add(gprLabel);

        JPanel zeroRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel zeroLabelGPR = new JLabel("0");
        zeroTextGPR = new JTextField("",10);
        JButton zeroButtonGPR = new JButton();

        zeroButtonGPR.setPreferredSize(new Dimension(20, 18));
        zeroTextGPR.setEditable(false);

        zeroRowGPR.add(zeroLabelGPR);
        zeroRowGPR.add(zeroTextGPR);
        zeroRowGPR.add(zeroButtonGPR);

        zeroButtonGPR.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setGPR(0, val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel oneRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel oneLabelGPR = new JLabel("1");
        oneTextGPR = new JTextField("",10);
        JButton oneButtonGPR = new JButton();

        oneButtonGPR.setPreferredSize(new Dimension(20, 18));
        oneTextGPR.setEditable(false);

        oneRowGPR.add(oneLabelGPR);
        oneRowGPR.add(oneTextGPR);
        oneRowGPR.add(oneButtonGPR);

        oneButtonGPR.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setGPR(1, val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel twoRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel twoLabelGPR = new JLabel("2");
        twoTextGPR = new JTextField("",10);
        JButton twoButtonGPR = new JButton();

        twoButtonGPR.setPreferredSize(new Dimension(20, 18));
        twoTextGPR.setEditable(false);

        twoRowGPR.add(twoLabelGPR);
        twoRowGPR.add(twoTextGPR);
        twoRowGPR.add(twoButtonGPR);

        twoButtonGPR.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setGPR(2, val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel threeRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel threeLabelGPR = new JLabel("3");
        threeTextGPR = new JTextField("",10);
        JButton threeButtonGPR = new JButton();

        threeButtonGPR.setPreferredSize(new Dimension(20, 18));
        threeTextGPR.setEditable(false);

        threeRowGPR.add(threeLabelGPR);
        threeRowGPR.add(threeTextGPR);
        threeRowGPR.add(threeButtonGPR);

        threeButtonGPR.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setGPR(3, val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        firstCenterNorthWest.add(gprLabelRow);
        firstCenterNorthWest.add(zeroRowGPR);
        firstCenterNorthWest.add(oneRowGPR);
        firstCenterNorthWest.add(twoRowGPR);
        firstCenterNorthWest.add(threeRowGPR);

        JPanel ixrLabelRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel ixrLabel = new JLabel("IXR");

        ixrLabelRow.add(ixrLabel);

        JPanel zeroRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));

        JPanel oneRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel oneLabelIXR = new JLabel("1");
        oneTextIXR = new JTextField("",10);
        JButton oneButtonIXR = new JButton();

        oneButtonIXR.setPreferredSize(new Dimension(20, 18));
        oneTextIXR.setEditable(false);

        oneRowIXR.add(oneLabelIXR);
        oneRowIXR.add(oneTextIXR);
        oneRowIXR.add(oneButtonIXR);

        oneButtonIXR.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setIX(1, val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel twoRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel twoLabelIXR = new JLabel("2");
        twoTextIXR= new JTextField("",10);
        JButton twoButtonIXR = new JButton();

        twoButtonIXR.setPreferredSize(new Dimension(20, 18));
        twoTextIXR.setEditable(false);

        twoRowIXR.add(twoLabelIXR);
        twoRowIXR.add(twoTextIXR);
        twoRowIXR.add(twoButtonIXR);

        twoButtonIXR.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setIX(2, val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel threeRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel threeLabelIXR = new JLabel("3");
        threeTextIXR = new JTextField("",10);
        JButton threeButtonIXR = new JButton();

        threeButtonIXR.setPreferredSize(new Dimension(20, 18));
        threeTextIXR.setEditable(false);

        threeRowIXR.add(threeLabelIXR);
        threeRowIXR.add(threeTextIXR);
        threeRowIXR.add(threeButtonIXR);

        threeButtonIXR.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setIX(3, val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        firstCenterNorthCenter.add(ixrLabelRow);
        firstCenterNorthCenter.add(zeroRowIXR);
        firstCenterNorthCenter.add(oneRowIXR);
        firstCenterNorthCenter.add(twoRowIXR);
        firstCenterNorthCenter.add(threeRowIXR);

        JPanel pcRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel pcLabel = new JLabel("PC");
        pcText = new JTextField("",10);
        JButton pcButton = new JButton();

        pcButton.setPreferredSize(new Dimension(20, 18));
        pcText.setEditable(false);

        pcRow.add(pcLabel);
        pcRow.add(pcText);
        pcRow.add(pcButton);

        pcButton.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setPC(val);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel marRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel marLabel = new JLabel("MAR");
        marText = new JTextField("",10);
        JButton marButton = new JButton();

        marButton.setPreferredSize(new Dimension(20, 18));
        marText.setEditable(false);

        marRow.add(marLabel);
        marRow.add(marText);
        marRow.add(marButton);

        marButton.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setMAR(val);
                int memVal = memory.peek(val);
                cpu.setMBR(memVal);
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel mbrRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel mbrLabel = new JLabel("MBR");
        mbrText = new JTextField("",10);
        JButton mbrButton = new JButton();

        mbrButton.setPreferredSize(new Dimension(20, 18));
        mbrText.setEditable(false);

        mbrRow.add(mbrLabel);
        mbrRow.add(mbrText);
        mbrRow.add(mbrButton);

        mbrButton.addActionListener((e) -> {
            try {
                if (cpu == null) {
                    JOptionPane.showMessageDialog(this, "Press IPL first.");
                    return;
                }
                int val = Integer.parseInt(octalInput.getText(), 8);
                cpu.setMBR(val); 
                updateDisplays();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel irRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel irLabel = new JLabel("IR");
        irText = new JTextField("",10);

        irText.setEditable(false);

        irRow.add(irLabel);
        irRow.add(irText);

        JPanel ccRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel ccLabel = new JLabel("CC");
        JPanel ccTexts = new JPanel(new BorderLayout(5,5));
        JLabel oudeText = new JLabel("OUDE");
        ccText = new JTextField("",5);

        ccText.setEditable(false);

        ccTexts.add(ccText, BorderLayout.NORTH);
        ccTexts.add(oudeText, BorderLayout.SOUTH);
        ccRow.add(ccLabel);
        ccRow.add(ccTexts);

        JPanel mfrRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel mfrLabel = new JLabel("MFR");
        JPanel mfrTexts = new JPanel(new BorderLayout(5,5));
        JLabel mortText = new JLabel("MOTR");
        mfrText = new JTextField("",5);

        mfrText.setEditable(false);

        mfrTexts.add(mfrText, BorderLayout.NORTH);
        mfrTexts.add(mortText, BorderLayout.SOUTH);
        mfrRow.add(mfrLabel);
        mfrRow.add(mfrTexts);

        firstCenterNorthEast.add(pcRow);
        firstCenterNorthEast.add(marRow);
        firstCenterNorthEast.add(mbrRow);
        firstCenterNorthEast.add(irRow);
        firstCenterNorthEast.add(ccRow);
        firstCenterNorthEast.add(mfrRow);

        this.add(outer);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

    }

    private static int readStartAddressIfPresent(String startFilePath, int fallbackAddr) {
        try (BufferedReader br = new BufferedReader(new FileReader(startFilePath))) {
            String s = br.readLine();
            if (s == null) return fallbackAddr;
            s = s.trim();
            if (s.isEmpty()) return fallbackAddr;
            return Integer.parseInt(s); // decimal
        } catch (Exception e) {
            return fallbackAddr;
        }
    }

    private void updateDisplays() {
    if (cpu == null) return;
    SwingUtilities.invokeLater(() -> {
        zeroTextGPR.setText(String.format("%06o", cpu.getGPR(0) & 0xFFFF));
        oneTextGPR.setText(String.format("%06o", cpu.getGPR(1) & 0xFFFF));
        twoTextGPR.setText(String.format("%06o", cpu.getGPR(2) & 0xFFFF));
        threeTextGPR.setText(String.format("%06o", cpu.getGPR(3) & 0xFFFF));
        oneTextIXR.setText(String.format("%06o", cpu.getIX(1) & 0xFFFF));
        twoTextIXR.setText(String.format("%06o", cpu.getIX(2) & 0xFFFF));
        threeTextIXR.setText(String.format("%06o", cpu.getIX(3) & 0xFFFF));
        pcText.setText(String.format("%06o", cpu.getPC() & 0xFFFF));
        marText.setText(String.format("%06o", cpu.getMAR() & 0xFFFF));
        mbrText.setText(String.format("%06o", cpu.getMBR() & 0xFFFF));
        irText.setText(String.format("%06o", cpu.getIR() & 0xFFFF));
        ccText.setText(String.format("%04o", cpu.getCC() & 0xF));
        mfrText.setText(String.format("%04o", cpu.getMFR() & 0xF));
    });

    }

    public static void main(String args[]){

        gui simulator = new gui();
        simulator.setVisible(true);

    }
    
}
