package cn.liubinbin.pan.bcache;


/**
 *
 * @author liubinbin
 *
 * BaseCCSMap is the map who have the diff key and value.
 * for BaseCCSMap:
 * - meta:
 * - int status:
 * - int expiretime :expire time
 * - int next: offset of next time
 * - int dataLen :total data len
 * - data:
 *      -- int keyLen
 *      -- int valueLen
 *      -- byte[] key
 *      -- byte[] value
 */
public class ItemLinkedVersion {
    private byte status; // 0 does not exist, 1 does exist
    private long expiretime;
    private int hash;
    private int keyLength;
    private int valueLength;

    private byte[] key;
    private byte[] value;

    ItemLinkedVersion(){

	}

}
