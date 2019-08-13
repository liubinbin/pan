package cn.liubinbin.experiment.binbf;

import java.util.BitSet;

/***
 * 优点：体积小，判断快。。。
 * 缺点：有误判，不好确定参数。。。
 * @author liubinbin
 *
 * @param <T>
 */
public class BinBloomfilter<T> {

    private BitSet bits;
    private int[] seeds; //函数种子
    private int FuncSize = 6;

    public BinBloomfilter(int bitSize) {
        this.bits = new BitSet(bitSize);
        this.seeds = new int[]{3, 24, 33, 47, 53, 67};
    }

    public void add(T value) {
        int temp = value.hashCode();
        for (int i = 0; i < FuncSize; i++) {
            System.out.println("index : " + temp % seeds[i]);
            bits.set(temp % seeds[i]); //此处的hash函数其实可以封装，TODO
        }
    }

    //因为bloomfilter是容忍一定程度的误判的，所以会默认应该返回false
    public boolean contain(T value) {
        int temp = value.hashCode();
        for (int i = 0; i < FuncSize; i++) {
            if (!bits.get(temp % seeds[i])) {
                return false;
            }
        }
        return true;
    }

    public void printBits() {
        System.out.println(bits.toString());
    }

    public static void main(String[] args) {
        BinBloomfilter<String> beanBloomfilter = new BinBloomfilter<String>(100);
        beanBloomfilter.add("hello world");
        beanBloomfilter.printBits();
        System.out.println("contains : " + beanBloomfilter.contain("hello world"));
    }
}
