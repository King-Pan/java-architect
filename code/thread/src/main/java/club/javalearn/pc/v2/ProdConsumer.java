package club.javalearn.pc.v2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author king-pan
 * @date 2019/5/9
 * @Description ${DESCRIPTION}
 * volatile/CAS/atomicInteger/BlockingQueue/线程交互/原子引用
 */
public class ProdConsumer {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);
        MyResource myResource = new MyResource(blockingQueue);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t启动成功");
            try {
                myResource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "生产者").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t启动成功");
            try {
                myResource.myConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "消费者").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("5秒中时间到，停止生产者消费者");
        myResource.stop();

    }
}


class MyResource {
    /**
     * 默认开启生产者消费者
     */
    private volatile boolean FLAG = true;
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println("构造器:" + this.blockingQueue.getClass().getName());
    }

    public void myProd() throws Exception {
        boolean retValue;
        String data = null;
        while (FLAG) {
            data = atomicInteger.incrementAndGet() + "";
            retValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);
            if (retValue) {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列" + data + "成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列" + data + "失败");
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "\t 生产者停止生产，FLAG=false,生产结束");
    }


    public void myConsumer() throws Exception {
        String result = null;
        while (FLAG) {
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (result == null || result.equalsIgnoreCase("")) {
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t 消费队列 :" + result + "失败");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t 消费队列 :" + result + "成功");
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.FLAG = false;
    }

}