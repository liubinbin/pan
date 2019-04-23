package cn.liubinbin.experiment.atomiclongvsaddr;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author liubinbin
 */
public class TestAtomicLong {

    private final int roundCount = 1000 * 1000 * 1000;

    public static void main(String[] args) throws InterruptedException {
        TestAtomicLong testAtomicLong = new TestAtomicLong();
//		testAtomicLong.oneThreadWay();
        testAtomicLong.multiThreadsWay();
    }

    public void oneThreadWay() {
        AtomicLong data = new AtomicLong();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < roundCount; i++) {
            data.incrementAndGet();
        }
        System.out.println("TestAtomicLong oneThreadWay data " + data.get() + ", cost " + (System.currentTimeMillis() - startTime));
    }

    public void multiThreadsWay() throws InterruptedException {
        int taskNum = 4;

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        //Common Thread Pool
        ExecutorService executor = new ThreadPoolExecutor(taskNum, taskNum, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(taskNum);
        AtomicLong data = new AtomicLong();
        for (int i = 0; i < taskNum; i++) {
            executor.execute(new Task(start, end, data, 1000 * 1000 * 1000));
        }
        long startTime = System.currentTimeMillis();
        start.countDown();
        end.await();
        System.out.println("TestAtomicLong multiThreadsWay data " + data.get() + ", cost " + (System.currentTimeMillis() - startTime));
        executor.shutdown();
    }

    public class Task implements Runnable {

        private CountDownLatch start;
        private CountDownLatch end;
        private AtomicLong data;
        private int incCount;

        public Task(CountDownLatch start, CountDownLatch end, AtomicLong data, int incCount) {
            this.start = start;
            this.end = end;
            this.data = data;
            this.incCount = incCount;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("start to inc");
            for (int i = 0; i < incCount; i++) {
                data.incrementAndGet();
            }
            System.out.println("inc end");
            end.countDown();
        }

    }
}
