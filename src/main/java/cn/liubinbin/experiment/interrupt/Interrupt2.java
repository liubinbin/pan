package cn.liubinbin.experiment.interrupt;

/**
 * @author liubinbin
 */
public class Interrupt2 extends Thread {

    @Override
    public void run() {
        System.out.println("status 1 " + this.isInterrupted());
        double re = 0.0;
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j < Integer.MAX_VALUE; j++) {
                re = i / j;
            }
        }

        System.out.println("status 2 " + this.isInterrupted());
        System.out.println("status 3 " + interrupted());
        System.out.println("status 4 " + this.isInterrupted());
    }

    public static void main(String[] args) throws InterruptedException {
        Interrupt2 interrupt = new Interrupt2();
        interrupt.start();
        Thread.sleep(1000 * 1);
        interrupt.interrupt();
    }

}
