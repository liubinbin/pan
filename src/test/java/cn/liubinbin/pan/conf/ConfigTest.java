package cn.liubinbin.pan.conf;

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
    public void testChunkSlotSize() {
        int[] chunkSlotSize = cacheConfig.getChunkSlotSize();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        assertEquals(chunkSlotSize[0], 5120);
        assertEquals(chunkSlotSize[1], 9216);
        assertEquals(chunkSlotSize[2], 17408);
        assertEquals(chunkSlotSize[3], 41964);
        assertEquals(chunkSlotSize[4], 50176);
        assertEquals(chunkSlotSize[5], 58368);
        assertEquals(chunkSlotSize[6], 66560);
        assertEquals(chunkSlotSize[7], 132096);
        assertEquals(chunkSlotSize[8], 263168);
        assertEquals(chunkSlotSize[9], 525312);
        assertEquals(chunkSlotSize[10], 1049600);
        assertEquals(chunkSlotSize[11], 4195328);
        assertEquals(chunkSlotSize[12], 16778240);
    }
}
