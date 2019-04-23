package cn.liubinbin.experiment.filecache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author liubinbin
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int threadSize = 10;
        Processor processor = new Processor();
        ExecutorService executors = Executors.newCachedThreadPool();
        for (int i = 0; i < threadSize; i++) {
            executors.execute(new Worker(processor));
        }
        executors.shutdown();
        executors.awaitTermination(100, TimeUnit.HOURS);
        long endTime = System.currentTimeMillis();
        System.out.println("time : [" + (endTime - startTime) + "], num: [" + processor.cal() + "], callCount:[" + processor.callCount() + "]");
    }
}
