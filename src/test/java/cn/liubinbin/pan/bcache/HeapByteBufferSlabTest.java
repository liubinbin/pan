package cn.liubinbin.pan.bcache;

/**
 * Created by bin on 2019/4/19.
 */
public class HeapByteBufferSlabTest {

//    @Test
//    public void testPut() throws ChunkIsFullException {
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(1024, 1024 * 1024);
//        byte[] key = "hellokey".getBytes();
//        byte[] value = "hellovalue".getBytes();
//        int offset = -1;
//        offset = HeapByteBufferChunk.put(key, value);
//        Item item = HeapByteBufferChunk.readFrom(0);
//        System.out.println("item: " + item.toString());
//        assertEquals(1, item.getStatus());
//        assertTrue(Arrays.equals(key, item.getKey()));
//        assertTrue(Arrays.equals(value, item.getValue()));
//        assertEquals(key.length, item.getKeyLength());
//        assertEquals(value.length, item.getValueLength());
//
//        assertEquals(1, heapByteBufferChunk.getStatus(offset));
//        assertTrue(Arrays.equals(key, heapByteBufferChunk.getKey(offset)));
//        assertTrue(Arrays.equals(value, heapByteBufferChunk.getValue(offset)));
//        assertEquals(key.length, heapByteBufferChunk.getKeyLength(offset));
//        assertEquals(value.length, heapByteBufferChunk.getValueLength(offset));
//    }
//
//    @Test
//    public void testSeekAndWriteStatus() {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        byte[] key = "hellokey".getBytes();
//        byte[] value = "hellovalue".getBytes();
//        int seekOffset = heapByteBufferChunk.seekAndWriteStatus();
//        assertEquals(0, seekOffset);
//        seekOffset = heapByteBufferChunk.seekAndWriteStatus();
//        assertEquals(slotSize, seekOffset);
//
//        for (int i = 0; i < chunkSize / slotSize - 2; i++) {
//            seekOffset = heapByteBufferChunk.seekAndWriteStatus();
//            assertEquals(slotSize * (i + 2), seekOffset);
//        }
//
//        seekOffset = heapByteBufferChunk.seekAndWriteStatus();
//        assertEquals(-1, seekOffset);
//
//        seekOffset = heapByteBufferChunk.seekAndWriteStatus();
//        assertEquals(-1, seekOffset);
//    }
//
//    @Test
//    public void testGetByByteArray() throws ChunkIsFullException {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        String keyPrefixStr = "hellokey";
//        String valuePrefixStr = "hellovalue";
//
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
//        }
//
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            assertTrue(Arrays.equals((valuePrefixStr + i).getBytes(), heapByteBufferChunk.getByByteArray((keyPrefixStr + i).getBytes())));
//        }
//    }
//
//    @Test
//    public void testgetByByteBuf() throws ChunkIsFullException {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        String keyPrefixStr = "hellokey";
//        String valuePrefixStr = "hellovalue";
//
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
//        }
//
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            // TODO
//            byte[] valeInByteArray = (valuePrefixStr + i).getBytes();
//            ByteBuf valueInByteBuf = Unpooled.wrappedBuffer(valeInByteArray);
//            assertEquals(0, valueInByteBuf.compareTo( heapByteBufferChunk.getByByteBuf((keyPrefixStr + i).getBytes()) ));
//        }
//    }
//
//
//    @Test
//    public void testContainKey() throws ChunkIsFullException {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        byte[] key = "hellokey".getBytes();
//        byte[] value = "hellovalue".getBytes();
//        heapByteBufferChunk.put(key, value);
//        assertTrue(heapByteBufferChunk.containKey(key));
//        assertFalse(heapByteBufferChunk.containKey(value));
//    }
//
//    @Test
//    public void testGetAllKeys() throws ChunkIsFullException {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        String keyPrefixStr = "hellokey";
//        String valuePrefixStr = "hellovalue";
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
//        }
//        ArrayList<Key> allKeys = heapByteBufferChunk.getAllKeys();
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            assertTrue(allKeys.contains(new Key((keyPrefixStr + i).getBytes())));
//        }
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            assertFalse(allKeys.contains(new Key((keyPrefixStr + "abc" + i).getBytes())));
//        }
//    }
//
//    @Test
//    public void testGetdataTotalSize() throws ChunkIsFullException {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        byte[] key = "hellokey".getBytes();
//        byte[] value = "hellovalue".getBytes();
//        heapByteBufferChunk.put(key, value);
//        assertEquals(heapByteBufferChunk.getSlotsize(), heapByteBufferChunk.getdataTotalSize());
//    }
//
//    @Test
//    public void testDelete() throws ChunkIsFullException {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        String keyPrefixStr = "hellokey";
//        String valuePrefixStr = "hellovalue";
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
//        }
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            assertTrue(heapByteBufferChunk.containKey((keyPrefixStr + i).getBytes()));
//        }
//        int indexDeleted = 10;
//        int totalSize = heapByteBufferChunk.getdataTotalSize() - heapByteBufferChunk.getSlotsize();
//        heapByteBufferChunk.delete((keyPrefixStr + indexDeleted).getBytes());
//        assertEquals(heapByteBufferChunk.getdataTotalSize(), totalSize);
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            if (i == indexDeleted) {
//                assertFalse(heapByteBufferChunk.containKey((keyPrefixStr + i).getBytes()));
//            } else {
//                assertTrue(heapByteBufferChunk.containKey((keyPrefixStr + i).getBytes()));
//            }
//        }
//    }
//
//    @Test
//    public void testTime() throws ChunkIsFullException {
//        int slotSize = 1024;
//        int chunkSize = 1024 * 1024;
//        HeapByteBufferChunk heapByteBufferChunk = new HeapByteBufferChunk(slotSize, chunkSize);
//        String keyPrefixStr = "hellokey";
//        String valuePrefixStr = "hellovalue";
//
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.put((keyPrefixStr + i).getBytes(), (valuePrefixStr + i).getBytes());
//        }
//        System.out.println("testTime put " + (System.currentTimeMillis() - startTime));
//
//        startTime = System.currentTimeMillis();
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.getByByteArray((keyPrefixStr + i).getBytes());
//        }
//        System.out.println("testTime get " + (System.currentTimeMillis() - startTime));
//
//        startTime = System.currentTimeMillis();
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.containKey((keyPrefixStr + i).getBytes());
//        }
//        System.out.println("testTime containKey " + (System.currentTimeMillis() - startTime));
//
//        startTime = System.currentTimeMillis();
//        for (int i = 0; i < chunkSize / slotSize; i++) {
//            heapByteBufferChunk.delete((keyPrefixStr + i).getBytes());
//        }
//        System.out.println("testTime delete " + (System.currentTimeMillis() - startTime));
//    }
}
