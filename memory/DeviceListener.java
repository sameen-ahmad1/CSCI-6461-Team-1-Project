package memory;

public interface DeviceListener {
    /** Called when the printer receives a value via OUT */
    void onPrinterOutput(int value);

    /** Called when IN is executed — frontend should return the next keyboard char */
    int onKeyboardInput();

    /** Called when card reader is read — for status indicator updates */
    void onCardReaderRead(int remaining);

    /** Called when a device error occurs (empty buffer, bad devid, etc.) */
    void onDeviceError(int devid, String message);



   
}
