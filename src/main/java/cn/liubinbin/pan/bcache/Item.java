package cn.liubinbin.pan.bcache;


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
public class Item {
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

    public Item(byte[] key, byte[] value){
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
        byteBuffer.putLong(expiretime);
        byteBuffer.putInt(hash);
        byteBuffer.putInt(dataLen);
        byteBuffer.putInt(keyLength);
        byteBuffer.putInt(valueLength);
        byteBuffer.put(key);
        byteBuffer.put(value);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("status: ").append(status);
        sb.append(", expiretime: ").append(expiretime);
        sb.append(", hahs: " ).append(hash);
        sb.append(", dataLen: ").append(dataLen);
        sb.append(", keyLength: ").append(keyLength);
        sb.append(", key: ").append(new String(key));
        sb.append(", value: ").append(new String(value));
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
        ByteBuffer bBucket = ByteBuffer.allocate(1024 * 1024 * 2);
        byte[] key = "item.key".getBytes();
        byte[] value = "item.value".getBytes();

        Item bItem  = new Item(key, value);
        System.out.println("bItem " + bItem.toString());
        bItem.writeTo(bBucket, 0);
        System.out.println("bBucket " + bBucket.toString());
        bBucket.flip();
        byte status = bBucket.get();
        long expiretime = bBucket.getLong();
        int hash = bBucket.getInt();
        int dataLen = bBucket.getInt();
        int keyLength = bBucket.getInt();
        int valueLength = bBucket.getInt();
        byte[] keyre = new byte[keyLength];
        bBucket.get(keyre);
        byte[] valuere = new byte[valueLength];
        bBucket.get(valuere);

        System.out.println("status: " + status);
        System.out.println("expiretime: " + expiretime);
        System.out.println("hash: " + hash);
        System.out.println("dataLen: " + dataLen);
        System.out.println("key: " + new String(keyre));
        System.out.println("value: " + new String(keyre));

    }
}
