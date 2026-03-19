import assembler.Assembler;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import memory.CPU;
import memory.Cache;
import memory.DeviceListener;
import memory.MemoryBus;
import memory.simple.Memory;

public class gui extends JFrame implements DeviceListener{

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

        Font font = new Font("Courier New", Font.BOLD, 14);

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

        title.setForeground(Color.decode("#467ab9"));
        title.setFont(font);
        names.setForeground(Color.decode("#467ab9"));
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
        cacheContentLabel.setForeground(Color.decode("#467ab9"));
        cacheContentLabel.setFont(font);
        cacheContent = new JTextArea("",20,30);
        cacheContent.setFont(font);
        cacheContent.setBackground(Color.decode("#3c3c44"));
        cacheContent.setForeground(Color.decode("#B0D3D1"));
        cacheContent.setEditable(false);

        firstEastNorth.add(cacheContentLabel, BorderLayout.NORTH);
        firstEastNorth.add(cacheContent, BorderLayout.SOUTH);

        JLabel printerLabel = new JLabel("Printer");
        printerLabel.setForeground(Color.decode("#467ab9"));
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
        consoleInputLabel.setForeground(Color.decode("#467ab9"));
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
        programFileLabel.setForeground(Color.decode("#467ab9"));
        programFile = new JTextField("", 50);
        programFile.setFont(font);
        programFile.setBackground(Color.decode("#3c3c44"));
        programFile.setForeground(Color.decode("#B0D3D1"));

        firstCenterSouth.add(programFileLabel);
        firstCenterSouth.add(programFile);

        JPanel firstCenterCenterWest = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenterCenter = new JPanel(new GridBagLayout());
        JPanel firstCenterCenterEast = new JPanel(new GridBagLayout());

        firstCenterCenterWest.setBackground(Color.decode("#28282D"));
        firstCenterCenterCenter.setBackground(Color.decode("#28282D"));
        firstCenterCenterEast.setBackground(Color.decode("#28282D"));

        firstCenterCenter.add(firstCenterCenterWest, BorderLayout.WEST);
        firstCenterCenter.add(firstCenterCenterCenter, BorderLayout.CENTER);
        firstCenterCenter.add(firstCenterCenterEast, BorderLayout.EAST);

        // Binary and Octal Input stacked together in a single GridBagLayout panel
        JPanel firstCenterCenterWestNorth = new JPanel(new GridBagLayout());

        firstCenterCenterWestNorth.setBackground(Color.decode("#28282D"));

        firstCenterCenterWest.add(firstCenterCenterWestNorth, BorderLayout.CENTER);

        GridBagConstraints gbcWest = new GridBagConstraints();
        gbcWest.insets = new Insets(3, 5, 3, 5);
        gbcWest.anchor = GridBagConstraints.WEST;
        gbcWest.fill = GridBagConstraints.HORIZONTAL;

        JLabel binaryLabel = new JLabel("Binary");
        binaryLabel.setForeground(Color.decode("#467ab9"));
        binaryLabel.setFont(font);
        binary = new JTextField("", 10);
        binary.setFont(font);
        binary.setEditable(false);
        binary.setBackground(Color.decode("#3c3c44"));
        binary.setForeground(Color.decode("#B0D3D1"));

        gbcWest.gridx = 0; gbcWest.gridy = 0; gbcWest.gridwidth = 2;
        firstCenterCenterWestNorth.add(binaryLabel, gbcWest);
        gbcWest.gridy = 1;
        firstCenterCenterWestNorth.add(binary, gbcWest);
        gbcWest.gridwidth = 1;

        JLabel octalInputLabel = new JLabel("Octal Input");
        octalInputLabel.setForeground(Color.decode("#467ab9"));
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

        gbcWest.gridx = 0; gbcWest.gridy = 2;
        firstCenterCenterWestNorth.add(octalInputLabel, gbcWest);
        gbcWest.gridx = 1;
        firstCenterCenterWestNorth.add(octalInput, gbcWest);

        // shared GBC for the Load / Load+ / Store / Store+ column
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.insets = new Insets(5, 5, 5, 5);
        gbcCenter.anchor = GridBagConstraints.WEST;

