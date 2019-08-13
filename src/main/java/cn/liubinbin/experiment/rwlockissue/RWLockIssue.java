package cn.liubinbin.experiment.rwlockissue;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLockIssue {
    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        new RWLockIssue().doMain();
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
            reentrantReadWriteLock.readLock().lock();
            sleepMills(3600 * 1000);
            reentrantReadWriteLock.readLock().unlock();
        }
    }

    class T2 extends Thread {
        @Override
        public void run() {
            Thread.currentThread().setName("T2");
            while (true) {
                reentrantReadWriteLock.readLock().lock();
                reentrantReadWriteLock.readLock().unlock();
            }
        }
    }

    class T3 extends Thread {
        @Override
        public void run() {
            Thread.currentThread().setName("T3");
            sleepMills(5000);
            reentrantReadWriteLock.writeLock().lock();
            sleepMills(3000);
            reentrantReadWriteLock.writeLock().unlock();
        }
    }
}
