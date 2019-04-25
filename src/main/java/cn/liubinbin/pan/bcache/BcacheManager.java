package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author liubinbin
 */

public class BcacheManager {

    private ChunkPool chunkPool;
    private Chunk[] chunks;
    private int[] chunkSlotSize;
    private int hashMod;

    public BcacheManager(Config cacheConfig) {
        this.hashMod = cacheConfig.getHashMod();
        this.chunkSlotSize = cacheConfig.getChunkSlotSize();
        this.chunks = new ByteArrayChunk[hashMod];
        for (int chunkIdx = 0; chunkIdx < hashMod; chunkIdx++) {
            this.chunks[chunkIdx] = null;
        }
    }

    public static void main(String[] args) throws ConfigurationException, IOException {
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
        // find chunk that has data for this key


        // update index


        // mark header and size


    }

    /**
     * must have
     *
     * @param key
     * @param value
     */
    public void put(byte[] key, byte[] value) {
        // TODO
        // init
        int keyHash = ByteUtils.hashCode(key);

        while (true) {
            // find chunk




            // if chunk is null, allocate a chunk




            // put data



        }

        // done
    }

    /**
     * return a chunk
     * @param key
     * @param hashKey
     * @return
     */
    public Chunk findChunk(byte[] key, int hashKey){

        return null;
    }

    public int chooseChunkIdx(int valueLen) {
        for (int chunkIdx = 0; chunkIdx < chunkSlotSize.length; chunkIdx++) {
            if (valueLen < chunkSlotSize[chunkIdx]) {
                return chunkIdx;
            }
        }
        return -1;
    }

    public boolean checkContainKey(byte[] key) {
        int keyHash = ByteUtils.hashCode(key);

        return false;
    }
}
