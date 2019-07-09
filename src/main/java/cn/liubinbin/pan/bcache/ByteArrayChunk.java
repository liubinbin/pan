package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Contants;
import cn.liubinbin.pan.exceptions.ChunkIsFullException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.module.Item;
import cn.liubinbin.pan.module.Key;
import cn.liubinbin.pan.utils.ByteArrayUtils;
import cn.liubinbin.pan.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liubinbin
 */
public class ByteArrayChunk extends Chunk {

    private byte[] data;
    private int nextFreeSlot;
    private byte status; // opening for put; being source of compact; being target of compact
    private long ttlInDays = 30;
    /*
        0 stands for haven't been added
     */
    private AtomicInteger dataTotalSize;

    public ByteArrayChunk(int slotSize, int chunkSize) {
        super(slotSize, chunkSize);
        this.data = new byte[chunkSize];
        this.dataTotalSize = new AtomicInteger(0);
        this.nextFreeSlot = 0;
    }

    public byte[] getByByteArray(byte[] key) {
        int keyHash = ByteUtils.hashCode(key);
        int seekOffset = 0;
        while (seekOffset < getChunkSize()) {
            if (getStatus(seekOffset) == 1 && getHash(seekOffset) == keyHash
                    && ByteUtils.IsByteArrayEqual(getKey(seekOffset), key)) {
                return getValue(seekOffset);
            }
            seekOffset += getSlotsize();
        }
        return null;
    }

    public ByteBuf getByByteBuf(byte[] key) {
        int keyHash = ByteUtils.hashCode(key);
        int seekOffset = 0;
        while (seekOffset < getChunkSize()) {
            if (getStatus(seekOffset) == 1 && getHash(seekOffset) == keyHash
                    && ByteUtils.IsByteArrayEqual(getKey(seekOffset), key)) {
                return getValueByByteBuffer(seekOffset);
            }
            seekOffset += getSlotsize();
        }
        return null;
    }

    /**
     * @param value
     * @return
     */
    public int put(byte[] key, byte[] value) throws ChunkIsFullException, DataTooBiglException {
        if (getSlotsize() < value.length || getChunkSize() < value.length) {
            throw new DataTooBiglException("object is too big, value is " + value.length + " slotSize " + getSlotsize() + " chunkSize " + getChunkSize());
        }

        // find position
        if (dataTotalSize.get() >= getChunkSize()) {
            throw new ChunkIsFullException("chunk is full, slotSize: " + getSlotsize());
        }
        int seekOffset = seekAndWriteStatus();
        if (seekOffset < 0) {
            throw new ChunkIsFullException("chunk is full, slotSize: " + getSlotsize());
        }

        // set totalsize
        dataTotalSize.addAndGet(getSlotsize());

        // put data
        writeData(seekOffset, key, value);

        // we can find this key after we write meta, so before write meta.
        // we should make sure that key and value be written.
        // put meta;
        writeMeta(seekOffset, key, value);

        return seekOffset;
    }

