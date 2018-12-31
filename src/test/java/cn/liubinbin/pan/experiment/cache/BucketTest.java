package test.java.cn.liubinbin.pan.experiment.cache;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import io.netty.buffer.Unpooled;
import main.java.cn.liubinbin.pan.experiment.cache.Bucket;

/**
 *
 * @author liubinbin
 *
 */
public class BucketTest {

	@Test
	public void testBytesByByteArray() {
		Bucket bucket = new Bucket(128, 16384);
		byte[] data1 = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
		int data1Offset = bucket.put(data1);
		byte[] data2 = { 's', 'p', 'a', 'k', 'h', 'a' };
		int data2Offset = bucket.put(data2);
		assertEquals(data1.length + data2.length, bucket.getWriteIdx());
		assertTrue(Arrays.equals(data1, bucket.getByByteArray(data1Offset, data1.length)));
		assertTrue(Arrays.equals(data2, bucket.getByByteArray(data2Offset, data2.length)));
		assertFalse(Arrays.equals(data1, bucket.getByByteArray(data2Offset, data2.length)));
	}

	@Test
	public void testBytesByByteBuf() {
		Bucket bucket = new Bucket(128, 16384);
		byte[] data1 = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
		int data1Offset = bucket.put(data1);
		byte[] data2 = { 's', 'p', 'a', 'k', 'h', 'a' };
		int data2Offset = bucket.put(data2);
		assertEquals(data1.length + data2.length, bucket.getWriteIdx());
		assertEquals(bucket.getByByteBuf(data1Offset, data1.length).compareTo(Unpooled.wrappedBuffer(data1)), 0);
		assertEquals(bucket.getByByteBuf(data2Offset, data2.length).compareTo(Unpooled.wrappedBuffer(data2)), 0);
		assertNotEquals(bucket.getByByteBuf(data1Offset, data1.length).compareTo(Unpooled.wrappedBuffer(data2)), 0);
	}

	@Test
	public void testNull() {
		Bucket bucket = new Bucket(128, 16384);
		
	}
}
