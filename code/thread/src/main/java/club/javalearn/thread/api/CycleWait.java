package club.javalearn.thread.api;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class CycleWait implements Runnable {

    public String value;

    @Override
    public void run() {

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        value = "we have data now";
    }

    public static void main(String[] args) {
        CycleWait cycleWait = new CycleWait();
        new Thread(cycleWait).start();
        int index = 1;
        while (cycleWait.value == null) {
            try {
                System.out.println("休眠" + (index++) + "次");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(cycleWait.value);
    }
}
