package main.java.cn.liubinbin.experiment.groupbyer;
/**
 *
 * @author liubinbin
 *
 */
public class Pair {

	private int key;
	private int aggCount;
	
	public Pair(int key, int aggCount) {
		this.key = key;
		this.aggCount = aggCount;
	}
	
	public Pair(Record record) {
		this.setKey(record.getKey());
		this.setAggCount(1);
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
	
	public String toString() {
		return " {" + key + "," + aggCount + "} ";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			Pair pairToComp = (Pair)obj;
			return pairToComp.getKey() == this.getKey() && pairToComp.getAggCount() == this.getAggCount();
		} else {
			return false;
		}
	}
}
