package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Contants;
import cn.liubinbin.pan.exceptions.BucketIsFullException;
import cn.liubinbin.pan.module.Item;
import cn.liubinbin.pan.utils.ByteArrayUtils;
import cn.liubinbin.pan.utils.ByteUtils;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liubinbin
 */
public class ByteArrayBucket extends Bucket {

    //	private int slotSize;
//	private int segmentSize;
    private byte[] data;
    private int nextFreeSlot;
    private byte status; // opening for put; being source of compact; being target of compact
    private long ttlInDays = 30;
    /*
        0 stands for haven't been added
     */
    private AtomicInteger dataTotalSize;

    public ByteArrayBucket(int slotSize, int segmentSize) {
        super(slotSize, segmentSize);
//		this.slotSize = slotSize;
//		this.segmentSize = segmentSize;
        this.data = new byte[segmentSize];
        this.dataTotalSize = new AtomicInteger(0);
        this.nextFreeSlot = 0;
    }

    public byte[] getByByteArray(int offset, int length) {
        byte[] value = new byte[length];
//		System.arraycopy(data, offset, value, 0, length);
        return value;
    }

    public byte[] getByByteArray(byte[] key) {
        byte[] value = new byte[length];
//		System.arraycopy(data, offset, value, 0, length);
        return value;
    }

    public ByteBuf getByByteBuf(int offset, int length) {
        ByteBuf value = null;
//		ByteBuf value = Unpooled.wrappedBuffer(data, offset, length);
        return value;
    }

    /**
     * @param value
     * @return
     */
    public int put(byte[] key, byte[] value) throws BucketIsFullException {
        // find position
        if (dataTotalSize.get() >= getSegmentSize()) {
            throw new BucketIsFullException("bucket is full, slotSize: " + getSlotsize());
        }
        int seekOffset = seekAndWriteStatus();
        if (seekOffset < 0) {
            throw new BucketIsFullException("bucket is full, slotSize: " + getSlotsize());
        }

        // set totalsize
        dataTotalSize.addAndGet(getSlotsize());

        // put meta;
        writeMeta(seekOffset, key, value);

        // put data
        writeData(seekOffset, key, value);

        return seekOffset;
    }

    public int seekAndWriteStatus() {
        int seekOffset = 0;
        while (seekOffset < getSegmentSize()) {
            if (ByteArrayUtils.toInt(data, seekOffset) == 0) {
                if (ByteArrayUtils.compareAndSetInt(data, seekOffset, 0, 1)) {
                    return seekOffset;
                }
            }
            seekOffset += getSlotsize();
        }
        return -1;
    }

    /**
     * @param seekOffset
     * @param key
     * @param value
     */
    private void writeMeta(int seekOffset, byte[] key, byte[] value) {
        // expireTime
        System.out.println("write expireTime.offset : " + seekOffset + Contants.EXPIRETIME_SHIFT);
        System.out.println(" expireTime  " + (long) (System.currentTimeMillis() + ttlInDays * Contants.MsInADay));
        ByteArrayUtils.putLong(data, seekOffset + Contants.EXPIRETIME_SHIFT, System.currentTimeMillis() + ttlInDays * Contants.MsInADay);
        // hash
        System.out.println("write hash.offset : " + (seekOffset + Contants.HASH_SHIFT));
        ByteArrayUtils.putInt(data, seekOffset + Contants.HASH_SHIFT, ByteUtils.hashCode(key));
        // dataLen
        System.out.println("write dataLen.offset : " + (seekOffset + Contants.DATALEN_SHIFT));
        ByteArrayUtils.putInt(data, seekOffset + Contants.DATALEN_SHIFT, key.length + value.length);
        // keyLength
        System.out.println("write keyLength.offset : " + (seekOffset + Contants.KEYLENGTH_SHIFT) + " " + key.length);
        ByteArrayUtils.putInt(data, seekOffset + Contants.KEYLENGTH_SHIFT, key.length);
        // valueLength
        System.out.println("write valueLength.offset : " + (seekOffset + Contants.VALUELENGTH_SHIFT) + " " + value.length);
        ByteArrayUtils.putInt(data, seekOffset + Contants.VALUELENGTH_SHIFT, value.length);
    }

