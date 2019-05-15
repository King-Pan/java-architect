package club.javalearn.thc;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/11
 * @Description ${DESCRIPTION}
 */
public class DeadThreadDemo {


    public static void main(String[] args) {
        DeadDemo deadDemo = new DeadDemo();
        new Thread(() -> {
            deadDemo.a();
        }, "线程A").start();

        new Thread(() -> {
            deadDemo.b();
        }, "线程B").start();
    }
}

class DeadDemo {
    private Object lock = new Object();
    private Object lock2 = new Object();


    public void a() {

        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + "\t 执行方法a");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"\t等待获取b的锁");
            b();
        }
    }

    public void b() {
        synchronized (lock2) {
            System.out.println(Thread.currentThread().getName() + "\t 执行方法b");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"\t等待获取a的锁");
            a();
        }
    }
}
