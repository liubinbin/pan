package main.java.cn.liubinbin.pan.experiment.log.v1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class FileOutputstreamHandler implements Runnable {
	private FileOutputStreamWal wal;
	private AtomicInteger count;
	private CyclicBarrier barrier;
	
	public FileOutputstreamHandler(FileOutputStreamWal wal, AtomicInteger count, CyclicBarrier barrier) {
		this.wal = wal;
		this.count = count;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		} 
//		System.out.println(Thread.currentThread().getName() + " has started");
		byte[] data = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd'};
		while(true){
			int sequence = count.getAndIncrement();
//			long startTime = System.currentTimeMillis();
			wal.append(data);
//			System.out.println("append use " + (System.currentTimeMillis() - startTime));
//			startTime = System.currentTimeMillis();
			wal.flush(sequence);
//			System.out.println("flush use " + (System.currentTimeMillis() - startTime));
		}
	}

}
