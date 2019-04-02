package cn.liubinbin.experiment.wal.walv3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In v2, I cannot get ack from sync, so we cannot do some real wal append.
 * In v3, I want to get ack from sync that is in the other side of queue.
 * count done after five round (mac pro 2015mid)
 * batchOfSync\threadCount	1			2			4			8
 * 					1		4,136,419	7,577,261	4,522,228	
 * 					2		6,276,764	7,458,057	8,113,841
 * 					4		9,168,529	11,960,173	9,219,299
 * 					8		11,471,419	15,922,471	14,099,074
 * 
 * count done after one round (mac pro 2015mid)
 * batchOfSync\threadCount	1			2			4			8
 * 					1		865,306		1,443,577	912,347
 * 					2		1,305,061	1,456,064	1,602,153
 * 					4		1,801,453	2,309,524	2,034,663
 * 					8		2,177,296	3,319,266	2,737,060
 * result is weird when threadCount equals 2. TODO check the reason out
 * 
 * count done after one round (mac pro 2015mid) 
 * move force from flusher to onEvent(onEvent is too heavy and ack mechanism, so separate write and flush is necessary)
 * batchOfSync\threadCount	1			2			4			8
 * 					1		808,792		953,783		932,877
 * 					2		796,494		938,657		963,138
 * 					4		
 * 					8		
 * @author liubinbin
 */

public class WalV3 {
	
	private void doMain(String[] args) throws InterruptedException, BrokenBarrierException{
		int threadCount = 4;
		int batchOfSync = 2;
		
		int seqQueueSize = threadCount;
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
			long startTime = System.currentTimeMillis();
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("deal " + (count.get() - tempCount) + " use time: " + (System.currentTimeMillis() - startTime));
		}
		System.out.println("deal after five round " + count.get());
		executorService.shutdown();
	}
	
	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		new WalV3().doMain(args);
	}

}
