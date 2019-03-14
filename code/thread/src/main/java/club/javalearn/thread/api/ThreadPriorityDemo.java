package club.javalearn.thread.api;

import java.util.Optional;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 线程优先级: 从1 到10 数字越大优先级越高，主要看操作系统调度
 */
public class ThreadPriorityDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Optional.of(Thread.currentThread().getName() + "index-" + i).ifPresent(System.out::println);
            }
            System.out.println("=================================t1执行完毕");
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Optional.of(Thread.currentThread().getName() + "index-" + i).ifPresent(System.out::println);
            }
            System.out.println("-----------------------------------t2执行完毕");

        });
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Optional.of(Thread.currentThread().getName() + "index-" + i).ifPresent(System.out::println);
            }
            System.out.println("++++++++++++++++++++++++++++++++++++++++t2执行完毕");
        });
        t1.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
        t3.start();
    }
}
