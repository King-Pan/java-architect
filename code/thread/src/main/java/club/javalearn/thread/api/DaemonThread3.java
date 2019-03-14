package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class DaemonThread3 {

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " running");
                    Thread.sleep(1000000000);
                    System.out.println(Thread.currentThread().getName() + " done.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.setDaemon(true);
        t.start();


        Thread.sleep(50_000);
        System.out.println(Thread.currentThread().getName());
    }
}
