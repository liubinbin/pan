package main.java.cn.liubinbin.pan.experiment.cache;

/**
 *
 * @author liubinbin TODO key should be put into map
 */
public class Key implements Comparable {
	private byte[] key;

	public Key(byte[] key) {
		this.key = key;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	@Override
	public int compareTo(Object o) {
		return key.hashCode() - ((Key) o).hashCode();
	}
}
