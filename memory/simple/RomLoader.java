package memory.simple;
import java.io.*;

import memory.MachineFault;
import memory.MachineFault.Code;

public final class RomLoader {

    public static final class Result {
        public final int firstAddress;
        public final int linesLoaded;

        public Result(int firstAddress, int linesLoaded) {
            this.firstAddress = firstAddress;
            this.linesLoaded = linesLoaded;
        }
    }

    public static Result loadIntoMemory(File file, Memory memory) throws IOException {
        int firstAddr = -1;
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String raw;
            int lineNo = 0;

            while ((raw = br.readLine()) != null) {
                lineNo++;

                String line = raw.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith(";")) continue;

                String[] toks = line.split("\\s+");
                if (toks.length < 2) {
                    throw new MachineFault(
                            MachineFault.Code.LOADER_FORMAT_ERROR,
                            lineNo,
                            "Loader format error on line " + lineNo + ": expected address and value"
                    );
                }

                try {
                    int addr = Integer.parseInt(toks[0], 8);
                    int word = Integer.parseInt(toks[1], 8) & 0xFFFF;

                    memory.directWrite(addr, word);

                    if (firstAddr == -1) firstAddr = addr;
                    count++;
                } catch (NumberFormatException nfe) {
                    throw new MachineFault(
                            MachineFault.Code.LOADER_FORMAT_ERROR,
                            lineNo,
                            "Loader parse error on line " + lineNo + ": invalid octal number",
                            nfe
                    );
                }
            }
        }

        if (firstAddr == -1) firstAddr = 0;
        return new Result(firstAddr, count);
    }
}
