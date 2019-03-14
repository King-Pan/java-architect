package club.javalearn.thread.join;

import java.util.stream.IntStream;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class ThreadJoinDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            IntStream.rangeClosed(1, 1000).forEach(i -> System.out.println(Thread.currentThread().getName() + i));
            System.out.println(Thread.currentThread().getName() + " finished.");
        });
        t1.start();
        t1.join();
        System.out.println("main 线程执行结束");
    }
}
