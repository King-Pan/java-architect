package club.javalearn.thread.api;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String>  futureTask = new FutureTask<String>(new MyCallable());
        new Thread(futureTask).start();
        if(!futureTask.isDone()){
            System.out.println("task has not finished, please wait");
        }
        System.out.println("task return : " + futureTask.get());
    }
}
