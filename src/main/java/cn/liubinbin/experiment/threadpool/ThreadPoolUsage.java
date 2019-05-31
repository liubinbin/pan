package cn.liubinbin.experiment.threadpool;

import java.util.concurrent.*;

/**
 * LimitedQueue
 *  先有七个（maximumPoolSize + workQueueSize）submit成功，然后渐渐的加入剩余三个。
 * LinkedBlockingQueue
 *  先有七个（maximumPoolSize + workQueueSize）submit成功，然后剩余三个submit失败。
 * 默认handler为new ThreadPoolExecutor.AbortPolicy()
    public static class AbortPolicy implements RejectedExecutionHandler {
        public AbortPolicy() {
        }

        public void rejectedExecution(Runnable var1, ThreadPoolExecutor var2) {
            throw new RejectedExecutionException("Task " + var1.toString() + " rejected from " + var2.toString());
        }
    }
 * Created by bin on 2019/5/29.
 */
public class ThreadPoolUsage {

    public class MyTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class LimitedQueue<E> extends LinkedBlockingQueue<E> {
        public LimitedQueue(int maxSize) {
            super(maxSize);
        }

        @Override
        public boolean offer(E e) {
            // turn offer() and add() into a blocking calls (unless interrupted)
            try {
                put(e);
                return true;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }

    public void doMain() {
        int corePoolSize = 1;
        int maximumPoolSize = 3;
        int keepAliveTime = 5;
        int workQueueSize = 4;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(workQueueSize));
        Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++){
            System.out.println("submit one " + i);
            try {
                executor.submit(new MyTask());
            } catch (Exception e) {
                System.out.println("catch one exception " + i);
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    public static void main(String[] args) {
        new ThreadPoolUsage().doMain();
    }
}
