package cn.liubinbin.pan.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author liubinbin
 */
public class ByteArrayUtilsTest {

    @Test
    public void testToByte() {
        byte[] data = new byte[20];
        for (int i = 0; i < 20; i++) {
            data[i] = (byte) i;
        }
        for (int i = 0; i < 20; i++) {
            assertEquals((byte) i, ByteArrayUtils.toByte(data, i));
        }
        assertNotEquals((byte) 1, ByteArrayUtils.toByte(data, 0));
    }

    @Test
    public void testPutByte() {
        byte[] data = new byte[20];
        for (int i = 0; i < 20; i++) {
            ByteArrayUtils.putByte(data, i, (byte) i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals((byte) i, ByteArrayUtils.toByte(data, i));
        }
        assertNotEquals((byte) 1, ByteArrayUtils.toByte(data, 0));
    }

    @Test
    public void testToInt() {
        int expectNumber = (1 << 24) + (2 << 16) + (3 << 8) + 4;
        byte[] data = new byte[4];
        for (int i = 0; i < 4; i++) {
            data[i] = (byte) (i + 1);
        }
        assertEquals(expectNumber, ByteArrayUtils.toInt(data, 0));
        assertEquals(expectNumber, ByteUtils.byteArrayToInt(data));
    }

    @Test
    public void testPutInt() {
        int expectNumber = 1234;
        byte[] data = new byte[4];
        ByteArrayUtils.putInt(data, 0, expectNumber);
        assertEquals(expectNumber, ByteArrayUtils.toInt(data, 0));
    }

    @Test
    public void testCASInt() {
        byte[] data = new byte[4];
        int expectNumber = 1234;
        int wrongExpectNumber = 234;
        int update = 6789;
        ByteArrayUtils.putInt(data, 0, expectNumber);

        boolean wrongExcept = ByteArrayUtils.compareAndSetInt(data, 0, wrongExpectNumber, update);
        assertNotEquals(ByteArrayUtils.toInt(data, 0), update);
        assertEquals(false, wrongExcept);

        boolean rightExcept = ByteArrayUtils.compareAndSetInt(data, 0, expectNumber, update);
        assertEquals(ByteArrayUtils.toInt(data, 0), update);
        assertEquals(true, rightExcept);
    }

    @Test
    public void testToLong() {
        long expectNumber = (1L << 56) + (2L << 48) + (3L << 40) + (4L << 32) + (5L << 24) + (6L << 16) + (7L << 8) + 8L;
        byte[] data = new byte[8];
        for (int i = 0; i < 8; i++) {
            data[i] = (byte) (i + 1);
        }
        assertEquals(expectNumber, ByteArrayUtils.toLong(data, 0));
    }

    @Test
    public void testPutLong() {
        int expectNumber = 1234;
        byte[] data = new byte[4];
        ByteArrayUtils.putInt(data, 0, expectNumber);
        assertEquals(expectNumber, ByteArrayUtils.toInt(data, 0));
    }

    @Test
    public void testCASLong() {
        byte[] data = new byte[4];
        int expectNumber = 1234;
        int wrongExpectNumber = 234;
        int update = 6789;
        ByteArrayUtils.putInt(data, 0, expectNumber);

        boolean wrongExcept = ByteArrayUtils.compareAndSetInt(data, 0, wrongExpectNumber, update);
        assertNotEquals(ByteArrayUtils.toInt(data, 0), update);
        assertEquals(false, wrongExcept);

        boolean rightExcept = ByteArrayUtils.compareAndSetInt(data, 0, expectNumber, update);
        assertEquals(ByteArrayUtils.toInt(data, 0), update);
        assertEquals(true, rightExcept);
    }
}