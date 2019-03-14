package club.javalearn.thread.api;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author king-pan
 * @date 2019/2/15
 * @Description ${DESCRIPTION}
 */
public class CallableDemo implements Callable<String> {


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CallableDemo callableDemo = new CallableDemo();
        Future<String> future = executorService.submit(callableDemo);
        try {
            System.out.println(future.get());
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String call() throws Exception {
        return "call:" + new Date();
    }
}
