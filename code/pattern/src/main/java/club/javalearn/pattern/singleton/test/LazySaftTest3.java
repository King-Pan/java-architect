package club.javalearn.pattern.singleton.test;

import club.javalearn.pattern.singleton.lazy.LazySingleton2;
import club.javalearn.pattern.singleton.lazy.LazySingleton3;

import java.util.concurrent.CountDownLatch;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description 是线程安全的，synchronized消耗性能
 */
public class LazySaftTest3 {

    public static void main(String[] args) {
        int count = 100;
        CountDownLatch latch = new CountDownLatch(count);
        CountDownLatch latch2 = new CountDownLatch(count);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            new Thread(){
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"等待中");
                    latch.countDown();
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+"开始执行");
                    System.out.println(LazySingleton3.getInstance());
                    latch2.countDown();
                }
            }.start();
        }


        try {
            latch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("总耗时： " + (System.currentTimeMillis() - startTime));
    }
}
