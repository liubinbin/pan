package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

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
	private BlockingQueue<Integer> seqQueue;
//	private Flusher flusher;
	private Disruptor<Entry> disruptor;
	
//	public class Flusher implements Runnable {
//
//		@Override
//		public void run() {
//			int sequence = 0;
//			while (true) {
//				try {
//					sequence = seqQueue.take();
//					fileChannel.force(true);
//				} catch (InterruptedException | IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}

	public FielChannelWalV3(String filePath) {
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
		this.seqQueue = new LinkedBlockingQueue<Integer>(4);
//		this.flusher = new Flusher();
//		new Thread(this.flusher).start();
		
		ThreadFactory simpleThreadFactory  = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);  
			}
		};
		EntryFactory factory = new EntryFactory();
		int bufferSize = 16;
		this.disruptor = new Disruptor<Entry>(factory, bufferSize, simpleThreadFactory);
		this.disruptor.handleEventsWith(new EntryHandler());
		this.disruptor.start();
	}

	public void appendAndWaitForSynced(ByteBuffer byteBuffer, int sequence ) {
		
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
