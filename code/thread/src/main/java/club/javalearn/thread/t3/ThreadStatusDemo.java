package club.javalearn.thread.t3;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/2/18
 * @Description ${DESCRIPTION}
 */
public class ThreadStatusDemo {

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "timewaiting").start();

        new Thread(()->{
            while(true){
                synchronized (ThreadStatusDemo.class){
                    try {
                        ThreadStatusDemo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"waiting").start();


        new Thread(new BlockDemo(),"BlockDemo-1").start();

        new Thread(new BlockDemo(),"BlockDemo-2").start();

        new Thread(()->{},"").start();
    }

    static class BlockDemo extends Thread{
        @Override
        public void run(){
            synchronized (BlockDemo.class){
                while(true){
                    try {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
