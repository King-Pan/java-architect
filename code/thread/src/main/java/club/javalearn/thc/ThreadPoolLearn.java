package club.javalearn.thc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author king-pan
 * @date 2019/5/11
 * @Description ${DESCRIPTION}
 */
public class ThreadPoolLearn {


    public static void main(String[] args) {
        //只有一个线程的线程池,
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        ExecutorService executorService1 = Executors.newFixedThreadPool(3);

        ExecutorService executorService2 = Executors.newCachedThreadPool();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    }


}
