package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class RunnableDemo {


    public static void main(String[] args) {
        MyRunnable r1 = new MyRunnable("Runnable1");
        MyRunnable r2 = new MyRunnable("Runnable2");
        MyRunnable r3 = new MyRunnable("Runnable3");
        MyRunnable r4 = new MyRunnable("Runnable4");

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        Thread t4 = new Thread(r4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
