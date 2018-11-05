package test.java.cn.liubinbin.pan.conf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.java.cn.liubinbin.pan.conf.FeConfig;

/**
 *
 * @author liubinbin
 *
 */
public class FeConfigTest {
	
	private FeConfig feConfig = null;
	
	public FeConfigTest() {
		try {
			this.feConfig = new FeConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPort(){
		assertEquals("fe port", 50501, feConfig.getFePort());
	}
}
