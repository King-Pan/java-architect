package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class DaemonThread {

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " running");
                    Thread.sleep(100000);
                    System.out.println(Thread.currentThread().getName() + " done.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        t.setDaemon(true);
        //runnable  ->running| ->dead| ->blocked


        Thread.sleep(5_000);
        System.out.println(Thread.currentThread().getName());
    }
}
