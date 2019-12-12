# Java线程基础篇

## Java多线程简介

## Java创建线程的四种方式

> **创建多线程的方式有很多种,下面列举几种常见的创建方式**

* 继承Thread类,重写run方法
* 实现Runnable接口,实现run方法
* 实现Callable接口
* 通过Executor创建线程

> **继承Thread类创建多线程**

* 继承Thread类
* 重写run()
* new SumThread().start(); //创建并启动线程

```
package com.mscncn.learn.thread.create;

public class SumThread extends Thread{
	@Override
	public void run() {
		int sum = 0;
		for(int i=0;i<=100;i++){
			sum += i;
		}
		System.out.println("求和: 1到100之间的正整数之后?");
		System.out.println("1 + ... + 100 =" + sum);
	}
	public static void main(String[] args) {
		SumThread sum = new SumThread();
		//线程的启动方法时start()不是run(),run()是线程调用执行的方法
		sum.start();
	}
}
```

> **实现Runnable接口创建多线程**

* 实现Runnable接口
* 实现run()方法
* Thread t1 = new Thread(new DownLoadThread()).start();//创建并启动线程

```
/**
 * 模拟下载百度离线地图线程.
 * @author King-Pan
 *
 */
public class DownLoadThread implements Runnable {

	@Override
	public void run() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("下载百度地图碎片: " + i + "_200_300.png");
		}
	}
	public static void main(String[] args) {
		DownLoadThread downLoad = new DownLoadThread();
		Thread t1 = new Thread(downLoad);
		t1.start();
	}
}
```

> **实现Callable接口创建多线程**

* 实现Callable<T>接口
* 重写call()方法[有返回值]
* new Thread(new FutureTask<>(new CallableThreadTest()), "有返回值的线程").start();//创建并启动线程

```
package com.mscncn.learn.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableThreadTest implements Callable<Integer> {

	public static void main(String[] args) {
		CallableThreadTest ctt = new CallableThreadTest();
		FutureTask<Integer> ft = new FutureTask<>(ctt);
		for (int i = 0; i < 100; i++) {
			System.out.println(Thread.currentThread().getName() + " 的循环变量i的值" + i);
			if (i == 20) {
				new Thread(ft, "有返回值的线程").start();
			}
		}
		try {
			System.out.println("子线程的返回值：" + ft.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Integer call() throws Exception {
		int i = 0;
		for (; i < 100; i++) {
			System.out.println(Thread.currentThread().getName() + " " + i);
		}
		return i;
	}

}
```

> **通过Executor创建线程**

* 实现Callable接口和Runable接口都可以创建线程
* Callable接口的call()有返回值,Runable接口的run()没有返回值

#### Runnable接口

```
package com.mscncn.learn.thread.create;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCacheThreadPool implements Runnable {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			executorService.execute(new TestCacheThreadPool());
		}
		executorService.shutdown();
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()+" 正在执行.");
	}
}
```
#### Callable<T>接口

```
package com.mscncn.learn.thread.create;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableExecutorTest implements Callable<Integer> {
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<Integer>> resultList = new ArrayList<Future<Integer>>();
		for (int i = 0; i < 5; i++) {
			Future<Integer> future = executorService.submit(new CallableExecutorTest());
			resultList.add(future);
		}
		for (Future<Integer> fu:resultList) {
			try {
				while(!fu.isDone());
				System.out.println(fu.get());
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				executorService.shutdown();
			}
		}
	}
	
	@Override
	public Integer call() throws Exception {
		System.out.println("call()被调用: " + Thread.currentThread().getName());
		int sum = 0;
		for(int i=0;i<=100;i++){
			sum += i;
		}
		System.out.println("求和: 1到100之间的正整数之后?");
		System.out.println("1 + ... + 100 =" + sum);
		return sum;
	}

}
```

## 创建多线程方式的优缺点


* 继承现Thread类代码简单明了，但是不使用在多线程和共享数据的场景下,不可以继承其他类
* 实现Runnable接口和Callable接口,代码稍微复杂,但是适合复杂的多线程和数据共享的场景下,可以继承其他类
* ExecutorService线程池创建线程只能是实现了Runnable和Callable接口,ExecutorService提供了以下几种执行线程的方法:
* void execute(Runnable run);
* Future<T> submit(Runnable);//返回值为null
* Future<T> submit(Callable);

## 多线程安全问题

在多线程中,可能会引起线程安全问题,所有我们引入了线程同步的概念

> **多线程安全解决方式**

* a. 同步代码块 (synchronized)
* b. 同步方法(synchronized)
* c. 同步锁(jdk1.5以后java.util.concurrent.locks.Lock)


> **注意:**

* synchronized 是隐式锁
* Lock是显示锁,需要通过lock()方法上锁,必须通过unlock()方法进行释放锁(必须在finally代码块中释放锁).


```

```
