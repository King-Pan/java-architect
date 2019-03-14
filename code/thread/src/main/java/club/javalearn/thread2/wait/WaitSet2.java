package club.javalearn.thread2.wait;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 1. 所有对象都有一个wait set，用来存放调用了该对象wait方法之后进入WAIT、TIME_WAIT状态的线程
 * 2. 线程notify后，不一定立即执行
 * 3. 线程从wait状态唤醒没有顺序。
 */
public class WaitSet2 {

    private static final Object LOCK = new Object();

    private static void work() {
        synchronized (LOCK) {
            System.out.println(Thread.currentThread().getName() + " : Begin ... ...");
            try {
                System.out.println(Thread.currentThread().getName() + " will coming");
                LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : Thread will out.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            work();
        }).start();
        TimeUnit.SECONDS.sleep(2);
        synchronized (LOCK) {
            LOCK.notify();
        }
    }
}