    public int seekAndWriteStatus() {
        int seekOffset = 0;
        while (seekOffset < getChunkSize()) {
            if (ByteArrayUtils.toInt(data, seekOffset) == 0) {
                if (ByteArrayUtils.compareAndSetInt(data, seekOffset + Contants.STATUS_SHIFT_LINKED, 0, 1)) {
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
//        System.out.println("write expireTime.offset : " + seekOffset + Contants.EXPIRETIME_SHIFT);
//        System.out.println(" expireTime  " + (long) (System.currentTimeMillis() + ttlInDays * Contants.MsInADay));
        ByteArrayUtils.putLong(data, seekOffset + Contants.EXPIRETIME_SHIFT, System.currentTimeMillis() + ttlInDays * Contants.MsInADay);
        // hash
//        System.out.println("write hash.offset : " + (seekOffset + Contants.HASH_SHIFT));
        ByteArrayUtils.putInt(data, seekOffset + Contants.HASH_SHIFT, ByteUtils.hashCode(key));
        // dataLen
//        System.out.println("write dataLen.offset : " + (seekOffset + Contants.DATALEN_SHIFT));
        ByteArrayUtils.putInt(data, seekOffset + Contants.DATALEN_SHIFT, key.length + value.length);
        // keyLength
//        System.out.println("write keyLength.offset : " + (seekOffset + Contants.KEYLENGTH_SHIFT) + " " + key.length);
        ByteArrayUtils.putInt(data, seekOffset + Contants.KEYLENGTH_SHIFT, key.length);
        // valueLength
//        System.out.println("write valueLength.offset : " + (seekOffset + Contants.VALUELENGTH_SHIFT) + " " + value.length);
        ByteArrayUtils.putInt(data, seekOffset + Contants.VALUELENGTH_SHIFT, value.length);
    }

    /**
     * @param seekOffset
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
//        System.out.println("write key.offset : " + (seekOffset + Contants.KEYVALUE_SHIFT));
        ByteArrayUtils.putBytes(data, seekOffset + Contants.KEYVALUE_SHIFT, key);
        // value
//        System.out.println("write value.offset : " + (seekOffset + Contants.KEYVALUE_SHIFT + key.length));
        ByteArrayUtils.putBytes(data, seekOffset + Contants.KEYVALUE_SHIFT + key.length, value);
    }

    public Item readFrom(int offset) {
        // status
        int status = ByteArrayUtils.toInt(data, offset + Contants.STATUS_SHIFT);
//        System.out.println("readFrom.status " + status + " " + (offset + Contants.STATUS_SHIFT));
        // expireTime
        long expireTime = ByteArrayUtils.toLong(data, offset + Contants.EXPIRETIME_SHIFT);
//        System.out.println("readFrom.expireTime " + expireTime + " " + (offset + Contants.EXPIRETIME_SHIFT));
        // hash
        int hash = ByteArrayUtils.toInt(data, offset + Contants.HASH_SHIFT);
//        System.out.println("readFrom.hash " + hash + " " + (Contants.HASH_SHIFT));
        // datalen
        int dataLen = ByteArrayUtils.toInt(data, offset + Contants.DATALEN_SHIFT);
//        System.out.println("readFrom.dataLen " + dataLen + " " + (Contants.DATALEN_SHIFT));
        // keyLength
        int keyLength = ByteArrayUtils.toInt(data, offset + Contants.KEYLENGTH_SHIFT);
//        System.out.println("readFrom.keyLength " + keyLength + " " + (offset + Contants.KEYLENGTH_SHIFT));
        // valueLength
        int valueLength = ByteArrayUtils.toInt(data, offset + Contants.VALUELENGTH_SHIFT);
//        System.out.println("readFrom.valueLength " + valueLength + " " + (offset + Contants.VALUELENGTH_SHIFT));

        byte[] key = ByteArrayUtils.getBytes(data, offset + Contants.KEYVALUE_SHIFT, keyLength);
//        System.out.println("readFrom.key " + new String(key) + " " + (offset + Contants.KEYVALUE_SHIFT));
        byte[] value = ByteArrayUtils.getBytes(data, offset + Contants.KEYVALUE_SHIFT + keyLength, valueLength);
//        System.out.println("readFrom.value " + new String(value) + " " + (offset + Contants.KEYVALUE_SHIFT + keyLength));
        return new Item(status, expireTime, hash, dataLen, keyLength, valueLength, key, value);
    }

    public int getStatus(int offset) {
        int status = ByteArrayUtils.toInt(data, offset + Contants.STATUS_SHIFT);
        return status;
    }

    public long getExpireTime(int offset){
        long expireTime = ByteArrayUtils.toLong(data, offset + Contants.EXPIRETIME_SHIFT);
        return expireTime;
    }

    public int getHash(int offset){
        int hash = ByteArrayUtils.toInt(data, offset + Contants.HASH_SHIFT);
        return hash;
    }

    public int getDataLen(int offset) {
        int dataLen = ByteArrayUtils.toInt(data, offset + Contants.DATALEN_SHIFT);
        return dataLen;
    }

    public int getKeyLength(int offset) {
        int keyLength = ByteArrayUtils.toInt(data, offset + Contants.KEYLENGTH_SHIFT);
        return keyLength;
    }

    public int getValueLength(int offset){
        int valueLength = ByteArrayUtils.toInt(data, offset + Contants.VALUELENGTH_SHIFT);
        return valueLength;
    }

    public byte[] getKey(int offset){
        int keyLength = getKeyLength(offset);
        byte[] key = ByteArrayUtils.getBytes(data, offset + Contants.KEYVALUE_SHIFT, keyLength);
        return key;
    }

    public byte[] getValue(int offset){
        int keyLength = getKeyLength(offset);
        int valueLength = getValueLength(offset);
        byte[] value = ByteArrayUtils.getBytes(data, offset + Contants.KEYVALUE_SHIFT + keyLength, valueLength);
        return value;
    }

    public ByteBuf getValueByByteBuffer(int offset){
        int keyLength = getKeyLength(offset);
        int valueLength = getValueLength(offset);
        ByteBuf value = Unpooled.wrappedBuffer(data, offset + Contants.KEYVALUE_SHIFT + keyLength, valueLength);
        return value;
    }

    public void delete(byte[] key) {
        int offset = -1;
        int keyHash = ByteUtils.hashCode(key);
        int seekOffset = 0;
        while (seekOffset < getChunkSize()) {
            if (getStatus(seekOffset) == 1 && getHash(seekOffset) == keyHash
                    && ByteUtils.IsByteArrayEqual(getKey(seekOffset), key)) {
                offset = seekOffset;
                break;
            }
            seekOffset += getSlotsize();
        }

        // this means "not found"
        if (offset == -1) {
            return;
        }

        ByteArrayUtils.compareAndSetInt(data, offset, 1, 0);

        // race to set header
        resetMeta(offset);

        // race to set totalsize
        while (!dataTotalSize.compareAndSet(dataTotalSize.get(), dataTotalSize.get() - getSlotsize())) {
            System.out.println("dataTotalSize " + getdataTotalSize());
        }
    }

    public boolean containKey(byte[] key) {
        return getByByteArray(key) != null;
    }

    public ArrayList<Key> getAllKeys() {
        ArrayList<Key> keys = new ArrayList<Key>();
        int seekOffset = 0;
        while (seekOffset < getChunkSize()) {
            if (getStatus(seekOffset) == 1 ) {
                keys.add(new Key(getKey(seekOffset)));
            }
            seekOffset += getSlotsize();
        }
        return keys;
    }

    /**
     *
     * @param length
     * @return true for can be added data, false for full chunk
     */
    public boolean checkWriteForLen(int length) {
        return length < getSlotsize() && getdataTotalSize() < getChunkSize();
    }

    public int getdataTotalSize() {
        return dataTotalSize.get();
    }
}
