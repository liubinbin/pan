package main.java.cn.liubinbin.pan.experiment.log.v1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	
	private void doMain(String[] args){
		int nThreads = 8;
		Wal wal = new Wal("wal");
		AtomicInteger count = new AtomicInteger(0);
		CyclicBarrier barrier = new CyclicBarrier(nThreads);
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		for (int i = 0; i < nThreads; i++) {
			executorService.execute(new Handler(wal, count, barrier));
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
		executorService.shutdown();
	}
	
	public static void main(String[] args) {
		new Main().doMain(args);
	}

}
