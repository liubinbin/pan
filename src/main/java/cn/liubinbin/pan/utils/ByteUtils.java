package cn.liubinbin.pan.utils;

import java.util.Arrays;

/**
 * @author liubinbin
 */
public class ByteUtils {

    public static boolean IsByteArrayEqual(byte[] buf1, byte[] buf2){
        return Arrays.equals(buf1, buf2);
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
        return hash % mod;
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
}
