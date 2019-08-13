package cn.liubinbin.experiment.interrupt;

/**
 * @author liubinbin
 */
public class Interrupt extends Thread {

    @Override
    public void run() {
        System.out.println("status 1 " + this.isInterrupted());
        while (!this.isInterrupted()) {
            ;
        }
        System.out.println("status 3 " + this.isInterrupted());
    }

    public static void main(String[] args) throws InterruptedException {
        Interrupt interrupt = new Interrupt();
        interrupt.start();
        Thread.sleep(1000 * 1);
        interrupt.interrupt();
    }

}
