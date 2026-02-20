import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Flow;

public class gui extends JFrame{

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
        JTextArea cacheContent = new JTextArea("",20,30);
        cacheContent.setEditable(false);

        firstEastNorth.add(cacheContentLabel, BorderLayout.NORTH);
        firstEastNorth.add(cacheContent, BorderLayout.SOUTH);

        JLabel printerLabel = new JLabel("Printer");
        JTextArea printer = new JTextArea("",10,20);
        printer.setEditable(false);

        firstEastCenter.add(printerLabel, BorderLayout.NORTH);
        firstEastCenter.add(printer, BorderLayout.SOUTH);

        JLabel consoleInputLabel = new JLabel("Console Input");
        JTextField consoleInput = new JTextField("", 20);

        firstEastSouth.add(consoleInputLabel, BorderLayout.NORTH);
        firstEastSouth.add(consoleInput, BorderLayout.SOUTH);

        JPanel firstCenterNorth = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterCenter = new JPanel(new BorderLayout(5,5));
        JPanel firstCenterSouth = new JPanel(new FlowLayout());

        firstCenter.add(firstCenterNorth, BorderLayout.NORTH);
        firstCenter.add(firstCenterCenter, BorderLayout.CENTER);
        firstCenter.add(firstCenterSouth, BorderLayout.SOUTH);

        JLabel programFileLabel = new JLabel("Program File");
        JTextField programFile = new JTextField("", 50);

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
        JTextField binary = new JTextField("", 10);
        binary.setEditable(false);

        firstCenterCenterWestNorth.add(binaryLabel, BorderLayout.NORTH);
        firstCenterCenterWestNorth.add(binary, BorderLayout.SOUTH);

