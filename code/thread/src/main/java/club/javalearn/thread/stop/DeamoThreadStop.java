package club.javalearn.thread.stop;

/**
 * @author king-pan
 * @date 2019/3/8
 * @Description ${DESCRIPTION}
 */
public class DeamoThreadStop {

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    System.out.println("i=" + i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            t1.setDaemon(true);
            t1.start();
            System.out.println("开始执行任务");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务执行完成");
        });

        t2.start();


    }
}
