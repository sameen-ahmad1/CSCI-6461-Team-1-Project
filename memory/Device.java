package memory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handles I/O peripherals for the simulator.
 * Device IDs:
 *   0 - Keyboard  (input only)
 *   1 - Printer   (output only)
 *   2 - Card Reader (input only)
 */
public class Device {

    private static final int DEVID_KEYBOARD    = 0;
    private static final int DEVID_PRINTER     = 1;
    private static final int DEVID_CARD_READER = 2;

    // Card reader buffer — loaded before execution via loadCards()
    private final BlockingQueue<Integer> cardBuffer = new LinkedBlockingQueue<>();
    // Keyboard buffer — loaded before execution via loadKeyboardInput()
    private final BlockingQueue<Integer> keyboardBuffer = new LinkedBlockingQueue<>();

    public DeviceListener listener = null;

    // sets a listener for device events (printer output, keyboard input, etc.)
    public void setListener(DeviceListener listener) {
        this.listener = listener;
    }   

    // ----------------------------------------------------------------
    // Called by Memory.inputDevice(devid)
    // ----------------------------------------------------------------
    public int read(int devid) {
        switch (devid) {
            case DEVID_KEYBOARD:
                return readKeyboard();
            case DEVID_CARD_READER:
                return readCardReader();
            default:
                System.err.println("Device: IN from unknown or write-only device: " + devid);
                return 0;
        }
    }

    // ----------------------------------------------------------------
    // Called by Memory.outputDevice(devid, value)
    // ----------------------------------------------------------------
    public void write(int devid, int value) {
        switch (devid) {
            case DEVID_PRINTER:
                writePrinter(value);
                break;
            default:
                System.err.println("Device: OUT to unknown or read-only device: " + devid);
        }
    }

    // ----------------------------------------------------------------
    // Device implementations
    // ----------------------------------------------------------------

    private int readKeyboard() {
        System.out.println("DEBUG: IN waiting for keyboard input...");
        try {
            int value = keyboardBuffer.take(); // blocks until a character is available
            System.out.println("DEBUG: IN received value: " + value + " char: " + (char)(value & 0xFF));
            return value & 0xFFFF;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        }
    }

    // GUI calls this to push a character into the queue
    public void sendKeyboardInput(int value) {
        keyboardBuffer.add(value & 0xFFFF);
    }



    public void loadKeyboardInput(String input) {
        keyboardBuffer.clear();
        for (char c : input.toCharArray()) {
            keyboardBuffer.add((int) c);
        }
    }


    private void writePrinter(int value) {
        System.out.println("DEBUG writePrinter called, listener=" + (listener != null ? "SET" : "NULL") + " value=" + value);
        if (listener != null) {
            // print to the GUI printer area
            listener.onPrinterOutput(value & 0xFFFF);
        } else {
            System.out.print((char) (value & 0xFF));
        }
    }

    private int readCardReader() {
        if (!cardBuffer.isEmpty()) {
            return cardBuffer.poll() & 0xFFFF;
        }
        System.err.println("Device: card reader buffer is empty");
        return 0;
    }

    // ----------------------------------------------------------------
    // Card reader setup — call this before starting execution
    // ----------------------------------------------------------------

    /**
     * Pre-loads the card reader buffer with an array of values.
     * Each entry represents one "card" word read by an IN r, 2 instruction.
     */
    public void loadCards(int[] cards) {
        cardBuffer.clear();
        for (int card : cards) {
            cardBuffer.add(card & 0xFFFF);
        }
    }

    /**
     * Returns how many card words remain unread.
     * Useful for post-execution sanity checks in tests.
     */
    public int cardBufferSize() {
        return cardBuffer.size();
    }
}
