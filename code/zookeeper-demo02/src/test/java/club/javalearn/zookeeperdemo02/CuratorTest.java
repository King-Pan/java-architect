package club.javalearn.zookeeperdemo02;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author king-pan
 * @date 2019/1/16
 * @Description ${DESCRIPTION}
 */
public class CuratorTest {


    @Test
    public void test() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().build();
                    InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, "/mylocks");
                    interProcessMutex.acquire();
                } catch (Exception e) {
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
}
