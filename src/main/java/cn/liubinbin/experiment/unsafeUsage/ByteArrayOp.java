package cn.liubinbin.experiment.unsafeUsage;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by bin on 2019/4/9.
 */
public class ByteArrayOp {

    class Node {
        private int id;
        Node(int id) {
            this.id = id;
        }

        public int getId(){
            return id;
        }
    }
    public void doMain(){
        Node[] nodes = new Node[300];
        for (int i = 0; i < 300; i++){
            nodes[i] = new Node(i);
        }

        // get unsafe
        Unsafe unsafe = null;
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get shift
        Class nc = Node[].class;
        int NBASE = unsafe.arrayBaseOffset(nc);
        int ns = unsafe.arrayIndexScale(nc);
        int NSHIFT = 31 - Integer.numberOfLeadingZeros(ns);

        System.out.println("NBASE: " + NBASE + " ns: " + ns + " NSHIFT: " +  NSHIFT);

        int idx = 234;
        Node temp = (Node)unsafe.getObjectVolatile(nodes, (long)((idx << NSHIFT) + NBASE));
        System.out.println("temp: " + temp.getId());

        Node toAdd = new Node(999999);
        unsafe.putOrderedObject(nodes, (long)((idx << NSHIFT) + NBASE), toAdd);

        Node temp1 = (Node)unsafe.getObjectVolatile(nodes, (long)((idx << NSHIFT) + NBASE));
        System.out.println("temp: " + temp1.getId());

    }



    public void doMain1() {
        System.out.println("hello world doMain1");
        byte[] data = new byte[20];
        data[0] = 11;
        data[1] = 2;
        data[2] = 3;
        System.out.println("===== direct get from array by index");
        System.out.println("data[0] " + data[0]);
        System.out.println("data[0] " + data[1]);
        System.out.println("data[0] " + data[2]);
        System.out.println("===== use unsafe");
        // get unsafe
        Unsafe unsafe = null;
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe)field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long BYTE_ARRAY_BASE_OFFSET =  unsafe.arrayBaseOffset(byte[].class);
        System.out.println("BYTE_ARRAY_BASE_OFFSET " + BYTE_ARRAY_BASE_OFFSET);
        System.out.println("data[0] " + unsafe.getByte(data, BYTE_ARRAY_BASE_OFFSET + 0L) );
        System.out.println("data[0] " + unsafe.getByte(data, BYTE_ARRAY_BASE_OFFSET + 1L ) );
        System.out.println("data[0] " + unsafe.getByte(data, BYTE_ARRAY_BASE_OFFSET + 2L) );
        System.out.println("data[0] " + unsafe.getByte(data, BYTE_ARRAY_BASE_OFFSET+ 3L) );
        System.out.println("data[0] " + unsafe.getByte(data, BYTE_ARRAY_BASE_OFFSET+ 4L) );

    }

    public static void main(String[] args) throws InterruptedException {
//        new ArrayOp().doMain();
        new ByteArrayOp().doMain1();
    }
}
