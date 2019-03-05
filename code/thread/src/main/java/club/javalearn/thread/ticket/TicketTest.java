package club.javalearn.thread.ticket;

/**
 * @author king-pan
 * @date 2019/2/26
 * @Description ${DESCRIPTION}
 */
public class TicketTest {

    public static void main(String[] args) {
        new Thread(new TicketMake()).start();

        TicketWindow ticketWindow = new TicketWindow();

        new Thread(ticketWindow, "现金窗口1").start();
        new Thread(ticketWindow, "现金窗口2").start();
        new Thread(ticketWindow, "vip窗口1").start();
        new Thread(ticketWindow, "vip窗口2").start();


    }
}
