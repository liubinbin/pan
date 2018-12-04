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
public class CacheConfig {
	
	private PropertiesConfiguration configuration = null;
	
	public CacheConfig() throws FileNotFoundException, ConfigurationException, IOException {
		configuration = new PropertiesConfiguration();
		configuration.read(new FileReader("conf/be-config.properties"));
	}
	
	public int[] getBucketSlotSize() {
		
		return null;
	}
}
