package club.javalearn.thread.producter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author king-pan
 * @date 2019/3/12
 * @Description ${DESCRIPTION}
 */
public class Main {
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition empty = lock.newCondition();
    public static Condition full = lock.newCondition();

    public static void main(String[] args) {
        List<PCData> queue = new ArrayList<PCData>();
        int length = 10;
        ProducterV1 p1 = new ProducterV1(queue, length);
        ProducterV1 p2 = new ProducterV1(queue, length);
        ProducterV1 p3 = new ProducterV1(queue, length);
        ConsumerV1 c1 = new ConsumerV1(queue);
        ConsumerV1 c2 = new ConsumerV1(queue);
        ConsumerV1 c3 = new ConsumerV1(queue);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(p1);
        service.execute(p2);
        service.execute(p3);
        service.execute(c1);
        service.execute(c2);
        service.execute(c3);
    }
}
