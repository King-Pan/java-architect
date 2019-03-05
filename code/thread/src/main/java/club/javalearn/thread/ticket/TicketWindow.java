package club.javalearn.thread.ticket;

/**
 * @author king-pan
 * @date 2019/2/26
 * @Description ${DESCRIPTION}
 */
public class TicketWindow implements Runnable {

    @Override
    public void run() {
        while (true) {
            String ticket = null;
            try {
                ticket = TicketMake.ticket.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("请【" + ticket + "】到窗口【" + Thread.currentThread().getName() + "】办理业务");
        }
    }
}
