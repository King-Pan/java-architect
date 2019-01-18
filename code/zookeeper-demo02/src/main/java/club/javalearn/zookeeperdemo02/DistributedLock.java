package club.javalearn.zookeeperdemo02;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author king-pan
 * @date 2019/1/16
 * @Description ${DESCRIPTION}
 */
public class DistributedLock implements Watcher, Lock {

    private ZooKeeper zooKeeper = null;
    /**
     * 定义根节点
     */
    private static final String ROOT_LOCK = "/locks";
    /**
     * 等待前一个锁
     */
    private String waitLock;
    /**
     * 表示当前的锁
     */
    private String currentLock;

    private CountDownLatch countDownLatch;

    public DistributedLock() {
        try {
            zooKeeper = new ZooKeeper("47.92.72.146:2181", 4000, this);
            //判断根节点是否存在
            Stat stat = zooKeeper.exists(ROOT_LOCK, false);
            if (stat == null) {
                //如果不存在，创建根节点
                zooKeeper.create(ROOT_LOCK, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lock() {
        //获取锁成功
        if (this.tryLock()) {
            System.out.println(Thread.currentThread().getName() + "->" + currentLock + "->获取锁成功");
            return;
        }
        try {
            //没有获得锁，继续等待获得锁
            waitForLock(waitLock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean waitForLock(String prev) throws Exception {
        //监听当前节点的上一个节点
        Stat stat = zooKeeper.exists(prev, true);
        if (stat != null) {
            System.out.println(Thread.currentThread().getName() + "->等待锁" + ROOT_LOCK + "/" + prev + "释放");
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            //TODO watcher 触发以后，还需要再次判断当前等待的节点是不是最小的
            System.out.println(Thread.currentThread().getName() + "->获得锁成功");
        }
        return true;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {

        try {
            //创建临时有序节点
            currentLock = zooKeeper.create(ROOT_LOCK + "/", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + "->" + currentLock + ",尝试竞争锁");
            //获取根节点下所有的子节点
            List<String> children = zooKeeper.getChildren(ROOT_LOCK, false);

            SortedSet<String> sortedSet = new TreeSet<>();
            for (String c : children) {
                sortedSet.add(ROOT_LOCK + "/" + c);

            }
            //获取当前根节点下最小的子节点
            String firstNode = sortedSet.first();

            SortedSet<String> lessThenMe = ((TreeSet<String>) sortedSet).headSet(currentLock);
            //当前锁的节点与子节点集合中对比，如果相等，则当前节点是最小的
            if (currentLock.equals(firstNode)) {
                return true;
            }
            if (!lessThenMe.isEmpty()) {
                //获得比当前节点更小的最后一个节点，设置给waitLock
                waitLock = lessThenMe.last();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        System.out.println(Thread.currentThread().getName() + "->释放锁" + currentLock);
        try {
            zooKeeper.delete(currentLock, -1);
            currentLock = null;
            zooKeeper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent event) {
        if (this.countDownLatch != null) {
            this.countDownLatch.countDown();
        }
    }
}
