package cn.liubinbin.pan.conf;

import cn.liubinbin.pan.exceptions.SlotBiggerThanChunkException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author liubinbin
 */
public class ConfigTest {

    private Config cacheConfig = null;

    public ConfigTest() {
        try {
            this.cacheConfig = new Config();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChunkSlotSize() throws SlotBiggerThanChunkException {
        int[] slotSizes = cacheConfig.getSlotSizes();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        assertEquals(slotSizes[0], 5120);
        assertEquals(slotSizes[1], 9216);
        assertEquals(slotSizes[2], 17408);
        assertEquals(slotSizes[3], 41964);
        assertEquals(slotSizes[4], 50176);
        assertEquals(slotSizes[5], 58368);
        assertEquals(slotSizes[6], 66560);
        assertEquals(slotSizes[7], 132096);
        assertEquals(slotSizes[8], 263168);
        assertEquals(slotSizes[9], 525312);
        assertEquals(slotSizes[10], 1049600);
        assertEquals(slotSizes[11], 4195328);
        assertEquals(slotSizes[12], 16778240);
    }
}
