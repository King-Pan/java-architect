# 12-并发编程之Callable

## Callable定义

```java
package java.util.concurrent;
@FunctionalInterface
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```

* Callable<V>带泛型，可以指定返回值类型
* call()方法抛出异常，并且返回值为泛型V

## Callable基本实现

```java
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
```



## 执行Callable

> 使用FutureTask启动Callable线程

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
    FutureTask<Integer> futureTask2 = new FutureTask<>(new MyThread());

    new Thread(futureTask, "Thread启动Callable").start();
    new Thread(futureTask2, "BB").start();
    int result01 = 100;
    int result02 = futureTask.get();
    System.out.println("计算结果:" + (result01 + result02));

}
```

也可以像自旋锁那样循环判断是否结束

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
    FutureTask<Integer> futureTask2 = new FutureTask<>(new MyThread());

    new Thread(futureTask, "Thread启动Callable").start();
    new Thread(futureTask2, "BB").start();
    int result01 = 100;
    //模拟自旋锁，循环判断futureTask是否结束，没有结束循环，结束走下面的流程,不推荐
    while (!futureTask.isDone()){

    }
    int result02 = futureTask.get();
    System.out.println("计算结果:" + (result01 + result02));
}
```

> 使用线程池执行Callable线程

