package club.javalearn.thread.wait;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class InterruptDemo {


    public static void main(String[] args) throws InterruptedException {
        Runnable interruptTask = new Runnable() {
            @Override
            public void run() {
                int i = 0;
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Thread.sleep(50);
                        i++;
                        System.out.println(Thread.currentThread().getName() + "(" + Thread.currentThread().getState() + ") loop i=" + i);
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + "(" + Thread.currentThread().getState() + ") catch exception i=" + i);
                }
            }
        };

        Thread t1 = new Thread(interruptTask, "t1");
        System.out.println(t1.getName() + "( " + t1.getState() + " ) is new");

        t1.start();
        System.out.println(t1.getName() + "( " + t1.getState() + " ) is started");
        Thread.sleep(300);
        t1.interrupt();
        System.out.println(t1.getName() + "( " + t1.getState() + " ) is interrupted");
    }
}
