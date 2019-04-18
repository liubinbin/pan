package cn.liubinbin.pan.bcache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author liubinbin
 */
public abstract class DirectByteBufferBucket extends Bucket {

	private int slotsize;
	private ByteBuffer data;
	private int nextFreeSlot;
	private byte status; // opening for put; being source of compact; being target of compact
	/*
		0 stands for haven't been added
	 */
	private AtomicInteger dataTotalSize;

	public DirectByteBufferBucket(int slotSize, int segmentSize) {
		this.slotsize = slotSize;
		this.data = ByteBuffer.allocateDirect(segmentSize);
		this.dataTotalSize.set(0);
		this.nextFreeSlot = 0;
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
	
	public boolean checkWriteForLen(int length) {
        return false;
	}

	public int getdataTotalSize() {
		return dataTotalSize.get();
	}
}
