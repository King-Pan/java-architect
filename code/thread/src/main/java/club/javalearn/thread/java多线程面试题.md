# Java多线程面试题

## 常见面试题

### jvm是多线程还是单线程

```java
    jvm是多线，在启动jvm时，后台会启动垃圾回收线程+前台线程
```

### thread类的start()与run()的区别

   * start() 方法会创建一个子线程，并启动
   * run() 是thread类的普通方法的调用

```java
public class StartAndRun {

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            attack();
        });
        t.start();
        //t.run();
        System.out.println("current main thread is : " + Thread.currentThread().getName());
    }

    private static void attack() {
        System.out.println("fight");
        System.out.println("Current Thread is : " + Thread.currentThread().getName());
    }
}
```
### Thread与Runnable的区别

* Thread是一个类Runnable是一个接口
* Thread实现了Runnable接口，使得run()支持多线程
* 因类的单继承原则，推荐使用Runnable接口达到多继承的目的


### 如何给run方法传参

* 构造方法
* 成用变量
* 回调函数

### 线程的返回值

* 轮询等待法
* 使用Thread类的join方法阻塞当前线程以等待子线程处理完毕
* 通过Callable接口实现 通过FutureTask or 线程池获取


### 线程的状态

java.lang.Thread.State

```java
public enum State {
        /**
         * Thread state for a thread which has not yet started.
         */
        NEW,

        /**
         * Thread state for a runnable thread.  A thread in the runnable
         * state is executing in the Java virtual machine but it may
         * be waiting for other resources from the operating system
         * such as processor.
         */
        RUNNABLE,

        /**
         * Thread state for a thread blocked waiting for a monitor lock.
         * A thread in the blocked state is waiting for a monitor lock
         * to enter a synchronized block/method or
         * reenter a synchronized block/method after calling
         * {@link Object#wait() Object.wait}.
         */
        BLOCKED,

        /**
         * Thread state for a waiting thread.
         * A thread is in the waiting state due to calling one of the
         * following methods:
         * <ul>
         *   <li>{@link Object#wait() Object.wait} with no timeout</li>
         *   <li>{@link #join() Thread.join} with no timeout</li>
         *   <li>{@link LockSupport#park() LockSupport.park}</li>
         * </ul>
         *
         * <p>A thread in the waiting state is waiting for another thread to
         * perform a particular action.
         *
         * For example, a thread that has called <tt>Object.wait()</tt>
         * on an object is waiting for another thread to call
         * <tt>Object.notify()</tt> or <tt>Object.notifyAll()</tt> on
         * that object. A thread that has called <tt>Thread.join()</tt>
         * is waiting for a specified thread to terminate.
         */
        WAITING,

        /**
         * Thread state for a waiting thread with a specified waiting time.
         * A thread is in the timed waiting state due to calling one of
         * the following methods with a specified positive waiting time:
         * <ul>
         *   <li>{@link #sleep Thread.sleep}</li>
         *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
         *   <li>{@link #join(long) Thread.join} with timeout</li>
         *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
         *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
         * </ul>
         */
        TIMED_WAITING,

        /**
         * Thread state for a terminated thread.
         * The thread has completed execution.
         */
        TERMINATED;
    }
```

> NEW 新建状态
> RUNABLE 调用了线程的start方法 Running 和Ready
> BOLOCKED 等待获取排他锁
> TIME_WAIT   Thread.sleep(),设置了timeout的Object.wait()和Thread.join()、LockSupport.parkNanos()、LockSupport.parkUntil()方法,在等待一定时间后，被系统自动唤醒
> WAIT   无限等待Object.wait()、Thread.join()、LockSupport.park()
> TERMINATED 线程已经结束



### Thread.sleep和Object.wait方法的区别
* sleep是Thread的方法，wait是Object的native方法
* sleep可以在任何方法中使用,wait必须在synchronized块中使用
* sleep只会让出cpu，不会让出锁资源. wait不仅让出CPU，还让出锁资源
*

```java
Exception in thread "Thread-A" java.lang.IllegalMonitorStateException
```

### notify、notifyAll

* notify 随机通知一个线程
* notifyAll通知所有在改对象等待的线程
* notify、notifyAll、wait方法只能在synchronized块中调用,

### yield
当调用Thread.yield函数时，会给线程调度器一个当前线程愿意让出CPU使用的暗示，但是线程调度器可能会忽略这个暗示.


### 线程优雅关闭

* 废弃的方法stop、supend()、resume()
* 调用interrupt通知线程中断: 1. 如果线程处于阻塞状态，那么线程将会立即退出被阻塞状态，并且抛出一个InterruptException异常
2. 如果线程处于正常活动状态，那么会将该线程的中断标志设置为true,被设置为中断标志的线程将继续执行，不受影响。

*
*
## 高级面试题