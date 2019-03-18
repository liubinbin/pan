package main.java.cn.liubinbin.pan.conf;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author liubinbin
 *
 */
public class Config {

	private PropertiesConfiguration configuration = null;

	public Config() throws FileNotFoundException, ConfigurationException, IOException {
		configuration = new PropertiesConfiguration();
		configuration.read(new FileReader("conf/config.properties"));
	}

	public int getPort() {
		return configuration.getInt(Contants.CACHE_SERVER_PORT, Contants.DEFAULT_CACHE_SERVER_PORT);
	}

	public int getNettyThreadCount() {
		return configuration.getInt(Contants.CACHE_NETTY_SERVER_THREAD_COUNT,
				Contants.DEFAULT_CACHE_NETTY_SERVER_THREAD_COUNT);
	}

	public int[] getBucketSlotSize() {
		int[] bucketSlotSize = null;
		String bucketSlotSizeStr = configuration.getString(Contants.SLOT_SIZE, Contants.DEFAULT_SLOT_SIZE );
		String[] bucketSlotSizeStrArray = bucketSlotSizeStr.split(",");
		bucketSlotSize = new int[bucketSlotSizeStrArray.length];
		for (int i = 0; i < bucketSlotSizeStrArray.length; i++) {
			bucketSlotSize[i] = Integer.parseInt(bucketSlotSizeStrArray[i].trim());
		}
		return bucketSlotSize;
	}

	public int getSegmentSize() {
		return configuration.getInt(Contants.CACHE_SEGMENT_SIZE, Contants.DEFAULT_CACHE_SEGMENT_SIZE);
	}
	
	public int getTotalSize() {
		return configuration.getInt(Contants.CACHE_TOTAL_SIZE, Contants.DEFAULT_TOTAL_SEGMENT_SIZE);
	}
}
