package club.javalearn.thread2.wait;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description
 *
 *  1. 所有对象都有一个wait set，用来存放调用了该对象wait方法之后进入WAIT、TIME_WAIT状态的线程
 *  2. 线程notify后，不一定立即执行
 *  3. 线程从wait状态唤醒没有顺序。
 */
public class WaitSet {

    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 10).forEach(i -> new Thread((String.valueOf(i))) {
            @Override
            public void run() {
                synchronized (LOCK) {
                    try {
                        Optional.of(Thread.currentThread().getName() + " will come to wait set.").ifPresent(System.out::println);
                        LOCK.wait();
                        Optional.of(Thread.currentThread().getName() + " will live wait set.").ifPresent(System.out::println);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start());

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        IntStream.rangeClosed(1,10).forEach(i->{
            synchronized (LOCK){
                try {
                    LOCK.notify();
                    System.out.println("随机notify唤醒了一个线程");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
