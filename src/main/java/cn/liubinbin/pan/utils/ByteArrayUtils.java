package cn.liubinbin.pan.utils;

/**
 * @author liubinbin
 */
public class ByteArrayUtils {

    private ByteArrayUtils() {
    }

    // for byte
    public static void putByte(byte[] byteArray, int offset, byte val) {
        UnsafeHelp.putByte(byteArray, offset, val);
    }

    public static byte toByte(byte[] byteArray, int offset) {
        return UnsafeHelp.toByte(byteArray, offset);
    }

    public static boolean compareAndSetByte(byte[] byteArray, int offset, byte expected,
                                            byte update) {
        return UnsafeHelp.compareAndSetByte(byteArray, offset, expected, update);
    }

    // for int
    public static void putInt(byte[] byteArray, int offset, int val) {
        UnsafeHelp.putInt(byteArray, offset, val);
    }

    public static int toInt(byte[] byteArray, int offset) {
        return UnsafeHelp.toInt(byteArray, offset);
    }

    public static boolean compareAndSetInt(byte[] byteArray, int offset, int expected,
                                           int update) {
        return UnsafeHelp.compareAndSetInt(byteArray, offset, expected, update);
    }

    // for long
    public static void putLong(byte[] byteArray, int offset, long val) {
        UnsafeHelp.putLong(byteArray, offset, val);
    }

    public static long toLong(byte[] byteArray, int offset) {
        return UnsafeHelp.toLong(byteArray, offset);
    }

    public static boolean compareAndSetLong(byte[] byteArray, int offset, long expected,
                                            long update) {
        return UnsafeHelp.compareAndSetLong(byteArray, offset, expected, update);
    }

    // for bytes
    public static void putBytes(byte[] byteArray, int offset, byte[] val) {
        UnsafeHelp.putBytes(byteArray, offset, val);
    }

    public static byte[] getBytes(byte[] byteArray, int offset, int length) {
        byte[] subBytes = new byte[length];
        System.arraycopy(byteArray, offset, subBytes, 0, length);
        return subBytes;
    }

    /**
     * Copy from one buffer to another from given offset. This will be absolute positional copying and
     * won't affect the position of any of the buffers.
     *
     * @param in                source buffer
     * @param out               destination buffer
     * @param sourceOffset      offset in the source buffer
     * @param destinationOffset offset in the destination buffer
     * @param length            how many bytes to copy
     */
//    public static void copyFromBufferToBuffer(ByteBuffer in, ByteBuffer out, int sourceOffset,
//                                              int destinationOffset, int length) {
//        org.apache.hadoop.hbase.util.ByteBufferUtils
//                .copyFromBufferToBuffer(in, out, sourceOffset, destinationOffset, length);
//    }


//    public static boolean compareAndSetLong(ByteBuffer byteBuffer, int offset, long expected,
//                                            long update) {
//        return UnsafeHelp.compareAndSetLong(byteBuffer, offset, expected, update);
//    }

//    public static boolean compareAndSetLong(ByteBuffer byteBuffer, int offset, long expected,
//                                            long update) {
//        return UnsafeHelp.compareAndSetLong(byteBuffer, offset, expected, update);
//    }

//    public static boolean compareAndSetInt(byte[] byteArray, int offset, int expected,
//                                           int update) {
//        return UnsafeHelp.compareAndSetInt(byteArray, offset, expected, update);
//    }


//    public static int compareTo(ByteBuffer buf1, int o1, int l1, ByteBuffer buf2, int o2, int l2) {
//        return org.apache.hadoop.hbase.util.ByteBufferUtils.compareTo(buf1, o1, l1, buf2, o2, l2);
//    }

//    public static int compareTo(ByteBuffer buf1, int o1, int l1, byte[] buf2, int o2, int l2) {
//        return org.apache.hadoop.hbase.util.ByteBufferUtils.compareTo(buf1, o1, l1, buf2, o2, l2);
//    }

}
