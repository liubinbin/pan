package cn.liubinbin.experiment.queue;

import java.util.LinkedList;

/**
 * @author liubinbin
 */
public class QueueTest {

    private final int SIZE = 4;
    private LinkedList<Integer> data;

    public QueueTest() {
        this.data = new LinkedList<>();
    }

    public static void main(String[] args) throws InterruptedException {
        new QueueTest().doMain();
    }

    private void doMain() {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    private class Producer implements Runnable {

        private int dataIdx;

        public Producer() {
            this.dataIdx = 0;
        }

        @Override
        public void run() {
            System.out.println("Producer start ");
            while (true) {
                synchronized (data) {
                    if (data.size() >= SIZE) {
                        try {
                            data.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    data.add(dataIdx);
                    System.out.println("producer data: " + dataIdx);
                    dataIdx++;
                    data.notifyAll();
                }
            }
        }

    }

    private class Consumer implements Runnable {

        public Consumer() {

        }

        @Override
        public void run() {
            System.out.println("Consumer start ");
            while (true) {
                synchronized (data) {
                    if (data.size() <= 0) {
                        try {
                            data.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("concumer data: " + data.removeFirst());
                    data.notify();
                }
            }

        }

    }

}
