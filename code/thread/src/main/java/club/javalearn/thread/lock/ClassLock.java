package club.javalearn.thread.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/11
 * @Description ${DESCRIPTION}
 */
public class ClassLock {

    public static void main(String[] args) {
        ClassLock classLock = new ClassLock();
        ClassLock classLock2 = new ClassLock();
        new Thread(()->{
            try {
                classLock.m1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                classLock.m2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                classLock2.m1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                classLock2.m2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public  void  m1() throws InterruptedException {
        synchronized (ClassLock.class){
            System.out.println(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);
        }

    }
    public  void  m2() throws InterruptedException {
        synchronized(ClassLock.class){
            System.out.println(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(10);

        }
    }
}
