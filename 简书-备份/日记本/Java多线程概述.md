# Java多线程概述

## 程序、进程和线程

> ==程序(program):==
>
> 是为了完成特定任务、用某种语言编写的一组指令的集合。指的是一端静态的代码，静态对象

> ==进程(process):==
>
> 是程序的一次执行过程，是系统运行程序的基本单位。程序是静止的，进程是动态的。系统运行一个程序即是一个进程从创建、运行到消亡的过程。多任务(multi task) 在一个系统中可以同时运行多个程序，即有多个独立运行的任务，每个任务对应一个进程 。

> ==线程(thread):== 
>
> 比进程更小的运行单位，是程序中单个顺序的流程控制，一个进程中可以包含多个线程。简单来讲，线程是一个独立的执行流，是进程内部的一个独立执行单元，相当于一个子程序。
>
> 线程是进程的组成部分，一个进程可以拥有多个线程，而一个线程必须拥有一个父进程。线程可以拥有自己的堆栈，自己的程序计数器和自己的局部变量，但不能拥有系统资源。它与父进程的其他线程共享该进程的所有资源。

一个进程中的所有线程都在该进程的虚拟地址空间中，使用该进程的全局变量和系统资源。

操作系统给每个线程分配不同的CPU时间片，在某一时刻，单核CPU只执行一个时间片段内的线程，多个时间片中的相应线程在CPU内轮流执行。

> 线程的特点：

- 线程可以完成一定任务，可以和其它线程共享父进程的共享变量和部分环境，相互协作来完成任务。
- 线程是独立运行的，其不知道进程中是否还有其他线程存在。
- 线程的执行是抢占式的，也就是说，当前执行的线程随时可能被挂起，以便运行另一个线程。


- 一个线程可以创建或撤销另一个线程，一个进程中的多个线程可以并发执行。

## 线程的创建及使用

### 继承Thread类创建线程类

> 步骤：

① 定义Thread类的子类 并重写该类的Run方法，该run方法的方法体就代表了该线程需要完成的任务

② 创建Thread类的实例，即创建了线程对象

③ 调用线程的start方法来启动线程

> 线程代码

```
package club.javalearn.learn.thread;

public class TicketThread  extends Thread {
    private int tick=100;
    @Override
    public void run()
    {
        while(true)
        {
            if(tick>0)
            {
                tick--;
                System.out.println(Thread.currentThread().getName()+"剩余票数:--"+tick);
            }else{
                break;
            }
        }
    }

    public static void main(String[] args) {
        TicketThread  ticket = new TicketThread ();
        TicketThread  ticket2 = new TicketThread ();

        ticket.start();
        ticket2.start();
    }
}
```

> 输出结果:

