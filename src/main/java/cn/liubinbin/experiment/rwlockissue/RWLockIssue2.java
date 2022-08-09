package cn.liubinbin.experiment.rwlockissue;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RWLockIssue2 {

    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);

    public static void main(String[] args) {
        new RWLockIssue2().doMain();
    }

    public void doMain() {
        new Thread(new T1()).start();
        new Thread(new T2()).start();
        new Thread(new T3()).start();
        for (int i = 0; i< 10000; i++) {
            new Thread(new T1()).start();
            new Thread(new T2()).start();
        }

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
            sleepMills(1 * 1000);
            reentrantReadWriteLock.readLock().lock();
            System.out.println("T1 start ley 1");
            sleepMills(20 * 1000);
            reentrantReadWriteLock.readLock().lock();
            System.out.println("T1 start ley 2");
            sleepMills(10 * 1000);
            System.out.println("T1 end ley 2");
            reentrantReadWriteLock.readLock().unlock();
            System.out.println("T1 end ley 1");
            reentrantReadWriteLock.readLock().unlock();
        }
    }

    class T2 extends Thread {
        @Override
        public void run() {
            Thread.currentThread().setName("T2");
            sleepMills(5 * 1000);
            System.out.println("T2, start acquire lock");
            reentrantReadWriteLock.readLock().lock();
            System.out.println("T2 enter, and start to sleep");
            sleepMills(10 * 1000);
            System.out.println("T2 end");
            reentrantReadWriteLock.readLock().unlock();
        }
    }

    class T3 extends Thread {
        @Override
        public void run() {
            Thread.currentThread().setName("T3");
            sleepMills(2 * 1000);
            System.out.println("T3, start acquire lock");
            reentrantReadWriteLock.writeLock().lock();
            System.out.println("T3 enter, and start to sleep");
            sleepMills(10 * 1000);
            System.out.println("T3 end");
            reentrantReadWriteLock.writeLock().unlock();
        }
    }
}
