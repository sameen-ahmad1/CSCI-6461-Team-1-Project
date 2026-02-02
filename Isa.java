// Isa.java
import java.util.HashMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class Isa {
    private Isa() {}

    // Operand shapes (assembly-level) used by parser
    public enum Format {
        NONE,               // HLT
        TRAP_CODE,          // TRAP code

        R_X_ADDR_I,         // r, x, address[,I]   (LDR/STR/LDA/JZ/JNE/SOB/JGE/AMR/SMR/CNVRT)
        CC_X_ADDR_I,        // cc, x, address[,I]  (JCC)
        X_ADDR_I,           // x, address[,I]      (LDX/STX/JMA/JSR)

        R_IMM,              // r, immed            (AIR/SIR)
        IMM,                // immed               (RFS)

        RX_RY,              // rx, ry              (TRR/AND/ORR/MLT/DVD)
        RX_ONLY,            // rx                  (NOT)

        SHIFT_ROT,          // r, count, L/R, A/L  (SRC/RRC)
        IO_R_DEVID,         // r, devid            (IN/OUT/CHK)

        FR_X_ADDR_I         // fr, x, address[,I]  (FADD/FSUB/VADD/VSUB/LDFR/STFR)
    }

    /**
     * Instruction set: mnemonic + octal opcode + format.
     * opcode is parsed from octal for later encoding/decoding.
     */
    public enum Instruction {
        // ===== Control =====
        HLT  ("00", Format.NONE),
        TRAP ("30", Format.TRAP_CODE),

        // ===== Load / Store =====
        LDR  ("01", Format.R_X_ADDR_I),
        STR  ("02", Format.R_X_ADDR_I),
        LDA  ("03", Format.R_X_ADDR_I),

        LDX  ("41", Format.X_ADDR_I),
        STX  ("42", Format.X_ADDR_I),

        // ===== Transfers / Branch =====
        JZ   ("10", Format.R_X_ADDR_I),
        JNE  ("11", Format.R_X_ADDR_I),
        JCC  ("12", Format.CC_X_ADDR_I),
        JMA  ("13", Format.X_ADDR_I),
        JSR  ("14", Format.X_ADDR_I),
        RFS  ("15", Format.IMM),
        SOB  ("16", Format.R_X_ADDR_I),
        JGE  ("17", Format.R_X_ADDR_I),

        // ===== Arithmetic / Logical (memory) =====
        AMR  ("04", Format.R_X_ADDR_I),
        SMR  ("05", Format.R_X_ADDR_I),

        // ===== Arithmetic (immediate) =====
        AIR  ("06", Format.R_IMM),
        SIR  ("07", Format.R_IMM),

        // ===== Register-Register Ops =====
        MLT  ("70", Format.RX_RY),
        DVD  ("71", Format.RX_RY),
        TRR  ("72", Format.RX_RY),
        AND  ("73", Format.RX_RY),
        ORR  ("74", Format.RX_RY),
        NOT  ("75", Format.RX_ONLY),

        // ===== Shift / Rotate =====
        SRC  ("31", Format.SHIFT_ROT),
        RRC  ("32", Format.SHIFT_ROT),

        // ===== I/O =====
        IN   ("61", Format.IO_R_DEVID),
        OUT  ("62", Format.IO_R_DEVID),
        CHK  ("63", Format.IO_R_DEVID),

        // ===== Floating / Vector / Convert =====
        FADD ("33", Format.FR_X_ADDR_I),
        FSUB ("34", Format.FR_X_ADDR_I),
        VADD ("35", Format.FR_X_ADDR_I),
        VSUB ("36", Format.FR_X_ADDR_I),
        CNVRT("37", Format.R_X_ADDR_I),

        LDFR ("50", Format.FR_X_ADDR_I),
        STFR ("51", Format.FR_X_ADDR_I);

        public final String opcodeOctal;
        public final int opcode;        // parsed from octal (0..63)
        public final String mnemonic;   // same as enum name
        public final Format format;

        Instruction(String opcodeOctal, Format format) {
            this.opcodeOctal = opcodeOctal;
            this.opcode = Integer.parseInt(opcodeOctal, 8);
            this.mnemonic = name().toUpperCase(Locale.ROOT);
            this.format = format;
        }
        
    }
}
