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
public class FeConfig {

	private PropertiesConfiguration configuration = null;

	public FeConfig() throws FileNotFoundException, ConfigurationException, IOException {
		configuration = new PropertiesConfiguration();
		configuration.read(new FileReader("conf/fe-config.properties"));
	}

	public int getFePort() {
		return configuration.getInt(Contants.FE_SERVER_PORT, 50501);
	}

	public static void main(String[] args) throws FileNotFoundException, ConfigurationException, IOException {
		FeConfig feConfig = new FeConfig();
		System.out.println("port " + feConfig.getFePort());
	}
}
