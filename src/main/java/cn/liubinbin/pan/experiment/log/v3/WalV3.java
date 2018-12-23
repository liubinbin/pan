package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In v2, I cannot get ack from sync, so we cannot do some real wal append.
 * In v3, I want to get ack from sync that is in the other side of queue.
 * deal after five round (mac pro 2015mid)
 * batch: 1
 * threads	1			2			4			8
 * count	4,571,051	7,043,799	4,452,564	7,543,725
 * @author liubinbin
 */

public class WalV3 {
	
	private void doMain(String[] args) throws InterruptedException, BrokenBarrierException{
		int nThreads = 1;
		FielChannelWalV3 wal = new FielChannelWalV3("wal");
		AtomicInteger count = new AtomicInteger(0);
		CyclicBarrier barrier = new CyclicBarrier(nThreads + 1);
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		for (int i = 0; i < nThreads; i++) {
			executorService.execute(new FileChannelHandlerV3(wal, count, barrier));
		}
		barrier.await();
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
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		new WalV3().doMain(args);
	}

}
