package cn.liubinbin.pan.server;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.SlotBiggerThanChunkException;
import cn.liubinbin.pan.oldcache.ChunkManager;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author liubinbin
 */
public class ManagerTest {

    private ChunkManager chunkManager;

    public ManagerTest() throws ConfigurationException, IOException, SlotBiggerThanChunkException {
        this.chunkManager = new ChunkManager(new Config());
    }

    @Test
    public void testPut() {
        byte[] key = {'k', 'e', 'y'};
        byte[] key1 = {'k', 'e', 'y', '1'};
        byte[] value = {'v', 'a', 'l', 'u', 'e'};
        byte[] value1 = {'v', 'a', 'l', 'u', 'e', '1'};

        chunkManager.put(key, value);
        chunkManager.put(key1, value1);
        byte[] valueFromCache = chunkManager.getByByteArray(key);
        assertTrue(Arrays.equals(value, valueFromCache));
    }

    @Test
    public void testDelete() {
        byte[] key = {'k', 'e', 'y'};
        byte[] key1 = {'k', 'e', 'y', '1'};
        byte[] value = {'v', 'a', 'l', 'u', 'e'};
        byte[] value1 = {'v', 'a', 'l', 'u', 'e', '1'};

        chunkManager.put(key, value);
        chunkManager.put(key1, value1);
        chunkManager.delete(key);
        assertNull(chunkManager.getByByteArray(key));
        assertNull(chunkManager.getByByteBuf(key));
        assertNotNull(chunkManager.getByByteArray(key1));
        assertNotNull(chunkManager.getByByteBuf(key1));
    }

    @Test
    public void testGet() throws ConfigurationException, IOException, SlotBiggerThanChunkException {
        Config cacheConfig = new Config();
        ChunkManager cacheManager = new ChunkManager(cacheConfig);
        byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] CONTENT1 = {'j', 'a', 'v', 'a', 'i', 's', 'g', 'r', 'e', 'a', 't'};
        byte[] CONTENT2 = new byte[73060];
        CONTENT2[73060 - 1] = '1';
        cacheManager.put("abcd".getBytes(), CONTENT);
        cacheManager.put("abc".getBytes(), CONTENT1);
        cacheManager.put("abcde".getBytes(), CONTENT2);
        assertNotNull(cacheManager.getByByteBuf("abcd".getBytes()));
        assertTrue(cacheManager.checkContainKey());
    }
}
