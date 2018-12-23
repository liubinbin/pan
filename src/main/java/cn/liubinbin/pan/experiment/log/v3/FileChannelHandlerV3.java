package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.nio.ByteBuffer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class FileChannelHandlerV3 implements Runnable {
	private FielChannelWalV3 wal;
	private AtomicInteger count;
	private CyclicBarrier barrier;
	private ThreadLocal<SyncMark> syncMark;

	public FileChannelHandlerV3(FielChannelWalV3 wal, AtomicInteger count, CyclicBarrier barrier) {
		this.wal = wal;
		this.count = count;
		this.barrier = barrier;
		this.syncMark = new ThreadLocal<SyncMark>() {
			@Override
			protected SyncMark initialValue() {
				return new SyncMark();
			}
		};
	}

	@Override
	public void run() {
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		} 
		byte[] data = {'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd'};
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		while(true){
			int sequence = count.getAndIncrement();
			byteBuffer.clear();
			byteBuffer.put(data);
			byteBuffer.flip();
			wal.appendAndWaitForSynced(byteBuffer, sequence, syncMark.get());
		}
	}

}
