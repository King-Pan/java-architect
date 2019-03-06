package club.javalearn.thread.api;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class StartAndRun {

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            attack();
        });
        t.start();
        //t.run();
        System.out.println("current main thread is : " + Thread.currentThread().getName());
    }

    private static void attack() {
        System.out.println("fight");
        System.out.println("Current Thread is : " + Thread.currentThread().getName());
    }
}
