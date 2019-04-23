package cn.liubinbin.experiment.atomiclongvsaddr;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author liubinbin
 */
public class TestLongAdder {

    public static void main(String[] args) throws InterruptedException {
        TestAtomicLong testAtomicLong = new TestAtomicLong();
//		testAtomicLong.oneThreadWay();
        testAtomicLong.multiThreadsWay();
    }

    public void multiThreadsWay() throws InterruptedException {
        int taskNum = 4;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        //Common Thread Pool
        ExecutorService executor = new ThreadPoolExecutor(taskNum, taskNum, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(taskNum);
        LongAdder data = new LongAdder();
        for (int i = 0; i < taskNum; i++) {
            executor.execute(new Task(start, end, data, 1000 * 1000 * 1000));
        }
        long startTime = System.currentTimeMillis();
        start.countDown();
        end.await();
        System.out.println("TestAtomicAddr multiThreadsWay data " + data.sum() + ", cost " + (System.currentTimeMillis() - startTime));
        executor.shutdown();
    }

    public class Task implements Runnable {

        private CountDownLatch start;
        private CountDownLatch end;
        private LongAdder data = new LongAdder();
        private int incCount;

        public Task(CountDownLatch start, CountDownLatch end, LongAdder data, int incCount) {
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
                data.increment();
            }
            System.out.println("inc end");
            end.countDown();
        }

    }
}
