package cn.liubinbin.pan.conf;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author liubinbin
 */
public class Config {

    private PropertiesConfiguration configuration = null;

    public Config() throws FileNotFoundException, ConfigurationException, IOException {
        configuration = new PropertiesConfiguration();
        configuration.read(new FileReader("conf/config.properties"));
    }

    public int getPort() {
        return configuration.getInt(Contants.PAN_SERVER_PORT, Contants.DEFAULT_CACHE_SERVER_PORT);
    }

    public int getNettyThreadCount() {
        return configuration.getInt(Contants.PAN_NETTY_SERVER_THREAD_COUNT,
                Contants.DEFAULT_CACHE_NETTY_SERVER_THREAD_COUNT);
    }

    public int[] getChunkSlotSize() {
        int[] chunkSlotSize = null;
        String chunkSlotSizeStr = configuration.getString(Contants.SLOT_SIZE, Contants.DEFAULT_SLOT_SIZE);
        String[] chunkSlotSizeStrArray = chunkSlotSizeStr.split(",");
        chunkSlotSize = new int[chunkSlotSizeStrArray.length];
        for (int i = 0; i < chunkSlotSizeStrArray.length; i++) {
            chunkSlotSize[i] = Integer.parseInt(chunkSlotSizeStrArray[i].trim());
        }
        return chunkSlotSize;
    }

    public int getHashMod() {
        int hashMod = configuration.getInt(Contants.HASH_MOD, Contants.DEFAULT_HASHMOD);
        return hashMod;
    }

    public int getChunkSize() {
        return configuration.getInt(Contants.CACHE_CHUNK_SIZE, Contants.DEFAULT_CACHE_CHUNK_SIZE);
    }

    public int getTotalSize() {
        return configuration.getInt(Contants.CACHE_TOTAL_SIZE, Contants.DEFAULT_TOTAL_SEGMENT_SIZE);
    }
}
