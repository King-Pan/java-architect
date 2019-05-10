package club.javalearn.thc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/10
 * @Description ${DESCRIPTION}
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        System.out.println("CPU核心数:"+Runtime.getRuntime().availableProcessors());


        //线程池中5个处理线程  fixed固定个数
        //某个具体的银行网点，共有5个柜台服务人员
        //ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //线程池只有一个线程
        //ExecutorService threadPool = Executors.newSingleThreadExecutor();
        //线程池有N个线程
        ExecutorService threadPool = Executors.newCachedThreadPool();

        //newFixedThreadPool、newSingleThreadExecutor、newCachedThreadPool底层都是new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        //                                      60L, TimeUnit.SECONDS,
        //                                      new SynchronousQueue<Runnable>());

        //10个用户来办理业务,每个用户就是一个来自外部的请求线程
        try{
            for(int i=0;i<10;i++){
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+" \t 正在办理业务");
                    try {
                        TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            threadPool.shutdown();
        }


    }
}
