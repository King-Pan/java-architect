package club.javalearn.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author king-pan
 * @date 2019/4/12
 * @Description ${DESCRIPTION}
 *
 * 1. cas是什么?
 *
 */
public class CASDemo {


    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5,2019) + "\t current value=" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5,2020) + "\t current value=" + atomicInteger.get());
    }
}
