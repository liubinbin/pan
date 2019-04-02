package test.java.cn.liubinbin.experiment.binfile;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Test;

import cn.liubinbin.experiment.binfile.filev1.BinFile;
import cn.liubinbin.experiment.binfile.filev1.KeyValue;

/**
 *
 * @author liubinbin
 *
 */
public class BinFileTest {

	@Test
	public void testByteBuffer() {
		String keyInStr = "hello";
		String valueInStr = "world";
		long timestamp = System.currentTimeMillis();
		KeyValue keyValue = new KeyValue(keyInStr, valueInStr, timestamp);
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(26);
		byteBuffer.clear();
		byteBuffer.putInt(keyValue.getKey().length);
		byteBuffer.putInt(keyValue.getValue().length);
		byteBuffer.putLong(keyValue.getTimestamp());
		byteBuffer.put(keyValue.getKey());
		byteBuffer.put(keyValue.getValue());
		byteBuffer.flip();
		
		byte[] keyInByte = new byte[5];
		byte[] valueInByte = new byte[5];
		byteBuffer.getInt();
		byteBuffer.getInt();
		byteBuffer.getLong();
		byteBuffer.get(keyInByte);
		byteBuffer.get(valueInByte);
		assertTrue(Arrays.equals(keyInStr.getBytes(), keyInByte));
		assertTrue(Arrays.equals(valueInStr.getBytes(), valueInByte));
	}
	
	@Test
	public void testWRMode() {
		
	}
	
	@Test
	public void testReadWrite() throws IOException {
		String key = "hello";
		String value = "world";
		long timestamp = System.currentTimeMillis();
		BinFile binFile = new BinFile("binfile");
		binFile.writeTo(new KeyValue(key, value, timestamp));
		binFile.flush();
		KeyValue keyValue = binFile.readFrom(0);
		assertEquals(timestamp, keyValue.getTimestamp());
		assertTrue(Arrays.equals(key.getBytes(), keyValue.getKey()));
		assertTrue(Arrays.equals(value.getBytes(), keyValue.getValue()));
	}
}
