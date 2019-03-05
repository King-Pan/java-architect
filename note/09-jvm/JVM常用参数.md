# JVM常用参数



## 设置java程序启动内存

```java
java -Xms64m -Xmx128m  JVMTest # 设置
```





## JPS命令

jps名称是查看系统中java进程的命令



> jps -q

**-p  :仅仅显示VM 标示，不显示jar,class, main参数等信息.**

jps -q 查看java进程的进程id

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -q
6529
29558
11162
31051
1085
```

> jps -m

**-m:输出主函数和传入的参数**

jps -m 查看主函数和传入的参数

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -m
29558 Bootstrap start
11162 Main -port 9999 /java/jvm/dump.dat
31051 WhileTest
8619 Jps -m
1085 Application
```

