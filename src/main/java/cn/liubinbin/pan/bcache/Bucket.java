package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.BucketIsFullException;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author liubinbin
 */
public abstract class Bucket {

    private int slotsize;
    private int segmentSize;
	private byte status; // opening for put; being source of compact; being target of compact

	public Bucket(int slotSize, int segmentSize) {
		this.slotsize = slotSize;
		this.segmentSize = segmentSize;
	}

	public abstract byte[] getByByteArray(int offset, int length);

	public abstract ByteBuf getByByteBuf(int offset, int length);

	/**
	 * @param value
	 * @return
	 */
	public abstract int put(byte[] key, byte[] value) throws BucketIsFullException;

	public abstract void delete(byte[] key, int offset);

    public int getSlotsize() {
        return slotsize;
    }

    public int getSegmentSize() {
        return segmentSize;
    }
}
