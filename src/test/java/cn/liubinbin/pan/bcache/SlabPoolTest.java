package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.TooManySlabsException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.exceptions.SlotBiggerThanSlabException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by bin on 2019/4/26.
 */
public class SlabPoolTest {

    @Test
    public void testChooseSlabIdx() throws IOException, ConfigurationException, SlotBiggerThanSlabException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
//        System.out.println("getSlotSizes " + Arrays.asList(cacheConfig.getSlotSizes()));
        SlabPool slabPool = new SlabPool(cacheConfig);
        assertEquals(0, slabPool.chooseSlabIdx(20));
        assertEquals(1, slabPool.chooseSlabIdx(5122));
        assertEquals(4, slabPool.chooseSlabIdx(41965));
        assertEquals(-1, slabPool.chooseSlabIdx(16778241));
    }

    @Test
    public void testCasSlabByIdx() throws IOException, ConfigurationException, SlotBiggerThanSlabException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        SlabPool slabPool = new SlabPool(cacheConfig);
//        System.out.println("cacheConfig.getSlotSizes().length " + cacheConfig.getSlotSizes().length);
        int slotSizes = cacheConfig.getSlotSizes().length;
        for (int i = 0; i < slotSizes; i++) {
            Slab slab1 = new ByteArraySlab(cacheConfig.getSlotSizes()[i], cacheConfig.getSlabSize());
            Slab slab2 = new ByteArraySlab(cacheConfig.getSlotSizes()[i], cacheConfig.getSlabSize());
            Slab slab3 = new ByteArraySlab(cacheConfig.getSlotSizes()[i], cacheConfig.getSlabSize());
            assertFalse(slabPool.casSlabByIdx(i, slab2, slab1));
            assertTrue(slabPool.casSlabByIdx(i, null, slab1));
            assertTrue(slabPool.casSlabByIdx(i, slab1, slab3));
            assertFalse(slabPool.casSlabByIdx(i, slab2, slab3));
        }
    }

    @Test
    public void testgetSlabByIdx() throws IOException, ConfigurationException, SlotBiggerThanSlabException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        SlabPool slabPool = new SlabPool(cacheConfig);
        int tempSlotSize = 234;
        int tempSlabSize = 1234;
        Slab slab = new ByteArraySlab(tempSlotSize, tempSlabSize);
        assertEquals(null, slabPool.getSlabByIdx(0));
        slabPool.casSlabByIdx(0, null, slab);
        Slab slabGot = slabPool.getSlabByIdx(0);
        assertEquals(tempSlotSize, slabGot.getSlotsize());
        assertEquals(tempSlabSize, slabGot.getSlabSize());
        assertEquals(slabGot, slabPool.getSlabByIdx(0));
        assertEquals(null, slabPool.getSlabByIdx(1));
    }

    @Test
    public void testAllocate() throws IOException, ConfigurationException, DataTooBiglException, TooManySlabsException, SlotBiggerThanSlabException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        SlabPool slabPool = new SlabPool(cacheConfig);
        int tempSlotSize = 234;
        int tempSlabSize = 1234;
        Slab slab = slabPool.allocate(2);
        int choosenIdx = slabPool.chooseSlabIdx(2);
        assertEquals(slab, slabPool.getSlabByIdx(choosenIdx));
        slabPool.allocate(2);
        Slab headSlabForIdx = slabPool.getSlabByIdx(choosenIdx);
        boolean in = false;
        while (headSlabForIdx != null){
            if (headSlabForIdx == slab) {
                in = true;
                break;
            } else {
                headSlabForIdx = headSlabForIdx.getNext();
            }
        }
        assertEquals(true, in);
    }


    @Test(expected = TooManySlabsException.class)
    public void testAllocateTooManaySlabs() throws TooManySlabsException, DataTooBiglException, IOException, ConfigurationException, SlotBiggerThanSlabException {
        Config cacheConfig = new Config();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        SlabPool slabPool = new SlabPool(cacheConfig);
        for (int i = 1; i <= cacheConfig.getSlabMaxCount(); i++) {
            slabPool.allocate(2);
        }
        slabPool.allocate(2);
    }
}
