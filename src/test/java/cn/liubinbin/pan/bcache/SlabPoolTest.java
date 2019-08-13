package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.SlabTooManyException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.exceptions.SlotBiggerThanChunkException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by bin on 2019/4/26.
 */
public class SlabPoolTest {

    @Test
    public void testChooseChunkIdx() throws IOException, ConfigurationException, SlotBiggerThanChunkException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
//        System.out.println("getSlotSizes " + Arrays.asList(cacheConfig.getSlotSizes()));
        SlabPool slabPool = new SlabPool(cacheConfig);
        assertEquals(0, slabPool.chooseChunkIdx(20));
        assertEquals(1, slabPool.chooseChunkIdx(5122));
        assertEquals(4, slabPool.chooseChunkIdx(41965));
        assertEquals(-1, slabPool.chooseChunkIdx(16778241));
    }

    @Test
    public void testCasChunkByIdx() throws IOException, ConfigurationException, SlotBiggerThanChunkException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        SlabPool slabPool = new SlabPool(cacheConfig);
//        System.out.println("cacheConfig.getSlotSizes().length " + cacheConfig.getSlotSizes().length);
        int slotSizes = cacheConfig.getSlotSizes().length;
        for (int i = 0; i < slotSizes; i++) {
            Slab chunk1 = new ByteArraySlab(cacheConfig.getSlotSizes()[i], cacheConfig.getSlabSize());
            Slab chunk2 = new ByteArraySlab(cacheConfig.getSlotSizes()[i], cacheConfig.getSlabSize());
            Slab chunk3 = new ByteArraySlab(cacheConfig.getSlotSizes()[i], cacheConfig.getSlabSize());
            assertFalse(slabPool.casChunkByIdx(i, chunk2, chunk1));
            assertTrue(slabPool.casChunkByIdx(i, null, chunk1));
            assertTrue(slabPool.casChunkByIdx(i, chunk1, chunk3));
            assertFalse(slabPool.casChunkByIdx(i, chunk2, chunk3));
        }
    }

    @Test
    public void testgetChunkByIdx() throws IOException, ConfigurationException, SlotBiggerThanChunkException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        SlabPool slabPool = new SlabPool(cacheConfig);
        int tempSlotSize = 234;
        int tempChunkSize = 1234;
        Slab chunk = new ByteArraySlab(tempSlotSize, tempChunkSize);
        assertEquals(null, slabPool.getChunkByIdx(0));
        slabPool.casChunkByIdx(0, null, chunk);
        Slab chunkGot = slabPool.getChunkByIdx(0);
        assertEquals(tempSlotSize, chunkGot.getSlotsize());
        assertEquals(tempChunkSize, chunkGot.getChunkSize());
        assertEquals(chunkGot, slabPool.getChunkByIdx(0));
        assertEquals(null, slabPool.getChunkByIdx(1));
    }

    @Test
    public void testAllocate() throws IOException, ConfigurationException, DataTooBiglException, SlabTooManyException, SlotBiggerThanChunkException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        SlabPool slabPool = new SlabPool(cacheConfig);
        int tempSlotSize = 234;
        int tempChunkSize = 1234;
        Slab chunk = slabPool.allocate(2);
        int choosenIdx = slabPool.chooseChunkIdx(2);
        assertEquals(chunk, slabPool.getChunkByIdx(choosenIdx));
        slabPool.allocate(2);
        Slab headChunkForIdx = slabPool.getChunkByIdx(choosenIdx);
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
