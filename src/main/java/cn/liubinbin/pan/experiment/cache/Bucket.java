package main.java.cn.liubinbin.pan.experiment.cache;
/**
 *
 * @author liubinbin
 * not threadsafe and performance problem exists
 */
public class Bucket {
	
	private int slotsize;
	private byte[] data;
	private int writeIdx;
	
	public Bucket(int slotSize, int dataSize) {
		this.slotsize = slotSize;
		this.data = new byte[dataSize]; 
		this.writeIdx = 0;
	}
	
	public byte[] get(int offset, int length) {
		byte[] value = new byte[length];
		System.arraycopy(data, offset, value, 0, length);
		return null;
	}
	
	public int put(byte[] value) { 
		int offset = writeIdx;
		System.arraycopy(value, writeIdx, data, 0, value.length);
		writeIdx += value.length;
		return offset;
	}
	
	public boolean checkWriteForLen(int length) {
		return (writeIdx + length > slotsize) ? false:true;
	}
}
