# 1、jstack命令

## 1.1、jstack命令简介

​	有些时候我们需要查看下jvm中的线程执行情况，比如，发现服务器的CPU的负载突然增
高了、出现了死锁、死循环等，我们该如何分析呢？
​	由于程序是正常运行的，没有任何的输出，从日志方面也看不出什么问题，所以就需要
看下jvm的内部线程的执行情况，然后再进行分析查找出原因。
​	这个时候，就需要借助于jstack命令了，jstack的作用是将正在运行的jvm的线程情况进
行快照，并且打印出来：

## 1.2、jstack命令格式

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstack -help
Usage:
    jstack [-l] <pid>
        (to connect to running process)
    jstack -F [-m] [-l] <pid>
        (to connect to a hung process)
    jstack [-m] [-l] <executable> <core>
        (to connect to a core file)
    jstack [-m] [-l] [server_id@]<remote server IP or hostname>
        (to connect to a remote debug server)

Options:
    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)
    -m  to print both java and native frames (mixed mode)
    -l  long listing. Prints additional information about locks
    -h or -help to print this help message
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstack -options
```

> 常用的命令格式

```shell
jstack pid
```



## 1.3、jstack命令实例



```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstack 29558
2019-02-28 15:58:15
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.161-b12 mixed mode):

"Attach Listener" #72 daemon prio=9 os_prio=0 tid=0x00007f3480005800 nid=0x158c waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"ajp-nio-8009-AsyncTimeout" #70 daemon prio=5 os_prio=0 tid=0x00007f34a84ce800 nid=0x7619 waiting on condition [0x00007f346fdfc000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at org.apache.coyote.AbstractProtocol$AsyncTimeout.run(AbstractProtocol.java:1133)
        at java.lang.Thread.run(Thread.java:748)

"ajp-nio-8009-Acceptor-0" #69 daemon prio=5 os_prio=0 tid=0x00007f34a84cc800 nid=0x7618 runnable [0x00007f346fefd000]
   java.lang.Thread.State: RUNNABLE
        at sun.nio.ch.ServerSocketChannelImpl.accept0(Native Method)
        at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:422)
        at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:250)
        - locked <0x00000000ecfaef50> (a java.lang.Object)
        at org.apache.tomcat.util.net.NioEndpoint$Acceptor.run(NioEndpoint.java:455)
        at java.lang.Thread.run(Thread.java:748)

"ajp-nio-8009-ClientPoller-0" #68 daemon prio=5 os_prio=0 tid=0x00007f34a82f4800 nid=0x7617 runnable [0x00007f346fffe000]
   java.lang.Thread.State: RUNNABLE
        at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
        at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
        at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:93)
        at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
        - locked <0x00000000ee95fa40> (a sun.nio.ch.Util$3)
        - locked <0x00000000ee95fa30> (a java.util.Collections$UnmodifiableSet)
        - locked <0x00000000ee95fa50> (a sun.nio.ch.EPollSelectorImpl)
        at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
        at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:793)
        at java.lang.Thread.run(Thread.java:748)

"ajp-nio-8009-exec-10" #67 daemon prio=5 os_prio=0 tid=0x00007f34a82f3800 nid=0x7616 waiting on condition [0x00007f3474a80000]
   java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000ee95fc48> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)

"ajp-nio-8009-exec-9" #66 daemon prio=5 os_prio=0 tid=0x00007f34a82f2800 nid=0x7615 waiting on condition [0x00007f3476094000]
   java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000ee95fc48> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
        at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)
......

"VM Periodic Task Thread" os_prio=0 tid=0x00007f34a80b9000 nid=0x738c waiting on condition 

JNI global references: 268
```



## 1.2、 死锁模拟



> 新建一个DeadLock.java、javac DeadLock.java 、java DeadLock

```java
public class DeadLock {

    private static Object obj1 = new Object();
    private static Object obj2 = new Object();

    public static void main(String[] args) {
        new Thread(new Thread1(),"线程1").start();
        new Thread(new Thread2(),"线程2").start();
    }

