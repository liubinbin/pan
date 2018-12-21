package main.java.cn.liubinbin.pan.experiment.log.v3;

import com.lmax.disruptor.EventFactory;

public class LongEventFactory implements EventFactory<LongEvent> {
	
	@Override
	public LongEvent newInstance() {
		return new LongEvent();
	}
}