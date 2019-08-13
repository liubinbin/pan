package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.exceptions.SlabIsFullException;
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
public class ByteArraySlabTest {

    @Test
    public void testPut() throws SlabIsFullException, DataTooBiglException {
        ByteArraySlab byteArraySlab = new ByteArraySlab(1024, 1024 * 1024);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        int offset = -1;
        offset = byteArraySlab.put(key, value);
        Item item = byteArraySlab.readFrom(0);
        assertEquals(1, item.getStatus());
        assertTrue(Arrays.equals(key, item.getKey()));
        assertTrue(Arrays.equals(value, item.getValue()));
        assertEquals(key.length, item.getKeyLength());
        assertEquals(value.length, item.getValueLength());

        assertEquals(1, byteArraySlab.getStatus(offset));
        assertTrue(Arrays.equals(key, byteArraySlab.getKey(offset)));
        assertTrue(Arrays.equals(value, byteArraySlab.getValue(offset)));
        assertEquals(key.length, byteArraySlab.getKeyLength(offset));
        assertEquals(value.length, byteArraySlab.getValueLength(offset));
    }

    @Test
    public void testSeekAndWriteStatus() {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        int seekOffset = byteArraySlab.seekAndWriteStatus();
        assertEquals(0, seekOffset);
        seekOffset = byteArraySlab.seekAndWriteStatus();
        assertEquals(slotSize, seekOffset);

        for (int i = 0; i < slabSize / slotSize - 2; i++) {
            seekOffset = byteArraySlab.seekAndWriteStatus();
            assertEquals(slotSize * (i + 2), seekOffset);
        }

        seekOffset = byteArraySlab.seekAndWriteStatus();
        assertEquals(-1, seekOffset);

        seekOffset = byteArraySlab.seekAndWriteStatus();
        assertEquals(-1, seekOffset);
    }

    @Test
    public void testGetByByteArray() throws SlabIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }

        for (int i = 0; i < slabSize / slotSize; i++) {
            assertTrue(Arrays.equals((valuePrefixStr + i).getBytes(), byteArraySlab.getByByteArray((keyPrefixStr + i).getBytes())));
        }
    }

    @Test
    public void testgetByByteBuf() throws SlabIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }

        for (int i = 0; i < slabSize / slotSize; i++) {
            // TODO
            byte[] valeInByteArray = (valuePrefixStr + i).getBytes();
            ByteBuf valueInByteBuf = Unpooled.wrappedBuffer(valeInByteArray);
            assertEquals(0, valueInByteBuf.compareTo( byteArraySlab.getByByteBuf((keyPrefixStr + i).getBytes()) ));
        }
    }


    @Test
    public void testContainKey() throws SlabIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        byteArraySlab.put(key, value);
        assertTrue(byteArraySlab.containKey(key));
        assertFalse(byteArraySlab.containKey(value));
    }

    @Test
    public void testGetAllKeys() throws SlabIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";
        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        ArrayList<Key> allKeys = byteArraySlab.getAllKeys();
        for (int i = 0; i < slabSize / slotSize; i++) {
            assertTrue(allKeys.contains(new Key((keyPrefixStr + i).getBytes())));
        }
        for (int i = 0; i < slabSize / slotSize; i++) {
            assertFalse(allKeys.contains(new Key((keyPrefixStr + "abc" + i).getBytes())));
        }
    }

    @Test
    public void testGetdataTotalSize() throws SlabIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        byte[] key = "hellokey".getBytes();
        byte[] value = "hellovalue".getBytes();
        byteArraySlab.put(key, value);
        assertEquals(byteArraySlab.getSlotsize(), byteArraySlab.getdataTotalSize());
    }

    @Test
    public void testDelete() throws SlabIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";
        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        for (int i = 0; i < slabSize / slotSize; i++) {
            assertTrue(byteArraySlab.containKey((keyPrefixStr + i).getBytes()));
        }
        int indexDeleted = 10;
        int totalSize = byteArraySlab.getdataTotalSize() - byteArraySlab.getSlotsize();
        byteArraySlab.delete((keyPrefixStr + indexDeleted).getBytes());
        assertEquals(byteArraySlab.getdataTotalSize(), totalSize);
        for (int i = 0; i < slabSize / slotSize; i++) {
            if (i == indexDeleted) {
                assertFalse(byteArraySlab.containKey((keyPrefixStr + i).getBytes()));
            } else {
                assertTrue(byteArraySlab.containKey((keyPrefixStr + i).getBytes()));
            }
        }
    }

    @Test
    public void testTime() throws SlabIsFullException, DataTooBiglException {
        int slotSize = 1024;
        int slabSize = 1024 * 1024;
        ByteArraySlab byteArraySlab = new ByteArraySlab(slotSize, slabSize);
        String keyPrefixStr = "hellokey";
        String valuePrefixStr = "hellovalue";

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
        }
        System.out.println("testTime put " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.getByByteArray((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime get " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.containKey((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime containKey " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();
        for (int i = 0; i < slabSize / slotSize; i++) {
            byteArraySlab.delete((keyPrefixStr + i).getBytes());
        }
        System.out.println("testTime delete " + (System.currentTimeMillis() - startTime));
    }
}
