package club.javalearn.thread.join;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class ThreadJoinDemo2 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is running.");
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName() + " is done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t1.join(1000);
        System.out.println("main 线程执行结束");
    }
}
