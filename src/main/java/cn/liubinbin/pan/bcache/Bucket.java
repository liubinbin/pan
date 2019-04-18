package cn.liubinbin.pan.bcache;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author liubinbin
 */
public abstract class Bucket {

	private int slotsize;
	private int nextFreeSlot;
	private byte status; // opening for put; being source of compact; being target of compact
	/*
		0 stands for haven't been added
	 */
	private AtomicInteger dataTotalSize;

	public Bucket(int slotSize, int segmentSize) {
		this.slotsize = slotSize;
		this.dataTotalSize.set(0);
		this.nextFreeSlot = 0;
	}

	public abstract byte[] getByByteArray(int offset, int length);

	public abstract ByteBuf getByByteBuf(int offset, int length);

	/**
	 * @param value
	 * @return
	 */
	public int put(byte[] key, byte[] value) {
		// find position


        // race to set totalsize


        // put data




		int offset = writeIdx;
		System.arraycopy(value, 0, data, writeIdx, value.length);
		writeIdx += value.length;
		dataTotalSize += value.length;
		return offset;
	}

	public void delete(byte[] value, int offset, int length) {

        // find position


        // race to set header


        // race to set totalsize


	}
	
//	public boolean checkWriteForLen(int length) {
//      return false;
//	}
//
//	public int getdataTotalSize() {
//		return dataTotalSize.get();
//	}
}
