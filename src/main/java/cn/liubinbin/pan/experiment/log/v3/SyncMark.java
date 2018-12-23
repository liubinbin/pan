package main.java.cn.liubinbin.pan.experiment.log.v3;

public class SyncMark {
	private volatile boolean isDone;
	private String threadName;
	private int sequence;
	
	public SyncMark() {
		this.isDone = false;
		this.threadName = Thread.currentThread().getName();
		this.sequence = 0;
	}
	
	public boolean isDone() {
		return isDone;
	}

	public void done() {
		this.isDone = true;
	}
	
	public void WaitForDone() {
		while(!isDone()){
			// reduce cpu occupation
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public String getThreadName() {
		return threadName;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequenceAndSetNotDone(int sequence) {
		this.sequence = sequence;
		this.isDone = false;
	}
}
