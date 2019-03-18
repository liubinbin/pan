package main.java.cn.liubinbin.experiment.wal.walv2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * nThreads: 1 
 * hdd (low level windows)
 * sequence 	%1 			%2 			%4 			%8 
 * 				2,948,822	5,152,055	7,687,817	11,585,537
 * ssd (mac pro 2015mid)
 * sequence 	%1 			%2 			%4 			%8 
 * 				5,056,326	7,282,300	13,151,423	19,885,006
 * 
 * @author liubinbin
 */
public class FielChannelWalV2 {

	private final int DATA_CHUNK = 128 * 1024 * 1024;
	private final byte[] DATA = new byte[DATA_CHUNK];
	private FileChannel fileChannel;
	private RandomAccessFile randomAccessFile;
	private BlockingQueue<Integer> seqQueue;
	private Flusher flusher;

	public class Flusher implements Runnable {

		@Override
		public void run() {
			int sequence = 0;
			while (true) {
				try {
					sequence = seqQueue.take();
					fileChannel.force(true);
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public FielChannelWalV2(String filePath) {
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
		this.flusher = new Flusher();
		new Thread(this.flusher).start();
	}

	public void append(ByteBuffer byteBuffer) {
		try {
			fileChannel.write(byteBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void flush(int sequence) {
		if (sequence % 8 == 0) {
			try {
				seqQueue.put(sequence);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

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