```
Thread-0剩余票数:--99
Thread-1剩余票数:--99
Thread-0剩余票数:--98
Thread-1剩余票数:--98
Thread-0剩余票数:--97
Thread-1剩余票数:--97
Thread-0剩余票数:--96
Thread-1剩余票数:--96
Thread-0剩余票数:--95
Thread-0剩余票数:--94
Thread-0剩余票数:--93
Thread-1剩余票数:--95
Thread-0剩余票数:--92
Thread-1剩余票数:--94
Thread-1剩余票数:--93
Thread-1剩余票数:--92
Thread-1剩余票数:--91
Thread-1剩余票数:--90
Thread-1剩余票数:--89
Thread-0剩余票数:--91
Thread-0剩余票数:--90
Thread-0剩余票数:--89
Thread-0剩余票数:--88
Thread-0剩余票数:--87
Thread-0剩余票数:--86
Thread-0剩余票数:--85
Thread-0剩余票数:--84
Thread-0剩余票数:--83
Thread-0剩余票数:--82
Thread-0剩余票数:--81
Thread-1剩余票数:--88
Thread-0剩余票数:--80
Thread-1剩余票数:--87
Thread-0剩余票数:--79
Thread-1剩余票数:--86
Thread-0剩余票数:--78
Thread-1剩余票数:--85
Thread-0剩余票数:--77
Thread-1剩余票数:--84
Thread-0剩余票数:--76
Thread-1剩余票数:--83
Thread-0剩余票数:--75
Thread-1剩余票数:--82
Thread-0剩余票数:--74
Thread-1剩余票数:--81
Thread-0剩余票数:--73
Thread-1剩余票数:--80
Thread-0剩余票数:--72
Thread-1剩余票数:--79
Thread-0剩余票数:--71
Thread-1剩余票数:--78
Thread-0剩余票数:--70
Thread-1剩余票数:--77
Thread-0剩余票数:--69
Thread-1剩余票数:--76
Thread-0剩余票数:--68
Thread-1剩余票数:--75
Thread-0剩余票数:--67
Thread-1剩余票数:--74
Thread-0剩余票数:--66
Thread-1剩余票数:--73
Thread-1剩余票数:--72
Thread-0剩余票数:--65
Thread-1剩余票数:--71
Thread-0剩余票数:--64
Thread-1剩余票数:--70
Thread-1剩余票数:--69
Thread-0剩余票数:--63
Thread-1剩余票数:--68
Thread-1剩余票数:--67
Thread-0剩余票数:--62
Thread-1剩余票数:--66
Thread-0剩余票数:--61
Thread-1剩余票数:--65
Thread-0剩余票数:--60
Thread-1剩余票数:--64
Thread-0剩余票数:--59
Thread-1剩余票数:--63
Thread-0剩余票数:--58
Thread-1剩余票数:--62
Thread-0剩余票数:--57
Thread-1剩余票数:--61
Thread-0剩余票数:--56
Thread-1剩余票数:--60
Thread-0剩余票数:--55
Thread-1剩余票数:--59
Thread-0剩余票数:--54
Thread-1剩余票数:--58
Thread-0剩余票数:--53
Thread-1剩余票数:--57
Thread-0剩余票数:--52
Thread-1剩余票数:--56
Thread-0剩余票数:--51
Thread-1剩余票数:--55
Thread-1剩余票数:--54
Thread-0剩余票数:--50
Thread-0剩余票数:--49
Thread-1剩余票数:--53
Thread-0剩余票数:--48
Thread-0剩余票数:--47
Thread-1剩余票数:--52
Thread-1剩余票数:--51
Thread-1剩余票数:--50
Thread-0剩余票数:--46
Thread-1剩余票数:--49
Thread-0剩余票数:--45
Thread-1剩余票数:--48
Thread-0剩余票数:--44
Thread-1剩余票数:--47
Thread-0剩余票数:--43
Thread-1剩余票数:--46
Thread-0剩余票数:--42
Thread-1剩余票数:--45
Thread-0剩余票数:--41
Thread-1剩余票数:--44
Thread-1剩余票数:--43
Thread-1剩余票数:--42
Thread-1剩余票数:--41
Thread-0剩余票数:--40
Thread-1剩余票数:--40
Thread-1剩余票数:--39
Thread-1剩余票数:--38
Thread-1剩余票数:--37
Thread-1剩余票数:--36
Thread-0剩余票数:--39
Thread-1剩余票数:--35
Thread-0剩余票数:--38
Thread-1剩余票数:--34
Thread-0剩余票数:--37
Thread-0剩余票数:--36
Thread-1剩余票数:--33
Thread-0剩余票数:--35
Thread-1剩余票数:--32
Thread-0剩余票数:--34
Thread-1剩余票数:--31
Thread-1剩余票数:--30
Thread-0剩余票数:--33
Thread-1剩余票数:--29
Thread-0剩余票数:--32
Thread-1剩余票数:--28
Thread-0剩余票数:--31
Thread-0剩余票数:--30
Thread-1剩余票数:--27
Thread-1剩余票数:--26
Thread-0剩余票数:--29
Thread-1剩余票数:--25
Thread-0剩余票数:--28
Thread-0剩余票数:--27
Thread-1剩余票数:--24
Thread-0剩余票数:--26
Thread-1剩余票数:--23
Thread-0剩余票数:--25
Thread-1剩余票数:--22
Thread-0剩余票数:--24
Thread-1剩余票数:--21
Thread-0剩余票数:--23
Thread-1剩余票数:--20
Thread-0剩余票数:--22
Thread-1剩余票数:--19
Thread-0剩余票数:--21
Thread-1剩余票数:--18
Thread-0剩余票数:--20
Thread-1剩余票数:--17
Thread-0剩余票数:--19
Thread-0剩余票数:--18
Thread-1剩余票数:--16
Thread-0剩余票数:--17
Thread-1剩余票数:--15
Thread-0剩余票数:--16
Thread-1剩余票数:--14
Thread-1剩余票数:--13
Thread-1剩余票数:--12
Thread-0剩余票数:--15
Thread-0剩余票数:--14
Thread-0剩余票数:--13
Thread-0剩余票数:--12
Thread-0剩余票数:--11
Thread-0剩余票数:--10
Thread-0剩余票数:--9
Thread-0剩余票数:--8
Thread-0剩余票数:--7
Thread-0剩余票数:--6
Thread-0剩余票数:--5
Thread-0剩余票数:--4
Thread-0剩余票数:--3
Thread-0剩余票数:--2
Thread-0剩余票数:--1
Thread-0剩余票数:--0
Thread-1剩余票数:--11
Thread-1剩余票数:--10
Thread-1剩余票数:--9
Thread-1剩余票数:--8
Thread-1剩余票数:--7
Thread-1剩余票数:--6
Thread-1剩余票数:--5
Thread-1剩余票数:--4
Thread-1剩余票数:--3
Thread-1剩余票数:--2
Thread-1剩余票数:--1
Thread-1剩余票数:--0

Process finished with exit code 0
```

