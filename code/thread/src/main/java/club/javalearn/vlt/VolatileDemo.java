package club.javalearn.vlt;


import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/4/12
 * @Description ${DESCRIPTION}
 */
public class VolatileDemo {
    public static void main(String[] args) {
        MyData myData = new MyData();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in.");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTo10();
            System.out.println(Thread.currentThread().getName() + "\t end. value=" + myData.num);
        }).start();


        while (myData.num == 0) {

        }

        System.out.println(Thread.currentThread().getName() + "\t value=" + myData.num);

    }
}


class MyData {
    //内存不可见
    int num;

    public void addTo10() {
        this.num = 10;
    }
}