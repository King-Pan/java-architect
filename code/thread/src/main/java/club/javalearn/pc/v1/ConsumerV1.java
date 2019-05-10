package club.javalearn.pc.v1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author king-pan
 * @date 2019/5/9
 * @Description ${DESCRIPTION}
 * 1. 线程    操作      资源类
 * 2. 判断    干活      通知
 * 3. 防止虚假判断
 */
public class ConsumerV1 {

    public static void main(String[] args) {

    }
}

class ShareData {
    private int number = 0;
    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public void increment() throws Exception {
        lock.lock();
        try {
            while (number != 0) {
                //等待消费,不能生产
                condition.await();
            }
            //生产者生产
            number++;
            System.out.println(Thread.currentThread().getName() + " \t " + number);
            //通知
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception {
        lock.lock();
        try {
            //没有资源消费，等待生产者生产
            while (number == 0) {
                condition.await();
            }
            //有资源，消费者消费
            number--;
            //通知
            condition.signalAll();
            System.out.println(Thread.currentThread().getName() + "\t " + number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "生产者").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "消费者").start();
    }
}