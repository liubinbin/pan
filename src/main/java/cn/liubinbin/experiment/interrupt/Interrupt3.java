package cn.liubinbin.experiment.interrupt;

/**
 * @author liubinbin
 */
public class Interrupt3 extends Thread {

    @Override
    public void run() {
        System.out.println("status 1 " + this.isInterrupted());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("status 2 " + this.isInterrupted());
        }
        System.out.println("status 3 " + this.isInterrupted());
    }

    public static void main(String[] args) throws InterruptedException {
        Interrupt3 interrupt = new Interrupt3();
        interrupt.start();
        Thread.sleep(1000 * 1);
        interrupt.interrupt();
    }

}
