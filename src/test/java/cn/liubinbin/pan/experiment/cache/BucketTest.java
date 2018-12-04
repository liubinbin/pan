package test.java.cn.liubinbin.pan.experiment.cache;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import main.java.cn.liubinbin.pan.experiment.cache.Bucket;

/**
 *
 * @author liubinbin
 *
 */
public class BucketTest {

	@Test
	public void testBytes() {
		Bucket bucket = new Bucket(128, 16384);
		byte[] data1 =  { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
		int data1Offset = bucket.put(data1);
		byte[] data2 =  { 's', 'p', 'a', 'k', 'h', 'a'};
		int data2Offset = bucket.put(data2);
		assertEquals(data1.length + data2.length, bucket.getWriteIdx());
		assertEquals(Arrays.equals(data1, bucket.get(data1Offset, data1.length)), true);
		assertEquals(Arrays.equals(data2, bucket.get(data2Offset, data2.length)), true);
		assertEquals(Arrays.equals(data1, bucket.get(data2Offset, data2.length)), false);
	}

	
	@Test
	public void testNull() {
		Bucket bucket = new Bucket(128, 16384);
		
	}
}
