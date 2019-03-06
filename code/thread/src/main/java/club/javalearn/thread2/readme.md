## 创建线程Thread

> 默认线程名

```java
# 默认线程名由: "Thread-" + nextThreadNum()组成
/* For autonumbering anonymous threads. */
    private static int threadInitNumber;
    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }
```

## 最简单的线程

```java
new Thread().start();
```


```java
//Thread类实现了Runable接口
public class Thread implements Runable{}

//Thread默认构造器中传入了空的target
public Thread() {
        init(null, null, "Thread-" + nextThreadNum(), 0);
    }
```

## 如果创建Thread没有传入ThreadGroup，线程会获取父线程的ThreadGroup当作默认值,当前线程和父线程在同一个线程



## 获取线程组

Thread.getCurrentThread().get