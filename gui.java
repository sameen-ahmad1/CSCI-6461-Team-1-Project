import assembler.Assembler;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import memory.CPU;
import memory.Cache;
import memory.MemoryBus;
import memory.simple.Memory;

public class gui extends JFrame{

    private boolean isRunning = false;
    private CPU cpu;
    private MemoryBus memory;

    private int cycleCount = 0;
    private String printerText = "";

    private JTextField zeroTextGPR, oneTextGPR, twoTextGPR, threeTextGPR;

    private JTextField oneTextIXR, twoTextIXR, threeTextIXR;

    private JTextField pcText, marText, mbrText, irText, ccText, mfrText;

    private JTextField binary, octalInput, programFile;
    private JTextArea cacheContent, printer;
    private JTextField consoleInput;

    public gui(){

        Font font = new Font("Courier New", Font.PLAIN, 12);

        JPanel outer = new JPanel(new BorderLayout(10,10));

        outer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outer.setBackground(Color.decode("#28282D"));

        JPanel firstNorth = new JPanel(new BorderLayout(10,10));
        JPanel firstCenter = new JPanel(new BorderLayout(10,10));
        JPanel firstEast = new JPanel(new BorderLayout(10,10));

        firstNorth.setBackground(Color.decode("#28282D"));
        firstCenter.setBackground(Color.decode("#28282D"));
        firstEast.setBackground(Color.decode("#28282D"));

        outer.add(firstNorth, BorderLayout.NORTH);
        outer.add(firstCenter, BorderLayout.CENTER);
        outer.add(firstEast, BorderLayout.EAST);

        JLabel title = new JLabel("CSCI 6461 Machine Simulator");
        JLabel names = new JLabel("Group 1: Zack Rahbar, Liza Mozolyuk, Wesam Abu Rabia, Sameen Ahmad");

        title.setForeground(Color.decode("#386192"));
        title.setFont(font);
        names.setForeground(Color.decode("#386192"));
        names.setFont(font);

        firstNorth.add(title, BorderLayout.NORTH);
        firstNorth.add(names, BorderLayout.SOUTH);

        JPanel firstEastNorth = new JPanel(new BorderLayout(5,5));
        JPanel firstEastCenter = new JPanel(new BorderLayout(5,5));
        JPanel firstEastSouth = new JPanel(new BorderLayout(5,5));

        firstEastNorth.setBackground(Color.decode("#28282D"));
        firstEastCenter.setBackground(Color.decode("#28282D"));
        firstEastSouth.setBackground(Color.decode("#28282D"));

        firstEast.add(firstEastNorth, BorderLayout.NORTH);
        firstEast.add(firstEastCenter, BorderLayout.CENTER);
        firstEast.add(firstEastSouth, BorderLayout.SOUTH);

        JLabel cacheContentLabel = new JLabel("Cache Content");
        cacheContentLabel.setForeground(Color.decode("#386192"));
        cacheContentLabel.setFont(font);
        cacheContent = new JTextArea("",20,30);
        cacheContent.setFont(font);
        cacheContent.setBackground(Color.decode("#3c3c44"));
        cacheContent.setForeground(Color.decode("#B0D3D1"));
        cacheContent.setEditable(false);

        firstEastNorth.add(cacheContentLabel, BorderLayout.NORTH);
        firstEastNorth.add(cacheContent, BorderLayout.SOUTH);

        JLabel printerLabel = new JLabel("Printer");
        printerLabel.setForeground(Color.decode("#386192"));
        printerLabel.setFont(font);
        printer = new JTextArea("",10,20);
        printer.setFont(font);
        printer.setEditable(false);
        printer.setBackground(Color.decode("#3c3c44"));
        printer.setForeground(Color.decode("#B0D3D1"));
        JScrollPane printerScroll = new JScrollPane(printer);
        printerScroll.setBorder(BorderFactory.createEmptyBorder());
        printerScroll.getViewport().setBackground(Color.decode("#3C3C44"));

        firstEastCenter.add(printerScroll, BorderLayout.SOUTH);
        firstEastCenter.add(printerLabel, BorderLayout.NORTH);

        JLabel consoleInputLabel = new JLabel("Console Input");
        consoleInputLabel.setFont(font);
        consoleInputLabel.setForeground(Color.decode("#386192"));
        consoleInput = new JTextField("", 20);
        consoleInput.setFont(font);
        consoleInput.setBackground(Color.decode("#3c3c44"));
        consoleInput.setForeground(Color.decode("#B0D3D1"));

        firstEastSouth.add(consoleInputLabel, BorderLayout.NORTH);
        firstEastSouth.add(consoleInput, BorderLayout.SOUTH);

        JPanel firstCenterNorth = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenter = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterSouth = new JPanel(new FlowLayout());

        firstCenterNorth.setBackground(Color.decode("#28282D"));
        firstCenterCenter.setBackground(Color.decode("#28282D"));
        firstCenterSouth.setBackground(Color.decode("#28282D"));

        firstCenter.add(firstCenterNorth, BorderLayout.NORTH);
        firstCenter.add(firstCenterCenter, BorderLayout.CENTER);
        firstCenter.add(firstCenterSouth, BorderLayout.SOUTH);

        JLabel programFileLabel = new JLabel("Program File");
        programFileLabel.setFont(font);
        programFileLabel.setForeground(Color.decode("#386192"));
        programFile = new JTextField("", 50);
        programFile.setFont(font);
        programFile.setBackground(Color.decode("#3c3c44"));
        programFile.setForeground(Color.decode("#B0D3D1"));

        firstCenterSouth.add(programFileLabel);
        firstCenterSouth.add(programFile);

        JPanel firstCenterCenterWest = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenterCenter = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel firstCenterCenterEast = new JPanel(new GridLayout(0, 1, 5, 5));

        firstCenterCenterWest.setBackground(Color.decode("#28282D"));
        firstCenterCenterCenter.setBackground(Color.decode("#28282D"));
        firstCenterCenterEast.setBackground(Color.decode("#28282D"));

        firstCenterCenter.add(firstCenterCenterWest, BorderLayout.WEST);
        firstCenterCenter.add(firstCenterCenterCenter, BorderLayout.CENTER);
        firstCenterCenter.add(firstCenterCenterEast, BorderLayout.EAST);

        JPanel firstCenterCenterWestNorth = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenterWestSouth = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));

        firstCenterCenterWestNorth.setBackground(Color.decode("#28282D"));
        firstCenterCenterWestSouth.setBackground(Color.decode("#28282D"));

        firstCenterCenterWest.add(firstCenterCenterWestNorth, BorderLayout.NORTH);
        firstCenterCenterWest.add(firstCenterCenterWestSouth, BorderLayout.SOUTH);

        JLabel binaryLabel = new JLabel("Binary");
        binaryLabel.setForeground(Color.decode("#386192"));
        binaryLabel.setFont(font);
        binary = new JTextField("", 10);
        binary.setFont(font);
        binary.setEditable(false);
        binary.setBackground(Color.decode("#3c3c44"));
        binary.setForeground(Color.decode("#B0D3D1"));

        firstCenterCenterWestNorth.add(binaryLabel, BorderLayout.NORTH);
        firstCenterCenterWestNorth.add(binary, BorderLayout.SOUTH);

        JLabel octalInputLabel = new JLabel("Octal Input");
        octalInputLabel.setForeground(Color.decode("#386192"));
        octalInputLabel.setFont(font);
        octalInput = new JTextField("", 8);
        octalInput.setFont(font);
        octalInput.setBackground(Color.decode("#3c3c44"));
        octalInput.setForeground(Color.decode("#B0D3D1"));

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

        loadLabel.setForeground(Color.decode("#386192"));
        loadLabel.setFont(font);

        loadButton.setPreferredSize(new Dimension(20, 18));
        loadButton.setBackground(Color.decode("#FFBA52"));
        loadButton.setOpaque(true);
        loadButton.setBorderPainted(false);

        loadRow.setBackground(Color.decode("#28282D"));
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
                memory.writeWord(marAddr, octalVal);
                cpu.setMBR(octalVal);
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel loadPlusRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton loadPlusButton = new JButton();
        JLabel loadPlusLabel = new JLabel("Load+");

        loadPlusLabel.setForeground(Color.decode("#386192"));
        loadPlusLabel.setFont(font);

        loadPlusButton.setPreferredSize(new Dimension(20, 18));
        loadPlusButton.setBackground(Color.decode("#FFBA52"));
        loadPlusButton.setOpaque(true);
        loadPlusButton.setBorderPainted(false);

        loadPlusRow.setBackground(Color.decode("#28282D"));
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
                memory.writeWord(marAddr, octalVal);
                cpu.setMBR(octalVal); 
                cpu.setMAR(marAddr + 1);
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel storeRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton storeButton = new JButton();
        JLabel storeLabel = new JLabel("Store");

        storeLabel.setForeground(Color.decode("#386192"));
        storeLabel.setFont(font);

        storeButton.setPreferredSize(new Dimension(20, 18));
        storeButton.setBackground(Color.decode("#FFBA52"));
        storeButton.setOpaque(true);
        storeButton.setBorderPainted(false);

        storeRow.setBackground(Color.decode("#28282D"));
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
                memory.writeWord(marAddr, mbrVal);
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid value in MBR or MAR");
            }
        });

        JPanel storePlusRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton storePlusButton = new JButton();
        JLabel storePlusLabel = new JLabel("Store+");

        storePlusLabel.setForeground(Color.decode("#386192"));
        storePlusLabel.setFont(font);

        storePlusButton.setPreferredSize(new Dimension(20, 18));
        storePlusButton.setBackground(Color.decode("#FFBA52"));
        storePlusButton.setOpaque(true);
        storePlusButton.setBorderPainted(false);

        storePlusRow.setBackground(Color.decode("#28282D"));
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
                memory.writeWord(marAddr, mbrVal);
                cpu.setMAR(marAddr + 1);
                updateTexts();
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

        runLabel.setForeground(Color.decode("#386192"));
        runLabel.setFont(font);

        runButton.setPreferredSize(new Dimension(20, 18));
        runButton.setBackground(Color.decode("#FFBA52"));
        runButton.setOpaque(true);
        runButton.setBorderPainted(false);

        runRow.setBackground(Color.decode("#28282D"));
        runRow.add(runButton);
        runRow.add(runLabel);

        runButton.addActionListener((e) -> {

            printerText = "running file\n" + printerText;

            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "No program loaded. Press IPL first.");
                return;
            }
            if (isRunning == false){

                isRunning = true;

                new Thread(() -> {
                    while (isRunning) {

                        cpu.cycle();
                        cycleCount = cycleCount + 1;
                        printerText = "running cycle " + Integer.toString(cycleCount) + "\n" + printerText;
                        updateTexts();
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

        stepLabel.setForeground(Color.decode("#386192"));
        stepLabel.setFont(font);

        stepButton.setPreferredSize(new Dimension(20, 18));
        stepButton.setBackground(Color.decode("#FFBA52"));
        stepButton.setOpaque(true);
        stepButton.setBorderPainted(false);

        stepRow.setBackground(Color.decode("#28282D"));
        stepRow.add(stepButton);
        stepRow.add(stepLabel);

        stepButton.addActionListener((e) -> {

            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "Press IPL first.");
                return;
            }

            cpu.cycle();
            cpu.listRegisters();
            cycleCount = cycleCount + 1;
            printerText = "stepping cycle " + Integer.toString(cycleCount) + "\n" + printerText;
            updateTexts();
            
        });

        JPanel haltRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton haltButton = new JButton();
        JLabel haltLabel = new JLabel("Halt");

        haltLabel.setForeground(Color.decode("#386192"));
        haltLabel.setFont(font);

        haltButton.setPreferredSize(new Dimension(20, 18));
        haltButton.setBackground(Color.decode("#FFBA52"));
        haltButton.setOpaque(true);
        haltButton.setBorderPainted(false);

        haltRow.setBackground(Color.decode("#28282D"));
        haltRow.add(haltButton);
        haltRow.add(haltLabel);

        haltButton.addActionListener((e) -> {

            isRunning = false;
            cpu.listRegisters();
            printerText = "halting run\n" + printerText;
            updateTexts();
            
        });

        JPanel IPLRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton IPLButton = new JButton();
        JLabel IPLLabel = new JLabel("IPL");

        IPLLabel.setForeground(Color.decode("#386192"));
        IPLLabel.setFont(font);

        IPLButton.setPreferredSize(new Dimension(20, 18));
        IPLButton.setBackground(Color.decode("#FFBA52"));
        IPLButton.setOpaque(true);
        IPLButton.setBorderPainted(false);

        IPLRow.setBackground(Color.decode("#28282D"));
        IPLRow.add(IPLButton);
        IPLRow.add(IPLLabel);

        IPLButton.addActionListener((e) -> {
            String filePath = programFile.getText().trim();
            if (filePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a program file path.");
                return;
            }

            try {
                isRunning = false;
                // memory.reset();
                // cpu = new CPU(memory);

                 // The physical RAM
                Memory rawMem = new Memory();  
                // The Cache layer   
                this.memory = new Cache(rawMem);  
                // Connect CPU to the Bus 
                this.cpu = new CPU(this.memory);   

                
                this.memory.reset();

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

                        memory.writeWord(addr, val);
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

                printerText = "file loaded\n" + printerText;
                cycleCount = 0;
                updateTexts();

            } catch (java.io.FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "File not found: " + filePath);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid format in load file.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading program: " + ex.getMessage());
            }
        });

        //ADDED CLEAR CACHE BUTTON
        JPanel clearCacheRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton clearCacheButton = new JButton();
        JLabel clearCacheLabel = new JLabel("Clear Cache");

        clearCacheLabel.setForeground(Color.decode("#386192"));
        clearCacheLabel.setFont(font);
        
        clearCacheButton.setPreferredSize(new Dimension(20, 18));
        clearCacheButton.setBackground(Color.decode("#FFBA52"));
        clearCacheButton.setOpaque(true);
        clearCacheButton.setBorderPainted(false);

        clearCacheRow.setBackground(Color.decode("#28282D"));
        clearCacheRow.add(clearCacheButton);
        clearCacheRow.add(clearCacheLabel);

        clearCacheButton.addActionListener((e) -> {
            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "Press IPL first.");
                return;
            }
            
            memory.reset(); 
            printerText = "Cache Cleared Manually\n" + printerText;
            updateTexts(); 
        });

        // Add it to the East column of the Center panel
        firstCenterCenterEast.add(clearCacheRow);

        firstCenterCenterEast.add(runRow);
        firstCenterCenterEast.add(stepRow);
        firstCenterCenterEast.add(haltRow);
        firstCenterCenterEast.add(IPLRow);

        JPanel firstCenterNorthWest = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel firstCenterNorthCenter = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel firstCenterNorthEast = new JPanel(new GridLayout(0, 1, 5, 5));

        firstCenterNorthWest.setBackground(Color.decode("#28282D"));
        firstCenterNorthCenter.setBackground(Color.decode("#28282D"));
        firstCenterNorthEast.setBackground(Color.decode("#28282D"));

        firstCenterNorth.add(firstCenterNorthWest, BorderLayout.WEST);
        firstCenterNorth.add(firstCenterNorthCenter, BorderLayout.CENTER);
        firstCenterNorth.add(firstCenterNorthEast, BorderLayout.EAST);

        JPanel gprLabelRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel gprLabel = new JLabel("GPR");

        gprLabel.setForeground(Color.decode("#386192"));
        gprLabel.setFont(font);
        gprLabelRow.setBackground(Color.decode("#28282D"));
        gprLabelRow.add(gprLabel);

        JPanel zeroRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel zeroLabelGPR = new JLabel("0");
        zeroLabelGPR.setFont(font);
        zeroLabelGPR.setForeground(Color.decode("#386192"));
        zeroTextGPR = new JTextField("",10);
        zeroTextGPR.setFont(font);
        zeroTextGPR.setBackground(Color.decode("#3c3c44"));
        zeroTextGPR.setForeground(Color.decode("#B0D3D1"));
        JButton zeroButtonGPR = new JButton();

        zeroButtonGPR.setPreferredSize(new Dimension(20, 18));
        zeroButtonGPR.setBackground(Color.decode("#FFBA52"));
        zeroButtonGPR.setOpaque(true);
        zeroButtonGPR.setBorderPainted(false);

        zeroTextGPR.setEditable(false);

        zeroRowGPR.setBackground(Color.decode("#28282D"));
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
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel oneRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel oneLabelGPR = new JLabel("1");
        oneLabelGPR.setForeground(Color.decode("#386192"));
        oneLabelGPR.setFont(font);
        oneTextGPR = new JTextField("",10);
        oneTextGPR.setFont(font);
        oneTextGPR.setBackground(Color.decode("#3c3c44"));
        oneTextGPR.setForeground(Color.decode("#B0D3D1"));
        JButton oneButtonGPR = new JButton();

        oneButtonGPR.setPreferredSize(new Dimension(20, 18));
        oneButtonGPR.setBackground(Color.decode("#FFBA52"));
        oneButtonGPR.setOpaque(true);
        oneButtonGPR.setBorderPainted(false);

        oneTextGPR.setEditable(false);

        oneRowGPR.setBackground(Color.decode("#28282D"));
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
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel twoRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel twoLabelGPR = new JLabel("2");
        twoLabelGPR.setForeground(Color.decode("#386192"));
        twoLabelGPR.setFont(font);
        twoTextGPR = new JTextField("",10);
        twoTextGPR.setFont(font);
        twoTextGPR.setBackground(Color.decode("#3c3c44"));
        twoTextGPR.setForeground(Color.decode("#B0D3D1"));
        JButton twoButtonGPR = new JButton();

        twoButtonGPR.setPreferredSize(new Dimension(20, 18));
        twoButtonGPR.setBackground(Color.decode("#FFBA52"));
        twoButtonGPR.setOpaque(true);
        twoButtonGPR.setBorderPainted(false);

        twoTextGPR.setEditable(false);

        twoRowGPR.setBackground(Color.decode("#28282D"));
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
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel threeRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel threeLabelGPR = new JLabel("3");
        threeLabelGPR.setFont(font);
        threeLabelGPR.setForeground(Color.decode("#386192"));
        threeTextGPR = new JTextField("",10);
        threeTextGPR.setFont(font);
        threeTextGPR.setBackground(Color.decode("#3c3c44"));
        threeTextGPR.setForeground(Color.decode("#B0D3D1"));
        JButton threeButtonGPR = new JButton();

        threeButtonGPR.setPreferredSize(new Dimension(20, 18));
        threeButtonGPR.setBackground(Color.decode("#FFBA52"));
        threeButtonGPR.setOpaque(true);
        threeButtonGPR.setBorderPainted(false);

        threeTextGPR.setEditable(false);

        threeRowGPR.setBackground(Color.decode("#28282D"));
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
                updateTexts();
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

        ixrLabel.setForeground(Color.decode("#386192"));
        ixrLabel.setFont(font);
        ixrLabelRow.setBackground(Color.decode("#28282D"));
        ixrLabelRow.add(ixrLabel);

        JPanel zeroRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));

        zeroRowIXR.setBackground(Color.decode("#28282D"));

        JPanel oneRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel oneLabelIXR = new JLabel("1");
        oneLabelIXR.setFont(font);
        oneLabelIXR.setForeground(Color.decode("#386192"));
        oneTextIXR = new JTextField("",10);
        oneTextIXR.setFont(font);
        oneTextIXR.setBackground(Color.decode("#3c3c44"));
        oneTextIXR.setForeground(Color.decode("#B0D3D1"));
        JButton oneButtonIXR = new JButton();

        oneButtonIXR.setPreferredSize(new Dimension(20, 18));
        oneButtonIXR.setBackground(Color.decode("#FFBA52"));
        oneButtonIXR.setOpaque(true);
        oneButtonIXR.setBorderPainted(false);

        oneTextIXR.setEditable(false);

        oneRowIXR.setBackground(Color.decode("#28282D"));
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
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel twoRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel twoLabelIXR = new JLabel("2");
        twoLabelIXR.setForeground(Color.decode("#386192"));
        twoLabelIXR.setFont(font);
        twoTextIXR= new JTextField("",10);
        twoTextIXR.setFont(font);
        twoTextIXR.setBackground(Color.decode("#3c3c44"));
        twoTextIXR.setForeground(Color.decode("#B0D3D1"));
        JButton twoButtonIXR = new JButton();

        twoButtonIXR.setPreferredSize(new Dimension(20, 18));
        twoButtonIXR.setBackground(Color.decode("#FFBA52"));
        twoButtonIXR.setOpaque(true);
        twoButtonIXR.setBorderPainted(false);

        twoTextIXR.setEditable(false);

        twoRowIXR.setBackground(Color.decode("#28282D"));
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
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel threeRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel threeLabelIXR = new JLabel("3");
        threeLabelIXR.setForeground(Color.decode("#386192"));
        threeLabelIXR.setFont(font);
        threeTextIXR = new JTextField("",10);
        threeRowIXR.setFont(font);
        threeTextIXR.setBackground(Color.decode("#3c3c44"));
        threeTextIXR.setForeground(Color.decode("#B0D3D1"));
        JButton threeButtonIXR = new JButton();

        threeButtonIXR.setPreferredSize(new Dimension(20, 18));
        threeButtonIXR.setBackground(Color.decode("#FFBA52"));
        threeButtonIXR.setOpaque(true);
        threeButtonIXR.setBorderPainted(false);

        threeTextIXR.setEditable(false);

        threeRowIXR.setBackground(Color.decode("#28282D"));
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
                updateTexts();
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
        pcLabel.setFont(font);
        pcLabel.setForeground(Color.decode("#386192"));
        pcText = new JTextField("",10);
        pcText.setFont(font);
        pcText.setBackground(Color.decode("#3c3c44"));
        pcText.setForeground(Color.decode("#B0D3D1"));
        JButton pcButton = new JButton();

        pcButton.setPreferredSize(new Dimension(20, 18));
        pcButton.setBackground(Color.decode("#FFBA52"));
        pcButton.setOpaque(true);
        pcButton.setBorderPainted(false);

        pcText.setEditable(false);

        pcRow.setBackground(Color.decode("#28282D"));
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
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel marRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel marLabel = new JLabel("MAR");
        marLabel.setFont(font);
        marLabel.setForeground(Color.decode("#386192"));
        marText = new JTextField("",10);
        marText.setFont(font);
        marText.setBackground(Color.decode("#3c3c44"));
        marText.setForeground(Color.decode("#B0D3D1"));
        JButton marButton = new JButton();

        marButton.setPreferredSize(new Dimension(20, 18));
        marButton.setBackground(Color.decode("#FFBA52"));
        marButton.setOpaque(true);
        marButton.setBorderPainted(false);

        marText.setEditable(false);

        marRow.setBackground(Color.decode("#28282D"));
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
                int memVal = memory.readWord(val);
                cpu.setMBR(memVal);
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel mbrRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel mbrLabel = new JLabel("MBR");
        mbrLabel.setFont(font);
        mbrLabel.setForeground(Color.decode("#386192"));
        mbrText = new JTextField("",10);
        mbrText.setFont(font);
        mbrText.setBackground(Color.decode("#3c3c44"));
        mbrText.setForeground(Color.decode("#B0D3D1"));
        JButton mbrButton = new JButton();

        mbrButton.setPreferredSize(new Dimension(20, 18));
        mbrButton.setBackground(Color.decode("#FFBA52"));
        mbrButton.setOpaque(true);
        mbrButton.setBorderPainted(false);

        mbrText.setEditable(false);

        mbrRow.setBackground(Color.decode("#28282D"));
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
                updateTexts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid octal input");
            }
        });

        JPanel irRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel irLabel = new JLabel("IR");
        irLabel.setFont(font);
        irLabel.setForeground(Color.decode("#386192"));
        irText = new JTextField("",10);
        irText.setFont(font);
        irText.setBackground(Color.decode("#3c3c44"));
        irText.setForeground(Color.decode("#B0D3D1"));

        irText.setEditable(false);

        irRow.setBackground(Color.decode("#28282D"));
        irRow.add(irLabel);
        irRow.add(irText);

        JPanel ccRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel ccLabel = new JLabel("CC");
        ccLabel.setFont(font);
        ccLabel.setForeground(Color.decode("#386192"));
        JPanel ccTexts = new JPanel(new BorderLayout(5,5));
        JLabel oudeText = new JLabel("OUDE");
        oudeText.setFont(font);
        oudeText.setForeground(Color.decode("#386192"));
        ccText = new JTextField("",5);
        ccText.setFont(font);
        ccText.setBackground(Color.decode("#3c3c44"));
        ccText.setForeground(Color.decode("#B0D3D1"));

        ccText.setEditable(false);

        ccTexts.setBackground(Color.decode("#28282D"));
        ccTexts.add(ccText, BorderLayout.NORTH);
        ccTexts.add(oudeText, BorderLayout.SOUTH);
        ccRow.setBackground(Color.decode("#28282D"));
        ccRow.add(ccLabel);
        ccRow.add(ccTexts);

        JPanel mfrRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel mfrLabel = new JLabel("MFR");
        mfrLabel.setForeground(Color.decode("#386192"));
        mfrLabel.setFont(font);
        JPanel mfrTexts = new JPanel(new BorderLayout(5,5));
        JLabel mortText = new JLabel("MOTR");
        mortText.setForeground(Color.decode("#386192"));
        mortText.setFont(font);
        mfrText = new JTextField("",5);
        mfrText.setFont(font);
        mfrText.setBackground(Color.decode("#3c3c44"));
        mfrText.setForeground(Color.decode("#B0D3D1"));

        mfrText.setEditable(false);

        mfrTexts.setBackground(Color.decode("#28282D"));
        mfrTexts.add(mfrText, BorderLayout.NORTH);
        mfrTexts.add(mortText, BorderLayout.SOUTH);
        mfrRow.setBackground(Color.decode("#28282D"));
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

    private void updateTexts() {
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
        
        //added cache update
        cacheContent.setText(memory.getCacheStatus());

        printer.setText(printerText);
        printer.setCaretPosition(0);
    });

    }

    public static void main(String args[]){

        gui simulator = new gui();
        simulator.setVisible(true);

    }
    
}
