package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.ChunkTooManyException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by bin on 2019/4/26.
 */
public class ChunkPoolTest {

    @Test
    public void testChooseChunkIdx() throws IOException, ConfigurationException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
//        System.out.println("getSlotSizes " + Arrays.asList(cacheConfig.getSlotSizes()));
        ChunkPool chunkPool = new ChunkPool(cacheConfig);
        assertEquals(0, chunkPool.chooseChunkIdx(20));
        assertEquals(1, chunkPool.chooseChunkIdx(5122));
        assertEquals(4, chunkPool.chooseChunkIdx(41965));
        assertEquals(-1, chunkPool.chooseChunkIdx(16778241));
    }

    @Test
    public void testCasChunkByIdx() throws IOException, ConfigurationException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        ChunkPool chunkPool = new ChunkPool(cacheConfig);
//        System.out.println("cacheConfig.getSlotSizes().length " + cacheConfig.getSlotSizes().length);
        int slotSizes = cacheConfig.getSlotSizes().length;
        for (int i = 0; i < slotSizes; i++) {
            Chunk chunk1 = new ByteArrayChunk(cacheConfig.getSlotSizes()[i], cacheConfig.getChunkSize());
            Chunk chunk2 = new ByteArrayChunk(cacheConfig.getSlotSizes()[i], cacheConfig.getChunkSize());
            Chunk chunk3 = new ByteArrayChunk(cacheConfig.getSlotSizes()[i], cacheConfig.getChunkSize());
            assertFalse(chunkPool.casChunkByIdx(i, chunk2, chunk1));
            assertTrue(chunkPool.casChunkByIdx(i, null, chunk1));
            assertTrue(chunkPool.casChunkByIdx(i, chunk1, chunk3));
            assertFalse(chunkPool.casChunkByIdx(i, chunk2, chunk3));
        }
    }

    @Test
    public void testgetChunkByIdx() throws IOException, ConfigurationException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        ChunkPool chunkPool = new ChunkPool(cacheConfig);
        int tempSlotSize = 234;
        int tempChunkSize = 1234;
        Chunk chunk = new ByteArrayChunk(tempSlotSize, tempChunkSize);
        assertEquals(null, chunkPool.getChunkByIdx(0));
        chunkPool.casChunkByIdx(0, null, chunk);
        Chunk chunkGot = chunkPool.getChunkByIdx(0);
        assertEquals(tempSlotSize, chunkGot.getSlotsize());
        assertEquals(tempChunkSize, chunkGot.getChunkSize());
        assertEquals(chunkGot, chunkPool.getChunkByIdx(0));
        assertEquals(null, chunkPool.getChunkByIdx(1));
    }

    @Test
    public void testAllocate() throws IOException, ConfigurationException, DataTooBiglException, ChunkTooManyException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        ChunkPool chunkPool = new ChunkPool(cacheConfig);
        int tempSlotSize = 234;
        int tempChunkSize = 1234;
        Chunk chunk = chunkPool.allocate(2);
        int choosenIdx = chunkPool.chooseChunkIdx(2);
        assertEquals(chunk, chunkPool.getChunkByIdx(choosenIdx));
        chunkPool.allocate(2);
        Chunk headChunkForIdx = chunkPool.getChunkByIdx(choosenIdx);
        boolean in = false;
        while (headChunkForIdx != null){
            if (headChunkForIdx == chunk) {
                in = true;
                break;
            } else {
                headChunkForIdx = headChunkForIdx.getNext();
            }
        }
        assertEquals(true, in);
    }
}