    /**
     * @param seekOffset
     * @param key
     * @param value
     */
    private void resetMeta(int seekOffset) {
        // expireTime
        ByteArrayUtils.putLong(data, seekOffset + Contants.EXPIRETIME_SHIFT, 0);
        // hash
        ByteArrayUtils.putInt(data, seekOffset + Contants.HASH_SHIFT, 0);
        // dataLen
        ByteArrayUtils.putInt(data, seekOffset + Contants.DATALEN_SHIFT, 0);
        // keyLength
        ByteArrayUtils.putInt(data, seekOffset + Contants.KEYLENGTH_SHIFT, 0);
        // valueLength
        ByteArrayUtils.putInt(data, seekOffset + Contants.VALUELENGTH_SHIFT, 0);
    }

    private void writeData(int seekOffset, byte[] key, byte[] value) {
        // key
        System.out.println("write key.offset : " + (seekOffset + Contants.KEYVALUE_SHIFT));
        ByteArrayUtils.putBytes(data, seekOffset + Contants.KEYVALUE_SHIFT, key);
        // value
        System.out.println("write value.offset : " + (seekOffset + Contants.KEYVALUE_SHIFT + key.length));
        ByteArrayUtils.putBytes(data, seekOffset + Contants.KEYVALUE_SHIFT + key.length, value);
    }

    public Item readFrom(int offset) {
        // status
        int status = ByteArrayUtils.toInt(data, offset + Contants.STATUS_SHIFT);
        System.out.println("readFrom.status " + status + " " + (offset + Contants.STATUS_SHIFT));
        // expireTime
        long expireTime = ByteArrayUtils.toLong(data, offset + Contants.EXPIRETIME_SHIFT);
        System.out.println("readFrom.expireTime " + expireTime + " " + (offset + Contants.EXPIRETIME_SHIFT));
        // hash
        int hash = ByteArrayUtils.toInt(data, offset + Contants.HASH_SHIFT);
        System.out.println("readFrom.hash " + hash + " " + (Contants.HASH_SHIFT));
        // datalen
        int dataLen = ByteArrayUtils.toInt(data, offset + Contants.DATALEN_SHIFT);
        System.out.println("readFrom.dataLen " + dataLen + " " + (Contants.DATALEN_SHIFT));
        // keyLength
        int keyLength = ByteArrayUtils.toInt(data, offset + Contants.KEYLENGTH_SHIFT);
        System.out.println("readFrom.keyLength " + keyLength + " " + (offset + Contants.KEYLENGTH_SHIFT));
        // valueLength
        int valueLength = ByteArrayUtils.toInt(data, offset + Contants.VALUELENGTH_SHIFT);
        System.out.println("readFrom.valueLength " + valueLength + " " + (offset + Contants.VALUELENGTH_SHIFT));

        byte[] key = ByteArrayUtils.getBytes(data, offset + Contants.KEYVALUE_SHIFT, keyLength);
        System.out.println("readFrom.key " + new String(key) + " " + (offset + Contants.KEYVALUE_SHIFT));
        byte[] value = ByteArrayUtils.getBytes(data, offset + Contants.KEYVALUE_SHIFT + keyLength, valueLength);
        System.out.println("readFrom.value " + new String(value) + " " + (offset + Contants.KEYVALUE_SHIFT + keyLength));
        return new Item(status, expireTime, hash, dataLen, keyLength, valueLength, key, value);
    }

    public void delete(byte[] key, int offset) {
        ByteArrayUtils.compareAndSetInt(data, offset, 1, 0);

        // race to set header
        resetMeta(offset);

        // race to set totalsize
        while (dataTotalSize.compareAndSet(dataTotalSize.get(), dataTotalSize.get() - getSlotsize())) {

        }
    }

    public boolean checkWriteForLen(int length) {
        return false;
    }

    public int getdataTotalSize() {
        return dataTotalSize.get();
    }
}
