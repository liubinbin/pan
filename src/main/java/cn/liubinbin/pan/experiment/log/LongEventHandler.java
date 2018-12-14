package main.java.cn.liubinbin.pan.experiment.log;

import com.lmax.disruptor.EventHandler;

public class LongEventHandler implements EventHandler<LongEvent> {
	
	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
//		try {
//			Thread.sleep(1000 * 2);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Event: " + event.toString());
	}
}