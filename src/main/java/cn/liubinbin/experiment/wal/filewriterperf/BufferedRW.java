package cn.liubinbin.experiment.wal.filewriterperf;

import java.io.*;

/**
 * @author liubinbin
 */
public class BufferedRW extends IoModeSpeedTest {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new BufferedRW().test(args);
    }

    @Override
    public void write(String filePath, String content, int repeatCount) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < repeatCount; i++) {
            bufferedWriter.write(content + "\n");
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    @Override
    public void read(String filePath, String Conten, int repeatCount) throws IOException {
        File file = new File(filePath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
        }
        bufferedReader.close();
    }
}
