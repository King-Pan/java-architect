package club.javalearn.basic;

/**
 * @author king-pan
 * @date 2019/11/22 23:54
 */
public class VolatileTest {

    public static void main(String[] args) {
        VolatileCs test = new VolatileCs();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.add();
                System.out.println(Thread.currentThread().getName() + "-num=" + test.num);
            }
        }).start();

        while (test.num == 0) {

        }
        System.out.println(Thread.currentThread().getName() + "--num=" + test.num);

    }


}

class VolatileCs {
    int num;

    public synchronized void add() {
        num = 10;
    }
}
