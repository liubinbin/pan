package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In v2, I cannot get ack from sync, so we cannot do some real wal append.
 * In v3, I want to get ack from sync that is in the other side of queue.
 * count done after five round (mac pro 2015mid)
 * 		
 * batchOfSync\threadCount	1			2			4			8
 * 					1		4,136,419	7,577,261	4,522,228	
 * 					2		6,276,764	7,458,057	8,113,841
 * 					4		9,168,529	11,960,173	9,219,299
 * 					8		11,471,419	15,922,471	14,099,074
 * 
 * @author liubinbin
 */

public class WalV3 {
	
	private void doMain(String[] args) throws InterruptedException, BrokenBarrierException{
		int threadCount = 2;
		int batchOfSync = 1;
		
		// buf size(we won't change too much, let's keep it still)
		int seqQueueSize = 128;
		int disruptorBufSize = 128;
		
		FielChannelWalV3 wal = new FielChannelWalV3("wal", batchOfSync, seqQueueSize, disruptorBufSize);
		AtomicInteger count = new AtomicInteger(0);
		CyclicBarrier barrier = new CyclicBarrier(threadCount + 1);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) {
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
