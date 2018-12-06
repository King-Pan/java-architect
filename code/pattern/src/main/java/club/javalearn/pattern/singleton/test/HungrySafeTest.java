package club.javalearn.pattern.singleton.test;

import club.javalearn.pattern.singleton.hungry.HungrySingleton;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description 线程安全
 */
public class HungrySafeTest {


    public static void main(String[] args) {
        int count =10;
        final CountDownLatch latch = new CountDownLatch(count);

        //final Set<HungrySingleton> syncSet = Collections.synchronizedSet(new HashSet<>());
        for (int i = 0 ;i<count;i++){
            new Thread(){
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName()+"等待中......");
                        latch.countDown(); //计数
                        latch.await(); //等待计数为0执行await后面的代码
                        System.out.println("i="+ Thread.currentThread().getName());
                        System.out.println(HungrySingleton.getInstance());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }.start();

        }

        System.out.println("主线程执行完毕.");


    }
}
