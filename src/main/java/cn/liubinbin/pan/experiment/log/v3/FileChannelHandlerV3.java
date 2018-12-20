package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FileChannelHandlerV3 implements Runnable {
	private FielChannelWalV3 wal;
	private AtomicInteger count;
	private CyclicBarrier barrier;
	

	public FileChannelHandlerV3(FielChannelWalV3 wal, AtomicInteger count, CyclicBarrier barrier) {
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
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		while(true){
			int sequence = count.getAndIncrement();
			long startTime = System.nanoTime();
			byteBuffer.clear();
			byteBuffer.put(data);
			byteBuffer.flip();
			wal.append(byteBuffer);
			
//			System.out.println("append use " + (System.nanoTime() - startTime));
//			startTime = System.nanoTime();
			wal.flush(sequence);
//			System.out.println("flush use " + (System.nanoTime() - startTime));
		}
	}

}
