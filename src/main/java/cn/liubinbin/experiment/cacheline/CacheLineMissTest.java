package cn.liubinbin.experiment.cacheline;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * padding 0 longs      120593  124010  120782
 * padding 6 longs      17045   16977   17459
 * padding 7 longs      44171   44541   43843
 * ref http://ifeve.com/false-sharing/
 */
public class CacheLineMissTest {

    private final static int EXECOUNT = 10000 * 10000 * 10;

    public class TaskWithPadding implements Runnable {

        private IntWithPadding[] data;
        private int idx;
        private CountDownLatch latch;

        TaskWithPadding(IntWithPadding[] data, int idx, CountDownLatch latch) {
            this.data = data;
            this.idx = idx;
            this.latch = latch;
        }

        @Override
        public void run() {
            for (int i = 0; i < EXECOUNT; i++) {
                data[idx].inc();
            }
            this.latch.countDown();
        }
    }

    public class IntWithPadding {

        private volatile long num;
        private long n1, n2, n3, n4, n5, n6, n7; //padding

        IntWithPadding(long num) {
            this.num = num;
        }

        public void inc() {
            this.num++;
        }
    }

    public void test() throws InterruptedException {

        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);

        IntWithPadding[] data = new IntWithPadding[threadCount];
        for (int i = 0; i < threadCount; i++) {
            data[i] = new IntWithPadding(0);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(new TaskWithPadding(data, i, latch));
        }

        latch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("testWithoutPadding use " + (endTime - startTime) + " ms");

        executorService.shutdownNow();
        while (!executorService.isTerminated()) {

        }
    }

    public static void main(String[] args) throws InterruptedException {
        CacheLineMissTest cacheLineMissTest = new CacheLineMissTest();
        cacheLineMissTest.test();
    }
}
