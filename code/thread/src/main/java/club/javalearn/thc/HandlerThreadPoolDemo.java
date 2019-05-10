package club.javalearn.thc;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/10
 * @Description ${DESCRIPTION}
 */
public class HandlerThreadPoolDemo {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(2,
                        5,
                        60L,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(3),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy());

        try{
            for(int i=0;i<10;i++){
                threadPoolExecutor.execute(()->{
                    System.out.println(Thread.currentThread().getName() +"\t 正在办理业务");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            threadPoolExecutor.shutdown();
        }

    }

}
