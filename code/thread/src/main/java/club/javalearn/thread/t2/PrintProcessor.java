package club.javalearn.thread.t2;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author king-pan
 * @date 2019/2/15
 * @Description ${DESCRIPTION}
 */
public class PrintProcessor extends Thread implements RequestProcessor {
    LinkedBlockingQueue<Request> linkedBlockingQueue = new LinkedBlockingQueue<>();

    private final RequestProcessor nextProcessor;

    public PrintProcessor(RequestProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }


    @Override
    public void run() {
        while (true) {
            try {
                Request request = linkedBlockingQueue.take();
                System.out.println("print data:" + request);
                nextProcessor.processorRequest(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processorRequest(Request request) {
        linkedBlockingQueue.add(request);
    }
}
