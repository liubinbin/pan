package cn.liubinbin.experiment.wal.walv1;

import java.nio.ByteBuffer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class FileChannelHandlerV1 implements Runnable {
    private FielChannelWalV1 wal;
    private AtomicInteger count;
    private CyclicBarrier barrier;

    public FileChannelHandlerV1(FielChannelWalV1 wal, AtomicInteger count, CyclicBarrier barrier) {
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
        while (true) {
            int sequence = count.getAndIncrement();
            long startTime = System.nanoTime();
            byteBuffer.clear();
            byteBuffer.put(data);
            byteBuffer.flip();
            wal.append(byteBuffer);
//			System.out.println("append use " + (System.nanoTime() - startTime));
            startTime = System.nanoTime();
            wal.flush(sequence);
//			System.out.println("flush use " + (System.nanoTime() - startTime));
        }
    }

}
