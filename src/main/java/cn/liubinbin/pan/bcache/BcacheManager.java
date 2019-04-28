package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.ChunkIsFullException;
import cn.liubinbin.pan.exceptions.ChunkTooManyException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.configuration2.ex.ConfigurationException;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author liubinbin
 */

public class BcacheManager {

    private ChunkPool chunkPool;
    private Chunk[] chunksInManager;
    private int[] slotSizes;
    private int hashMod;
    private sun.misc.Unsafe unsafe;
    private int NBASE;
    private int NSHIFT;

    public BcacheManager(Config cacheConfig) {
        this.chunkPool = new ChunkPool(cacheConfig);
        this.hashMod = cacheConfig.getHashMod();
        this.slotSizes = cacheConfig.getSlotSizes();
        this.chunksInManager = new ByteArrayChunk[hashMod];
        for (int chunkIdx = 0; chunkIdx < hashMod; chunkIdx++) {
            this.chunksInManager[chunkIdx] = null;
        }

        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Class nc = Chunk[].class;
        NBASE = unsafe.arrayBaseOffset(nc);
        int ns = unsafe.arrayIndexScale(nc);
        NSHIFT = 31 - Integer.numberOfLeadingZeros(ns);
    }

    /**
     * must have
     *
     * @param key key to location data
     * @return return data for key in byte[]
     */
    public byte[] getByByteArray(byte[] key) {
        int keyHash = ByteUtils.hashCode(key);
        // find chunk
        Chunk chunk = getChunkByIdx(keyHash);
        byte[] value = null;
        while (chunk != null) {
            if ((value = chunk.getByByteArray(key)) != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * must have
     *
     * @param key key to location data
     * @return return data for key in ByteBuf
     */
    public ByteBuf getByByteBuf(byte[] key) {
        int keyHash = ByteUtils.hashCode(key);
        // find chunk
        Chunk chunk = getChunkByIdx(keyHash);
        ByteBuf value;
        while (chunk != null) {
            if ((value = chunk.getByByteBuf(key)) != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * must have
     *
     * @param key key to location data
     */
    public void delete(byte[] key) {
        int keyHash = ByteUtils.hashCode(key);
        // find chunk
        Chunk chunk = getChunkByIdx(keyHash);
        while (chunk != null) {
            chunk.delete(key);
            chunk = chunk.getNext();
        }
    }

    /**
     * must have
     *
     * @param key   key to location data
     * @param value data related to key
     */
    public void put(byte[] key, byte[] value) throws DataTooBiglException, ChunkTooManyException {
        int keyHash = ByteUtils.hashCode(key);

        while (true) {
            // find chunk
            Chunk chunk = getChunkByIdx(keyHash);
            while (chunk != null) {
                if (chunk.checkWriteForLen(value.length)) {
                    break;
                } else {
                    chunk = chunk.getNext();
                }
            }

            // if chunk is null, allocate a chunk
            if (chunk == null) {
                chunk = chunkPool.allocate(value.length);
                if (chunk == null) {
                    // we cannot allocate chunk, maybe we have already too many chunksInManager.
                    // TODO expire item by value.length
                    continue;
                }
                addChunk(keyHash, chunk);
            }

            // put data
            try {
                chunk.put(key, value);
                break;
            } catch (ChunkIsFullException e) {
                // just retry, do nothing
            }
        }
    }

    public void addChunk(int hashKey, Chunk update) {
        if (getChunkByIdx(hashKey) == null) {
            if (casChunkByIdx(hashKey, null, update)) {
                return;
            }
        }
        Chunk expected = getChunkByIdx(hashKey);
        update.setNext(expected);
        while (!casChunkByIdx(hashKey, expected, update)) {
            expected = getChunkByIdx(hashKey);
            update.setNext(expected);
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

    public boolean checkContainKey(byte[] key) {
        int keyHash = ByteUtils.hashCode(key);
        Chunk chunk = getChunkByIdx(keyHash);
        while (chunk != null) {
            if (chunk.containKey(key)) {
                return true;
            }
        }
        return false;
    }

    public Chunk getChunkByIdx(int idx) {
        return (Chunk) unsafe.getObjectVolatile(chunksInManager, (long) ((idx << NSHIFT) + NBASE));
    }

    public boolean casChunkByIdx(int idx, Chunk expected, Chunk update) {
        return unsafe.compareAndSwapObject(chunksInManager, (long) ((idx << NSHIFT) + NBASE), expected, update);
    }

    public static void main(String[] args) throws ConfigurationException, IOException, DataTooBiglException, ChunkTooManyException {
//        Config cacheConfig = new Config();
//        BcacheManager cacheManager = new BcacheManager(cacheConfig);
//        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
//        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
//        byte[] CONTENT2 = new byte[73060];
//        CONTENT2[73060 - 1] = '1';
//        cacheManager.put("abcd".getBytes(), CONTENT);
//        cacheManager.put("abc".getBytes(), CONTENT1);
//        cacheManager.put("abcde".getBytes(), CONTENT2);
//        System.out.println(cacheManager.getByByteBuf("abcd".getBytes()));
//        System.out.println(cacheManager.getByByteArray("abcd".getBytes()).length);
//        System.out.println(cacheManager.checkContainKey("abcd".getBytes()));
    }

}
