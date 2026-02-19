import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Flow;

public class gui extends JFrame{

    public gui(){

        JPanel outer = new JPanel(new BorderLayout());

        JPanel firstNorth = new JPanel(new BorderLayout());
        JPanel firstCenter = new JPanel(new BorderLayout());
        JPanel firstEast = new JPanel(new BorderLayout());

        outer.add(firstNorth, BorderLayout.NORTH);
        outer.add(firstCenter, BorderLayout.CENTER);
        outer.add(firstEast, BorderLayout.EAST);

        JLabel title = new JLabel("CSCI 6461 Machine SImulator");
        JLabel names = new JLabel("Group 1: Zack Rahbar, Liza Mozolyuk, Wesam Abu Rabia, Sameen Ahmad");

        firstNorth.add(title, BorderLayout.NORTH);
        firstNorth.add(names, BorderLayout.SOUTH);

        JPanel firstEastNorth = new JPanel(new BorderLayout());
        JPanel firstEastCenter = new JPanel(new BorderLayout());
        JPanel firstEastSouth = new JPanel(new BorderLayout());

        firstEast.add(firstEastNorth, BorderLayout.NORTH);
        firstEast.add(firstEastCenter, BorderLayout.CENTER);
        firstEast.add(firstEastSouth, BorderLayout.SOUTH);

        JLabel cacheContentLabel = new JLabel("Cache Content");
        JTextArea cacheContent = new JTextArea("");
        cacheContent.setEditable(false);

        firstEastNorth.add(cacheContentLabel, BorderLayout.NORTH);
        firstEastNorth.add(cacheContent, BorderLayout.SOUTH);

        JLabel printerLabel = new JLabel("Printer");
        JTextArea printer = new JTextArea("");
        printer.setEditable(false);

        firstEastCenter.add(printerLabel, BorderLayout.NORTH);
        firstEastCenter.add(printer, BorderLayout.SOUTH);

        JLabel consoleInputLabel = new JLabel("Console Input");
        JTextField consoleInput = new JTextField("");
        consoleInput.setEditable(false);

        firstEastSouth.add(consoleInputLabel, BorderLayout.NORTH);
        firstEastSouth.add(consoleInput, BorderLayout.SOUTH);

        JPanel firstCenterNorth = new JPanel(new BorderLayout());
        JPanel firstCenterCenter = new JPanel(new BorderLayout());
        JPanel firstCenterSouth = new JPanel(new FlowLayout());

        firstCenter.add(firstCenterNorth, BorderLayout.NORTH);
        firstCenter.add(firstCenterCenter, BorderLayout.CENTER);
        firstCenter.add(firstCenterSouth, BorderLayout.SOUTH);

        JLabel programFileLabel = new JLabel("Program File");
        JTextField programFile = new JTextField("");
        programFile.setEditable(false);

        firstCenterSouth.add(programFileLabel);
        firstCenterSouth.add(programFile);

        JPanel firstCenterCenterWest = new JPanel(new BorderLayout());
        JPanel firstCenterCenterCenter = new JPanel(new GridLayout(0, 1));
        JPanel firstCenterCenterEast = new JPanel(new GridLayout(0, 1));

        firstCenterCenter.add(firstCenterCenterWest, BorderLayout.WEST);
        firstCenterCenter.add(firstCenterCenterCenter, BorderLayout.CENTER);
        firstCenterCenter.add(firstCenterCenterEast, BorderLayout.EAST);

        JPanel firstCenterCenterWestNorth = new JPanel(new BorderLayout());
        JPanel firstCenterCenterWestSouth = new JPanel(new FlowLayout());

        firstCenterCenterWest.add(firstCenterCenterWestNorth, BorderLayout.NORTH);
        firstCenterCenterWest.add(firstCenterCenterWestSouth, BorderLayout.SOUTH);

        JLabel binaryLabel = new JLabel("Binary");
        JTextField binary = new JTextField("");
        binary.setEditable(false);

        firstCenterCenterWestNorth.add(binaryLabel, BorderLayout.NORTH);
        firstCenterCenterWestNorth.add(binary, BorderLayout.SOUTH);

        JLabel octalInputLabel = new JLabel("Octal Input");
        JTextField octalInput = new JTextField("");

        firstCenterCenterWestSouth.add(octalInputLabel);
        firstCenterCenterWestSouth.add(octalInput);

        JPanel loadRow = new JPanel(new FlowLayout());
        JButton loadButton = new JButton();
        JLabel loadLabel = new JLabel("Load");

        loadRow.add(loadButton);
        loadRow.add(loadLabel);

        JPanel loadPlusRow = new JPanel(new FlowLayout());
        JButton loadPlusButton = new JButton();
        JLabel loadPlusLabel = new JLabel("Load+");

        loadPlusRow.add(loadPlusButton);
        loadPlusRow.add(loadPlusLabel);

        JPanel storeRow = new JPanel(new FlowLayout());
        JButton storeButton = new JButton();
        JLabel storeLabel = new JLabel("Store");

        storeRow.add(storeButton);
        storeRow.add(storeLabel);

        JPanel storePlusRow = new JPanel(new FlowLayout());
        JButton storelusButton = new JButton();
        JLabel storePlusLabel = new JLabel("Store+");

        storePlusRow.add(storelusButton);
        storePlusRow.add(storePlusLabel);

        firstCenterCenterCenter.add(loadRow);
        firstCenterCenterCenter.add(loadPlusRow);
        firstCenterCenterCenter.add(storeRow);
        firstCenterCenterCenter.add(storePlusRow);

        JPanel runRow = new JPanel(new FlowLayout());
        JButton runButton = new JButton();
        JLabel runLabel = new JLabel("Run");

        runRow.add(runButton);
        runRow.add(runLabel);

        JPanel stepRow = new JPanel(new FlowLayout());
        JButton stepButton = new JButton();
        JLabel stepLabel = new JLabel("Step");

        stepRow.add(stepButton);
        stepRow.add(stepLabel);

        JPanel haltRow = new JPanel(new FlowLayout());
        JButton haltButton = new JButton();
        JLabel haltLabel = new JLabel("Halt");

        haltRow.add(haltButton);
        haltRow.add(haltLabel);

        JPanel IPLRow = new JPanel(new FlowLayout());
        JButton IPLButton = new JButton();
        JLabel IPLLabel = new JLabel("IPL");

        IPLRow.add(IPLButton);
        IPLRow.add(IPLLabel);

        firstCenterCenterEast.add(runRow);
        firstCenterCenterEast.add(stepRow);
        firstCenterCenterEast.add(haltRow);
        firstCenterCenterEast.add(IPLRow);

        JPanel firstCenterNorthWest = new JPanel(new GridLayout(0, 1));
        JPanel firstCenterNorthCenter = new JPanel(new GridLayout(0, 1));
        JPanel firstCenterNorthEast = new JPanel(new GridLayout(0, 1));

        firstCenterNorth.add(firstCenterNorthWest, BorderLayout.WEST);
        firstCenterNorth.add(firstCenterNorthCenter, BorderLayout.CENTER);
        firstCenterNorth.add(firstCenterNorthEast, BorderLayout.EAST);

        JLabel gprLabel = new JLabel("GPR");

        JPanel zeroRowGPR = new JPanel(new FlowLayout());
        JLabel zeroLabelGPR = new JLabel("0");
        JTextField zeroTextGPR = new JTextField("");
        JButton zeroButtonGPR = new JButton();

        zeroRowGPR.add(zeroLabelGPR);
        zeroRowGPR.add(zeroTextGPR);
        zeroRowGPR.add(zeroButtonGPR);

        JPanel oneRowGPR = new JPanel(new FlowLayout());
        JLabel oneLabelGPR = new JLabel("1");
        JTextField oneTextGPR = new JTextField("");
        JButton oneButtonGPR = new JButton();

        oneRowGPR.add(oneLabelGPR);
        oneRowGPR.add(oneTextGPR);
        oneRowGPR.add(oneButtonGPR);

        JPanel twoRowGPR = new JPanel(new FlowLayout());
        JLabel twoLabelGPR = new JLabel("2");
        JTextField twoTextGPR = new JTextField("");
        JButton twoButtonGPR = new JButton();

        twoRowGPR.add(twoLabelGPR);
        twoRowGPR.add(twoTextGPR);
        twoRowGPR.add(twoButtonGPR);

        JPanel threeRowGPR = new JPanel(new FlowLayout());
        JLabel threeLabelGPR = new JLabel("3");
        JTextField threeTextGPR = new JTextField("");
        JButton threeButtonGPR = new JButton();

        threeRowGPR.add(threeLabelGPR);
        threeRowGPR.add(threeTextGPR);
        threeRowGPR.add(threeButtonGPR);

        firstCenterNorthWest.add(gprLabel);
        firstCenterNorthWest.add(zeroRowGPR);
        firstCenterNorthWest.add(oneRowGPR);
        firstCenterNorthWest.add(twoRowGPR);
        firstCenterNorthWest.add(threeRowGPR);

        JLabel ixrLabel = new JLabel("IXR");

        JPanel zeroRowIXR = new JPanel(new FlowLayout());

        JPanel oneRowIXR = new JPanel(new FlowLayout());
        JLabel oneLabelIXR = new JLabel("1");
        JTextField oneTextIXR = new JTextField("");
        JButton oneButtonIXR = new JButton();

        oneRowIXR.add(oneLabelIXR);
        oneRowIXR.add(oneTextIXR);
        oneRowIXR.add(oneButtonIXR);

        JPanel twoRowIXR = new JPanel(new FlowLayout());
        JLabel twoLabelIXR = new JLabel("2");
        JTextField twoTextIXR= new JTextField("");
        JButton twoButtonIXR = new JButton();

        twoRowIXR.add(twoLabelIXR);
        twoRowIXR.add(twoTextIXR);
        twoRowIXR.add(twoButtonIXR);

        JPanel threeRowIXR = new JPanel(new FlowLayout());
        JLabel threeLabelIXR = new JLabel("3");
        JTextField threeTextIXR = new JTextField("");
        JButton threeButtonIXR = new JButton();

        threeRowIXR.add(threeLabelIXR);
        threeRowIXR.add(threeTextIXR);
        threeRowIXR.add(threeButtonIXR);

        firstCenterNorthCenter.add(ixrLabel);
        firstCenterNorthCenter.add(zeroRowIXR);
        firstCenterNorthCenter.add(oneRowIXR);
        firstCenterNorthCenter.add(twoRowIXR);
        firstCenterNorthCenter.add(threeRowIXR);

        JPanel pcRow = new JPanel(new FlowLayout());
        JLabel pcLabel = new JLabel("PC");
        JTextField pcText = new JTextField("");
        JButton pcButton = new JButton();

        pcRow.add(pcLabel);
        pcRow.add(pcText);
        pcRow.add(pcButton);

        JPanel marRow = new JPanel(new FlowLayout());
        JLabel marLabel = new JLabel("MAR");
        JTextField marText = new JTextField("");
        JButton marButton = new JButton();

        marRow.add(marLabel);
        marRow.add(marText);
        marRow.add(marButton);

        JPanel mbrRow = new JPanel(new FlowLayout());
        JLabel mbrLabel = new JLabel("MBR");
        JTextField mbrText = new JTextField("");
        JButton mbrButton = new JButton();

        mbrRow.add(mbrLabel);
        mbrRow.add(mbrText);
        mbrRow.add(mbrButton);

        JPanel irRow = new JPanel(new FlowLayout());
        JLabel irLabel = new JLabel("IR");
        JTextField irText = new JTextField("");
        JButton irButton = new JButton();

        irRow.add(irLabel);
        irRow.add(irText);
        irRow.add(irButton);

        JPanel ccRow = new JPanel(new FlowLayout());
        JLabel ccLabel = new JLabel("CC");
        JPanel ccTexts = new JPanel(new BorderLayout());
        JLabel oudeText = new JLabel("OUDE");
        JTextField ccText = new JTextField("");

        ccTexts.add(ccText, BorderLayout.NORTH);
        ccTexts.add(oudeText, BorderLayout.SOUTH);
        ccRow.add(ccLabel);
        ccRow.add(ccTexts);

        JPanel mfrRow = new JPanel(new FlowLayout());
        JLabel mfrLabel = new JLabel("MFR");
        JPanel mfrTexts = new JPanel(new BorderLayout());
        JLabel mortText = new JLabel("MOTR");
        JTextField mfrText = new JTextField("");

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
