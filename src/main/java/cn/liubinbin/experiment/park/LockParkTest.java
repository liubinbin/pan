package cn.liubinbin.experiment.park;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by bin on 2019/7/17.
 */
public class LockParkTest extends Thread {
    @Override
    public void run() {
        System.out.println("before park");
        LockSupport.park();
        System.out.println("after park");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new LockParkTest();
        thread.start();
        Thread.sleep(1000 * 3);
        System.out.println("start to unpark");
        LockSupport.unpark(thread);
        System.out.println("all done");
    }

}
