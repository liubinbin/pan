package cn.liubinbin.pan.manager.bcache;


import cn.liubinbin.pan.utils.ByteUtils;

import java.nio.ByteBuffer;

/**
 *
 * @author liubinbin
 *
 * BaseCCSMap is the map who have the diff key and value.
 * for BaseCCSMap:
 * - meta:
 * - int status:
 * - int expiretime :expire time
 * - int hash: key.hash
 * - int dataLen :total data len
 * - data:
 *      -- int keyLen
 *      -- int valueLen
 *      -- byte[] key
 *      -- byte[] value
 */
public class BItem {
    // meta
    // 0 does not exist or deleted, 1 does exist
    private byte status;        // 1 byte, 0
    private long expiretime;    // 8 bytes, 0 + 1
    private int hash;           // 4 bytes, 0 + 1 + 8
    private int dataLen;        // 4 bytes, 0 + 1 + 8 + 4
    private int keyLength;      // 4 bytes, 0 + 1 + 8 + 4 + 4
    private int valueLength;    // 4 bytes, 0 + 1 + 8 + 4 + 4 + 4
    // data
    private byte[] key;         // key.length, 0 + 1 + 8 + 4 + 4 + 4 + 4
    private byte[] value;       // value.length, 0 + 1 + 8 + 4 + 4 + 4 + 4 + keyLength

    public BItem(byte[] key, byte[] value){
        this.status = 1;
        this.expiretime = System.currentTimeMillis();
        this.hash = ByteUtils.hashCode(key);
        this.keyLength = key.length;
        this.valueLength = value.length;
        this.dataLen = 8 + 8 + keyLength + valueLength;

        this.key = key;
        this.value = value;
	}

	public void writeTo(ByteBuffer byteBuffer, int offset){
        byteBuffer.put(status); // use cas to race this slot
        byteBuffer.put(expiretime);
        byteBuffer.putInt(hash);
        byteBuffer.putInt(dataLen);
        byteBuffer.putInt(keyLength);
        byteBuffer.putInt(valueLength);
        byteBuffer.put(key);
        byteBuffer.put(value);
    }
}
