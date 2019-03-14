package club.javalearn.thread.dead;

/**
 * @author king-pan
 * @date 2019/2/28
 * @Description ${DESCRIPTION}
 */
public class DeadLock {

    private static Object obj1 = new Object();
    private static Object obj2 = new Object();

    public static void main(String[] args) {
        new Thread(new Thread1(),"线程1").start();
        new Thread(new Thread2(),"线程2").start();
    }

    static class Thread1 implements Runnable {
        @Override
        public void run() {
            synchronized (obj1) {
                System.out.println(Thread.currentThread().getName() + "获取到: obj1锁");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"等待obj2锁");
                synchronized (obj2) {
                    System.out.println(Thread.currentThread().getName() + "获取到obj2锁");
                }
            }
        }
    }


    static class Thread2 implements Runnable {
        @Override
        public void run() {
            synchronized (obj2) {
                System.out.println(Thread.currentThread().getName() + "获取到: obj2锁");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"等待obj1锁");
                synchronized (obj1) {
                    System.out.println(Thread.currentThread().getName() + "获取到obj1锁");
                }
            }
        }
    }
}
