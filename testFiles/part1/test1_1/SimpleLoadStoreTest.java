package testFiles.part1.test1_1;
import assembler.Isa;
import memory.Decoder;
import memory.simple.Memory;

public class SimpleLoadStoreTest {

    private static void check(boolean ok, String msg) {
        if (!ok) throw new RuntimeException("FAILED: " + msg);
    }

    // Encode like your assembler for R_X_ADDR_I:
    // [OP:6][R:2][X:2][I:1][ADDR:5]
    private static int enc_R_X_ADDR_I(Isa.Instruction ins, int r, int x, int addr, int i) {
        int w = (ins.opcode << 10);
        w |= (r & 0x3) << 8;
        w |= (x & 0x3) << 6;
        w |= (i & 0x1) << 5;
        w |= (addr & 0x1F);
        return w & 0xFFFF;
    }

    // Encode like your assembler for X_ADDR_I:
    // [OP:6][R:2=0][X:2][I:1][ADDR:5]
    private static int enc_X_ADDR_I(Isa.Instruction ins, int x, int addr, int i) {
        int w = (ins.opcode << 10);
        w |= (x & 0x3) << 6;
        w |= (i & 0x1) << 5;
        w |= (addr & 0x1F);
        return w & 0xFFFF;
    }

    // Minimal EA computation matching your current design (5-bit addr + optional indexing + optional indirect)
    private static int computeEA(Decoder.Decoded d, int[] IX, Memory mem) {
        int ea = d.addr & 0x1F; // 0..31

        // Indexing ONLY for r,x,address[,I] family
        if (d.ins == Isa.Instruction.LDR || d.ins == Isa.Instruction.STR || d.ins == Isa.Instruction.LDA) {
            if (d.x != 0) {
                ea = ea + (IX[d.x] & 0xFFFF);
            }
        }

        // Clamp to memory size for current Memory.SIZE=2048
        ea &= 0x7FF;

        // Indirect for both families if I=1
        if (d.i == 1) {
            ea = mem.peek(ea) & 0x7FF;
        }

        return ea;
    }

    public static void main(String[] args) {
        Memory mem = new Memory();

        int[] GPR = new int[4];
        int[] IX  = new int[4];

        // ----------------------------
        // 1) STR: store GPR[r] -> M[EA]
        // ----------------------------
        GPR[2] = 0xBEEF;

        int strWord = enc_R_X_ADDR_I(Isa.Instruction.STR, 2, 0, 10, 0);
        Decoder.Decoded dSTR = Decoder.decode(strWord);

        check(dSTR.ins == Isa.Instruction.STR, "Decode STR mnemonic");
        check(dSTR.r == 2 && dSTR.x == 0 && dSTR.i == 0 && dSTR.addr == 10, "Decode STR fields");

        int eaSTR = computeEA(dSTR, IX, mem);
        mem.directWrite(eaSTR, GPR[dSTR.r]);
        check(mem.peek(10) == 0xBEEF, "STR should write 0xBEEF to M[10]");

        // ----------------------------
        // 2) LDR: load M[EA] -> GPR[r]
        // ----------------------------
        GPR[2] = 0;

        int ldrWord = enc_R_X_ADDR_I(Isa.Instruction.LDR, 2, 0, 10, 0);
        Decoder.Decoded dLDR = Decoder.decode(ldrWord);

        check(dLDR.ins == Isa.Instruction.LDR, "Decode LDR mnemonic");
        check(dLDR.r == 2 && dLDR.x == 0 && dLDR.i == 0 && dLDR.addr == 10, "Decode LDR fields");

        int eaLDR = computeEA(dLDR, IX, mem);
        GPR[dLDR.r] = mem.peek(eaLDR);
        check(GPR[2] == 0xBEEF, "LDR should load 0xBEEF into R2");

        // ----------------------------
        // 3) LDA: load EA -> GPR[r] (no memory read/write)
        // Add indexing to prove X works: IX1=7, addr=20 => EA=27
        // ----------------------------
        IX[1] = 7;

        int ldaWord = enc_R_X_ADDR_I(Isa.Instruction.LDA, 1, 1, 20, 0);
        Decoder.Decoded dLDA = Decoder.decode(ldaWord);

        check(dLDA.ins == Isa.Instruction.LDA, "Decode LDA mnemonic");
        check(dLDA.r == 1 && dLDA.x == 1 && dLDA.i == 0 && dLDA.addr == 20, "Decode LDA fields");

        int eaLDA = computeEA(dLDA, IX, mem);
        GPR[dLDA.r] = eaLDA & 0xFFFF;
        check(GPR[1] == 27, "LDA should set R1 to EA=27");

        // ----------------------------
        // 4) STX: store IX[x] -> M[EA]
        // ----------------------------
        IX[2] = 0x1234;

        int stxWord = enc_X_ADDR_I(Isa.Instruction.STX, 2, 15, 0);
        Decoder.Decoded dSTX = Decoder.decode(stxWord);

        int eaSTX = computeEA(dSTX, IX, mem);
        mem.directWrite(eaSTX, IX[dSTX.x]);

        check(mem.peek(eaSTX) == 0x1234,
            "STX should write 0x1234 to M[EA]. EA=" + eaSTX + " got=" + Integer.toHexString(mem.peek(eaSTX)));


        // ----------------------------
        // 5) LDX: load M[EA] -> IX[x]
        // ----------------------------
        IX[2] = 0;

        int ldxWord = enc_X_ADDR_I(Isa.Instruction.LDX, 2, 15, 0);
        Decoder.Decoded dLDX = Decoder.decode(ldxWord);

        int eaLDX = computeEA(dLDX, IX, mem);
        IX[dLDX.x] = mem.peek(eaLDX);

        check(IX[2] == 0x1234,
            "LDX should load 0x1234 into X2 from M[EA]. EA=" + eaLDX);

        // ----------------------------
        // Optional: indirect addressing sanity check (LDR with I=1)
        // M[5] = 21, M[21] = 0xCAFE, so LDR r0,0,5,1 loads 0xCAFE
        // ----------------------------
        mem.directWrite(5, 21);
        mem.directWrite(21, 0xCAFE);

        int ldrIndWord = enc_R_X_ADDR_I(Isa.Instruction.LDR, 0, 0, 5, 1);
        Decoder.Decoded dInd = Decoder.decode(ldrIndWord);

        check(dInd.ins == Isa.Instruction.LDR, "Decode indirect LDR mnemonic");
        check(dInd.i == 1 && dInd.addr == 5, "Decode indirect LDR fields");

        int eaInd = computeEA(dInd, IX, mem);
        GPR[dInd.r] = mem.peek(eaInd);

        check(eaInd == 21, "Indirect EA should become 21");
        check(GPR[0] == 0xCAFE, "Indirect LDR should load 0xCAFE into R0");

        System.out.println("SimpleLoadStoreTests PASSED");
    }
}