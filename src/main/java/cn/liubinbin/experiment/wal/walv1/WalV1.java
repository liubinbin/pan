package cn.liubinbin.experiment.wal.walv1;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * deal after five round
 * threads	1			2			4			8
 * count	901,022		825,085		822,268		809,802
 */

public class WalV1 {

    public static void main(String[] args) {
        new WalV1().doMain(args);
    }

    private void doMain(String[] args) {
        int nThreads = 8;
        FielChannelWalV1 wal = new FielChannelWalV1("wal");
//		FileOutputStreamWal wal = new FileOutputStreamWal("wal");
        AtomicInteger count = new AtomicInteger(0);
        CyclicBarrier barrier = new CyclicBarrier(nThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            executorService.execute(new FileChannelHandlerV1(wal, count, barrier));
//			executorService.execute(new FileOutputstreamHandler(wal, count, barrier));
        }
        for (int i = 0; i < 5; i++) {
            int tempCount = count.get();
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("deal " + (count.get() - tempCount));
        }
        System.out.println("deal after five round " + count.get());
        executorService.shutdown();
    }

}
