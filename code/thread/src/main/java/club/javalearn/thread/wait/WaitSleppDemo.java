package club.javalearn.thread.wait;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class WaitSleppDemo {

    public static void main(String[] args) throws InterruptedException {
        final Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " is wait to get lock");
                try {
                    Thread.sleep(20);
                    System.out.println(Thread.currentThread().getName() + " wait method");
                    lock.wait(1000);
                    System.out.println(Thread.currentThread().getName() + " is done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-A").start();
        TimeUnit.MICROSECONDS.sleep(10);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " is wait to get lock");
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " get lock");
                    System.out.println(Thread.currentThread().getName() + " is sleep 20 ms");
                    Thread.sleep(20);
                    System.out.println(Thread.currentThread().getName() + " wait method");
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-B").start();
    }
}
