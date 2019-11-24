package club.javalearn.face;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author king-pan
 * @date 2019/5/9
 * @Description ${DESCRIPTION}
 */
public class ABCForEach10 {


}

class ShareResource {
    /**
     * A=1,B=2,C=3
     */
    private volatile AtomicInteger number = new AtomicInteger(1);
    private Lock lock = new ReentrantLock();
    private Condition a1 = lock.newCondition();
    private Condition b2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print5() {
        lock.lock();
        try {
            while (this.number.get() % 3 != 1) {
                a1.await();
            }
            print(5);
            number.incrementAndGet();
            b2.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10() {
        lock.lock();
        try {
            while (this.number.get() % 3 != 2) {
                b2.await();
            }
            print(10);
            number.incrementAndGet();
            c3.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15() {
        lock.lock();
        try {
            while (this.number.get() % 3 != 0) {
                c3.await();
            }
            print(15);
            number.incrementAndGet();
            a1.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void print(int num) {
        for (int i = 1; i <= num; i++) {
            System.out.println(Thread.currentThread().getName() + "\t" + i);
        }
    }

    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print5();
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print10();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print15();
            }
        }, "C").start();
    }

}
