package main.java.cn.liubinbin.experiment.jobmanager;

import java.util.Random;

/**
 *
 * @author liubinbin
 *
 */
public class Job implements Runnable{

	private CountSyncer countSyncer;
	
	public Job(CountSyncer countSyncer) {
		this.countSyncer = countSyncer;
	} 

	@Override
	public void run() {
		try {
			try {
				Random random = new Random();
				int sleepTime = (countSyncer.getJobType().ordinal() + 1 + random.nextInt(3)) * 1000;
				System.out.println("job starts {type " + countSyncer.getJobType() + " time " + sleepTime + "} starts");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			countSyncer.release();
		}
	}

	public static void main(String[] args) {
		
	}
}
