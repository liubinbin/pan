package cn.liubinbin.experiment.wal.filewriterperf;

import java.io.*;

/**
 * @author liubinbin
 */
public class BufferedStream extends IoModeSpeedTest {

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
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bs = content.getBytes();
        for (int i = 0; i < repeatCount; i++) {
            bufferedOutputStream.write(bs);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    @Override
    public void read(String filePath, String content, int repeatCount) throws IOException {
        byte[] bs = content.getBytes();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
        int n = 0;
        ;
        while ((n = bufferedInputStream.read(bs)) != -1) {
        }
        bufferedInputStream.close();
    }
}
