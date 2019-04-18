package cn.liubinbin.pan.utils;

/**
 * @author liubinbin
 */
public class ByteUtils {

    public static int hashCode(byte[] buf) {
        int hash = 1;
        for (int i = 0; i < buf.length; i++) {
            hash = (31 * hash) + (int) buf[i];
        }
        return hash;
    }
}
