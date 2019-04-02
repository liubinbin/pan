package cn.liubinbin.experiment.wal.walv3;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.lmax.disruptor.EventHandler;

/**
 * @author liubinbin
 *
 */
public class EntryHandler implements EventHandler<Entry> {

	private FileChannel fileChannel;
	private Flusher flusher;
	private BlockingQueue<SyncMark> seqQueue;
	private int batchOfSync;
	
	public EntryHandler(FileChannel fileChannel, int batchOfSync, int seqQueueSize) {
		this.fileChannel = fileChannel;
		this.flusher = new Flusher();
		this.batchOfSync = batchOfSync;
		this.seqQueue = new ArrayBlockingQueue<SyncMark>(seqQueueSize);
		new Thread(this.flusher).start();
	}
	
	@Override
	public void onEvent(Entry entry, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
		fileChannel.write(entry.getByteBuffer());
		long startTime = System.nanoTime();
		seqQueue.put(entry.getSyncMark());
//		System.out.println("sequence: " + sequence + " use time: " + (System.nanoTime() - startTime));
//		System.out.println("On event entry: entry.sequence: " + entry.getSequence() + " " + sequence);
	}
	
	public class Flusher implements Runnable {

		@Override
		public void run() {
			SyncMark syncMark = null;
			int batchIdx = 0;
			while (true) {
				try {
					long startTime = System.nanoTime();
					syncMark = seqQueue.take();
					batchIdx = (++batchIdx) % batchOfSync;
					if (batchOfSync == 1 || batchIdx == 0) { 
						fileChannel.force(true);
					} else {
					}
					syncMark.done();
//					System.out.println(" Flusher batchIdx: " + batchIdx + " batchOfSync: " + batchOfSync + " syncMark: " + syncMark.getThreadName() + " sequence: " + syncMark.getSequence());
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}