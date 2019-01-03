package club.javalearn.zookeeperdemo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class WatcherDemo {
    private static ZooKeeper zooKeeper;

    @BeforeClass
    public static void before() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            zooKeeper = new ZooKeeper("47.92.72.146:2181", 4000, new Watcher() {
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
    public void watcherTest() {

        try {
            //1. 创建节点
            zooKeeper.create("/watcher-demo", "watcher-demo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //2. 绑定监听事件 getData、exists、getChildren

            //zooKeeper.exists(path,boolean) 通过默认的监听事件监听
            Stat stat = zooKeeper.exists("/watcher-demo", new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println(event.getType() + "->" + event.getPath());
                }
            });
            //修改节点，path，value，版本号，乐观锁，如果版本号小于当前版本号，修改失败
            stat = zooKeeper.setData("/watcher-demo","watcher-demo-modify".getBytes(),stat.getVersion());
            Thread.sleep(1000);
            // 删除节点: path和版本号
            zooKeeper.delete("/watcher-demo",stat.getVersion());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
