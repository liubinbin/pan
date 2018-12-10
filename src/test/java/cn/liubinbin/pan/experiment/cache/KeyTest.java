package test.java.cn.liubinbin.pan.experiment.cache;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.cn.liubinbin.pan.experiment.cache.Key;

/**
 * 
 * @author liubinbin
 *
 */
public class KeyTest {

	@Test
	public void testHashCode(){
		byte[] keyByteArray = { 'k', 'e', 'y'};
		byte[] key1ByteArray = { 'k', 'e', 'y', '1'};
		Key key1 = new Key(keyByteArray);
		Key key2 = new Key(keyByteArray);
		Key key3 = new Key(key1ByteArray);
		assertEquals(key1.hashCode(), key2.hashCode());
		assertNotEquals(key1.hashCode(), key3.hashCode());
	}
	
	@Test
	public void testEquals() {
		byte[] keyByteArray = { 'k', 'e', 'y'};
		byte[] key1ByteArray = { 'k', 'e', 'y', '1'};
		Key key1 = new Key(keyByteArray);
		Key key2 = new Key(keyByteArray);
		Key key3 = new Key(key1ByteArray);
		assertTrue(key1.equals(key2));
		assertFalse(key1.equals(key3));
	}
}
