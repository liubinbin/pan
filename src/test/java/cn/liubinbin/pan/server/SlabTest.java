package test.java.cn.liubinbin.pan.server;

import cn.liubinbin.pan.bcache.ByteArraySlab;
import cn.liubinbin.pan.bcache.Slab;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author liubinbin
 */
public class SlabTest {

//    @Test
//    public void testBytesByByteArray() {
//        Slab slab = new ByteArraySlab(128, 16384);
//        byte[] data1 = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
//        int data1Offset = slab.put(data1);
//        byte[] data2 = {'s', 'p', 'a', 'k', 'h', 'a'};
//        int data2Offset = slab.put(data2);
//        assertEquals(data1.length + data2.length, slab.getWriteIdx());
//        assertTrue(Arrays.equals(data1, slab.getByByteArray(data1Offset, data1.length)));
//        assertTrue(Arrays.equals(data2, slab.getByByteArray(data2Offset, data2.length)));
//        assertFalse(Arrays.equals(data1, slab.getByByteArray(data2Offset, data2.length)));
//    }

//    @Test
//    public void testBytesByByteBuf() {
//        Slab slab = new ByteArraySlab(128, 16384);
//        byte[] data1 = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
//        int data1Offset = slab.put(data1);
//        byte[] data2 = {'s', 'p', 'a', 'k', 'h', 'a'};
//        int data2Offset = slab.put(data2);
//        assertEquals(data1.length + data2.length, slab.getWriteIdx());
//        assertEquals(slab.getByByteBuf(data1Offset, data1.length).compareTo(Unpooled.wrappedBuffer(data1)), 0);
//        assertEquals(slab.getByByteBuf(data2Offset, data2.length).compareTo(Unpooled.wrappedBuffer(data2)), 0);
//        assertNotEquals(slab.getByByteBuf(data1Offset, data1.length).compareTo(Unpooled.wrappedBuffer(data2)), 0);
//    }

//    @Test
//    public void testNull() {
//        Slab chunk = new ByteArraySlab(128, 16384);
//
//    }
}
