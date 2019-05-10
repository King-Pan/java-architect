package club.javalearn.util;

import java.util.concurrent.CountDownLatch;

/**
 * @author king-pan
 * @date 2019/5/8
 * @Description ${DESCRIPTION}
 */
public class CountDownLatchDemo {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println("秦灭六国:" + Thread.currentThread().getName());
                countDownLatch.countDown();
            }, CountryEnum.forEachCountry(i).getMsg()).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("大秦帝国统一中华");

    }
}
