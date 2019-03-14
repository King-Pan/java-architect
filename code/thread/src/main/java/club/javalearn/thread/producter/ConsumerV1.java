package club.javalearn.thread.producter;

import java.util.List;

/**
 * @author king-pan
 * @date 2019/3/12
 * @Description ${DESCRIPTION}
 */
public class ConsumerV1 implements Runnable {

    private List<PCData> queue;

    public ConsumerV1(List<PCData> queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        try {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                PCData data = null;
                synchronized (queue) {
                    if (queue.size() == 0) {
                        queue.wait();
                        queue.notifyAll();
                    }
                    data = queue.remove(0);
                }
                System.out.println(
                        Thread.currentThread().getId() + " 消费了:" + data.getData() + " result:" + (data.getData() * data.getData()));
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
