package cn.liubinbin.pan.bcache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author liubinbin
 */
public abstract class ByteArrayBucket extends Bucket{

	private int slotsize;
	private int segmentSize;
	private byte[] data;
	private int nextFreeSlot;
	private byte status; // opening for put; being source of compact; being target of compact
	/*
		0 stands for haven't been added
	 */
	private AtomicInteger dataTotalSize;

	public ByteArrayBucket(int slotSize, int segmentSize) {
		this.slotsize = slotSize;
		this.segmentSize = segmentSize;
		this.data = new byte[segmentSize];
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

    public int putMeta(int offset){
        return null;
    }

	/**
	 * @param value
	 * @return
	 */
	public int put(byte[] key, byte[] value) {
        // find position
        if (dataTotalSize.get() >= segmentSize) {
            throw new Exception();
        }
	    int seekOffset = 0;
	    while (seekOffset < segmentSize ) {
            if (data[seekOffset] == 0 ){
                // use cas
            }
            seekOffset += slotsize;
        }



        // race to set totalsize
        dataTotalSize.addAndGet(slotsize);

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
