package cn.liubinbin.pan.utils;

import sun.misc.Unsafe;

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


//    public static int toInt(ByteBuffer buffer, int offset) {
//        return org.apache.hadoop.hbase.util.ByteBufferUtils.toInt(buffer, offset);
//    }

//    public static long toLong(ByteBuffer buffer, int offset) {
//        return org.apache.hadoop.hbase.util.ByteBufferUtils.toLong(buffer, offset);
//    }

//    public static int toShort(ByteBuffer buffer, int offset) {
//        return org.apache.hadoop.hbase.util.ByteBufferUtils.toShort(buffer, offset);
//    }

//    public static int toByte(ByteBuffer buffer, int offset) {
//        return org.apache.hadoop.hbase.util.ByteBufferUtils.toByte(buffer, offset);
//    }

//    public static void putInt(ByteBuffer buffer, int offset, int val) {
//        org.apache.hadoop.hbase.util.ByteBufferUtils.putInt(buffer, offset, val);
//    }

//    public static void putLong(ByteBuffer buffer, int offset, long val) {
//        org.apache.hadoop.hbase.util.ByteBufferUtils.putLong(buffer, offset, val);
//    }

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
