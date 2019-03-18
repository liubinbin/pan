package main.java.cn.liubinbin.experiment.groupbyer;
/**
 *
 * @author liubinbin
 *
 */
public class Pair {

	private int key;
	private int aggCount;
	
	public Pair(Record record) {
		this.setKey(record.getKey());
		this.setAggCount(0);
	}
	
	public void increment() {
		this.setAggCount(this.getAggCount() + 1);
	}
	
	public void increment(int countDelta) {
		this.setAggCount(this.getAggCount() + countDelta);
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getAggCount() {
		return aggCount;
	}

	public void setAggCount(int aggCount) {
		this.aggCount = aggCount;
	}

}
