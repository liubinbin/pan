package cn.liubinbin.pan.server;

import cn.liubinbin.pan.bcache.BcacheManager;
import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.DataTooBiglException;
import cn.liubinbin.pan.exceptions.TooManySlabsException;
import cn.liubinbin.pan.exceptions.SlotBiggerThanSlabException;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author liubinbin
 */
public class ManagerTest {

    private BcacheManager bcacheManager;

    public ManagerTest() throws ConfigurationException, IOException, SlotBiggerThanSlabException {
        this.bcacheManager = new BcacheManager(new Config());
    }

    @Test
    public void testPut() throws TooManySlabsException, DataTooBiglException {
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
    public void testDelete() throws TooManySlabsException, DataTooBiglException {
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
    }

    @Test
    public void testGet() throws ConfigurationException, IOException, SlotBiggerThanSlabException, TooManySlabsException, DataTooBiglException {
        Config cacheConfig = new Config();
        BcacheManager bcacheManager = new BcacheManager(cacheConfig);
        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
        byte[] CONTENT2 = new byte[73060];
        CONTENT2[73060 - 1] = '1';
        bcacheManager.put("abcd".getBytes(), CONTENT);
        bcacheManager.put("abc".getBytes(), CONTENT1);
        bcacheManager.put("abcde".getBytes(), CONTENT2);
        assertNotNull(bcacheManager.getByByteBuf("abcd".getBytes()));
    }
}
