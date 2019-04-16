package cn.liubinbin.pan.manager.bcache;


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
    private byte status; // 0 does not exist or deleted, 1 does exist
    private long expiretime;
    private int hash;
    private int keyLength;
    private int valueLength;
    private int dataLen;
    // data
    private byte[] key;
    private byte[] value;

    BItem(){

	}

}
