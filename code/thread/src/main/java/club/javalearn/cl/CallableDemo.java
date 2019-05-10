package club.javalearn.cl;

import java.util.concurrent.*;

/**
 * @author king-pan
 * @date 2019/5/10
 * @Description ${DESCRIPTION}
 * 1. 已经有Thread、Runnable为什么还需要Callable呢?
 * 1.1 线程有返回值
 * 1.2 异步编程,尽量避免阻塞(Runnable,)
 */
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
        FutureTask<Integer> futureTask2 = new FutureTask<>(new MyThread());

        new Thread(futureTask, "Thread启动Callable").start();
        new Thread(futureTask2, "BB").start();


        //模拟自旋锁，循环判断futureTask是否结束，没有结束循环，结束走下面的流程
        while (!futureTask.isDone()){

        }
        int result01 = 100;
        int result02 = futureTask.get();
        System.out.println("计算结果:" + (result01 + result02));

    }
}

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "\t 获取结果:1024");
        return 1024;
    }
}
