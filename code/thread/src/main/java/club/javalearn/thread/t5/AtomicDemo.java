package club.javalearn.thread.t5;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/2/18
 * @Description ${DESCRIPTION}
 */
public class AtomicDemo {
    public static void main(String[] args) throws InterruptedException {
        for (int i=0;i<1000;i++){
            new Thread(AtomicDemo::inc).start();
        }
        TimeUnit.SECONDS.sleep(4);
        System.out.println("运行结果:" + count);
    }

    private static int count=0;
    public static void inc(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
    }
}
