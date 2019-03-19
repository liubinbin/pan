package main.java.cn.liubinbin.experiment.wal.walv3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * nThreads: 1 
 * sequence 	%1 			%2 			%4 			%8 
 * 				2,948,822	5,152,055	7,687,817	11,585,537
 * @author liubinbin
 *
 */
public class FielChannelWalV3 {

	private FileChannel fileChannel;
	private RandomAccessFile randomAccessFile;
	private Disruptor<Entry> disruptor;
	private RingBuffer<Entry> ringBuffer;
	private int batchOfSync;
	
	public FielChannelWalV3(String filePath, int batchOfSync, int seqQueueSize, int disruptorBufSize) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}

		try {
			this.randomAccessFile = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.fileChannel = randomAccessFile.getChannel();
		this.batchOfSync = batchOfSync;
		ThreadFactory simpleThreadFactory  = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);  
			}
		};
		EntryFactory factory = new EntryFactory();
		int bufferSize = disruptorBufSize;
		this.disruptor = new Disruptor<Entry>(factory, bufferSize, simpleThreadFactory);
		this.disruptor.handleEventsWith(new EntryHandler(fileChannel, this.batchOfSync, seqQueueSize));
		this.disruptor.start();
		this.ringBuffer = disruptor.getRingBuffer();
	}

	public void appendAndWaitForSynced(ByteBuffer byteBuffer, int sequence, SyncMark syncMark) {
		syncMark.setSequenceAndSetNotDone(sequence);
		// Grab the next sequence
		long entryId = ringBuffer.next(); 
		try {
			// Get the entry in the Disruptor for the sequence
			Entry entry = ringBuffer.get(sequence);
			entry.setByteBuffer(byteBuffer);
			entry.setSequence(sequence);
			entry.setSyncMark(syncMark);
		} finally {
			ringBuffer.publish(sequence);
		}
		syncMark.WaitForDone();
	}

	public void close() {
		try {
			randomAccessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
