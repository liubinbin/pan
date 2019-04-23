package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.server.Addr;
import cn.liubinbin.pan.server.Key;
import io.netty.buffer.ByteBuf;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author liubinbin
 */

public class BcacheManager {

    private ConcurrentSkipListMap<Key, Addr> index;
    private cn.liubinbin.pan.bcache.Bucket[] buckets;
    private int[] bucketSlotSize;

    public BcacheManager(Config cacheConfig) {
        this.index = new ConcurrentSkipListMap<Key, Addr>();
        this.bucketSlotSize = cacheConfig.getBucketSlotSize();
        this.buckets = new ByteArrayBucket[bucketSlotSize.length];
        for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx++) {
            this.buckets[bucketIdx] = new ByteArrayBucket(bucketSlotSize[bucketIdx], cacheConfig.getSegmentSize());
        }
    }

    public static void main(String[] args) throws FileNotFoundException, ConfigurationException, IOException {
//		Config cacheConfig = new Config();
//		BcacheManager cacheManager = new BcacheManager(cacheConfig);
//		byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
//		byte[] CONTENT1 = { 'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't' };
//		byte[] CONTENT2 = new byte[73060];
//		CONTENT2[73060 - 1] = '1';
//		cacheManager.put("abcd".getBytes(), CONTENT);
//		cacheManager.put("abc".getBytes(), CONTENT1);
//		cacheManager.put("abcde".getBytes(), CONTENT2);
//		System.out.println(cacheManager.getByByteBuf("abcd".getBytes()));
//		System.out.println(cacheManager.getByByteArray("abcd".getBytes()).length);
//		System.out.println(cacheManager.checkContainKey());
    }

    /**
     * must have
     *
     * @param key
     * @return
     */
    public byte[] getByByteArray(byte[] key) {
        // TODO
        return null;
    }

    /**
     * must have
     *
     * @param key
     * @return
     */
    public ByteBuf getByByteBuf(byte[] key) {
        // TODO
        return null;
    }

    /**
     * must have
     *
     * @param key
     */
    public void delete(byte[] key) {
        // TODO
        // find bucket that has data for this key


        // update index


        // mark header and size


    }

    /**
     * two ways:
     * 1. hash
     * 2. linked list
     *
     * @param key
     * @param value
     */
    public void put(byte[] key, byte[] value) {
        // TODO
        // init


        // find bucket


        // put meta


        // update slotsize


        // put data


        // update index to let data searchable

        // done
    }

    public int chooseBucketIdx(int valueLen) {
        for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx++) {
            if (valueLen < bucketSlotSize[bucketIdx]) {
                return bucketIdx;
            }
        }
        return -1;
    }

    public boolean checkContainKey(byte[] key) {
        // TODO
        return false;
    }
}
