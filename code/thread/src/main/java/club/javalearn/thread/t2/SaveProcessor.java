package club.javalearn.thread.t2;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author king-pan
 * @date 2019/2/15
 * @Description ${DESCRIPTION}
 */
public class SaveProcessor extends Thread implements RequestProcessor {
    LinkedBlockingQueue<Request> linkedBlockingQueue = new LinkedBlockingQueue();

    @Override
    public void processorRequest(Request request) {
        linkedBlockingQueue.add(request);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = linkedBlockingQueue.take();
                System.out.println("save data:" + request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
