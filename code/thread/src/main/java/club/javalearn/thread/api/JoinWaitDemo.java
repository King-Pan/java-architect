package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class JoinWaitDemo {

    public static void main(String[] args) throws InterruptedException {
        CycleWait cycleWait = new CycleWait();
        Thread t = new Thread(cycleWait);
        t.start();
        t.join();
        System.out.println(cycleWait.value);

    }
}
