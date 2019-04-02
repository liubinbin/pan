package cn.liubinbin.experiment.groupbyer;
/**
 *
 * @author liubinbin
 *
 */
public class Record {
	
	private int key;
	private String value;
	
	public Record(int key, String value) {
		this.setKey(key);
		this.setValue(value);
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
