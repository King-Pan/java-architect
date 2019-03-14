package club.javalearn.thread.api;

import java.util.Optional;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class ThreadIdDemo {

    public static void main(String[] args) {
        Optional.of(Thread.currentThread().getName()).ifPresent(System.out::println);
        Optional.of(Thread.currentThread().getId()).ifPresent(System.out::println);
        //线程的优先级默认为5
        Optional.of(Thread.currentThread().getPriority()).ifPresent(System.out::println);

    }
}