        JButton loadButton = new JButton();
        JLabel loadLabel = new JLabel("Load");

        loadLabel.setForeground(Color.decode("#467ab9"));
        loadLabel.setFont(font);

        loadButton.setPreferredSize(new Dimension(20, 18));
        loadButton.setBackground(Color.decode("#FFBA52"));
        loadButton.setOpaque(true);
        loadButton.setBorderPainted(false);

        gbcCenter.gridx = 0; gbcCenter.gridy = 0;
        firstCenterCenterCenter.add(loadButton, gbcCenter);
        gbcCenter.gridx = 1;
        firstCenterCenterCenter.add(loadLabel, gbcCenter);

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

        JButton loadPlusButton = new JButton();
        JLabel loadPlusLabel = new JLabel("Load+");

        loadPlusLabel.setForeground(Color.decode("#467ab9"));
        loadPlusLabel.setFont(font);

        loadPlusButton.setPreferredSize(new Dimension(20, 18));
        loadPlusButton.setBackground(Color.decode("#FFBA52"));
        loadPlusButton.setOpaque(true);
        loadPlusButton.setBorderPainted(false);

        gbcCenter.gridx = 0; gbcCenter.gridy = 1;
        firstCenterCenterCenter.add(loadPlusButton, gbcCenter);
        gbcCenter.gridx = 1;
        firstCenterCenterCenter.add(loadPlusLabel, gbcCenter);

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

        JButton storeButton = new JButton();
        JLabel storeLabel = new JLabel("Store");

        storeLabel.setForeground(Color.decode("#467ab9"));
        storeLabel.setFont(font);

        storeButton.setPreferredSize(new Dimension(20, 18));
        storeButton.setBackground(Color.decode("#FFBA52"));
        storeButton.setOpaque(true);
        storeButton.setBorderPainted(false);

        gbcCenter.gridx = 0; gbcCenter.gridy = 2;
        firstCenterCenterCenter.add(storeButton, gbcCenter);
        gbcCenter.gridx = 1;
        firstCenterCenterCenter.add(storeLabel, gbcCenter);

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

        JButton storePlusButton = new JButton();
        JLabel storePlusLabel = new JLabel("Store+");

        storePlusLabel.setForeground(Color.decode("#467ab9"));
        storePlusLabel.setFont(font);

        storePlusButton.setPreferredSize(new Dimension(20, 18));
        storePlusButton.setBackground(Color.decode("#FFBA52"));
        storePlusButton.setOpaque(true);
        storePlusButton.setBorderPainted(false);

        gbcCenter.gridx = 0; gbcCenter.gridy = 3;
        firstCenterCenterCenter.add(storePlusButton, gbcCenter);
        gbcCenter.gridx = 1;
        firstCenterCenterCenter.add(storePlusLabel, gbcCenter);

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

        // shared GBC for the Clear Cache / Run / Step / Halt / IPL column
        GridBagConstraints gbcEast = new GridBagConstraints();
        gbcEast.insets = new Insets(5, 5, 5, 5);
        gbcEast.anchor = GridBagConstraints.WEST;

        JButton runButton = new JButton();
        JLabel runLabel = new JLabel("Run");

        runLabel.setForeground(Color.decode("#467ab9"));
        runLabel.setFont(font);

        runButton.setPreferredSize(new Dimension(20, 18));
        runButton.setBackground(Color.decode("#FFBA52"));
        runButton.setOpaque(true);
        runButton.setBorderPainted(false);

        gbcEast.gridx = 0; gbcEast.gridy = 1;
        firstCenterCenterEast.add(runButton, gbcEast);
        gbcEast.gridx = 1;
        firstCenterCenterEast.add(runLabel, gbcEast);

        runButton.addActionListener((e) -> {

            printerText = "running file\n" + printerText;

            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "No program loaded. Press IPL first.");
                return;
            }

