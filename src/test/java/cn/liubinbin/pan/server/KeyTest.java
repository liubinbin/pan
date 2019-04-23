package test.java.cn.liubinbin.pan.server;

import cn.liubinbin.pan.server.Key;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author liubinbin
 */
public class KeyTest {

    @Test
    public void testHashCode() {
        byte[] keyByteArray = {'k', 'e', 'y'};
        byte[] key1ByteArray = {'k', 'e', 'y', '1'};
        Key key1 = new Key(keyByteArray);
        Key key2 = new Key(keyByteArray);
        Key key3 = new Key(key1ByteArray);
        assertEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1.hashCode(), key3.hashCode());
    }

    @Test
    public void testEquals() {
        byte[] keyByteArray = {'k', 'e', 'y'};
        byte[] key1ByteArray = {'k', 'e', 'y', '1'};
        Key key1 = new Key(keyByteArray);
        Key key2 = new Key(keyByteArray);
        Key key3 = new Key(key1ByteArray);
        assertTrue(key1.equals(key2));
        assertFalse(key1.equals(key3));
    }

    @Test
    public void testCompare() {
        Key abc = new Key("abc".getBytes());
        Key abcd = new Key("abcd".getBytes());
        Key abce = new Key("abce".getBytes());
        Key abcde = new Key("abcde".getBytes());
        assertTrue(abc.compareTo(abcd) < 0);
        assertTrue(abcd.compareTo(abce) < 0);
        assertTrue(abcd.compareTo(abcde) < 0);
        assertTrue(abc.compareTo(abcde) < 0);

        assertTrue(abcd.compareTo(abc) > 0);
        assertTrue(abce.compareTo(abcd) > 0);
        assertTrue(abcde.compareTo(abcd) > 0);
        assertTrue(abcde.compareTo(abc) > 0);
    }
}
