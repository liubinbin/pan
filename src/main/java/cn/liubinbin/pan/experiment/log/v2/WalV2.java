package main.java.cn.liubinbin.pan.experiment.log.v2;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  deal after five round
 * threads	1			2			4			8
 * count	2,950,967	2,699,460	2,544,905	2,622,151
 * @author liubinbin
 * 
 */

public class WalV2 {
	
	private void doMain(String[] args){
		int nThreads = 1;
		FielChannelWalV2 wal = new FielChannelWalV2("wal");
		AtomicInteger count = new AtomicInteger(0);
		CyclicBarrier barrier = new CyclicBarrier(nThreads);
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		for (int i = 0; i < nThreads; i++) {
			executorService.execute(new FileChannelHandlerV2(wal, count, barrier));
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
	
	public static void main(String[] args) {
		new WalV2().doMain(args);
	}

}
