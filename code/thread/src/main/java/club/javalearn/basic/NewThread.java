package club.javalearn.basic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author king-pan
 * @date 2019/11/21 23:46
 */
public class NewThread {

    private static class UseRun implements Runnable {
        @Override
        public void run() {
            while (true) {
                System.out.println(" implements Runnable.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new UseRun());
        t1.start();
        FutureTask<String> useCall = new FutureTask<String>(new UseCall());
        new Thread(useCall).start();
        try {
            String s = useCall.get();
            System.out.println("result: " + s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private static class UseCall implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println(" implements Callable.");
            return "call method exec";
        }
    }

}
