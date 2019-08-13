package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.SlabTooManyException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.exceptions.SlotBiggerThanChunkException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author liubinbin
 */
public class BcacheManagerTest {

    private BcacheManager bcacheManager;

    public BcacheManagerTest() throws ConfigurationException, IOException, SlotBiggerThanChunkException {
        this.bcacheManager = new BcacheManager(new Config());
    }

    @Test
    public void testPut() throws DataTooBiglException, SlabTooManyException {
        byte[] key = {'k', 'e', 'y'};
        byte[] key1 = {'k', 'e', 'y', '1'};
        byte[] value = {'v', 'a', 'l', 'u', 'e'};
        byte[] value1 = {'v', 'a', 'l', 'u', 'e', '1'};

        bcacheManager.put(key, value);
        bcacheManager.put(key1, value1);
        byte[] valueFromCache = bcacheManager.getByByteArray(key);
        assertTrue(Arrays.equals(value, valueFromCache));
    }

    @Test
    public void testDelete() throws DataTooBiglException, SlabTooManyException {
        byte[] key = {'k', 'e', 'y'};
        byte[] key1 = {'k', 'e', 'y', '1'};
        byte[] value = {'v', 'a', 'l', 'u', 'e'};
        byte[] value1 = {'v', 'a', 'l', 'u', 'e', '1'};

        bcacheManager.put(key, value);
        bcacheManager.put(key1, value1);
        bcacheManager.delete(key);
        assertNull(bcacheManager.getByByteArray(key));
        assertNull(bcacheManager.getByByteBuf(key));
        assertNotNull(bcacheManager.getByByteArray(key1));
        assertNotNull(bcacheManager.getByByteBuf(key1));
        assertTrue(Arrays.equals(bcacheManager.getByByteArray(key1), value1));
    }

    @Test
    public void testGet() throws ConfigurationException, IOException, DataTooBiglException, SlabTooManyException, SlotBiggerThanChunkException {
        Config cacheConfig = new Config();
        BcacheManager bcacheManager = new BcacheManager(cacheConfig);
        byte[] key = "abcd".getBytes();
        byte[] key1 = "abc".getBytes();
        byte[] key2 = "abcde".getBytes();

        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
        byte[] CONTENT2 = new byte[60];
        CONTENT2[60 - 1] = '1';

        bcacheManager.put(key, CONTENT);
        bcacheManager.put(key1, CONTENT1);
        bcacheManager.put(key2, CONTENT2);
        bcacheManager.put("key4".getBytes(), CONTENT2);
        assertNotNull(bcacheManager.getByByteBuf(key));
        assertTrue(bcacheManager.checkContainKey(key));
    }


    @Test
    public void testUse() throws DataTooBiglException, SlabTooManyException, IOException, ConfigurationException, SlotBiggerThanChunkException {
        Config cacheConfig = new Config();
        BcacheManager cacheManager = new BcacheManager(cacheConfig);
        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
        byte[] CONTENT2 = new byte[73060];
        CONTENT2[73060 - 1] = '1';
        cacheManager.put("key".getBytes(), CONTENT);
        cacheManager.put("key1".getBytes(), CONTENT1);
        cacheManager.put("key2".getBytes(), CONTENT2);
        cacheManager.getByByteBuf("abc".getBytes());
    }
}
