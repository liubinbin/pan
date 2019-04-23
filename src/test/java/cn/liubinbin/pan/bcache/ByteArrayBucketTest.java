package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.BucketIsFullException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by bin on 2019/4/19.
 */
public class ByteArrayBucketTest {

    @Test
    public void testPut() throws BucketIsFullException {
        ByteArrayBucket byteArrayBucket = new ByteArrayBucket(1024, 1024 * 1024);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        byteArrayBucket.put(key, value);
        Item item = byteArrayBucket.readFrom(0);
        System.out.println("item: " + item.toString());
        assertEquals(1, item.getStatus());
        assertTrue(Arrays.equals(key, item.getKey()));
        assertTrue(Arrays.equals(value, item.getValue()));
        assertEquals(key.length, item.getKeyLength());
        assertEquals(value.length, item.getValueLength());
    }

    @Test
    public void testseekAndWriteStatus() {
        int slotSize = 1024;
        int segmentSize = 1024 * 1024;
        ByteArrayBucket byteArrayBucket = new ByteArrayBucket(slotSize, segmentSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        int seekOffset = byteArrayBucket.seekAndWriteStatus();
        assertEquals(0, seekOffset);
        System.out.println("seekOffset " + seekOffset);
        seekOffset = byteArrayBucket.seekAndWriteStatus();
        System.out.println("seekOffset " + seekOffset);
        assertEquals(slotSize, seekOffset);

        for (int i = 0; i < segmentSize / slotSize - 2; i++) {
            seekOffset = byteArrayBucket.seekAndWriteStatus();
            System.out.println("seekOffset " + seekOffset);
            assertEquals(slotSize * (i + 2), seekOffset);
        }

        seekOffset = byteArrayBucket.seekAndWriteStatus();
        System.out.println("seekOffset " + seekOffset);
        assertEquals(-1, seekOffset);

        seekOffset = byteArrayBucket.seekAndWriteStatus();
        System.out.println("seekOffset " + seekOffset);
        assertEquals(-1, seekOffset);
    }
}
