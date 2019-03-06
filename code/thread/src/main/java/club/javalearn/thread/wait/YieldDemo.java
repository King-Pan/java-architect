package club.javalearn.thread.wait;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class YieldDemo {

    public static void main(String[] args) {
        Runnable yieldTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + ":  i=" + i);
                    if(i ==5) {
                        Thread.yield();
                    }
                }
            }
        };
        Thread t1 = new Thread(yieldTask,"A");
        Thread t2 = new Thread(yieldTask,"B");
        t1.start();
        t2.start();
    }
}