### 实现Runnable接口

> 步骤:

①定义Runnable接口的实现类，并重写它的Run方法，run方法同样是该线程的执行体！

②创建Runnable实现类的实例，并将此实例作为Thread的target创建一个Thread对象，该Thread对象才是真正的线程对象！

③调用start方法启动该线程

> 线程代码

```
package club.javalearn.demo.thread;

public class TicketRunnable implements Runnable {
    private  int tick=100;
    @Override
    public void run()
    {
        while(true)
        {
            if(tick>0)
            {
                tick--;
                System.out.println(Thread.currentThread().getName()+"剩余票数:--"+tick);
            }else{
                break;
            }
        }
    }

    public static void main(String[] args) {
        TicketRunnable ticketRunnable = new TicketRunnable();
        Thread thread1 = new Thread(ticketRunnable);
        Thread thread2 = new Thread(ticketRunnable);
        thread1.start();
        thread2.start();
    }
}
```

> 输出结果

```
Thread-0剩余票数:--99
Thread-1剩余票数:--98
Thread-0剩余票数:--97
Thread-1剩余票数:--96
Thread-0剩余票数:--95
Thread-0剩余票数:--93
Thread-1剩余票数:--94
Thread-0剩余票数:--92
Thread-0剩余票数:--90
Thread-0剩余票数:--89
Thread-1剩余票数:--91
Thread-1剩余票数:--87
Thread-0剩余票数:--88
Thread-1剩余票数:--86
Thread-1剩余票数:--84
Thread-0剩余票数:--85
Thread-1剩余票数:--83
Thread-1剩余票数:--81
Thread-0剩余票数:--82
Thread-1剩余票数:--80
Thread-0剩余票数:--79
Thread-1剩余票数:--78
Thread-0剩余票数:--77
Thread-1剩余票数:--76
Thread-0剩余票数:--75
Thread-1剩余票数:--74
Thread-0剩余票数:--73
Thread-1剩余票数:--72
Thread-0剩余票数:--71
Thread-1剩余票数:--70
Thread-0剩余票数:--69
Thread-1剩余票数:--68
Thread-0剩余票数:--67
Thread-1剩余票数:--66
Thread-0剩余票数:--65
Thread-1剩余票数:--64
Thread-0剩余票数:--63
Thread-1剩余票数:--62
Thread-1剩余票数:--60
Thread-1剩余票数:--59
Thread-1剩余票数:--58
Thread-1剩余票数:--57
Thread-0剩余票数:--61
Thread-1剩余票数:--56
Thread-0剩余票数:--55
Thread-1剩余票数:--54
Thread-0剩余票数:--53
Thread-1剩余票数:--52
Thread-0剩余票数:--51
Thread-0剩余票数:--49
Thread-1剩余票数:--49
Thread-1剩余票数:--47
Thread-1剩余票数:--46
Thread-1剩余票数:--45
Thread-1剩余票数:--44
Thread-1剩余票数:--43
Thread-0剩余票数:--48
Thread-1剩余票数:--42
Thread-1剩余票数:--40
Thread-1剩余票数:--39
Thread-1剩余票数:--38
Thread-1剩余票数:--37
Thread-0剩余票数:--41
Thread-1剩余票数:--36
Thread-0剩余票数:--35
Thread-1剩余票数:--34
Thread-1剩余票数:--32
Thread-0剩余票数:--33
Thread-0剩余票数:--30
Thread-1剩余票数:--31
Thread-1剩余票数:--28
Thread-1剩余票数:--27
Thread-1剩余票数:--26
Thread-0剩余票数:--29
Thread-0剩余票数:--24
Thread-0剩余票数:--23
Thread-0剩余票数:--22
Thread-0剩余票数:--21
Thread-0剩余票数:--20
Thread-0剩余票数:--19
Thread-0剩余票数:--18
Thread-0剩余票数:--17
Thread-0剩余票数:--16
Thread-0剩余票数:--15
Thread-0剩余票数:--14
Thread-0剩余票数:--13
Thread-0剩余票数:--12
Thread-0剩余票数:--11
Thread-0剩余票数:--10
Thread-0剩余票数:--9
Thread-0剩余票数:--8
Thread-0剩余票数:--7
Thread-0剩余票数:--6
Thread-0剩余票数:--5
Thread-0剩余票数:--4
Thread-0剩余票数:--3
Thread-0剩余票数:--2
Thread-0剩余票数:--1
Thread-0剩余票数:--0
Thread-1剩余票数:--25

Process finished with exit code 0
```

