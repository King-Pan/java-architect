package club.javalearn.thread.producter;

import java.util.List;
import java.util.Random;

/**
 * @author king-pan
 * @date 2019/3/12
 * @Description ${DESCRIPTION}
 */
public class ProducterV1 implements Runnable {
    private List<PCData> queue;
    private int len;

    public ProducterV1(List<PCData> queue, int len) {
        this.queue = queue;
        this.len = len;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                Random r = new Random();
                PCData data = new PCData(r.nextInt(500));
                Main.lock.lock();
                if (queue.size() >= len) {
                    Main.empty.signalAll();
                    Main.full.await();
                }
                Thread.sleep(1000);
                queue.add(data);
                Main.lock.unlock();
                System.out.println("生产者ID:" + Thread.currentThread().getId() + " 生产了:" + data.getData());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
