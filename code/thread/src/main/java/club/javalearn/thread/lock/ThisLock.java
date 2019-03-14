package club.javalearn.thread.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/11
 * @Description ${DESCRIPTION}
 */
public class ThisLock {


    public static void main(String[] args) {
        ThisLock thisLock = new ThisLock();
        new Thread(()->{
            try {
                thisLock.m1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                thisLock.m2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public synchronized void  m1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(10);
    }
    public synchronized void  m2() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(10);
    }

}
