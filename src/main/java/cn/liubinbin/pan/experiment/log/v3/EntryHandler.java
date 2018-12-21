package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.io.IOException;
import java.nio.channels.FileChannel;

import com.lmax.disruptor.EventHandler;

public class EntryHandler implements EventHandler<Entry> {

	private FileChannel fileChannel;
	private Flusher flusher;
	
	public EntryHandler(FileChannel fileChannel) {
		this.fileChannel = fileChannel;
		this.flusher = new Flusher();
		new Thread(this.flusher).start();
	}
	
	@Override
	public void onEvent(Entry event, long sequence, boolean endOfBatch) {
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Event: " + event.toString());
	}
	
	public class Flusher implements Runnable {

		@Override
		public void run() {
			int sequence = 0;
			while (true) {
				try {
//					sequence = seqQueue.take();
					fileChannel.force(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}