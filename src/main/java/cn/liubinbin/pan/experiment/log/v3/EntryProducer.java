package main.java.cn.liubinbin.pan.experiment.log.v3;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

public class EntryProducer {
	private final RingBuffer<Entry> ringBuffer;

	public EntryProducer(RingBuffer<Entry> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void onData(ByteBuffer bb) {
		long sequence = ringBuffer.next(); // Grab the next sequence
//		System.out.println("getCursor after get next " + ringBuffer.getCursor());
		try {
			Entry event = ringBuffer.get(sequence); // Get the entry in the Disruptor for the sequence
			event.set(bb.getLong(0)); // Fill with data
		} finally {
			ringBuffer.publish(sequence);
//			System.out.println("getCursor after publish " + ringBuffer.getCursor());
		}
	}
}