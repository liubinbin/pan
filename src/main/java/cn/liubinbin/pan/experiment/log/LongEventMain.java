package main.java.cn.liubinbin.pan.experiment.log;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.RingBuffer;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadFactory;

/**
 * @author liubinbin
 */
public class LongEventMain {
	public static void main(String[] args) throws Exception {
		// Executor that will be used to construct new threads for consumers
//		Executor executor = Executors.newCachedThreadPool();
		/**
		 * 在disruptor包中已经将Executor传入Disruptor的构造函数的方法给废弃了。
		 * 所以，这里使用了Disruptor的构造函数的新方法
		 */
		ThreadFactory simpleThreadFactory  = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);  
			}
		};

		// The factory for the event
		LongEventFactory factory = new LongEventFactory();

		// Specify the size of the ring buffer, must be power of 2.
		int bufferSize = 16;

		// Construct the Disruptor
		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, simpleThreadFactory);

		// Connect the handler
		disruptor.handleEventsWith(new LongEventHandler());

		// Start the Disruptor, starts all threads running
		disruptor.start();

		// Get the ring buffer from the Disruptor to be used for publishing.
		/**
		 * 感觉RingBuffer是disrutor的重要组成部分。
		 */
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
//		System.out.println("getCursor " + ringBuffer.getCursor());

		LongEventProducer producer = new LongEventProducer(ringBuffer);

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; true; l++) {
			bb.putLong(0, l);
			producer.onData(bb);
			System.out.println("getCursor after onData " + ringBuffer.getCursor());
//			Thread.sleep(1000);
		}
	}
}