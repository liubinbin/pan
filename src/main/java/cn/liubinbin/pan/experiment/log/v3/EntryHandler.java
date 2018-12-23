package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.lmax.disruptor.EventHandler;

/**
 * ssd (mac pro 2015mid)
 * thread: 1
 * batch	 	%1 			%2 			%4 			%8 
 * 				4,672,512	6,445,806		
 * @author liubinbin
 *
 */
public class EntryHandler implements EventHandler<Entry> {

	private FileChannel fileChannel;
	private Flusher flusher;
	private BlockingQueue<SyncMark> seqQueue;
	private int batch;
	
	public EntryHandler(FileChannel fileChannel) {
		this.fileChannel = fileChannel;
		this.flusher = new Flusher();
		this.batch = 2;
		this.seqQueue = new LinkedBlockingQueue<SyncMark>(4);
		new Thread(this.flusher).start();
	}
	
	@Override
	public void onEvent(Entry entry, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
//		try {
//			Thread.sleep(1000 * 1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		fileChannel.write(entry.getByteBuffer());
		seqQueue.put(entry.getSyncMark());
		//TODO do sth about sequence 
//		System.out.println("On event entry: entry.sequence: " + entry.getSequence() + " " + sequence);
	}
	
	public class Flusher implements Runnable {

		@Override
		public void run() {
			SyncMark syncMark = null;
			int batchIdx = 0;
			while (true) {
				try {
					syncMark = seqQueue.take();
					batchIdx = (++batchIdx) % batch;
					if (batch == 1 || batchIdx == 0) { 
						fileChannel.force(true);
					}
					syncMark.done();
//					System.out.println(" Flusher batchIdx: " + batchIdx + " batch: " + batch + " syncMark: " + syncMark.getThreadName() + " sequence: " + syncMark.getSequence());
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}