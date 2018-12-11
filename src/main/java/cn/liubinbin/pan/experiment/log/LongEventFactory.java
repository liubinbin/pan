package main.java.cn.liubinbin.pan.experiment.log;

import com.lmax.disruptor.EventFactory;

public class LongEventFactory implements EventFactory<LongEvent> {
	
	@Override
	public LongEvent newInstance() {
		return new LongEvent();
	}
}