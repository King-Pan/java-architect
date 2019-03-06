package club.javalearn.thread.state;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class ThreadStateDemo {


    private static void attack(){
        System.out.println("fight");
        System.out.println("Current Thread is " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread t = new Thread(()->{
            attack();
        });
        t.start();
        try {
            t.join();
            t.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
