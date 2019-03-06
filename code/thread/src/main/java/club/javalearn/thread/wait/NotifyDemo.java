package club.javalearn.thread.wait;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class NotifyDemo {

    private volatile boolean go = false;

    public static void main(String[] args) throws InterruptedException {

        final NotifyDemo notifyDemo = new NotifyDemo();
        Runnable waitTask = new Runnable() {
            @Override
            public void run() {
                try {
                    notifyDemo.shouldGo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " finish Execution.");
            }
        };

        Runnable notifyTask = new Runnable() {
            @Override
            public void run() {
                try {
                    notifyDemo.go();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " finish Execution.");
            }
        };

        Thread t1 = new Thread(waitTask, "WT1");
        Thread t2 = new Thread(waitTask, "WT2");
        Thread t3 = new Thread(waitTask, "WT3");
        Thread t4 = new Thread(notifyTask, "NT1");
        t1.start();
        t2.start();
        t3.start();
        TimeUnit.SECONDS.sleep(10);
        t4.start();
    }

    private synchronized void shouldGo() throws InterruptedException {
        while (go != true) {
            System.out.println(Thread.currentThread().getName() + " is going to wait on this object");
            wait();
            System.out.println(Thread.currentThread().getName() + " is woken up");
        }
        go = false;
    }

    private  synchronized void go() {
        while (go == false) {
            System.out.println(Thread.currentThread().getName() + " is going to notify all or one thread wait on this object");
            go = true;
            //notify();
            notifyAll();
        }
    }
}