        JLabel octalInputLabel = new JLabel("Octal Input");
        JTextField octalInput = new JTextField("", 8);

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

        });

        JPanel loadPlusRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton loadPlusButton = new JButton();
        JLabel loadPlusLabel = new JLabel("Load+");

        loadPlusButton.setPreferredSize(new Dimension(20, 18));

        loadPlusRow.add(loadPlusButton);
        loadPlusRow.add(loadPlusLabel);

        loadPlusButton.addActionListener((e) -> {

        });

        JPanel storeRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton storeButton = new JButton();
        JLabel storeLabel = new JLabel("Store");

        storeButton.setPreferredSize(new Dimension(20, 18));

        storeRow.add(storeButton);
        storeRow.add(storeLabel);

        storeButton.addActionListener((e) -> {

        });

        JPanel storePlusRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton storePlusButton = new JButton();
        JLabel storePlusLabel = new JLabel("Store+");

        storePlusButton.setPreferredSize(new Dimension(20, 18));

        storePlusRow.add(storePlusButton);
        storePlusRow.add(storePlusLabel);

        storePlusButton.addActionListener((e) -> {

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

        });

        JPanel stepRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton stepButton = new JButton();
        JLabel stepLabel = new JLabel("Step");

        stepButton.setPreferredSize(new Dimension(20, 18));

        stepRow.add(stepButton);
        stepRow.add(stepLabel);

        stepButton.addActionListener((e) -> {

        });

        JPanel haltRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton haltButton = new JButton();
        JLabel haltLabel = new JLabel("Halt");

        haltButton.setPreferredSize(new Dimension(20, 18));

        haltRow.add(haltButton);
        haltRow.add(haltLabel);

        haltButton.addActionListener((e) -> {

        });

        JPanel IPLRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton IPLButton = new JButton();
        JLabel IPLLabel = new JLabel("IPL");

        IPLButton.setPreferredSize(new Dimension(20, 18));

        IPLRow.add(IPLButton);
        IPLRow.add(IPLLabel);

        IPLButton.addActionListener((e) -> {

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
        JTextField zeroTextGPR = new JTextField("",10);
        JButton zeroButtonGPR = new JButton();

        zeroButtonGPR.setPreferredSize(new Dimension(20, 18));
        zeroTextGPR.setEditable(false);

        zeroRowGPR.add(zeroLabelGPR);
        zeroRowGPR.add(zeroTextGPR);
        zeroRowGPR.add(zeroButtonGPR);

        zeroButtonGPR.addActionListener((e) -> {

        });

        JPanel oneRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel oneLabelGPR = new JLabel("1");
        JTextField oneTextGPR = new JTextField("",10);
        JButton oneButtonGPR = new JButton();

        oneButtonGPR.setPreferredSize(new Dimension(20, 18));
        oneTextGPR.setEditable(false);

        oneRowGPR.add(oneLabelGPR);
        oneRowGPR.add(oneTextGPR);
        oneRowGPR.add(oneButtonGPR);

        oneButtonGPR.addActionListener((e) -> {

        });

        JPanel twoRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel twoLabelGPR = new JLabel("2");
        JTextField twoTextGPR = new JTextField("",10);
        JButton twoButtonGPR = new JButton();

        twoButtonGPR.setPreferredSize(new Dimension(20, 18));
        twoTextGPR.setEditable(false);

        twoRowGPR.add(twoLabelGPR);
        twoRowGPR.add(twoTextGPR);
        twoRowGPR.add(twoButtonGPR);

        twoButtonGPR.addActionListener((e) -> {

        });

        JPanel threeRowGPR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel threeLabelGPR = new JLabel("3");
        JTextField threeTextGPR = new JTextField("",10);
        JButton threeButtonGPR = new JButton();

        threeButtonGPR.setPreferredSize(new Dimension(20, 18));
        threeTextGPR.setEditable(false);

        threeRowGPR.add(threeLabelGPR);
        threeRowGPR.add(threeTextGPR);
        threeRowGPR.add(threeButtonGPR);

        threeButtonGPR.addActionListener((e) -> {

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
        JTextField oneTextIXR = new JTextField("",10);
        JButton oneButtonIXR = new JButton();

        oneButtonIXR.setPreferredSize(new Dimension(20, 18));
        oneTextIXR.setEditable(false);

        oneRowIXR.add(oneLabelIXR);
        oneRowIXR.add(oneTextIXR);
        oneRowIXR.add(oneButtonIXR);

        oneButtonIXR.addActionListener((e) -> {

        });

        JPanel twoRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel twoLabelIXR = new JLabel("2");
        JTextField twoTextIXR= new JTextField("",10);
        JButton twoButtonIXR = new JButton();

        twoButtonIXR.setPreferredSize(new Dimension(20, 18));
        twoTextIXR.setEditable(false);

        twoRowIXR.add(twoLabelIXR);
        twoRowIXR.add(twoTextIXR);
        twoRowIXR.add(twoButtonIXR);

        twoButtonIXR.addActionListener((e) -> {

        });

        JPanel threeRowIXR = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel threeLabelIXR = new JLabel("3");
        JTextField threeTextIXR = new JTextField("",10);
        JButton threeButtonIXR = new JButton();

        threeButtonIXR.setPreferredSize(new Dimension(20, 18));
        threeTextIXR.setEditable(false);

        threeRowIXR.add(threeLabelIXR);
        threeRowIXR.add(threeTextIXR);
        threeRowIXR.add(threeButtonIXR);

        threeButtonIXR.addActionListener((e) -> {

        });

        firstCenterNorthCenter.add(ixrLabelRow);
        firstCenterNorthCenter.add(zeroRowIXR);
        firstCenterNorthCenter.add(oneRowIXR);
        firstCenterNorthCenter.add(twoRowIXR);
        firstCenterNorthCenter.add(threeRowIXR);

        JPanel pcRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel pcLabel = new JLabel("PC");
        JTextField pcText = new JTextField("",10);
        JButton pcButton = new JButton();

        pcButton.setPreferredSize(new Dimension(20, 18));
        pcText.setEditable(false);

        pcRow.add(pcLabel);
        pcRow.add(pcText);
        pcRow.add(pcButton);

        pcButton.addActionListener((e) -> {

        });

        JPanel marRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel marLabel = new JLabel("MAR");
        JTextField marText = new JTextField("",10);
        JButton marButton = new JButton();

        marButton.setPreferredSize(new Dimension(20, 18));
        marText.setEditable(false);

        marRow.add(marLabel);
        marRow.add(marText);
        marRow.add(marButton);

        marButton.addActionListener((e) -> {

        });

        JPanel mbrRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel mbrLabel = new JLabel("MBR");
        JTextField mbrText = new JTextField("",10);
        JButton mbrButton = new JButton();

        mbrButton.setPreferredSize(new Dimension(20, 18));
        mbrText.setEditable(false);

        mbrRow.add(mbrLabel);
        mbrRow.add(mbrText);
        mbrRow.add(mbrButton);

        mbrButton.addActionListener((e) -> {

        });

        JPanel irRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel irLabel = new JLabel("IR");
        JTextField irText = new JTextField("",10);
        JButton irButton = new JButton();

        irButton.setPreferredSize(new Dimension(20, 18));
        irText.setEditable(false);

        irRow.add(irLabel);
        irRow.add(irText);
        irRow.add(irButton);

        irButton.addActionListener((e) -> {

        });

        JPanel ccRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel ccLabel = new JLabel("CC");
        JPanel ccTexts = new JPanel(new BorderLayout(5,5));
        JLabel oudeText = new JLabel("OUDE");
        JTextField ccText = new JTextField("",5);

        ccText.setEditable(false);

        ccTexts.add(ccText, BorderLayout.NORTH);
        ccTexts.add(oudeText, BorderLayout.SOUTH);
        ccRow.add(ccLabel);
        ccRow.add(ccTexts);

        JPanel mfrRow = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JLabel mfrLabel = new JLabel("MFR");
        JPanel mfrTexts = new JPanel(new BorderLayout(5,5));
        JLabel mortText = new JLabel("MOTR");
        JTextField mfrText = new JTextField("",5);

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

    public static void main(String args[]){

        gui simulator = new gui();
        simulator.setVisible(true);

    }
    
}
