package test.java.cn.liubinbin.pan.server;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.manager.BucketManager;

/**
 *
 * @author liubinbin
 *
 */
public class ManagerTest {

	private BucketManager bucketManager;
	public ManagerTest() throws FileNotFoundException, ConfigurationException, IOException {
		this.bucketManager = new BucketManager(new Config());
	}
	
	@Test
	public void testput(){
		byte[] key = { 'k', 'e', 'y'};
		byte[] key1 = { 'k', 'e', 'y', '1'};
		byte[] value = { 'v', 'a', 'l', 'u', 'e' };
		byte[] value1 = { 'v', 'a', 'l', 'u', 'e', '1' };
		
		bucketManager.put(key, value);
		bucketManager.put(key1, value1);
		byte[] valueFromCache = bucketManager.getByByteArray(key);
		assertTrue(Arrays.equals(value, valueFromCache));
	}
	
	public void testdelete() {
		byte[] key = { 'k', 'e', 'y'};
		byte[] key1 = { 'k', 'e', 'y', '1'};
		byte[] value = { 'v', 'a', 'l', 'u', 'e' };
		byte[] value1 = { 'v', 'a', 'l', 'u', 'e', '1' };
		
		bucketManager.put(key, value);
		bucketManager.put(key1, value1);
		bucketManager.delete(key);
		assertNull(bucketManager.getByByteArray(key));
		assertNull(bucketManager.getByByteBuf(key));
		assertNotNull(bucketManager.getByByteArray(key1));
		assertNotNull(bucketManager.getByByteBuf(key1));
	}
	
	public void testget() throws FileNotFoundException, ConfigurationException, IOException{
		Config cacheConfig = new Config();
		BucketManager cacheManager = new BucketManager(cacheConfig);
		byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
		byte[] CONTENT1 = { 'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't' };
		byte[] CONTENT2 = new byte[73060];
		CONTENT2[73060 - 1 ] = '1';
		cacheManager.put("abcd".getBytes(), CONTENT);
		cacheManager.put("abc".getBytes(), CONTENT1);
		cacheManager.put("abcde".getBytes(), CONTENT2);
		assertNotNull(cacheManager.getByByteBuf("abcd".getBytes()));
		assertTrue(cacheManager.checkContainKey());
	}
}
