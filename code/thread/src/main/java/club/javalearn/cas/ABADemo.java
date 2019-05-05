package club.javalearn.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author king-pan
 * @date 2019/5/5
 * @Description ${DESCRIPTION}
 */
public class ABADemo {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);

    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        System.out.println("========================以下是ABA问题的产生========================");
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
            System.out.println(Thread.currentThread().getName() + " 执行完毕");
        }, "t1").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(atomicReference.compareAndSet(100, 2019));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();


        System.out.println("========================以下是ABA问题的解决========================");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号" + stamp);
            try {
                TimeUnit.SECONDS.sleep(1);
                stampedReference.compareAndSet(100, 101, stampedReference.getStamp(), stampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + "\t 第二次版本号" + stamp);
                stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + "\t 第三次版本号" + stamp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3").start();

        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号" + stamp);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result = stampedReference.compareAndSet(101, 2019, stamp, stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 修改成功：" + result);
            do{
                stamp = stampedReference.getStamp();
                System.out.println(Thread.currentThread().getName() + "\t 当前实际版本号:" + stamp);
            }while (!stampedReference.compareAndSet(100,2019,stamp,stampedReference.getStamp()+1));
            System.out.println(Thread.currentThread().getName() + "\t 最新版本号：" + stampedReference.getStamp() + ",最新值:" + stampedReference.getReference());
        }, "t4").start();

    }
}
