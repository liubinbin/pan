package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.ChunkIsFullException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
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
public class ByteArrayLinkedChunkTest {

    @Test
    public void testPut() throws ChunkIsFullException, DataTooBiglException {
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(1024, 1024 * 1024);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        int offset = -1;
        offset = byteArrayChunk.put(key, value);
        Item item = byteArrayChunk.readFrom(0);
        System.out.println("item: " + item.toString());
        assertEquals(1, item.getStatus());
        assertTrue(Arrays.equals(key, item.getKey()));
        assertTrue(Arrays.equals(value, item.getValue()));
        assertEquals(key.length, item.getKeyLength());
        assertEquals(value.length, item.getValueLength());

        assertEquals(1, byteArrayChunk.getStatus(offset));
        assertTrue(Arrays.equals(key, byteArrayChunk.getKey(offset)));
        assertTrue(Arrays.equals(value, byteArrayChunk.getValue(offset)));
        assertEquals(key.length, byteArrayChunk.getKeyLength(offset));
        assertEquals(value.length, byteArrayChunk.getValueLength(offset));
    }

    @Test
    public void testSeekAndWriteStatus() {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(slotSize, chunkSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        int seekOffset = byteArrayChunk.seekAndWriteStatus();
        assertEquals(0, seekOffset);
        seekOffset = byteArrayChunk.seekAndWriteStatus();
        assertEquals(slotSize, seekOffset);

        for (int i = 0; i < chunkSize / slotSize - 2; i++) {
            seekOffset = byteArrayChunk.seekAndWriteStatus();
            assertEquals(slotSize * (i + 2), seekOffset);
        }

        seekOffset = byteArrayChunk.seekAndWriteStatus();
        assertEquals(-1, seekOffset);

        seekOffset = byteArrayChunk.seekAndWriteStatus();
        assertEquals(-1, seekOffset);
    }

    @Test
    public void testGetByByteArray() throws ChunkIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }

        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertTrue(Arrays.equals((valuePrefixStr + i).getBytes(), byteArrayChunk.getByByteArray((keyPrefixStr + i).getBytes())));
        }
    }

    @Test
    public void testgetByByteBuf() throws ChunkIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }

        for (int i = 0; i < chunkSize / slotSize; i++) {
            // TODO
            byte[] valeInByteArray = (valuePrefixStr + i).getBytes();
            ByteBuf valueInByteBuf = Unpooled.wrappedBuffer(valeInByteArray);
            assertEquals(0, valueInByteBuf.compareTo( byteArrayChunk.getByByteBuf((keyPrefixStr + i).getBytes()) ));
        }
    }


    @Test
    public void testContainKey() throws ChunkIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(slotSize, chunkSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        byteArrayChunk.put(key, value);
        assertTrue(byteArrayChunk.containKey(key));
        assertFalse(byteArrayChunk.containKey(value));
    }

    @Test
    public void testGetAllKeys() throws ChunkIsFullException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayLinkedChunk byteArrayLinkedChunk = new ByteArrayLinkedChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayLinkedChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
            byteArrayLinkedChunk.scanAndPrintAllKeys();
        }
        ArrayList<Key> allKeys = byteArrayLinkedChunk.getAllKeys();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertTrue(allKeys.contains(new Key((keyPrefixStr + i).getBytes())));
        }
        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertFalse(allKeys.contains(new Key((keyPrefixStr + "abc" + i).getBytes())));
        }
    }

    @Test
    public void testGetdataTotalSize() throws ChunkIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(slotSize, chunkSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        byteArrayChunk.put(key, value);
        assertEquals(byteArrayChunk.getSlotsize(), byteArrayChunk.getdataTotalSize());
    }

    @Test
    public void testDelete() throws ChunkIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        for (int i = 0; i < chunkSize / slotSize; i++) {
            assertTrue(byteArrayChunk.containKey((keyPrefixStr + i).getBytes()));
        }
        int indexDeleted = 10;
        int totalSize = byteArrayChunk.getdataTotalSize() - byteArrayChunk.getSlotsize();
        byteArrayChunk.delete((keyPrefixStr + indexDeleted).getBytes());
        assertEquals(byteArrayChunk.getdataTotalSize(), totalSize);
        for (int i = 0; i < chunkSize / slotSize; i++) {
            if (i == indexDeleted) {
                assertFalse(byteArrayChunk.containKey((keyPrefixStr + i).getBytes()));
            } else {
                assertTrue(byteArrayChunk.containKey((keyPrefixStr + i).getBytes()));
            }
        }
    }

    @Test
    public void testTime() throws ChunkIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int chunkSize = 1024 * 1024;
        ByteArrayChunk byteArrayChunk = new ByteArrayChunk(slotSize, chunkSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        System.out.println("testTime put " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayChunk.getByByteArray((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime get " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayChunk.containKey((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime containKey " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < chunkSize / slotSize; i++) {
            byteArrayChunk.delete((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime delete " + (System.currentTimeMillis() - startTime));
    }
}
