package club.javalearn.thread.api;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        Future<String> future = newCachedThreadPool.submit(new MyCallable());
        if(!future.isDone()){
            System.out.println("task has not finished, please wait");
        }
        try {
            System.out.println("task return : " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            newCachedThreadPool.shutdown();
        }
    }
}