### 使用callable和future创建线程

从Java5开始，Java提供 Callable接口,Callable接口提供了一个call（）方法可以作为线程执行体，看起来和Runnable很像，但call（）方法更强大——call（）方法可以有返回值、call（）方法可以抛出异常
 Java5提供了Future接口来代表Callable接口的call（）方法的返回值，并为Future接口提供了一个FutureTask实现类，该实现类实现类Future接口，也实现了Runnable接口——可以作为Thread的target。

> 实现步骤：

①创建Callable接口的实现类，并实现call方法，该call方法会成为线程执行体，且call方法具有返回值，在创建callable接口的实现类！

②使用FutrueTask类来包装Callable对象，该FutrueTask封装类Callable的call方法的返回值

③使用FutrueTask对象作为Thread的target创建并启动新线程！

④使用FutrueTask的get方法获取执行结束后的返回值

> 线程代码

```
package club.javalearn.demo.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class TicketCallable implements Callable<Integer> {
    private int tick=100;
    @Override
    public Integer call() {
        while(true)
        {
            if(tick>0)
            {
                tick--;
                System.out.println(Thread.currentThread().getName()+"剩余票数:--"+tick);
            }else{
                return tick;
            }
        }
    }

    public static void main(String[] args) {
        Callable<Integer> callable = new TicketCallable();
        FutureTask<Integer> task = new FutureTask<>(callable);
        Thread thread = new Thread(task);
        thread.start();
    }
}
```

> 输出结果

