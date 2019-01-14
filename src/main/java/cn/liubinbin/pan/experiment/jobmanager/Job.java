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
				Thread.sleep(1000);
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
