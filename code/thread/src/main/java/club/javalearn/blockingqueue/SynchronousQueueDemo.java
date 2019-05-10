package club.javalearn.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/9
 * @Description ${DESCRIPTION}
 */
public class SynchronousQueueDemo {


    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>(true);
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " \t put a begin");
                blockingQueue.put("a");
                System.out.println(Thread.currentThread().getName() + " \t put a  end");
                System.out.println(Thread.currentThread().getName() + " \t put b  begin");
                blockingQueue.put("b");
                System.out.println(Thread.currentThread().getName() + " \t put b  end");
                System.out.println(Thread.currentThread().getName() + " \t put c   begin");
                blockingQueue.put("c");
                System.out.println(Thread.currentThread().getName() + " \t put c   end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "生产者").start();
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "\t take元素:" + blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "消费者").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
                System.out.println(Thread.currentThread().getName() + "\t take元素:" + blockingQueue.take());
                TimeUnit.SECONDS.sleep(5L);
                System.out.println(Thread.currentThread().getName() + "\t take元素:" + blockingQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "消费者2").start();

    }
}
