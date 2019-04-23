package cn.liubinbin.experiment.wal.filewriterperf;

import java.io.IOException;

/**
 * @author liubinbin
 */
public abstract class IoModeSpeedTest {

    public abstract void write(String filePath, String content, int repeatCount) throws IOException;

    public abstract void read(String filePath, String Conten, int repeatCount) throws IOException;

    private void printHelp() {
        System.out.println("usgae: java -jar *.jar filePath 100000");
    }

    public void test(String[] args) throws IOException {
        if (args.length != 2) {
            printHelp();
            System.exit(1);
        }
        String filePath = args[0];
        String content = "hello world";
        int repeatCount = 0;
        try {
            repeatCount = Integer.parseInt(args[1]);
        } catch (Exception e) {
            printHelp();
            System.exit(1);
        }
        long startTime = System.currentTimeMillis();
        write(filePath, content, repeatCount);
        System.out.println("write use " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        read(filePath, content, repeatCount);
        System.out.println("read use " + (System.currentTimeMillis() - startTime));
    }

}
