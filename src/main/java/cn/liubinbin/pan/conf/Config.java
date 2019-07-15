package cn.liubinbin.pan.conf;

import cn.liubinbin.pan.exceptions.SlotBiggerThanChunkException;
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

    public int[] getSlotSizes() throws SlotBiggerThanChunkException {
        int chunkSize = getSlabSize();
        int[] slotSizes = null;
        String slotSizesStr = configuration.getString(Contants.SLOT_SIZE, Contants.DEFAULT_SLOT_SIZE);
        String[] slotSizesStrArray = slotSizesStr.split(",");
        slotSizes = new int[slotSizesStrArray.length];
        for (int i = 0; i < slotSizesStrArray.length; i++) {
            slotSizes[i] = Integer.parseInt(slotSizesStrArray[i].trim());
            if (slotSizes[i] > chunkSize) {
                throw new SlotBiggerThanChunkException("slotSize " + slotSizes[i] +  " is bigger than chunkSize " + chunkSize);
            }
        }
        return slotSizes;
    }

    public int getHashMod() {
        int hashMod = configuration.getInt(Contants.HASH_MOD, Contants.DEFAULT_HASHMOD);
        return hashMod;
    }

    public int getSlabSize() {
        return configuration.getInt(Contants.CACHE_SLAB_SIZE, Contants.DEFAULT_CACHE_SLAB_SIZE);
    }

    public int getSlabMaxCount(){
        return configuration.getInt(Contants.CACHE_SLAB_MAX_COUNT, Contants.DEFAULT_CACHE_SLAB_MAX_COUNT);
    }

    public boolean getMetricsPrint(){
        return configuration.getBoolean(Contants.PAN_METRICS_PRINT, Contants.DEFAULT_PAN_METRICS_PRINT);
    }

    public int getRmiPort(){
        return configuration.getInt(Contants.PAN_METRICS_PRINT, Contants.DEFAULT_PAN_RMI_PORT);
    }
}
