package club.javalearn.thread.ticket;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author king-pan
 * @date 2019/2/26
 * @Description 叫号机
 */
public class TicketMake implements Runnable {

    public static LinkedBlockingQueue<String> ticket = new LinkedBlockingQueue<>();


    private final static int MAX = 50;

    @Override
    public void run() {
        int i = 1;
        while (i <= MAX) {
            ticket.add("号码:" + i);
            ++i;
        }
    }
}
