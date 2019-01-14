package main.java.cn.liubinbin.pan.experiment.jobmanager;
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
				int sleepTime = (countSyncer.getJobType().ordinal() + 1 ) * 1000;
				System.out.println("job start to do type " + countSyncer.getJobType() + " time " + sleepTime);
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
