package club.javalearn.zookeeperdemo02;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author king-pan
 * @date 2019/1/16
 * @Description ${DESCRIPTION}
 */
public class LockTest {


    @Test
    public void test() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    DistributedLock distributedLock = new DistributedLock();
                    //获得锁
                    distributedLock.lock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Thread-" + i).start();
            countDownLatch.countDown();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void t1(){
        DistributedLock distributedLock = new DistributedLock();
        //获得锁
        distributedLock.lock();
    }
}
