package club.javalearn.thread.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/11
 * @Description ${DESCRIPTION}
 */
public class ThisLock2 {

    private final Object LOCK = new Object();

    public static void main(String[] args) {
        ThisLock2 thisLock = new ThisLock2();
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


    public  void  m1() throws InterruptedException {
        synchronized (LOCK){
            System.out.println(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);
        }

    }
    public  void  m2() throws InterruptedException {
        synchronized(LOCK){
            System.out.println(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);

        }
    }

}
