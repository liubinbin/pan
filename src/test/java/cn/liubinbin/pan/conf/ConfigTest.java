package test.java.cn.liubinbin.pan.conf;

import cn.liubinbin.pan.conf.Config;
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
    public void testBucketSlotSize() {
        int[] bucketSlotSize = cacheConfig.getBucketSlotSize();
        // 5120,9216,17408,41964,50176,58368,66560,132096,263168,525312,1049600,4195328,16778240
        assertEquals(bucketSlotSize[0], 5120);
        assertEquals(bucketSlotSize[1], 9216);
        assertEquals(bucketSlotSize[2], 17408);
        assertEquals(bucketSlotSize[3], 41964);
        assertEquals(bucketSlotSize[4], 50176);
        assertEquals(bucketSlotSize[5], 58368);
        assertEquals(bucketSlotSize[6], 66560);
        assertEquals(bucketSlotSize[7], 132096);
        assertEquals(bucketSlotSize[8], 263168);
        assertEquals(bucketSlotSize[9], 525312);
        assertEquals(bucketSlotSize[10], 1049600);
        assertEquals(bucketSlotSize[11], 4195328);
        assertEquals(bucketSlotSize[12], 16778240);
    }
}
