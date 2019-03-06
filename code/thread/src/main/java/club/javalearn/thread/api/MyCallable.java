package club.javalearn.thread.api;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class MyCallable implements Callable<String> {
    @Override
    public String call() throws InterruptedException {
        String value = "test";
        System.out.println("ready to work");
        TimeUnit.SECONDS.sleep(5);
        System.out.println("task done");
        return value;
    }
}
