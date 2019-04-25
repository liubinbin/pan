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

public class ChunkManager {

    private ConcurrentSkipListMap<Key, Addr> index;
    // TODO do we need volatile, we have already lock.
    private Chunk[] chunks;
    private int[] slotSizes;
    private ReentrantReadWriteLock readWriteLock;
    private Lock rLock;
    private Lock wLock;

    public ChunkManager(Config cacheConfig) {
        this.index = new ConcurrentSkipListMap<>();
        this.slotSizes = cacheConfig.getSlotSizes();
        this.chunks = new Chunk[slotSizes.length];
        for (int chunkIdx = 0; chunkIdx < slotSizes.length; chunkIdx++) {
            this.chunks[chunkIdx] = new Chunk(slotSizes[chunkIdx], cacheConfig.getChunkSize());
        }
        this.readWriteLock = new ReentrantReadWriteLock();
        this.rLock = readWriteLock.readLock();
        this.wLock = readWriteLock.writeLock();
    }

    public static void main(String[] args) throws FileNotFoundException, ConfigurationException, IOException {
        Config cacheConfig = new Config();
        ChunkManager cacheManager = new ChunkManager(cacheConfig);
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
            byte[] value = chunks[addr.getChunkIdx()].getByByteArray(addr.getOffset(), addr.getLength());
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
            ByteBuf value = chunks[addr.getChunkIdx()].getByByteBuf(addr.getOffset(), addr.getLength());
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
            chunks[addr.getChunkIdx()].delete(key, addr.getOffset(), addr.getLength());
        } finally {
            rLock.unlock();
        }
    }

    public void put(byte[] key, byte[] value) {
        rLock.lock();
        try {
            int chunkIdx = chooseChunkIdx(value.length);
            int offset = chunks[chunkIdx].put(value);
            Key key1 = new Key(key);
            Addr addr = new Addr(chunkIdx, offset, value.length);
            index.put(key1, addr);
        } finally {
            rLock.unlock();
        }
    }

    public int chooseChunkIdx(int valueLen) {
        for (int chunkIdx = 0; chunkIdx < slotSizes.length; chunkIdx++) {
            if (valueLen < slotSizes[chunkIdx]) {
                return chunkIdx;
            }
        }
        return -1;
    }

    public String printKeys(Key key1) {
        ArrayList<String> keys = new ArrayList<>();
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