    static class Thread1 implements Runnable {
        @Override
        public void run() {
            synchronized (obj1) {
                System.out.println(Thread.currentThread().getName() + "获取到: obj1锁");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"等待obj2锁");
                synchronized (obj2) {
                    System.out.println(Thread.currentThread().getName() + "获取到obj2锁");
                }
            }
        }
    }


    static class Thread2 implements Runnable {
        @Override
        public void run() {
            synchronized (obj2) {
                System.out.println(Thread.currentThread().getName() + "获取到: obj2锁");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"等待obj1锁");
                synchronized (obj1) {
                    System.out.println(Thread.currentThread().getName() + "获取到obj1锁");
                }
            }
        }
    }
}
```



> 执行结果

```java
[root@iZ8vbesbzj30ty24cvijhvZ jvm]# java DeadLock
线程1获取到: obj1锁
线程2获取到: obj2锁
线程1等待obj2锁
线程2等待obj1锁
```

> jstack命令分析线程情况

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -l
21682 sun.tools.jps.Jps
29558 org.apache.catalina.startup.Bootstrap
21530 DeadLock
31051 WhileTest
1085 com.aliyun.tianji.cloudmonitor.Application
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstack 21530
2019-02-28 16:26:16
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.161-b12 mixed mode):

"Attach Listener" #11 daemon prio=9 os_prio=0 tid=0x00007f0a10001000 nid=0x5557 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"DestroyJavaVM" #10 prio=5 os_prio=0 tid=0x00007f0a38008000 nid=0x541b waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"线程2" #9 prio=5 os_prio=0 tid=0x00007f0a380f9000 nid=0x5425 waiting for monitor entry [0x00007f0a28486000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at DeadLock$Thread2.run(DeadLock.java:44)
        - waiting to lock <0x00000000e3470f80> (a java.lang.Object)
        - locked <0x00000000e3470f90> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)

"线程1" #8 prio=5 os_prio=0 tid=0x00007f0a380f7000 nid=0x5424 waiting for monitor entry [0x00007f0a28587000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at DeadLock$Thread1.run(DeadLock.java:24)
        - waiting to lock <0x00000000e3470f90> (a java.lang.Object)
        - locked <0x00000000e3470f80> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)

"Service Thread" #7 daemon prio=9 os_prio=0 tid=0x00007f0a380b3000 nid=0x5422 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread1" #6 daemon prio=9 os_prio=0 tid=0x00007f0a380b0000 nid=0x5421 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #5 daemon prio=9 os_prio=0 tid=0x00007f0a380ad800 nid=0x5420 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=0 tid=0x00007f0a380ac000 nid=0x541f runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=0 tid=0x00007f0a38079000 nid=0x541e in Object.wait() [0x00007f0a28c23000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000e3408ec0> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
        - locked <0x00000000e3408ec0> (a java.lang.ref.ReferenceQueue$Lock)
        at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
        at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)

"Reference Handler" #2 daemon prio=10 os_prio=0 tid=0x00007f0a38074800 nid=0x541d in Object.wait() [0x00007f0a28d24000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000e3406b68> (a java.lang.ref.Reference$Lock)
        at java.lang.Object.wait(Object.java:502)
        at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
        - locked <0x00000000e3406b68> (a java.lang.ref.Reference$Lock)
        at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=0 tid=0x00007f0a3806c800 nid=0x541c runnable 

"VM Periodic Task Thread" os_prio=0 tid=0x00007f0a380b8800 nid=0x5423 waiting on condition 

JNI global references: 6


Found one Java-level deadlock:
=============================
"线程2":
  waiting to lock monitor 0x00007f0a1c0062c8 (object 0x00000000e3470f80, a java.lang.Object),
  which is held by "��程1"
"线程1":
  waiting to lock monitor 0x00007f0a1c0038d8 (object 0x00000000e3470f90, a java.lang.Object),
  which is held by "线程2"

Java stack information for the threads listed above:
===================================================
"线程2":
        at DeadLock$Thread2.run(DeadLock.java:44)
        - waiting to lock <0x00000000e3470f80> (a java.lang.Object)
        - locked <0x00000000e3470f90> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)
"线程1":
        at DeadLock$Thread1.run(DeadLock.java:24)
        - waiting to lock <0x00000000e3470f90> (a java.lang.Object)
        - locked <0x00000000e3470f80> (a java.lang.Object)
        at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock.
```

