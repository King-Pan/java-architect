package club.javalearn.util;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/8
 * @Description ${DESCRIPTION}
 */
public class SeMaphoreDemo {


    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);


        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("第"+Thread.currentThread().getName()+"辆车进入停车场");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("第"+Thread.currentThread().getName()+"辆车离开停车场");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