            startRunLoop();

        });

        JButton stepButton = new JButton();
        JLabel stepLabel = new JLabel("Step");

        stepLabel.setForeground(Color.decode("#467ab9"));
        stepLabel.setFont(font);

        stepButton.setPreferredSize(new Dimension(20, 18));
        stepButton.setBackground(Color.decode("#FFBA52"));
        stepButton.setOpaque(true);
        stepButton.setBorderPainted(false);

        gbcEast.gridx = 0; gbcEast.gridy = 2;
        firstCenterCenterEast.add(stepButton, gbcEast);
        gbcEast.gridx = 1;
        firstCenterCenterEast.add(stepLabel, gbcEast);

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

        JButton haltButton = new JButton();
        JLabel haltLabel = new JLabel("Halt");

        haltLabel.setForeground(Color.decode("#467ab9"));
        haltLabel.setFont(font);

        haltButton.setPreferredSize(new Dimension(20, 18));
        haltButton.setBackground(Color.decode("#FFBA52"));
        haltButton.setOpaque(true);
        haltButton.setBorderPainted(false);

        gbcEast.gridx = 0; gbcEast.gridy = 3;
        firstCenterCenterEast.add(haltButton, gbcEast);
        gbcEast.gridx = 1;
        firstCenterCenterEast.add(haltLabel, gbcEast);

        haltButton.addActionListener((e) -> {

            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "Press IPL first.");
                return;
            }

            isRunning = false;
            cpu.halt();
            cpu.listRegisters();
            printerText = "halting run\n" + printerText;
            updateTexts();
            
        });

        JButton pausePlayButton = new JButton();
        JLabel pausePlayLabel = new JLabel("Pause");

        pausePlayLabel.setForeground(Color.decode("#467ab9"));
        pausePlayLabel.setFont(font);

        pausePlayButton.setPreferredSize(new Dimension(20, 18));
        pausePlayButton.setBackground(Color.decode("#FFBA52"));
        pausePlayButton.setOpaque(true);
        pausePlayButton.setBorderPainted(false);

        gbcEast.gridx = 0; gbcEast.gridy = 4;
        firstCenterCenterEast.add(pausePlayButton, gbcEast);
        gbcEast.gridx = 1;
        firstCenterCenterEast.add(pausePlayLabel, gbcEast);

        pausePlayButton.addActionListener((e) -> {

            if(pausePlayLabel.getText().equals("Pause")){

                pausePlayLabel.setText("Play");
                isRunning = false;
                printerText = "Pausing run\n" + printerText;
                updateTexts();

            }
            else{

                pausePlayLabel.setText("Pause");
                printerText = "Resuming run\n" + printerText;
                startRunLoop();
                updateTexts();

            }
            
        });

        JButton IPLButton = new JButton();
        JLabel IPLLabel = new JLabel("IPL");

        IPLLabel.setForeground(Color.decode("#467ab9"));
        IPLLabel.setFont(font);

        IPLButton.setPreferredSize(new Dimension(20, 18));
        IPLButton.setBackground(Color.decode("#FFBA52"));
        IPLButton.setOpaque(true);
        IPLButton.setBorderPainted(false);

        gbcEast.gridx = 0; gbcEast.gridy = 5;
        firstCenterCenterEast.add(IPLButton, gbcEast);
        gbcEast.gridx = 1;
        firstCenterCenterEast.add(IPLLabel, gbcEast);

        IPLButton.addActionListener((e) -> {
            String filePath = programFile.getText().trim();
            if (filePath.isEmpty()) {
                Memory rawMem = new Memory();
                // set the listener for device events (printer output, keyboard input, etc.)
                rawMem.getDevice().setListener(this);
                this.memory = new Cache(rawMem);
                this.cpu = new CPU(this.memory);
                this.memory.reset();
                cycleCount = 0;
                printerText = "testing individual instructions\n";
                updateTexts();
                return;
            }

            try {
                isRunning = false;
                // memory.reset();
                // cpu = new CPU(memory);

                 // The physical RAM
                Memory rawMem = new Memory();  
                // set the listener for device events (printer output, keyboard input, etc.)
                rawMem.getDevice().setListener(this);
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
        JButton clearCacheButton = new JButton();
        JLabel clearCacheLabel = new JLabel("Clear Cache");

        clearCacheLabel.setForeground(Color.decode("#467ab9"));
        clearCacheLabel.setFont(font);
        
        clearCacheButton.setPreferredSize(new Dimension(20, 18));
        clearCacheButton.setBackground(Color.decode("#FFBA52"));
        clearCacheButton.setOpaque(true);
        clearCacheButton.setBorderPainted(false);

        gbcEast.gridx = 0; gbcEast.gridy = 0;
        firstCenterCenterEast.add(clearCacheButton, gbcEast);
        gbcEast.gridx = 1;
        firstCenterCenterEast.add(clearCacheLabel, gbcEast);

        clearCacheButton.addActionListener((e) -> {
            if (cpu == null) {
                JOptionPane.showMessageDialog(this, "Press IPL first.");
                return;
            }
            
            memory.reset(); 
            printerText = "Cache Cleared Manually\n" + printerText;
            updateTexts(); 
        });

        JPanel firstCenterNorthWest = new JPanel(new GridBagLayout());
        JPanel firstCenterNorthCenter = new JPanel(new GridBagLayout());
        JPanel firstCenterNorthEast = new JPanel(new GridBagLayout());

        firstCenterNorthWest.setBackground(Color.decode("#28282D"));
        firstCenterNorthCenter.setBackground(Color.decode("#28282D"));
        firstCenterNorthEast.setBackground(Color.decode("#28282D"));

        firstCenterNorth.add(firstCenterNorthWest, BorderLayout.WEST);
        firstCenterNorth.add(firstCenterNorthCenter, BorderLayout.CENTER);
        firstCenterNorth.add(firstCenterNorthEast, BorderLayout.EAST);

        // shared GBC for GPR column
        GridBagConstraints gbcGPR = new GridBagConstraints();
        gbcGPR.insets = new Insets(4, 5, 4, 5);
        gbcGPR.anchor = GridBagConstraints.WEST;

        JPanel gprLabelRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel gprLabel = new JLabel("GPR");

        gprLabel.setForeground(Color.decode("#467ab9"));
        gprLabel.setFont(font);
        gprLabelRow.setBackground(Color.decode("#28282D"));
        gprLabelRow.add(gprLabel);

        gbcGPR.gridx = 0; gbcGPR.gridy = 0; gbcGPR.gridwidth = 3; gbcGPR.anchor = GridBagConstraints.CENTER;
        firstCenterNorthWest.add(gprLabelRow, gbcGPR);
        gbcGPR.gridwidth = 1; gbcGPR.anchor = GridBagConstraints.WEST;

        JLabel zeroLabelGPR = new JLabel("0");
        zeroLabelGPR.setFont(font);
        zeroLabelGPR.setForeground(Color.decode("#467ab9"));
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

        gbcGPR.gridx = 0; gbcGPR.gridy = 1; gbcGPR.anchor = GridBagConstraints.EAST;
        firstCenterNorthWest.add(zeroLabelGPR, gbcGPR);
        gbcGPR.gridx = 1; gbcGPR.anchor = GridBagConstraints.WEST;
        firstCenterNorthWest.add(zeroTextGPR, gbcGPR);
        gbcGPR.gridx = 2;
        firstCenterNorthWest.add(zeroButtonGPR, gbcGPR);

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

        JLabel oneLabelGPR = new JLabel("1");
        oneLabelGPR.setForeground(Color.decode("#467ab9"));
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

        gbcGPR.gridx = 0; gbcGPR.gridy = 2; gbcGPR.anchor = GridBagConstraints.EAST;
        firstCenterNorthWest.add(oneLabelGPR, gbcGPR);
        gbcGPR.gridx = 1; gbcGPR.anchor = GridBagConstraints.WEST;
        firstCenterNorthWest.add(oneTextGPR, gbcGPR);
        gbcGPR.gridx = 2;
        firstCenterNorthWest.add(oneButtonGPR, gbcGPR);

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

        JLabel twoLabelGPR = new JLabel("2");
        twoLabelGPR.setForeground(Color.decode("#467ab9"));
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

        gbcGPR.gridx = 0; gbcGPR.gridy = 3; gbcGPR.anchor = GridBagConstraints.EAST;
        firstCenterNorthWest.add(twoLabelGPR, gbcGPR);
        gbcGPR.gridx = 1; gbcGPR.anchor = GridBagConstraints.WEST;
        firstCenterNorthWest.add(twoTextGPR, gbcGPR);
        gbcGPR.gridx = 2;
        firstCenterNorthWest.add(twoButtonGPR, gbcGPR);

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

        JLabel threeLabelGPR = new JLabel("3");
        threeLabelGPR.setFont(font);
        threeLabelGPR.setForeground(Color.decode("#467ab9"));
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

        gbcGPR.gridx = 0; gbcGPR.gridy = 4; gbcGPR.anchor = GridBagConstraints.EAST;
        firstCenterNorthWest.add(threeLabelGPR, gbcGPR);
        gbcGPR.gridx = 1; gbcGPR.anchor = GridBagConstraints.WEST;
        firstCenterNorthWest.add(threeTextGPR, gbcGPR);
        gbcGPR.gridx = 2;
        firstCenterNorthWest.add(threeButtonGPR, gbcGPR);

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

        // shared GBC for IXR column
        GridBagConstraints gbcIXR = new GridBagConstraints();
        gbcIXR.insets = new Insets(4, 5, 4, 5);
        gbcIXR.anchor = GridBagConstraints.WEST;

        JPanel ixrLabelRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel ixrLabel = new JLabel("IXR");

        ixrLabel.setForeground(Color.decode("#467ab9"));
        ixrLabel.setFont(font);
        ixrLabelRow.setBackground(Color.decode("#28282D"));
        ixrLabelRow.add(ixrLabel);

        gbcIXR.gridx = 0; gbcIXR.gridy = 0; gbcIXR.gridwidth = 3; gbcIXR.anchor = GridBagConstraints.CENTER;
        firstCenterNorthCenter.add(ixrLabelRow, gbcIXR);
        gbcIXR.gridwidth = 1; gbcIXR.anchor = GridBagConstraints.WEST;

        // IXR has no index 0 — empty placeholder keeps vertical alignment matching GPR
        JPanel zeroRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));

        zeroRowIXR.setBackground(Color.decode("#28282D"));

        gbcIXR.gridx = 0; gbcIXR.gridy = 1;
        firstCenterNorthCenter.add(zeroRowIXR, gbcIXR);

        JLabel oneLabelIXR = new JLabel("1");
        oneLabelIXR.setFont(font);
        oneLabelIXR.setForeground(Color.decode("#467ab9"));
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

        gbcIXR.gridx = 0; gbcIXR.gridy = 2; gbcIXR.anchor = GridBagConstraints.EAST;
        firstCenterNorthCenter.add(oneLabelIXR, gbcIXR);
        gbcIXR.gridx = 1; gbcIXR.anchor = GridBagConstraints.WEST;
        firstCenterNorthCenter.add(oneTextIXR, gbcIXR);
        gbcIXR.gridx = 2;
        firstCenterNorthCenter.add(oneButtonIXR, gbcIXR);

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

        JLabel twoLabelIXR = new JLabel("2");
        twoLabelIXR.setForeground(Color.decode("#467ab9"));
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

        gbcIXR.gridx = 0; gbcIXR.gridy = 3; gbcIXR.anchor = GridBagConstraints.EAST;
        firstCenterNorthCenter.add(twoLabelIXR, gbcIXR);
        gbcIXR.gridx = 1; gbcIXR.anchor = GridBagConstraints.WEST;
        firstCenterNorthCenter.add(twoTextIXR, gbcIXR);
        gbcIXR.gridx = 2;
        firstCenterNorthCenter.add(twoButtonIXR, gbcIXR);

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

        JLabel threeLabelIXR = new JLabel("3");
        threeLabelIXR.setForeground(Color.decode("#467ab9"));
        threeLabelIXR.setFont(font);
        threeTextIXR = new JTextField("",10);
        threeTextIXR.setFont(font);
        threeTextIXR.setBackground(Color.decode("#3c3c44"));
        threeTextIXR.setForeground(Color.decode("#B0D3D1"));
        JButton threeButtonIXR = new JButton();

        threeButtonIXR.setPreferredSize(new Dimension(20, 18));
        threeButtonIXR.setBackground(Color.decode("#FFBA52"));
        threeButtonIXR.setOpaque(true);
        threeButtonIXR.setBorderPainted(false);

        threeTextIXR.setEditable(false);

        gbcIXR.gridx = 0; gbcIXR.gridy = 4; gbcIXR.anchor = GridBagConstraints.EAST;
        firstCenterNorthCenter.add(threeLabelIXR, gbcIXR);
        gbcIXR.gridx = 1; gbcIXR.anchor = GridBagConstraints.WEST;
        firstCenterNorthCenter.add(threeTextIXR, gbcIXR);
        gbcIXR.gridx = 2;
        firstCenterNorthCenter.add(threeButtonIXR, gbcIXR);

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

        // shared GBC for PC / MAR / MBR / IR / CC / MFR column
        GridBagConstraints gbcRegs = new GridBagConstraints();
        gbcRegs.insets = new Insets(4, 5, 4, 5);

        JLabel pcLabel = new JLabel("PC");
        pcLabel.setFont(font);
        pcLabel.setForeground(Color.decode("#467ab9"));
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

        gbcRegs.gridx = 0; gbcRegs.gridy = 0; gbcRegs.anchor = GridBagConstraints.EAST;
        firstCenterNorthEast.add(pcLabel, gbcRegs);
        gbcRegs.gridx = 1; gbcRegs.anchor = GridBagConstraints.WEST;
        firstCenterNorthEast.add(pcText, gbcRegs);
        gbcRegs.gridx = 2;
        firstCenterNorthEast.add(pcButton, gbcRegs);

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

        JLabel marLabel = new JLabel("MAR");
        marLabel.setFont(font);
        marLabel.setForeground(Color.decode("#467ab9"));
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

        gbcRegs.gridx = 0; gbcRegs.gridy = 1; gbcRegs.anchor = GridBagConstraints.EAST;
        firstCenterNorthEast.add(marLabel, gbcRegs);
        gbcRegs.gridx = 1; gbcRegs.anchor = GridBagConstraints.WEST;
        firstCenterNorthEast.add(marText, gbcRegs);
        gbcRegs.gridx = 2;
        firstCenterNorthEast.add(marButton, gbcRegs);

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

        JLabel mbrLabel = new JLabel("MBR");
        mbrLabel.setFont(font);
        mbrLabel.setForeground(Color.decode("#467ab9"));
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

        gbcRegs.gridx = 0; gbcRegs.gridy = 2; gbcRegs.anchor = GridBagConstraints.EAST;
        firstCenterNorthEast.add(mbrLabel, gbcRegs);
        gbcRegs.gridx = 1; gbcRegs.anchor = GridBagConstraints.WEST;
        firstCenterNorthEast.add(mbrText, gbcRegs);
        gbcRegs.gridx = 2;
        firstCenterNorthEast.add(mbrButton, gbcRegs);

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

        //action listener to wait for user input in the console text field. It sends the input to the keyboard device of the memory, which the CPU can read from.
        consoleInput.addActionListener((e) -> {
            if (cpu == null) return;

            String text = consoleInput.getText().trim();
            if (text.isEmpty()) return;

            Memory rawMem = ((Cache) memory).getUnderlyingMemory();

            try {
                // try to parse as integer first
                int value = Integer.parseInt(text);
                if (value < -32768 || value > 32767) {
                    JOptionPane.showMessageDialog(this, "Input must be between -32768 and 32767.");
                    return;
                }
                rawMem.getDevice().sendKeyboardInput(value & 0xFFFF);

            } catch (NumberFormatException ex) {
                // not an integer — treat as characters
                for (char c : text.toCharArray()) {
                    rawMem.getDevice().sendKeyboardInput((int) c);
                }
            }

            consoleInput.setText("");
        });

        JLabel irLabel = new JLabel("IR");
        irLabel.setFont(font);
        irLabel.setForeground(Color.decode("#467ab9"));
        irText = new JTextField("",10);
        irText.setFont(font);
        irText.setBackground(Color.decode("#3c3c44"));
        irText.setForeground(Color.decode("#B0D3D1"));

        irText.setEditable(false);

        gbcRegs.gridx = 0; gbcRegs.gridy = 3; gbcRegs.anchor = GridBagConstraints.EAST;
        firstCenterNorthEast.add(irLabel, gbcRegs);
        gbcRegs.gridx = 1; gbcRegs.anchor = GridBagConstraints.WEST;
        firstCenterNorthEast.add(irText, gbcRegs);

        JLabel ccLabel = new JLabel("CC");
        ccLabel.setFont(font);
        ccLabel.setForeground(Color.decode("#467ab9"));
        JPanel ccTexts = new JPanel(new BorderLayout(5,5));
        JLabel oudeText = new JLabel("OUDE");
        oudeText.setFont(font);
        oudeText.setForeground(Color.decode("#467ab9"));
        ccText = new JTextField("",5);
        ccText.setFont(font);
        ccText.setBackground(Color.decode("#3c3c44"));
        ccText.setForeground(Color.decode("#B0D3D1"));

        ccText.setEditable(false);

        ccTexts.setBackground(Color.decode("#28282D"));
        ccTexts.add(ccText, BorderLayout.NORTH);
        ccTexts.add(oudeText, BorderLayout.SOUTH);

        gbcRegs.gridx = 0; gbcRegs.gridy = 4; gbcRegs.anchor = GridBagConstraints.EAST;
        firstCenterNorthEast.add(ccLabel, gbcRegs);
        gbcRegs.gridx = 1; gbcRegs.anchor = GridBagConstraints.WEST;
        firstCenterNorthEast.add(ccTexts, gbcRegs);

        JLabel mfrLabel = new JLabel("MFR");
        mfrLabel.setForeground(Color.decode("#467ab9"));
        mfrLabel.setFont(font);
        JPanel mfrTexts = new JPanel(new BorderLayout(5,5));
        JLabel mortText = new JLabel("MOTR");
        mortText.setForeground(Color.decode("#467ab9"));
        mortText.setFont(font);
        mfrText = new JTextField("",5);
        mfrText.setFont(font);
        mfrText.setBackground(Color.decode("#3c3c44"));
        mfrText.setForeground(Color.decode("#B0D3D1"));

        mfrText.setEditable(false);

        mfrTexts.setBackground(Color.decode("#28282D"));
        mfrTexts.add(mfrText, BorderLayout.NORTH);
        mfrTexts.add(mortText, BorderLayout.SOUTH);

        gbcRegs.gridx = 0; gbcRegs.gridy = 5; gbcRegs.anchor = GridBagConstraints.EAST;
        firstCenterNorthEast.add(mfrLabel, gbcRegs);
        gbcRegs.gridx = 1; gbcRegs.anchor = GridBagConstraints.WEST;
        firstCenterNorthEast.add(mfrTexts, gbcRegs);

        this.add(outer);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);

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
    
    private void startRunLoop() {

        if (cpu == null || isRunning) return;

        isRunning = true;

        new Thread(() -> {
            while (isRunning) {

                cpu.cycle();
                cycleCount++;
                printerText = "running cycle " + cycleCount + "\n" + printerText;
                updateTexts();
                if (cpu.isHalted() || cpu.getMFR() != 0) {
                    isRunning = false;
                    break;
                }
                try { Thread.sleep(2000); }
                catch (InterruptedException ex) { break; }

            }

            isRunning = false;

        }).start();

    }

    @Override
    public void onPrinterOutput(int value) {
        String output;
        // if value is a printable ASCII character, display as char
        if (value >= 32 && value <= 126) {
            output = String.valueOf((char)(value & 0xFF));
        } else {
            // otherwise display as integer
            output = String.valueOf(value) + "\n";
        }
        printerText = printerText + output;
        SwingUtilities.invokeLater(() -> {
            printer.setText(printerText);
            printer.setCaretPosition(printer.getDocument().getLength());
        });
    }

    @Override
    public void onCardReaderRead(int remaining) {
        // no-op for now
    }

    @Override
    public void onDeviceError(int devid, String message) {
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, message, "Device Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    public static void main(String args[]){

        gui simulator = new gui();
        simulator.setVisible(true);

    }
    
}