package club.javalearn.thread.t4;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/2/18
 * @Description ${DESCRIPTION}
 */
public class ThreadInterruptDemo2 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread=new Thread(()->{
            while(true){
                try {
                    Thread.sleep(121);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("before:"+thread.isInterrupted());
        thread.interrupt();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("after:"+thread.isInterrupted());
    }
}