```
Thread-0剩余票数:--99
Thread-0剩余票数:--98
Thread-0剩余票数:--97
Thread-0剩余票数:--96
Thread-0剩余票数:--95
Thread-0剩余票数:--94
Thread-0剩余票数:--93
Thread-0剩余票数:--92
Thread-0剩余票数:--91
Thread-0剩余票数:--90
Thread-0剩余票数:--89
Thread-0剩余票数:--88
Thread-0剩余票数:--87
Thread-0剩余票数:--86
Thread-0剩余票数:--85
Thread-0剩余票数:--84
Thread-0剩余票数:--83
Thread-0剩余票数:--82
Thread-0剩余票数:--81
Thread-0剩余票数:--80
Thread-0剩余票数:--79
Thread-0剩余票数:--78
Thread-0剩余票数:--77
Thread-0剩余票数:--76
Thread-0剩余票数:--75
Thread-0剩余票数:--74
Thread-0剩余票数:--73
Thread-0剩余票数:--72
Thread-0剩余票数:--71
Thread-0剩余票数:--70
Thread-0剩余票数:--69
Thread-0剩余票数:--68
Thread-0剩余票数:--67
Thread-0剩余票数:--66
Thread-0剩余票数:--65
Thread-0剩余票数:--64
Thread-0剩余票数:--63
Thread-0剩余票数:--62
Thread-0剩余票数:--61
Thread-0剩余票数:--60
Thread-0剩余票数:--59
Thread-0剩余票数:--58
Thread-0剩余票数:--57
Thread-0剩余票数:--56
Thread-0剩余票数:--55
Thread-0剩余票数:--54
Thread-0剩余票数:--53
Thread-0剩余票数:--52
Thread-0剩余票数:--51
Thread-0剩余票数:--50
Thread-0剩余票数:--49
Thread-0剩余票数:--48
Thread-0剩余票数:--47
Thread-0剩余票数:--46
Thread-0剩余票数:--45
Thread-0剩余票数:--44
Thread-0剩余票数:--43
Thread-0剩余票数:--42
Thread-0剩余票数:--41
Thread-0剩余票数:--40
Thread-0剩余票数:--39
Thread-0剩余票数:--38
Thread-0剩余票数:--37
Thread-0剩余票数:--36
Thread-0剩余票数:--35
Thread-0剩余票数:--34
Thread-0剩余票数:--33
Thread-0剩余票数:--32
Thread-0剩余票数:--31
Thread-0剩余票数:--30
Thread-0剩余票数:--29
Thread-0剩余票数:--28
Thread-0剩余票数:--27
Thread-0剩余票数:--26
Thread-0剩余票数:--25
Thread-0剩余票数:--24
Thread-0剩余票数:--23
Thread-0剩余票数:--22
Thread-0剩余票数:--21
Thread-0剩余票数:--20
Thread-0剩余票数:--19
Thread-0剩余票数:--18
Thread-0剩余票数:--17
Thread-0剩余票数:--16
Thread-0剩余票数:--15
Thread-0剩余票数:--14
Thread-0剩余票数:--13
Thread-0剩余票数:--12
Thread-0剩余票数:--11
Thread-0剩余票数:--10
Thread-0剩余票数:--9
Thread-0剩余票数:--8
Thread-0剩余票数:--7
Thread-0剩余票数:--6
Thread-0剩余票数:--5
Thread-0剩余票数:--4
Thread-0剩余票数:--3
Thread-0剩余票数:--2
Thread-0剩余票数:--1
Thread-0剩余票数:--0

Process finished with exit code 0
```

### 使用ExecutorService、Callable、Future实现有返回结果的线程

ExecutorService、Callable、Future三个接口实际上都是属于Executor框架。返回结果的线程是在JDK1.5中引入的新特征，有了这种特征就不需要再为了得到返回值而大费周折了。而且自己实现了也可能漏洞百出。

可返回值的任务必须实现Callable接口。类似的，无返回值的任务必须实现Runnable接口。

执行Callable任务后，可以获取一个Future的对象，在该对象上调用get就可以获取到Callable任务返回的Object了。

注意：get方法是阻塞的，即：线程无返回结果，get方法会一直等待。

再结合线程池接口ExecutorService就可以实现传说中有返回结果的多线程了。

> 实现步骤：

①创建一个线程池

②创建多个有返回值的任务

③执行任务并获取Future对象

④关闭线程池

⑤获取所有并发任务的运行结果

> 线程代码

```
package club.javalearn.demo.thread;

import java.util.concurrent.Callable;

public class PersonCallable implements Callable<Integer> {

    private int type;
    public PersonCallable(int type){
        this.type = type;
    }
    @Override
    public Integer call() throws Exception {
        Integer salary =0 ;
        if(type==1){
            System.out.println("V1员工工资3500");
            salary = 3500;
        }else if(type ==2){
            System.out.println("V2员工工资5000");
        }else if(type ==3){
            System.out.println("经理工资10000");
            salary = 10000;
        }else if(type ==4){
            System.out.println("总经理工资20000");
            salary = 20000;
        }else{
            System.out.println("boss工资50000");
            salary =50000;
        }
        return salary;
    }
}
```

