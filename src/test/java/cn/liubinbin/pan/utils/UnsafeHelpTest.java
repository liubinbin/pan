package cn.liubinbin.pan.utils;

import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

public final class UnsafeHelpTest {

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

    private UnsafeHelpTest() {
    }

    public static boolean compareAndSetLongTest() {

    }

    public static boolean compareAndSetIntTest() {

    }
}