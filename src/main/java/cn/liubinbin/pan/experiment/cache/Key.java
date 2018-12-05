package main.java.cn.liubinbin.pan.experiment.cache;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.Arrays;

import main.java.cn.liubinbin.pan.utils.ByteUtils;

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
		if (!(o instanceof Key)) {
			return -1;
		}
		
		byte[] keyToCom = ((Key)o).getKey();
		if (key.length > keyToCom.length) {
			return 1;
		}
		if (Arrays.equals(key, keyToCom)){
			return 0;
		}
		for (int keyIdx = 0; keyIdx < key.length; keyIdx++) {
			if (key[keyIdx] < keyToCom[keyIdx]) {
				return -1;
			}
		}
		return 1;
	}
	
	@Override
	public int hashCode() {
		return ByteUtils.hashCode(key);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Key)) {
			return false;
		}
		if (this.hashCode() == ((Key) obj).hashCode() && this.compareTo(obj) == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		byte[] keyByteArray = { 'k', 'e', 'y'};
//		byte[] key1ByteArray = { 'k', 'e', 'y', '1'};
		Key key1 = new Key(keyByteArray);
		Key key2 = new Key(keyByteArray);
		System.out.println(key1.equals(key2));
	}
}