```
package club.javalearn.demo.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceTest {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int taskSize = 5;
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);

        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<>();

        for (int i = 0; i < taskSize; i++) {
            Callable c = new PersonCallable(i+1);
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            try {
                System.out.println("第一次获取返回值:(可能返回默认值)" + f.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            list.add(f);
        }

        // 关闭线程池
        pool.shutdown();

        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            try {
                System.out.println("最终获取返回值(一定会返回结果): " + f.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("----程序结束运行----，程序运行时间【"
                + (end - start) + "毫秒】");
    }
}
```



```java
V1员工工资3500

第一次获取返回值:(可能返回默认值)3500

V2员工工资5000

第一次获取返回值:(可能返回默认值)5000

经理工资10000

第一次获取返回值:(可能返回默认值)10000

总经理工资20000

第一次获取返回值:(可能返回默认值)20000

boss工资50000

第一次获取返回值:(可能返回默认值)50000

最终获取返回值(一定会返回结果): 3500

最终获取返回值(一定会返回结果): 5000

最终获取返回值(一定会返回结果): 10000

最终获取返回值(一定会返回结果): 20000

最终获取返回值(一定会返回结果): 50000

----程序结束运行----，程序运行时间【10毫秒】

Process finished with exit code 0
```



## 线程的生命周期

> 线程的生命周期:

- 指线程从创建到启动，知道运行结束
- 可以通过调用Thread类的相关方法影响线程的运行状况

> 线程的运行状态

- 新建(New)
- 可执行(Runnable)
- 运行(Running)
- 阻塞(Blocking)
- 死亡(Dead)

![线程状态转换图](https://upload-images.jianshu.io/upload_images/6331401-68a2549cbad05ecf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


## 线程的常用方法

### 启动多线程

启动多线程是使用Thread类的start()方法，然后操作系统绝对何时调用线程的run()方法，如果直接调用run()方法，那么就是一次普通的java方法调用。

### 获取线程的信息

#### 获取当前线程

```java
Thread thread=Thread.currentThread();
```

获取当前线程名称

```java
Thread.currentThread().getName()
    
//第二种
Thread thread=Thread.currentThread();
String threadName = thread.getName();
```



#### sleep()

使当前线程（即调用该方法的线程）暂停执行一段时间，让其他线程有机会继续执行，但它并不释放对象锁。也就是说如果有synchronized同步快，其他线程仍然不能访问共享数据。注意该方法要捕捉异常。

例如有两个线程同时执行(没有synchronized)一个线程优先级为MAX_PRIORITY，另一个为MIN_PRIORITY，如果没有Sleep()方法，只有高优先级的线程执行完毕后，低优先级的线程才能够执行；但是高优先级的线程sleep(500)后，低优先级就有机会执行了。

总之，sleep()可以使低优先级的线程得到执行的机会，当然也可以让同优先级、高优先级的线程有执行的机会。

- sleep(long)使当前线程进入停滞状态，所以执行sleep()的线程在指定的时间内肯定不会被执行；
- sleep(long)可使优先级低的线程得到执行的机会，当然也可以让同优先级的线程有执行的机会；
- sleep(long)是不会释放锁标志的。

#### join

join()方法使调用该方法的线程在此之前执行完毕，也就是等待该方法的线程执行完毕后再往下继续执行。注意该方法也需要捕捉异常。

#### yield()

该方法与sleep()类似，只是不能由用户指定暂停多长时间，并且yield（）方法只能让同优先级的线程有执行的机会。

##### wait()和notify()、notifyAll()

这三个方法用于协调多个线程对共享数据的存取，所以必须在synchronized语句块内使用。synchronized关键字用于保护共享数据，阻止其他线程对共享数据的存取，但是这样程序的流程就很不灵活了，如何才能在当前线程还没退出synchronized数据块时让其他线程也有机会访问共享数据呢？此时就用这三个方法来灵活控制。

wait()方法使当前线程暂停执行并释放对象锁标示，让其他线程可以进入synchronized数据块，当前线程被放入对象等待池中。当调用notify()方法后，将从对象的等待池中移走一个任意的线程并放到锁标志等待池中，只有锁标志等待池中线程能够获取锁标志；如果锁标志等待池中没有线程，则notify()不起作用。

notifyAll()则从对象等待池中移走所有等待那个对象的线程并放到锁标志等待池中。

**注意 这三个方法都是java.lang.Object的方法。**
