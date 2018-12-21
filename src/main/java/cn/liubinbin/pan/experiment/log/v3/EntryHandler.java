package main.java.cn.liubinbin.pan.experiment.log.v3;

import com.lmax.disruptor.EventHandler;

public class EntryHandler implements EventHandler<Entry> {
	
	@Override
	public void onEvent(Entry event, long sequence, boolean endOfBatch) {
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Event: " + event.toString());
	}
}