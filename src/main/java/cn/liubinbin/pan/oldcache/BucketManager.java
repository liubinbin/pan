package cn.liubinbin.pan.oldcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.module.Addr;
import cn.liubinbin.pan.module.Key;
import io.netty.buffer.ByteBuf;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author liubinbin
 */

public class BucketManager {

    private ConcurrentSkipListMap<Key, Addr> index;
    // TODO do we need volatile, we have already lock.
    private Bucket[] buckets;
    private int[] bucketSlotSize;
    private ReentrantReadWriteLock readWriteLock;
    private Lock rLock;
    private Lock wLock;

    public BucketManager(Config cacheConfig) {
        this.index = new ConcurrentSkipListMap<Key, Addr>();
        this.bucketSlotSize = cacheConfig.getBucketSlotSize();
        this.buckets = new Bucket[bucketSlotSize.length];
        for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx++) {
            this.buckets[bucketIdx] = new Bucket(bucketSlotSize[bucketIdx], cacheConfig.getSegmentSize());
        }
        this.readWriteLock = new ReentrantReadWriteLock();
        this.rLock = readWriteLock.readLock();
        this.wLock = readWriteLock.writeLock();
    }

    public static void main(String[] args) throws FileNotFoundException, ConfigurationException, IOException {
        Config cacheConfig = new Config();
        BucketManager cacheManager = new BucketManager(cacheConfig);
        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
        byte[] CONTENT2 = new byte[73060];
        CONTENT2[73060 - 1] = '1';
        cacheManager.put("abcd".getBytes(), CONTENT);
        cacheManager.put("abc".getBytes(), CONTENT1);
        cacheManager.put("abcde".getBytes(), CONTENT2);
        System.out.println(cacheManager.getByByteBuf("abcd".getBytes()));
        System.out.println(cacheManager.checkContainKey());
    }

    public byte[] getByByteArray(byte[] key) {
        rLock.lock();
        try {
            Addr addr = index.get(new Key(key));
            // not found for key
            if (addr == null) {
                System.out.println("not found for " + new String(key));
                return null;
            }
            byte[] value = buckets[addr.getBucketIdx()].getByByteArray(addr.getOffset(), addr.getLength());
            return value;
        } finally {
            rLock.unlock();
        }
    }

    public ByteBuf getByByteBuf(byte[] key) {
        rLock.lock();
        try {
            Addr addr = index.get(new Key(key));
            // not found for key
            if (addr == null) {
                return null;
            }
            ByteBuf value = buckets[addr.getBucketIdx()].getByByteBuf(addr.getOffset(), addr.getLength());
            return value;
        } finally {
            rLock.unlock();
        }
    }

    public void delete(byte[] key) {
        rLock.lock();
        try {
            Addr addr = index.get(new Key(key));
            index.remove(new Key(key));
            buckets[addr.getBucketIdx()].delete(key, addr.getOffset(), addr.getLength());
        } finally {
            rLock.unlock();
        }
    }

    public void put(byte[] key, byte[] value) {
        rLock.lock();
        try {
            int bucketIdx = chooseBucketIdx(value.length);
            int offset = buckets[bucketIdx].put(value);
            Key key1 = new Key(key);
            Addr addr = new Addr(bucketIdx, offset, value.length);
            index.put(key1, addr);
        } finally {
            rLock.unlock();
        }
    }

    public int chooseBucketIdx(int valueLen) {
        for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx++) {
            if (valueLen < bucketSlotSize[bucketIdx]) {
                return bucketIdx;
            }
        }
        return -1;
    }

    public String printKeys(Key key1) {
        ArrayList<String> keys = new ArrayList<String>();
        for (Key key : index.keySet()) {
            keys.add(new String(key.getKey()));
        }
        return keys.toString();
    }

    public boolean checkContainKey() {
        for (Key key : index.keySet()) {
            if (!index.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
