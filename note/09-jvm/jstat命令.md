# 1、jstat命令

## 1.1、jstat命令简介

​	jstat是JDK自带的一个轻量级小工具。全称“Java Virtual Machine statistics monitoring tool”，它位于java的bin目录下，主要利用JVM内建的指令对Java应用程序的资源和性能进行实时的命令行的监控，包括了对Heap size和垃圾回收状况的监控。

## 1.2、jstat命令格式

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -help
Usage: jstat -help|-options
       jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]

Definitions:
  <option>      An option reported by the -options option
  <vmid>        Virtual Machine Identifier. A vmid takes the following form:
                     <lvmid>[@<hostname>[:<port>]]
                Where <lvmid> is the local vm identifier for the target
                Java virtual machine, typically a process id; <hostname> is
                the name of the host running the target Java virtual machine;
                and <port> is the port number for the rmiregistry on the
                target host. See the jvmstat documentation for a more complete
                description of the Virtual Machine Identifier.
  <lines>       Number of samples between header lines.
  <interval>    Sampling interval. The following forms are allowed:
                    <n>["ms"|"s"]
                Where <n> is an integer and the suffix specifies the units as 
                milliseconds("ms") or seconds("s"). The default units are "ms".
  <count>       Number of samples to take before terminating.
  -J<flag>      Pass <flag> directly to the runtime system.
```

```shell
jstat -<option> [-t] [-h<lines>] <vmid> [<interval> [<count>]]
```





## 1.3、jstat命令参数

##



> jstat -class 查看加载类的数量

>



## 1.3、jstat命令实例

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -options
-class  #查看加载类的数量
-compiler #
-gc
-gccapacity
-gccause
-gcmetacapacity
-gcnew
-gcnewcapacity
-gcold
-gcoldcapacity
-gcutil
-printcompilation
```

### 1.3.1、jstat -class加载类的数量

> jstat -class 查看加载类的数量

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -class 29558
Loaded  Bytes  Unloaded  Bytes     Time   
  9347 18275.1       74   107.8      14.09
```

* Loaded：加载class的数量
* Bytes：所占用空间大小
* Unloaded：未加载数量
* Bytes：未加载占用空间
* Time：时间

### 1.3.2、jstat -compiler编译统计

> jstat -compiler 查看编译统计

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -compiler 29558
Compiled Failed Invalid   Time   FailedType FailedMethod
    6968      1       0    43.79          1 com/mysql/jdbc/AbandonedConnectionCleanupThread run
```

* Compiled：编译数量。
* Failed：失败数量
* Invalid：不可用数量
* Time：时间
* FailedType：失败类型
* FailedMethod：失败的方法

### 1.3.3、jstat -gc垃圾回收统计

> jstat -gc 垃圾回收统计

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -gc 29558
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
2816.0 2816.0  41.2   0.0   22784.0   4514.3   56696.0    30367.3   55936.0 54461.7 6784.0 6420.3    302    1.425   5      0.704    2.130


#也可以指定打印的间隔和次数，每1秒中打印一次，共打印5次
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -gc 29558 1000 5
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
2816.0 2816.0  41.2   0.0   22784.0   4514.3   56696.0    30367.3   55936.0 54461.7 6784.0 6420.3    302    1.425   5      0.704    2.130
2816.0 2816.0  41.2   0.0   22784.0   4514.3   56696.0    30367.3   55936.0 54461.7 6784.0 6420.3    302    1.425   5      0.704    2.130
2816.0 2816.0  41.2   0.0   22784.0   4514.3   56696.0    30367.3   55936.0 54461.7 6784.0 6420.3    302    1.425   5      0.704    2.130
2816.0 2816.0  41.2   0.0   22784.0   4514.3   56696.0    30367.3   55936.0 54461.7 6784.0 6420.3    302    1.425   5      0.704    2.130
2816.0 2816.0  41.2   0.0   22784.0   4514.3   56696.0    30367.3   55936.0 54461.7 6784.0 6420.3    302    1.425   5      0.704    2.130
```

* S0C：第一个Survivor区的大小（KB）
* S1C：第二个Survivor区的大小（KB）
* S0U：第一个Survivor区的使用大小（KB）
* S1U：第二个Survivor区的使用大小（KB）
* EC：Eden区的大小（KB）
* EU：Eden区的使用大小（KB）
* OC：Old区大小（KB）
* OU：Old使用大小（KB）
* MC：方法区大小（KB）
* MU：方法区使用大小（KB）
* CCSC：压缩类空间大小（KB）
* CCSU：压缩类空间使用大小（KB）
* YGC：年轻代垃圾回收次数
* YGCT：年轻代垃圾回收消耗时间
* FGC：老年代垃圾回收次数
* FGCT：老年代垃圾回收消耗时间
* GCT：垃圾回收消耗总时间

### 1.3.4、jstat -gccapacity堆内存统计

> jstat -gccapacity 堆内存统计

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -gccapacity 29558
 NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC 
 10240.0 156992.0  28416.0 2816.0 2816.0  22784.0    20480.0   314048.0    56696.0    56696.0      0.0 1097728.0  55936.0      0.0 1048576.0   6784.0    302     5
```

- **NGCMN：**新生代最小容量
- **NGCMX：**新生代最大容量
- **NGC：**当前新生代容量
- **S0C：**第一个幸存区大小
- **S1C：**第二个幸存区的大小
- **EC：**伊甸园区的大小
- **OGCMN：**老年代最小容量
- **OGCMX：**老年代最大容量
- **OGC：**当前老年代大小
- **OC:**当前老年代大小
- **MCMN:**最小元数据容量
- **MCMX：**最大元数据容量
- **MC：**当前元数据空间大小
- **CCSMN：**最小压缩类空间大小
- **CCSMX：**最大压缩类空间大小
- **CCSC：**当前压缩类空间大小
- **YGC：**年轻代gc次数
- **FGC：**老年代GC次数

### 1.3.5、jstat -gccause查看gc原因

>jstat -gccause查看gc原因



 https://github.com/dmlloyd/openjdk/blob/jdk8u/jdk8u/hotspot/src/share/vm/gc_interface/gcCause.hpp#L39 中有一个Cause的枚举类，列举了都有哪些cause,用于在触发GC的时候做原因的区分。总共有27条，删掉源码中没有找到调用的，还剩17条。 

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jstat -gccause 29558
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT    LGCC                 GCC                 
  1.46   0.00  39.31  53.56  97.36  94.64    302    1.425     5    0.704    2.130 Allocation Failure   No GC 
```



* **S0:** 第一个幸存区
* **S1:** 第二个幸存区  
* **E:**   Eden区
* **O:**   老年区代 
* **M:**   元数据空间     
* **CCS:**    压缩类空间
* **YGC:**     *YGC**（**Young GC**）代表**Minor GC*
* **YGCT:**    *YGC**（**Young GC**）代表**Minor GC耗时*
* **FGC:**    *FGC**（**Full GC**）代表**Full GC* 
* **FGCT:**    *FGC**（**Full GC**）代表**Full GC**耗时*
* **GCT:**      *Minor & Full GC共计耗时*
* **LGCC:**  *上次GC的原因*           
* **GCC:** *本次GC的原因*    

### 1.3.6、其他







