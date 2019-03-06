package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class ThreadDemo {
    public static void main(String[] args) {
        MyThread t1 = new MyThread("Thread1");
        MyThread t2 = new MyThread("Thread2");
        MyThread t3 = new MyThread("Thread3");
        MyThread t4 = new MyThread("Thread4");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
