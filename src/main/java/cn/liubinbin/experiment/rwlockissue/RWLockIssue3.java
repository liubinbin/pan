package cn.liubinbin.experiment.rwlockissue;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLockIssue3 {
    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        new RWLockIssue3().doMain();
    }

    public void doMain() {
        new Thread(new T1()).start();
        new Thread(new T2()).start();
        new Thread(new T3()).start();
    }

    public void sleepMills(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class T1 extends Thread {
        @Override
        public void run() {
            Thread.currentThread().setName("T1");
            while (true) {
                System.out.println("T1 one round");
                reentrantReadWriteLock.readLock().lock();
                sleepMills(10 * 1000);
                reentrantReadWriteLock.readLock().unlock();
            }
        }
    }

    class T2 extends Thread {
        @Override
        public void run() {
            Thread.currentThread().setName("T2");
            while (true) {
                System.out.println("T2 one round");
                reentrantReadWriteLock.readLock().lock();
                sleepMills(1 * 1000);
                reentrantReadWriteLock.readLock().lock();
                sleepMills(1 * 1000);
                reentrantReadWriteLock.readLock().unlock();
                reentrantReadWriteLock.readLock().unlock();
            }
        }
    }

    class T3 extends Thread {
        @Override
        public void run() {
            System.out.println("T3 one round");
            Thread.currentThread().setName("T3");
            while (true) {
                System.out.println("T3 one round");
                sleepMills(5 * 1000);
                System.out.println("T3, start acquire lock");
                reentrantReadWriteLock.writeLock().lock();
                System.out.println("T3 enter, and start to sleep");
                sleepMills(10 * 1000);
                System.out.println("T3 end");
                reentrantReadWriteLock.writeLock().unlock();
            }

        }
    }
}
