public class MachineFault extends RuntimeException {

    public enum Code {
        ILLEGAL_MEMORY_ADDRESS(1),
        ILLEGAL_OPCODE(2),
        LOADER_FORMAT_ERROR(3),
        MEMORY_BUSY(4);

        public final int value;

        Code(int value) {
            this.value = value;
        }
    }

    private final Code code;
    private final int detail;

    public MachineFault(Code code, int detail, String message) {
        super(message);
        this.code = code;
        this.detail = detail;
    }

    public MachineFault(Code code, int detail, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.detail = detail;
    }

    public Code code() {
        return code;
    }

    public int detail() {
        return detail;
    }
}
