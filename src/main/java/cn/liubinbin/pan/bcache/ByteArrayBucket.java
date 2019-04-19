package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Contants;
import cn.liubinbin.pan.exceptions.BucketIsFullException;
import cn.liubinbin.pan.utils.ByteArrayUtils;
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
    private int ttlInDays = 30;
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
            throw new BucketIsFullException("bucket is full, slotSize: " + slotSize);
        }
        int seekOffset = seekAndWriteStatus();
	    if(seekOffset < 0) {
            throw new BucketIsFullException("bucket is full, slotSize: " + slotSize);
        }

        // set totalsize
        dataTotalSize.addAndGet(slotsize);

	    // put meta


        // put data


		return offset;
	}

	public int seekAndWriteStatus() {
		int seekOffset = 0;
		while (seekOffset < segmentSize ) {
			if (ByteArrayUtils.toInt(data, seekOffset) == 0 ){
                if (ByteArrayUtils.compareAndSetInt(data, seekOffset, 0, 1)) {
                    return seekOffset;
                }
			}
		}
		return -1;
	}

    /**
     // 0 does not exist or deleted, 1 does exist, preserve the rest
     private int status;         // 4 byte, 0
     private long expireTime;    // 8 bytes, 0 + 4
     private int hash;           // 4 bytes, 0 + 4 + 8
     private int dataLen;        // 4 bytes, 0 + 4 + 8 + 4
     private int keyLength;      // 4 bytes, 0 + 4 + 8 + 4 + 4
     private int valueLength;    // 4 bytes, 0 + 4 + 8 + 4 + 4 + 4
     // data
     private byte[] key;         // key.length, 0 + 4 + 8 + 4 + 4 + 4 + 4
     private byte[] value;       // value.length, 0 + 4 + 8 + 4 + 4 + 4 + 4 + keyLength
     * @param seekOffset
     * @param key
     * @param value
     */
	public void writeMeta(int seekOffset, byte[] key, byte[] value) {
        ByteArrayUtils.putLong(System.currentTimeMillis() + ttlInDays * Contants.MsInADay);
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
