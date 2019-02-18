package club.javalearn.thread.t4;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/2/18
 * @Description ${DESCRIPTION}
 */
public class ThreadInterruptDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thred = new Thread(() -> {
            while (true) {
                boolean in = Thread.currentThread().isInterrupted();
                if (in) {
                    System.out.println("before:" + in);
                    Thread.interrupted();//设置复位
                    System.out.println("after:" + Thread.currentThread().isInterrupted());
                }
            }
        }, "threadInterrupted");
        thred.start();
        TimeUnit.SECONDS.sleep(1);
        thred.interrupt(); //终端
        System.out.println("----------------结束");
    }
}
