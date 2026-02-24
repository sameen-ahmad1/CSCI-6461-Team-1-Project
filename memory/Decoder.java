package memory;
import java.util.HashMap;
import java.util.Map;

import assembler.Isa;

/**
 * Decoder class: converts a 16-bit instruction word into structured fields.
 * This is separate from Executor to keep decoding logic isolated and reusable.
 * The decode method throws MachineFault(ILLEGAL_OPCODE) if the opcode is not recognized.
 * The Decoded class contains all fields extracted from the instruction word, including:
 * - raw: the original 16-bit word
 * - ins: the Isa.Instruction enum corresponding to the opcode
 * - opcode: the numeric opcode (0..63)
 * - r, x, i, addr: common fields used by many instruction formats
 * - al, lr, count: fields specific to SHIFT_ROT format
 * The computeEA method in Executor can use the r, x, i, addr fields to compute effective addresses for load/store instructions.
 * The execLoadStore method in Executor can use the decoded instruction and computed EA to perform the actual load/store operations.
 */
public final class Decoder {
    private Decoder() {}

    // instruction lookup from Isa.Instruction.opcode
    private static final Map<Integer, Isa.Instruction> OPCODE_MAP = new HashMap<>();
    static {
        for (Isa.Instruction ins : Isa.Instruction.values()) {
            OPCODE_MAP.put(ins.opcode, ins);
        }
    }

    public static final class Decoded {
        public final int raw;               // full 16-bit word (0..65535)
        public final Isa.Instruction ins;   // mnemonic + format
        public final int opcode;            // 0..63

        // Common fields used by many formats:
        // [OP:6][R:2][X:2][I:1][ADDR:5]
        public final int r;                 // bits 9..8
        public final int x;                 // bits 7..6
        public final int i;                 // bit 5
        public final int addr;              // bits 4..0 (5-bit field)

        // SHIFT_ROT fields:
        // assumes [OP:6][R:2][A/L:1][L/R:1][COUNT:4][00:2]
        public final int al;                // bit 7
        public final int lr;                // bit 6
        public final int count;             // bits 5..2

        public Decoded(int raw, Isa.Instruction ins, int opcode,
                       int r, int x, int i, int addr,
                       int al, int lr, int count) {
            this.raw = raw & 0xFFFF;
            this.ins = ins;
            this.opcode = opcode & 0x3F;

            this.r = r & 0x03;
            this.x = x & 0x03;
            this.i = i & 0x01;
            this.addr = addr & 0x1F;

            this.al = al & 0x01;
            this.lr = lr & 0x01;
            this.count = count & 0x0F;
        }

        @Override
        public String toString() {
            return ins.mnemonic + " op=" + opcode +
                    " r=" + r + " x=" + x + " i=" + i + " addr=" + addr +
                    " (al=" + al + " lr=" + lr + " count=" + count + ")";
        }
    }

    /**
     * Decodes a 16-bit instruction word.
     * Throws MachineFault(ILLEGAL_OPCODE) if opcode not in Isa.Instruction.
     */
    public static Decoded decode(int word16) {
        int raw = word16 & 0xFFFF;

        // opcode is top 6 bits
        int opcode = (raw >>> 10) & 0x3F;
        Isa.Instruction ins = OPCODE_MAP.get(opcode);
        if (ins == null) {
            throw new MachineFault(
                    MachineFault.Code.ILLEGAL_OPCODE,
                    opcode,
                    "Illegal opcode: " + opcode + " (octal " + Integer.toString(opcode, 8) + ")"
            );
        }

        // Basic memory-format fields
        int r = (raw >>> 8) & 0x03;
        int x = (raw >>> 6) & 0x03;
        int i = (raw >>> 5) & 0x01;
        int addr = raw & 0x1F;

        // SHIFT_ROT extraction, matches how assembler packs it
        int al = (raw >>> 7) & 0x01;
        int lr = (raw >>> 6) & 0x01;
        int count = (raw >>> 2) & 0x0F;

        return new Decoded(raw, ins, opcode, r, x, i, addr, al, lr, count);
    }
}

