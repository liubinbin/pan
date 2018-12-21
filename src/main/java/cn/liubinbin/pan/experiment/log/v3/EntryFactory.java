package main.java.cn.liubinbin.pan.experiment.log.v3;

import com.lmax.disruptor.EventFactory;

public class EntryFactory implements EventFactory<Entry> {
	
	@Override
	public Entry newInstance() {
		return new Entry();
	}
}