package main.java.cn.liubinbin.pan.experiment.cache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author liubinbin not threadsafe and performance problem exists
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

	public byte[] getByByteArray(int offset, int length) {
		byte[] value = new byte[length];
		System.arraycopy(data, offset, value, 0, length);
		return value;
	}

	public ByteBuf getByByteBuf(int offset, int length) {
		ByteBuf value = Unpooled.wrappedBuffer(data, offset, length);
		return value;
	}

	public int put(byte[] value) {
		int offset = writeIdx;
		System.arraycopy(value, 0, data, writeIdx, value.length);
		writeIdx += value.length;
		return offset;
	}

	public boolean checkWriteForLen(int length) {
		return (writeIdx + length > slotsize) ? false : true;
	}

	public int getWriteIdx() {
		return writeIdx;
	}

	public static void main(String[] args) {
		Bucket bucket = new Bucket(128, 16384);
		byte[] data1 = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
		int data1Offset = bucket.put(data1);
		byte[] data2 = { 's', 'p', 'a', 'k', 'h', 'a' };
		int data2Offset = bucket.put(data2);
		System.out.println("data1.length + data2.length: " + data1.length + data2.length);
		System.out.println("bucket.getWriteIdx(): " + bucket.getWriteIdx());
		System.out.println("data1.length: " + data1.length);
		ByteBuf valueFromBucket = bucket.getByByteBuf(data1Offset, data1.length);
		ByteBuf valueOriginal = Unpooled.wrappedBuffer(data1);

		System.out.println(valueFromBucket.compareTo(valueOriginal));

	}

}
