package memory;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

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
    private final Queue<Integer> cardBuffer = new ArrayDeque<>();
    // Keyboard buffer — loaded before execution via loadKeyboardInput()
    private final Queue<Integer> keyboardBuffer = new ArrayDeque<>();
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
        if (listener != null) {
            return listener.onKeyboardInput() & 0xFFFF;
        }
        if (!keyboardBuffer.isEmpty()) {
            return keyboardBuffer.poll() & 0xFFFF;
        }
        // no listener, no buffer — return 0 instead of blocking
        System.err.println("Device: keyboard buffer empty, returning 0");
        return 0;
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
