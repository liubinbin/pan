package cn.liubinbin.pan.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bin on 2019/5/16.
 */
public class ByteUtilsTest {

    @Test
    public void testCompare() {
        byte[] a = "a".getBytes();
        byte[] b = "b".getBytes();
        assertTrue(ByteUtils.compare(a, b) == -1);
        assertTrue(ByteUtils.compare(b, a) == 1);
        assertTrue(ByteUtils.compare(a, a) == 0);
    }
}
