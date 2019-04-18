package cn.liubinbin.pan.utils;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

public final class UnsafeHelp {

    // Unsafe mechanics
    static final Unsafe UNSAFE;

    /**
     * The offset to the first element in a byte array.
     */
    public static final long BYTE_ARRAY_BASE_OFFSET;

    static {
        try {
            UNSAFE = (Unsafe) AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    try {
                        Field f = Unsafe.class.getDeclaredField("theUnsafe");
                        f.setAccessible(true);
                        return f.get(null);
                    } catch (Throwable e) {
                        throw new Error(e);
                    }
                }
            });
            BYTE_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private UnsafeHelp() {
    }

    public static boolean compareAndSetLong(ByteBuffer buf, int offset, long expected, long update) {
        if (buf.isDirect()) {
            return UNSAFE
                    .compareAndSwapLong(null, ((DirectBuffer) buf).address() + offset, expected, update);
        }
        return UNSAFE
                .compareAndSwapLong(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset,
                        expected, update);
    }

    public static boolean compareAndSetInt(ByteBuffer buf, int offset, int expected, int update) {
        if (buf.isDirect()) {
            return UNSAFE
                    .compareAndSwapInt(null, ((DirectBuffer) buf).address() + offset, expected, update);
        }
        return UNSAFE
                .compareAndSwapInt(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset,
                        expected, update);
    }

    public static boolean compareAndSetInt(byte[] buf, int offset, int expected, int update) {
        return UNSAFE.compareAndSwapInt(buf, offset, expected, update);
    }

    // APIs to read primitive data from a byte[] using Unsafe way

    /**
     * Converts a byte array to a short value considering it was written in big-endian format.
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @return the short value
     */
    public static short toShort(byte[] bytes, int offset) {

        return UNSAFE.getShort(bytes, offset + BYTE_ARRAY_BASE_OFFSET);

    }

    /**
     * Converts a byte array to an int value considering it was written in big-endian format.
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @return the int value
     */
    public static int toInt(byte[] bytes, int offset) {
        return UNSAFE.getInt(bytes, offset + BYTE_ARRAY_BASE_OFFSET);

    }

    /**
     * Converts a byte array to a long value considering it was written in big-endian format.
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @return the long value
     */
    public static long toLong(byte[] bytes, int offset) {
        return UNSAFE.getLong(bytes, offset + BYTE_ARRAY_BASE_OFFSET);

    }

    public static int putByte(byte[] bytes, int offset, byte val) {
        UNSAFE.putByte(bytes, offset + BYTE_ARRAY_BASE_OFFSET, val);
        return offset + Bytes.SIZEOF_INT;
    }

    // APIs to write primitive data to a byte[] using Unsafe way

    /**
     * Put a short value out to the specified byte array position in big-endian format.
     *
     * @param bytes  the byte array
     * @param offset position in the array
     * @param val    short to write out
     * @return incremented offset
     */
    public static int putShort(byte[] bytes, int offset, short val) {
        UNSAFE.putShort(bytes, offset + BYTE_ARRAY_BASE_OFFSET, val);
        return offset + Bytes.SIZEOF_SHORT;
    }

    /**
     * Put an int value out to the specified byte array position in big-endian format.
     *
     * @param bytes  the byte array
     * @param offset position in the array
     * @param val    int to write out
     * @return incremented offset
     */
    public static int putInt(byte[] bytes, int offset, int val) {
        UNSAFE.putInt(bytes, offset + BYTE_ARRAY_BASE_OFFSET, val);
        return offset + Bytes.SIZEOF_INT;
    }

    /**
     * Put a long value out to the specified byte array position in big-endian format.
     *
     * @param bytes  the byte array
     * @param offset position in the array
     * @param val    long to write out
     * @return incremented offset
     */
    public static int putLong(byte[] bytes, int offset, long val) {
        UNSAFE.putLong(bytes, offset + BYTE_ARRAY_BASE_OFFSET, val);
        return offset + Bytes.SIZEOF_LONG;
    }

    // APIs to read primitive data from a ByteBuffer using Unsafe way

    /**
     * Reads a short value at the given buffer's offset considering it was written in big-endian
     * format.
     *
     * @param buf
     * @param offset
     * @return short value at offset
     */
    public static short toShort(ByteBuffer buf, int offset) {
        return getAsShort(buf, offset);
    }

    /**
     * Reads a short value at the given Object's offset considering it was written in big-endian
     * format.
     *
     * @param ref
     * @param offset
     * @return short value at offset
     */
    public static short toShort(Object ref, long offset) {
        return UNSAFE.getShort(ref, offset);
    }

    /**
     * Reads bytes at the given offset as a short value.
     *
     * @param buf
     * @param offset
     * @return short value at offset
     */
    static short getAsShort(ByteBuffer buf, int offset) {
        if (buf.isDirect()) {
            return UNSAFE.getShort(((DirectBuffer) buf).address() + offset);
        }
        return UNSAFE.getShort(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset);
    }

    /**
     * Reads an int value at the given buffer's offset considering it was written in big-endian
     * format.
     *
     * @param buf
     * @param offset
     * @return int value at offset
     */
    public static int toInt(ByteBuffer buf, int offset) {
        return getAsInt(buf, offset);
    }

    /**
     * Reads a int value at the given Object's offset considering it was written in big-endian
     * format.
     *
     * @param ref
     * @param offset
     * @return int value at offset
     */
    public static int toInt(Object ref, long offset) {
        return UNSAFE.getInt(ref, offset);
    }

    /**
     * Reads bytes at the given offset as an int value.
     *
     * @param buf
     * @param offset
     * @return int value at offset
     */
    static int getAsInt(ByteBuffer buf, int offset) {
        if (buf.isDirect()) {
            return UNSAFE.getInt(((DirectBuffer) buf).address() + offset);
        }
        return UNSAFE.getInt(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset);
    }

    /**
     * Reads a long value at the given buffer's offset considering it was written in big-endian
     * format.
     *
     * @param buf
     * @param offset
     * @return long value at offset
     */
    public static long toLong(ByteBuffer buf, int offset) {
        return getAsLong(buf, offset);
    }

    /**
     * Reads a long value at the given Object's offset considering it was written in big-endian
     * format.
     *
     * @param ref
     * @param offset
     * @return long value at offset
     */
    public static long toLong(Object ref, long offset) {
        return UNSAFE.getLong(ref, offset);
    }

    /**
     * Reads bytes at the given offset as a long value.
     *
     * @param buf
     * @param offset
     * @return long value at offset
     */
    static long getAsLong(ByteBuffer buf, int offset) {
        if (buf.isDirect()) {
            return UNSAFE.getLong(((DirectBuffer) buf).address() + offset);
        }
        return UNSAFE.getLong(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset);
    }

    /**
     * Put an int value out to the specified ByteBuffer offset in big-endian format.
     *
     * @param buf    the ByteBuffer to write to
     * @param offset offset in the ByteBuffer
     * @param val    int to write out
     * @return incremented offset
     */
    public static int putInt(ByteBuffer buf, int offset, int val) {
        if (buf.isDirect()) {
            UNSAFE.putInt(((DirectBuffer) buf).address() + offset, val);
        } else {
            UNSAFE.putInt(buf.array(), offset + buf.arrayOffset() + BYTE_ARRAY_BASE_OFFSET, val);
        }
        return offset + Bytes.SIZEOF_INT;
    }

    // APIs to copy data. This will be direct memory location copy and will be much faster

    /**
     * Copies the bytes from given array's offset to length part into the given buffer.
     *
     * @param src
     * @param srcOffset
     * @param dest
     * @param destOffset
     * @param length
     */
    public static void copy(byte[] src, int srcOffset, ByteBuffer dest, int destOffset, int length) {
        long destAddress = destOffset;
        Object destBase = null;
        if (dest.isDirect()) {
            destAddress = destAddress + ((DirectBuffer) dest).address();
        } else {
            destAddress = destAddress + BYTE_ARRAY_BASE_OFFSET + dest.arrayOffset();
            destBase = dest.array();
        }
        long srcAddress = srcOffset + BYTE_ARRAY_BASE_OFFSET;
        unsafeCopy(src, srcAddress, destBase, destAddress, length);
    }

    private static void unsafeCopy(Object src, long srcAddr, Object dst, long destAddr, long len) {
        while (len > 0) {
            long size = (len > UNSAFE_COPY_THRESHOLD) ? UNSAFE_COPY_THRESHOLD : len;
            UNSAFE.copyMemory(src, srcAddr, dst, destAddr, len);
            len -= size;
            srcAddr += size;
            destAddr += size;
        }
    }

    /**
     * Copies specified number of bytes from given offset of {@code src} ByteBuffer to the
     * {@code dest} array.
     *
     * @param src
     * @param srcOffset
     * @param dest
     * @param destOffset
     * @param length
     */
    public static void copy(ByteBuffer src, int srcOffset, byte[] dest, int destOffset,
                            int length) {
        long srcAddress = srcOffset;
        Object srcBase = null;
        if (src.isDirect()) {
            srcAddress = srcAddress + ((DirectBuffer) src).address();
        } else {
            srcAddress = srcAddress + BYTE_ARRAY_BASE_OFFSET + src.arrayOffset();
            srcBase = src.array();
        }
        long destAddress = destOffset + BYTE_ARRAY_BASE_OFFSET;
        unsafeCopy(srcBase, srcAddress, dest, destAddress, length);
    }

    /**
     * Copies specified number of bytes from given offset of {@code src} buffer into the {@code dest}
     * buffer.
     *
     * @param src
     * @param srcOffset
     * @param dest
     * @param destOffset
     * @param length
     */
    public static void copy(ByteBuffer src, int srcOffset, ByteBuffer dest, int destOffset,
                            int length) {
        long srcAddress, destAddress;
        Object srcBase = null, destBase = null;
        if (src.isDirect()) {
            srcAddress = srcOffset + ((DirectBuffer) src).address();
        } else {
            srcAddress = (long) srcOffset + src.arrayOffset() + BYTE_ARRAY_BASE_OFFSET;
            srcBase = src.array();
        }
        if (dest.isDirect()) {
            destAddress = destOffset + ((DirectBuffer) dest).address();
        } else {
            destAddress = destOffset + BYTE_ARRAY_BASE_OFFSET + dest.arrayOffset();
            destBase = dest.array();
        }
        unsafeCopy(srcBase, srcAddress, destBase, destAddress, length);
    }

    // APIs to add primitives to BBs

    /**
     * Put a short value out to the specified BB position in big-endian format.
     *
     * @param buf    the byte buffer
     * @param offset position in the buffer
     * @param val    short to write out
     * @return incremented offset
     */
    public static int putShort(ByteBuffer buf, int offset, short val) {
        if (buf.isDirect()) {
            UNSAFE.putShort(((DirectBuffer) buf).address() + offset, val);
        } else {
            UNSAFE.putShort(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset, val);
        }
        return offset + Bytes.SIZEOF_SHORT;
    }

    /**
     * Put a long value out to the specified BB position in big-endian format.
     *
     * @param buf    the byte buffer
     * @param offset position in the buffer
     * @param val    long to write out
     * @return incremented offset
     */
    public static int putLong(ByteBuffer buf, int offset, long val) {
        if (buf.isDirect()) {
            UNSAFE.putLong(((DirectBuffer) buf).address() + offset, val);
        } else {
            UNSAFE.putLong(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset, val);
        }
        return offset + Bytes.SIZEOF_LONG;
    }

    /**
     * Put a byte value out to the specified BB position in big-endian format.
     *
     * @param buf    the byte buffer
     * @param offset position in the buffer
     * @param b      byte to write out
     * @return incremented offset
     */
    public static int putByte(ByteBuffer buf, int offset, byte b) {
        if (buf.isDirect()) {
            UNSAFE.putByte(((DirectBuffer) buf).address() + offset, b);
        } else {
            UNSAFE.putByte(buf.array(),
                    BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset, b);
        }
        return offset + 1;
    }

    /**
     * Returns the byte at the given offset
     *
     * @param buf    the buffer to read
     * @param offset the offset at which the byte has to be read
     * @return the byte at the given offset
     */
    public static byte toByte(ByteBuffer buf, int offset) {
        if (buf.isDirect()) {
            return UNSAFE.getByte(((DirectBuffer) buf).address() + offset);
        } else {
            return UNSAFE.getByte(buf.array(), BYTE_ARRAY_BASE_OFFSET + buf.arrayOffset() + offset);
        }
    }

    /**
     * Returns the byte at the given offset of the object
     *
     * @param ref
     * @param offset
     * @return the byte at the given offset
     */
    public static byte toByte(Object ref, long offset) {
        return UNSAFE.getByte(ref, offset);
    }
}