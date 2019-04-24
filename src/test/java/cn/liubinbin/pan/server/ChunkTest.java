package test.java.cn.liubinbin.pan.server;

import cn.liubinbin.pan.oldcache.Chunk;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author liubinbin
 */
public class ChunkTest {

    @Test
    public void testBytesByByteArray() {
        Chunk chunk = new Chunk(128, 16384);
        byte[] data1 = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        int data1Offset = chunk.put(data1);
        byte[] data2 = {'s', 'p', 'a', 'k', 'h', 'a'};
        int data2Offset = chunk.put(data2);
        assertEquals(data1.length + data2.length, chunk.getWriteIdx());
        assertTrue(Arrays.equals(data1, chunk.getByByteArray(data1Offset, data1.length)));
        assertTrue(Arrays.equals(data2, chunk.getByByteArray(data2Offset, data2.length)));
        assertFalse(Arrays.equals(data1, chunk.getByByteArray(data2Offset, data2.length)));
    }

    @Test
    public void testBytesByByteBuf() {
        Chunk chunk = new Chunk(128, 16384);
        byte[] data1 = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        int data1Offset = chunk.put(data1);
        byte[] data2 = {'s', 'p', 'a', 'k', 'h', 'a'};
        int data2Offset = chunk.put(data2);
        assertEquals(data1.length + data2.length, chunk.getWriteIdx());
        assertEquals(chunk.getByByteBuf(data1Offset, data1.length).compareTo(Unpooled.wrappedBuffer(data1)), 0);
        assertEquals(chunk.getByByteBuf(data2Offset, data2.length).compareTo(Unpooled.wrappedBuffer(data2)), 0);
        assertNotEquals(chunk.getByByteBuf(data1Offset, data1.length).compareTo(Unpooled.wrappedBuffer(data2)), 0);
    }

    @Test
    public void testNull() {
        Chunk chunk = new Chunk(128, 16384);

    }
}
