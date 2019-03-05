# 1、jps命令





## 1.1、jps命令简介



​	**JPS 名称: jps - Java Virtual Machine Process Status Tool**

​	用来查看基于HotSpot的JVM里面中，所有具有访问权限的Java进程的具体状态, 包括进程ID，进程启动的路径及启动参数等等，与unix上的ps类似，只不过jps是用来显示java进程，可以把jps理解为ps的一个子集。

​	使用jps时，如果没有指定hostid，它只会显示本地环境中所有的Java进程；如果指定了hostid，它就会显示指定hostid上面的java进程，不过这需要远程服务上开启了jstatd服务，可以参看前面的jstatd章节来启动jstad服务。



## 1.2、jps命令格式

```shell
jps [ options ] [ hostid ]
```



## 1.3、jps常用参数说明

> -q 忽略输出的类名、Jar名以及传递给main方法的参数，只输出pid。

> -m 输出传递给main方法的参数，如果是内嵌的JVM则输出为null。

> -l 输出应用程序主类的完整包名，或者是应用程序JAR文件的完整路径。

> -v 输出传给JVM的参数。

> -V 输出通过标记的文件传递给JVM的参数（.hotspotrc文件，或者是通过参数-XX:Flags=<filename>指定的文件）。

> -J 用于传递jvm选项到由javac调用的java加载器中，例如，“-J-Xms48m”将把启动内存设置为48M，使用-J选项可以非常方便的向基于Java的开发的底层虚拟机应用程序传递参数。



## 1.4、jps实例

### 1.4.1、jps

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps
17812 Jps
29558 Bootstrap
31051 WhileTest
1085 Application
```

### 1.4.2、jps -q

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -q
29558
31051
18188
1085
```

### 1.4.3、jps -m

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -m
18577 Jps -m
29558 Bootstrap start
31051 WhileTest
1085 Application
[root@iZ8vbesbzj30ty
```

### 1.4.4、jps -l

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -l
29558 org.apache.catalina.startup.Bootstrap
31051 WhileTest
1085 com.aliyun.tianji.cloudmonitor.Application
18941 sun.tools.jps.Jps
```

### 1.4.5、jps -v

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -v
29558 Bootstrap -Djava.util.logging.config.file=/root/tomcat8/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Dignore.endorsed.dirs= -Dcatalina.base=/root/tomcat8 -Dcatalina.home=/root/tomcat8 -Djava.io.tmpdir=/root/tomcat8/temp
19304 Jps -Dapplication.home=/usr/java/jdk1.8.0_161 -Xms8m
31051 WhileTest
1085 Application -Djava.compiler=none -XX:-UseGCOverheadLimit -XX:NewRatio=1 -XX:SurvivorRatio=8 -XX:+UseSerialGC -Djava.io.tmpdir=../../tmp -Xms16m -Xmx32m -Djava.library.path=../lib:../../lib -Dwrapper.key=D6Q2h2I4PR-2IdGI -Dwrapper.port=32000 -Dwrapper.jvm.port.min=31000 -Dwrapper.jvm.port.max=31999 -Dwrapper.disable_console_input=TRUE -Dwrapper.pid=992 -Dwrapper.version=3.5.27 -Dwrapper.native_library=wrapper -Dwrapper.arch=x86 -Dwrapper.service=TRUE -Dwrapper.cpu.timeout=10 -Dwrapper.jvmid=1
```

### 1.4.6、jps -V

```shell
[root@iZ8vbesbzj30ty24cvijhvZ ~]# jps -V
19777 Jps
29558 Bootstrap
31051 WhileTest
1085 Application
```

## 1.5、jps常用命令

​	常用的jps命令有 jps -l、jps -v

