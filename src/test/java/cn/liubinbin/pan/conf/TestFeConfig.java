package test.java.cn.liubinbin.pan.conf;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

import main.java.cn.liubinbin.pan.conf.FeConfig;

/**
 *
 * @author liubinbin
 *
 */
public class TestFeConfig {
	
	private FeConfig feConfig = null;
	
	public TestFeConfig() {
		try {
			this.feConfig = new FeConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPort(){
		assertEquals("fe port", 50501, feConfig.getFePort());
	}
}
