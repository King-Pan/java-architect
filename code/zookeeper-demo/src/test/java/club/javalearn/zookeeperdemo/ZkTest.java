package club.javalearn.zookeeperdemo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkTest {

    private static ZooKeeper zooKeeper;

    @BeforeClass
    public static void before() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            ZooKeeper zooKeeper = new ZooKeeper("47.92.72.146:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("默认事件:" + event.getType());
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        //如果收到服务端的响应事件，连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("47.92.72.146:2181", 4000000, null);
            System.out.println(zooKeeper.getState());//connecting
            Thread.sleep(1000);
            System.out.println(zooKeeper.getState());//connected
            zooKeeper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("47.92.72.146:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("默认事件:" + event.getType());
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        //如果收到服务端的响应事件，连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            System.out.println(zooKeeper.getState());//connecting
            Thread.sleep(1000);
            System.out.println(zooKeeper.getState());//connected
            zooKeeper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createTest() {
        try {
            //创建节点: 第一个参数节点名称，第二个参数节点值的byte数组，第三个参数，权限，第四个参数节点类型
            zooKeeper.create("/zk-king", "king-pan".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            Thread.sleep(1000);
            Stat stat = new Stat();
            // 获取节点值
            byte[] bytes = zooKeeper.getData("/zk-king", null, stat);
            System.out.println(new String(bytes));
            //修改节点的值
            zooKeeper.setData("/zk-king", "pan-king".getBytes(), stat.getVersion());
            bytes = zooKeeper.getData("/zk-king", null, stat);
            System.out.println(new String(bytes));

            //删除节点
            zooKeeper.delete("/zk-king",stat.getVersion());


        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
