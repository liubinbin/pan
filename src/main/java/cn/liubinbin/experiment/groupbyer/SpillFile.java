package cn.liubinbin.experiment.groupbyer;

import java.io.*;

/**
 * @author liubinbin
 */
public class SpillFile {

    public final static String SPILL_FILE_ROOT = "spill";
    private String filePath;
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;

    public SpillFile(String filePrefix, int hash) throws IOException {
        this(SPILL_FILE_ROOT + File.separator + filePrefix + "-" + hash);
    }

    public SpillFile(String filePath) throws IOException {
        this.setFilePath(filePath);
    }

    public void add(Pair pair) throws IOException {
        if (fileWriter == null) {
            this.fileWriter = new FileWriter(filePath);
        }
        fileWriter.write(pair.getKey() + " " + pair.getAggCount() + "\n");
    }

    public Pair next() throws IOException {
        if (fileWriter == null) {
            return null;
        }
        if (bufferedReader == null) {
            this.bufferedReader = new BufferedReader(new FileReader(filePath));
        }
        String pairStr = bufferedReader.readLine();
        if (pairStr == null) {
            return null;
        }
        try {
            String[] pairStrSplit = pairStr.split(" ");
            return new Pair(Integer.parseInt(pairStrSplit[0]), Integer.parseInt(pairStrSplit[1]));
        } catch (Exception e) {
            return null;
        }
    }

    public void closeWriter() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }

    public void closeReader() throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
