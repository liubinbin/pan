package cn.liubinbin.experiment.unsafeUsage;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

/**
 * Created by bin on 2019/4/9.
 */
public class ArrayOp {

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

    public static void main(String[] args) throws InterruptedException {
        new ArrayOp().doMain();
    }
}
