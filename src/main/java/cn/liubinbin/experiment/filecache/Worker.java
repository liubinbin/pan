package cn.liubinbin.experiment.filecache;

import java.util.Random;

public class Worker implements Runnable {
	private Random random = new Random();
	private Processor processor = null;
	
	public Worker(Processor processor) {
		this.processor = processor;
//		this.processor = new Processor();
		this.random = new Random();
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 10000000; i++){
			processor.getHandler(random.nextInt(16) + "", 80);
		}
//		System.out.println("num: [" + processor.cal() + "], callCount:[" + processor.callCount() + "]"); //for 多线程
	}

}
