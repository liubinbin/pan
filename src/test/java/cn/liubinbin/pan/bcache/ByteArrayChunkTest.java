package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.BucketIsFullException;
import cn.liubinbin.pan.module.Item;
import cn.liubinbin.pan.module.Key;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by bin on 2019/4/19.
 */
public class ByteArrayChunkTest {

    @Test
    public void testPut() throws BucketIsFullException {
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(1024, 1024 * 1024);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        int offset = -1;
        offset = byteArrayBucket.put(key, value);
        Item item = byteArrayBucket.readFrom(0);
        System.out.println("item: " + item.toString());
        assertEquals(1, item.getStatus());
        assertTrue(Arrays.equals(key, item.getKey()));
        assertTrue(Arrays.equals(value, item.getValue()));
        assertEquals(key.length, item.getKeyLength());
        assertEquals(value.length, item.getValueLength());

        assertEquals(1, byteArrayBucket.getStatus(offset));
        assertTrue(Arrays.equals(key, byteArrayBucket.getKey(offset)));
        assertTrue(Arrays.equals(value, byteArrayBucket.getValue(offset)));
        assertEquals(key.length, byteArrayBucket.getKeyLength(offset));
        assertEquals(value.length, byteArrayBucket.getValueLength(offset));
    }

    @Test
    public void testSeekAndWriteStatus() {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        int seekOffset = byteArrayBucket.seekAndWriteStatus();
        assertEquals(0, seekOffset);
        seekOffset = byteArrayBucket.seekAndWriteStatus();
        assertEquals(slotSize, seekOffset);

        for (int i = 0; i < chunkSize / slotSize - 2; i++) {
            seekOffset = byteArrayBucket.seekAndWriteStatus();
            assertEquals(slotSize * (i + 2), seekOffset);
        }

        seekOffset = byteArrayBucket.seekAndWriteStatus();
        assertEquals(-1, seekOffset);

        seekOffset = byteArrayBucket.seekAndWriteStatus();
        assertEquals(-1, seekOffset);
    }

    @Test
    public void testGetByByteArray() throws BucketIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }

        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertTrue(Arrays.equals((valuePrefixStr + i).getBytes(), byteArrayBucket.getByByteArray((keyPrefixStr + i).getBytes())));
        }
    }

    @Test
    public void testgetByByteBuf() throws BucketIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }

        for (int i = 0; i < chunkSize / slotSize; i++) {
            // TODO
            byte[] valeInByteArray = (valuePrefixStr + i).getBytes();
            ByteBuf valueInByteBuf = Unpooled.wrappedBuffer(valeInByteArray);
            assertEquals(0, valueInByteBuf.compareTo( byteArrayBucket.getByByteBuf((keyPrefixStr + i).getBytes()) ));
        }
    }


    @Test
    public void testContainKey() throws BucketIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        byteArrayBucket.put(key, value);
        assertTrue(byteArrayBucket.containKey(key));
        assertFalse(byteArrayBucket.containKey(value));
    }

    @Test
    public void testGetAllKeys() throws BucketIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        ArrayList<Key> allKeys = byteArrayBucket.getAllKeys();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertTrue(allKeys.contains(new Key((keyPrefixStr + i).getBytes())));
        }
        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertFalse(allKeys.contains(new Key((keyPrefixStr + "abc" + i).getBytes())));
        }
    }

    @Test
    public void testGetdataTotalSize() throws BucketIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        byteArrayBucket.put(key, value);
        assertEquals(byteArrayBucket.getSlotsize(), byteArrayBucket.getdataTotalSize());
    }

    @Test
    public void testDelete() throws BucketIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertTrue(byteArrayBucket.containKey((keyPrefixStr + i).getBytes()));
        }
        int indexDeleted = 10;
        int totalSize = byteArrayBucket.getdataTotalSize() - byteArrayBucket.getSlotsize();
        byteArrayBucket.delete((keyPrefixStr + indexDeleted).getBytes());
        assertEquals(byteArrayBucket.getdataTotalSize(), totalSize);
        for (int i = 0; i < chunkSize / slotSize; i++) {
            if (i == indexDeleted) {
                assertFalse(byteArrayBucket.containKey((keyPrefixStr + i).getBytes()));
            } else {
                assertTrue(byteArrayBucket.containKey((keyPrefixStr + i).getBytes()));
            }
        }
    }

    @Test
    public void testTime() throws BucketIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayBucket = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        System.out.println("testTime put " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.getByByteArray((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime get " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.containKey((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime containKey " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayBucket.delete((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime delete " + (System.currentTimeMillis() - startTime));
    }


}
