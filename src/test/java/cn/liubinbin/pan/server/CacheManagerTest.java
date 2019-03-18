package test.java.cn.liubinbin.pan.server;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import main.java.cn.liubinbin.pan.conf.Config;
import main.java.cn.liubinbin.pan.manager.CacheManager;

/**
 *
 * @author liubinbin
 *
 */
public class CacheManagerTest {

	private CacheManager cacheManager;
	public CacheManagerTest() throws FileNotFoundException, ConfigurationException, IOException {
		this.cacheManager = new CacheManager(new Config());
	}
	
	@Test
	public void testput(){
		byte[] key = { 'k', 'e', 'y'};
		byte[] key1 = { 'k', 'e', 'y', '1'};
		byte[] value = { 'v', 'a', 'l', 'u', 'e' };
		byte[] value1 = { 'v', 'a', 'l', 'u', 'e', '1' };
		
		cacheManager.put(key, value);
		cacheManager.put(key1, value1);
		byte[] valueFromCache = cacheManager.getByByteArray(key);
		assertTrue(Arrays.equals(value, valueFromCache));
	}
	
	public void testdelete() {
		byte[] key = { 'k', 'e', 'y'};
		byte[] key1 = { 'k', 'e', 'y', '1'};
		byte[] value = { 'v', 'a', 'l', 'u', 'e' };
		byte[] value1 = { 'v', 'a', 'l', 'u', 'e', '1' };
		
		cacheManager.put(key, value);
		cacheManager.put(key1, value1);
		cacheManager.delete(key);
		assertNull(cacheManager.getByByteArray(key));
		assertNull(cacheManager.getByByteBuf(key));
		assertNotNull(cacheManager.getByByteArray(key1));
		assertNotNull(cacheManager.getByByteBuf(key1));
	}
	
	public void testget() throws FileNotFoundException, ConfigurationException, IOException{
		Config cacheConfig = new Config();
		CacheManager cacheManager = new CacheManager(cacheConfig);
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
