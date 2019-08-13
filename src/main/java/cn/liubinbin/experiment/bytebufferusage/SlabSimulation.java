package cn.liubinbin.experiment.bytebufferusage;

import cn.liubinbin.pan.module.Item;

import java.nio.ByteBuffer;

/**
 * Created by bin on 2019/4/17.
 */
public class SlabSimulation {

    public static void main(String[] args) {
        System.out.println("hello SlabSimulation");
        ByteBuffer bSlab = ByteBuffer.allocate(1024 * 1024 * 2);
        System.out.println("bSlab " + bSlab.toString());
        int slabOffset = 0;

        int slotSize = 10240;
        byte[] firstKey = "first".getBytes();
        byte[] firstValue = "first time to say hello world".getBytes();
        Item first = new Item(firstKey, firstValue);
        byte[] secondKey = "secoond".getBytes();
        byte[] secondValue = "right now, we can do something together, coz we know each other better that before".getBytes();
        Item second = new Item(secondKey, secondValue);

        // write first KeyValue
        int offsetFirst = 0;
        slabOffset = offsetFirst;


        // write second KeyValue
        int offsetSecond = 10240;

        // get first KeyValue
        // offsetFirst
        // print key, value
        //get second KeyValue
        // offsetSecond

    }
}
