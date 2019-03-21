package main.java.cn.liubinbin.experiment.binfile.filev1;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author liubinbin
 * 
 */
public class KeyValue {

	private byte[] key;
	private byte[] value;
	private long timestamp;
	
	public KeyValue(String key, String value, long timestamp ) {
		this(key.getBytes(), value.getBytes(), timestamp);
	}
	
	public KeyValue(byte[] key, byte[] value, long timestamp ) {
		this.setKey(key);
		this.setValue(value);
		this.setTimestamp(timestamp);
	}
	
	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public String toString() {
		try {
			return "[ " + new String(key,"UTF-8") + "" + new String(value, "UTF-8") + " ]";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
