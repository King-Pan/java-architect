package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class DaemonThread4 {

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread() {

            @Override
            public void run() {
                Thread innerThread = new Thread(() -> {
                    try {
                        while (true) {
                            System.out.println("Do some thing for health check.");
                            Thread.sleep(1_000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                //setDemmon必须在start方法之前
                innerThread.setDaemon(true);
                innerThread.start();

                try {
                    Thread.sleep(1_000);
                    System.out.println("T thread finish done.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}
