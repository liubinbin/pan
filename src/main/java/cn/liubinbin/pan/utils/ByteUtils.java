package cn.liubinbin.pan.utils;

import java.util.Arrays;

/**
 * @author liubinbin
 */
public class ByteUtils {

    public static boolean IsByteArrayEqual(byte[] buf1, byte[] buf2) {
        return Arrays.equals(buf1, buf2);
    }

    /**
     * if buf1 > buf2, return 1
     * if buf1 = buf2, return 0
     * if buf1 < buf2, return -1
     *
     * @param buf1
     * @param buf2
     * @return
     */
    public static int compare(byte[] buf1, byte[] buf2) {
        if (buf1.length > buf2.length) {
            return 1;
        } else if (buf2.length < buf2.length) {
            return -1;
        }
        if (Arrays.equals(buf1, buf2)) {
            return 0;
        }
        for (int keyIdx = 0; keyIdx < buf1.length; keyIdx++) {
            if (buf1[keyIdx] > buf2[keyIdx]) {
                return 1;
            }
        }
        return -1;
    }

    public static int hashCode(byte[] buf) {
        int hash = 1;
        for (int i = 0; i < buf.length; i++) {
            hash = (31 * hash) + (int) buf[i];
        }
        return hash;
    }

    public static int hashCodeMod(byte[] buf, int mod) {
        int hash = 1;
        for (int i = 0; i < buf.length; i++) {
            hash = (31 * hash) + (int) buf[i];
        }
        return Math.abs(hash) % mod;
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
}
