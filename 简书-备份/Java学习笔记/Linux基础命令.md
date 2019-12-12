# Linux基础命令

### 显示当前工作空间

```
[centos@localhost ~]$ pwd
/home/centos
```

### 切换目录命令cd

```
cd /home/app  切换到home目录下的app目录[以/开头的为绝对路径]
cd app	切换到app目录 
cd ..	切换到上一层目录 
cd /		切换到系统根目录 
cd ~		切换到用户主目录 
cd -		切换到上一个所在目录
```

### 列出文件列表ls ll dir


ls(list)是一个非常有用的命令，用来显示当前目录下的内容。配合参数的使用，能以不同的方式显示目录内容。     格式：ls[参数] [路径或文件名]

常用：
在linux中以 . 开头的文件都是隐藏的文件

* ls
* ls -a  显示所有文件或目录（包含隐藏的文件）
* ls -l  缩写成ll
* ll -h  友好显示文件大小

###创建目录和移除目录mkdir rmdir

```
mkdir app   创建目录
rmdir app   可以删除空目录
```

```
[root@localhost ~]# mkdir app
[root@localhost ~]# cd app
[root@localhost app]# touch a.txt
[root@localhost app]# ll
total 0
-rw-r--r--. 1 root root 0 Jun 27 14:55 a.txt
[root@localhost app]# cd ..
[root@localhost ~]# rmdir app
rmdir: failed to remove `app': Directory not empty
[root@localhost ~]#  
```

删除非空目录可以使用rm -r Directory_name


mkdir -p 创建级联目录

```
[root@localhost ~]# mkdir -p app2/test
[root@localhost ~]# cd app2/test
[root@localhost test]# 
```

### 浏览文件cat more less tail

> **cat**

* cat查看整个文件内容
* cat格式: cat [参数] 文件名

```
cat note.txt
```

> **more**

一般用于要显示的内容会超过一个画面长度的情况。按空格键显示下一个画面。
回车显示下一行内容。

* 查看大文件一屏幕显示不完内容时使用more命令
* 空格或者回车键显示下一行内容
* 使用q或者ctrl+c退出

```
[root@localhost ~]# more install.log
Installing fontpackages-filesystem-1.41-1.1.el6.noarch
warning: fontpackages-filesystem-1.41-1.1.el6.noarch: Header V3 RSA/SHA256 Signature, key ID c105b9de: NOKEY
Installing liberation-fonts-common-1.05.1.20090721-5.el6.noarch
......省略部分
--More--(7%)
```
> **less**

* 用法和more类似，不同的是less可以通过PgUp、PgDn键来控制
* 可以向上翻行
* 使用q退出

```
[root@localhost ~]# less install.log
Installing fontpackages-filesystem-1.41-1.1.el6.noarch
warning: fontpackages-filesystem-1.41-1.1.el6.noarch: Header V3 RSA/SHA256 Signature, key ID c105b9de: NOKEY
Installing liberation-fonts-common-1.05.1.20090721-5.el6.noarch
Installing xml-common-0.6.3-32.el6.noarch
warning: fontpackages-filesystem-1.41-1.1.el6.noarch: Header V3 RSA/SHA256 Signature, key ID c105b9de: NOKEY
......省略部分
:
```
> **tail查看文件最后n行** 

tail命令是在实际使用过程中使用非常多的一个命令，它的功能是：用于显示文件后几行的内容。

* tail -10 显示最后10行内容
* tail -f 动态显示内容[一般查看日志文件]
* 使用ctrl+c退出

```
[root@localhost ~]# tail -5 install.log
Installing bridge-utils-1.2-10.el6.i686
Installing eject-2.1.5-17.el6.i686
Installing strace-4.5.19-1.17.el6.i686
Installing b43-fwcutter-012-2.2.el6.i686
*** FINISHED INSTALLING PACKAGES ***[root@localhost ~]# 
```

### 文件操作

> **touch创建空文件**

```
[root@localhost ~]# ll
total 76
-rw-------. 1 root root  3326 Jun 23 15:25 anaconda-ks.cfg
drwxr-xr-x. 3 root root  4096 Jun 27 14:57 app2
-rw-r--r--. 1 root root  6357 Jun 20 00:35 day23.txt
-rw-r--r--. 1 root root 39935 Jun 23 15:24 install.log
-rw-r--r--. 1 root root 10967 Jun 23 15:24 install.log.syslog
-rw-r--r--. 1 root root  2272 Jan  1  2014 note.txt
[root@localhost ~]# touch helloworld.txt
[root@localhost ~]# ll
total 76
-rw-------. 1 root root  3326 Jun 23 15:25 anaconda-ks.cfg
drwxr-xr-x. 3 root root  4096 Jun 27 14:57 app2
-rw-r--r--. 1 root root  6357 Jun 20 00:35 day23.txt
-rw-r--r--. 1 root root     0 Jun 27 15:20 helloworld.txt
-rw-r--r--. 1 root root 39935 Jun 23 15:24 install.log
-rw-r--r--. 1 root root 10967 Jun 23 15:24 install.log.syslog
-rw-r--r--. 1 root root  2272 Jan  1  2014 note.txt
```
> **vi/vim创建空文件**

vi/vim 的详细使用后面再说

```
vi Hello.java
vim World.java
```

> **rm删除文件或目录**

* 格式: rm [参数] 文件/目录
* rm a.txt 删除文件,询问删除
* rm -f a.txt 直接删除文件
* rm -r app 递归删除[删除非空目录][recursive递归的,回归的	英[rɪˈkɜ:sɪv]]

```
[root@localhost ~]# rm helloworld.txt 
rm: remove regular empty file `helloworld.txt'? y
[root@localhost ~]# 
```

```
[root@localhost ~]# touch a.txt
[root@localhost ~]# rm -f a.txt
```

```
rm -rf *    删除所有文件
rm -rf /*   自杀
```

> **cp拷贝文件**

* cp install.log install_log.log 把install.log拷贝为install_log.log
* cp install_log.log  ../ 把install_log.log拷贝到上级目录

```
[root@localhost ~]# cp install.log install_log.log
[root@localhost ~]# cp install_log.log  ../
```
> **mv移动文件**

mv移动或者重命名

* mv install_log.log install2.log 把install_log.log 重命名
* mv install2.log app/ 把install2.log 移动到app目录下

```
[root@localhost ~]# mv install_log.log install2.log
[root@localhost ~]# mv install2.log app/
```
> **tar打包或解压文件**

tar命令位于/bin目录下，它能够将用户所指定的文件或目录打包成一个文件，但不做压缩。一般Linux上常用的压缩方式是选用tar将许多文件打包成一个文件，再以gzip压缩命令压缩成xxx.tar.gz(或称为xxx.tgz)的文件。  常用参数：

* -c：创建一个新tar文件 
* -v：显示运行过程的信息 
* -f：指定文件名 
* -z：调用gzip压缩命令进行压缩 
* -t：查看压缩文件的内容 
* -x：解开tar文件

打包：

格式: tar –cvf xxx.tar ./*

```
[root@localhost app2]# ll
total 56
-rw-r--r--. 1 root root    13 Jun 27 16:05 1
-rw-r--r--. 1 root root    13 Jun 27 16:05 hello.java
-rw-r--r--. 1 root root 39935 Jun 27 15:58 install2.log
drwxr-xr-x. 2 root root  4096 Jun 27 14:57 test
-rw-r--r--. 1 root root    67 Jun 27 16:05 world.java
[root@localhost app2]# tar -cvf app2.tar ./*
./1
./hello.java
./install2.log
./test/
./world.java
[root@localhost app2]# ll
total 108
-rw-r--r--. 1 root root    13 Jun 27 16:05 1
-rw-r--r--. 1 root root 51200 Jun 27 16:06 app2.tar
-rw-r--r--. 1 root root    13 Jun 27 16:05 hello.java
-rw-r--r--. 1 root root 39935 Jun 27 15:58 install2.log
drwxr-xr-x. 2 root root  4096 Jun 27 14:57 test
-rw-r--r--. 1 root root    67 Jun 27 16:05 world.java
```

打包并且压缩：
tar –zcvf xxx.tar.gz ./* 

解压 
tar –xvf xxx.tar


```
[root@localhost gz]# tar -xvf app2.tar.gz 
./1
./app2.tar
./hello.java
./install2.log
./test/
./world.java
```

tar -xvf xxx.tar.gz -C  解压目录  [-C指定解压目录]


tar -xvf xxx.tar.gz -C /usr/aaa

### 下载文件

格式: wget url

```
wget  url
```

## Vi/Vim编辑器

在Linux下一般使用vi编辑器来编辑文件。 vi既可以查看文件也可以编辑文件。 三种模式：命令行、插入、底行模式。
切换到命令行模式：按Esc键；
切换到插入模式：按 i 、o、a键；

* i 在当前位置前插入
* I 在当前行首插入
* a 在当前位置后插入
* A 在当前行尾插入
* o 在当前行之后插入一行
* O 在当前行之前插入一行
    
切换到底行模式：按 :（冒号）； 更多详细用法，查询文档《Vim命令合集.docx》和《vi使用方法详细介绍.docx》


打开文件：vim file
退出：esc  :q
修改文件：输入i进入插入模式
保存并退出：esc :wq

不保存退出：esc :q!

> Vi/Vim编辑器的所有命令操作都是在命令模式下执行的


* i:在当前的光标所在处插入
* o:在当前光标所在的行的下一行插入
* a:在光标所在的下一个字符插入

* 快捷键：
* dd – 快速删除一行
* R – 替换


> **创建一个空的文件**


```
vi a.txt
esc :wq!
```

```
vim a.txt
esc :wq!
```


### 重定向输出

*   重定向输出>，覆盖原有内容
*   重定向输出>>，又追加功能


```
cat /etc/passwd>a.txt
cat /etc/passwd>a.txt
cat /etc/passwd>>b.txt
cat /etc/passwd>>b.txt
```

### 管道

管道是Linux命令中重要的一个概念，其作用是将一个命令的输出用作另一个命令的输入。 

```
ls --help | more    分页显示帮助信息
```

```
ps -ef|grep tomcat   查找进程中名称包含tomcat的进程
```




### &&命令执行控制

命令之间使用 && 连接，实现逻辑与的功能。  
只有在 && 左边的命令返回真（命令返回值 $? == 0），&& 右边的命令才会被执行。  
只要有一个命令返回假（命令返回值 $? == 1），后面的命令就不会被执行。


```
mkdir test && cd test
```


### 网络通讯命令

> **ifconfig显示或设置网络设备**

* ifconfig  显示网络设备
* ifconfig eth0 up 启用eth0网卡
* ifconfig eth0 down  停用eth0

> **ping测试网络状态**

* ctrl+c 退出

```
[root@localhost ~]# ping 192.168.164.1
PING 192.168.164.1 (192.168.164.1) 56(84) bytes of data.
64 bytes from 192.168.164.1: icmp_seq=1 ttl=128 time=0.245 ms
64 bytes from 192.168.164.1: icmp_seq=2 ttl=128 time=0.298 ms
64 bytes from 192.168.164.1: icmp_seq=3 ttl=128 time=0.264 ms
64 bytes from 192.168.164.1: icmp_seq=4 ttl=128 time=0.231 ms
^C
--- 192.168.164.1 ping statistics ---
4 packets transmitted, 4 received, 0% packet loss, time 3670ms
rtt min/avg/max/mdev = 0.231/0.259/0.298/0.029 ms
[root@localhost ~]# 
```


> **netstat查看网络端口**

* -a: 显示所有端口
* -n: 
* netstat -an | grep 3306 查询含有80端口的端口占用情况

```
[root@localhost ~]# netstat -an |grep 80
unix  2      [ ACC ]     STREAM     LISTENING     14961  /tmp/orbit-gdm/linc-885-0-4eb4cc805a91b
unix  3      [ ]         STREAM     CONNECTED     15480  
unix  2      [ ]         DGRAM                    15380  
unix  3      [ ]         STREAM     CONNECTED     15180  
unix  3      [ ]         STREAM     CONNECTED     14982  /tmp/orbit-gdm/linc-885-0-4eb4cc805a91b
unix  3      [ ]         STREAM     CONNECTED     14980  /tmp/orbit-gdm/linc-885-0-4eb4cc805a91b
[root@localhost ~]# 
```

> **date**

* date : 查看当前时间
* date -s "2017-06-28 12:00:00" 设置系统时间[必须要有root权限]

```
[root@localhost ~]# date
Wed Jun 28 15:22:18 PDT 2017
[root@localhost ~]# date -s "2017-06-28 12:00:00"
Wed Jun 28 12:00:00 PDT 2017
[root@localhost ~]# date -s "2017-06-28 22:22:00"
Wed Jun 28 22:22:00 PDT 2017
```

> **df查看磁盘信息**

* df -b,-k,-m,-g 按照byte,kb,mb,g格式显示磁盘信息
* df -h 友好的格式显示磁盘信息

```
[root@localhost ~]# df -h
Filesystem      Size  Used Avail Use% Mounted on
/dev/sda2        18G  2.5G   15G  15% /
tmpfs           504M   72K  504M   1% /dev/shm
/dev/sda1       291M   33M  244M  12% /boot
[root@localhost ~]# 
```

> **free显示内存状态**

* free –m 以mb单位显示内存组昂头 top 显示，管理执行中的程序

> **clear清屏幕 **

* clear 清屏幕 

> **ps查看正在运行进程的状态**

* ps 正在运行的某个进程的状态
* ps –ef  查看所有进程
* ps –ef | grep ssh 查找某一进程 

> **kill杀掉进程**

* kill pid : 杀掉进程
* kill 2868  杀掉2868编号的进程
* kill -9 pid :强制杀掉进程
* kill -9 2868  强制杀死进程2868

> **du 显示目录或文件的大小**

* du –h 显示当前目录的大小

```
[root@localhost ~]# du -h
4.0K    ./app2/test
4.0K    ./app2/gz/test
132K    ./app2/gz
244K    ./app2
352K    .
```

> **who 显示目前登入系统的用户信息**

```
[root@localhost ~]# who
root     pts/0        2017-06-28 15:17 (192.168.164.1)
```

> **hostname 查看当前主机名**

 

```
[root@localhost ~]# hostname
localhost.localdomain
```

* 修改：vi /etc/sysconfig/network 
* HOSTNAME=HADOOP

```
[root@localhost ~]# vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=localhost.localdomain
```

> **uname显示系统信息**

* uname 显示系统信息
* uname -a 显示本机详细信息。 依次为：内核名称(类别)，主机名，内核版本号，内核版本，内核编译日期，硬件名，处理器类型，硬件平台类型

```
[root@localhost ~]# uname 
Linux
[root@localhost ~]# uname -a
Linux localhost.localdomain 2.6.32-431.el6.i686 #1 SMP Fri Nov 22 00:26:36 UTC 2013 i686 i686 i386 GNU/Linux
[root@localhost ~]# 
```

## Linux的用户和组

> **useradd添加一个用户**

* useradd 添加一个用户
* useradd test 添加test用户,如果不存在test组自动创建一个test组,默认创建/home/test目录为test用户的主目录
* useradd test -d /home/t1  指定用户home目录 
* passwd  设置、修改密码
* passwd test  为test用户设置密码
* userdel test 删除test用户(不会删除home目录)
* userdel –r test  删除用户以及home目录
```
[root@localhost ~]# useradd test
[root@localhost ~]# ll /home
total 8
drwx------. 26 redis redis 4096 Jun 28 22:32 redis
drwx------.  4 test  test  4096 Jun 28 22:40 test
```

> **su切换用户**

* su 用户名
* su – 用户名   [直接切换到该用户下的环境变量][推荐使用]

```
[root@localhost ~]# su - redis
[redis@localhost ~]$ pwd
/home/redis
[redis@localhost ~]$ 
```

> **组管理**

当在创建一个新用户user时，若没有指定他所属于的组，就建立一个和该用户同名的私有组 
创建用户时也可以指定所在组 

* groupadd  创建组
* groupadd public  创建一个名为public的组
* useradd u1 –g public  创建用户指定组
* groupdel 删除组，如果该组有用户成员，必须先删除用户才能删除组。
* groupdel public

```
[root@localhost ~]# groupadd group1
[root@localhost ~]# useradd u1 -g group1
[root@localhost ~]# ll /home
total 12
drwx------. 26 redis redis  4096 Jun 28 22:32 redis
drwx------.  4 test  test   4096 Jun 28 22:40 test
drwx------.  4 u1    group1 4096 Jun 28 22:48 u1
[root@localhost ~]# groupdel group1
groupdel: cannot remove the primary group of user 'u1'
[root@localhost ~]# userdel -r u1
[root@localhost ~]# groupdel group1
[root@localhost ~]# 
```

> **id,su,sudo**

* id
* id 用户名
* su 用户名   [仅仅取得用户权限,工作环境不变]
* su - 用户名 [以用户登录,取得用户权限和工作环境]
* su - root 等同于 sudo

```
[centos@localhost ~]$ id
uid=500(centos) gid=500(centos) groups=500(centos) context=unconfined_u:unconfined_r:unconfined_t:s0-s0:c0.c1023
[centos@localhost ~]$ id root
uid=0(root) gid=0(root) groups=0(root)
[centos@localhost ~]$ 
```

#### sudo命令异常解决

```
[centos@localhost ~]$ sudo cat /ect/password
[sudo] password for centos: 
centos is not in the sudoers file.  This incident will be reported.
```

步骤:
1. ll /etc/sudoers   查看/etc/sudoers文件的详情
2. chmod u+w /etc/sudoers  添加写权限
3. vim /etc/sudoers  修改文件内容
4. chmod u-w /etc/sudoers  撤销写权限
5. su - centos 切换用户
6. sudo mkdir /root/a 验证是否成功

```
[centos@localhost ~]$ su - root
Password: 
[root@localhost ~]# file /etc/sudoers
/etc/sudoers: ASCII English text
[root@localhost ~]# ll /etc/sudoers
-r--r-----. 1 root root 3729 Dec  8  2015 /etc/sudoers
[root@localhost ~]# chmod u+w /etc/sudoers
[root@localhost ~]# vim /etc/sudoers
... 在root    ALL=(ALL)       ALL 下面添加一行,保存
centos  ALL=(ALL)       ALL
[root@localhost ~]# chmod u-w /etc/sudoers
[root@localhost ~]# su - centos
[centos@localhost ~]$ sudo mkdir /root/a
```

### Linux常用管理文件

> **/etc/passwd用户文件**

```
[centos@localhost ~]$ cat /etc/passwd
root:x:0:0:root:/root:/bin/bash
```

```
root:x:0:0:root:/root:/bin/bash 
账号名称：		在系统中是唯一的 
用户密码：		此字段存放加密口令 
用户标识码(User ID)：  系统内部用它来标示用户 
组标识码(Group ID)：   系统内部用它来标识用户属性 
用户相关信息：		例如用户全名等 
用户目录：		用户登录系统后所进入的目录 
用户环境:		用户工作的环境
```
> **/etc/shadow密码文件**  

```
[centos@localhost ~]$ su - root
Password: 
[root@localhost ~]# cat /etc/shadow
root:$1$nLvd7FYA$4sMeZlG2ZeUgqzVTiP/39/:17344:0:99999:7:::
```

```
shadow文件中每条记录用冒号间隔的9个字段组成. 
用户名：用户登录到系统时使用的名字，而且是惟一的 
口令：  存放加密的口令 
最后一次修改时间:  标识从某一时刻起到用户最后一次修改时间 
最大时间间隔:  口令保持有效的最大天数，即多少天后必须修改口令 
最小时间间隔：	再次修改口令之间的最小天数 
警告时间：从系统开始警告到口令正式失效的天数 
不活动时间：	口令过期少天后，该账号被禁用 
失效时间：指示口令失效的绝对天数(从1970年1月1日开始计算) 
标志：未使用 
```
> **/etc/group组信息文件**

```
[root@localhost ~]# cat /etc/group
root:x:0:
```

```
root:x:0: 
组名：用户所属组 
组口令：一般不用 
GID：组ID 
用户列表：属于该组的所有用户
```

### 文件权限

```
[centos@localhost ~]$ ll
total 36
-rw-rw-r--. 1 centos centos 1433 Jun 27 22:15 a.txt
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Desktop
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Documents
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Downloads
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Music
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Pictures
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Public
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Templates
drwxr-xr-x. 2 centos centos 4096 Jun 27 02:53 Videos
```

> **-rw-rw-r--详解**

* 第一位: -:表示普通文件,d: 表示目录, l:表示符号链接
* 第二到第十位: 如下所示:
* -表示没有权限,r:表示读权限, w:表示写权限,x:表示执行权限

```
属主（user）	属组（group）	其他用户
r	w	x	r	w	x	r	w	x
4	2	1	4	2	1	4	2	1
```

### 文件类型

> **Linux三种文件类型**

* 普通文件： 包括文本文件、数据文件、可执行的二进制程序文件等。 
* 目录文件： Linux系统把目录看成是一种特殊的文件，利用它构成文件系统的树型结构。   
* 设备文件： Linux系统把每一个设备都看成是一个文件


### 文件类型标识

***

* 普通文件（-） 
* 目录（d） 
* 符号链接（l）
* 进入etc可以查看，相当于快捷方式 字符设备文件
* （c） 块设备文件
* （s） 套接字（s）
* 命名管道（p）


### 文件权限管理


* chmod 变更文件或目录的权限。
* chmod 755 a.txt 
* chmod u=rwx,g=rx,o=rx a.txt
* chmod 000 a.txt  / chmod 777 a.txt chown 变更文件或目录改文件所属用户和组
* chmod u+w a.txt 添加写权限
* chmod u-w a.txt 撤销写权限
* chown u1:public a.txt	：变更当前的目录或文件的所属用户和组
* chown -R u1:public dir	：变更目录中的所有的子目录及文件的所属用户和组



```
chmod 变更文件或目录的权限。
chmod 755 a.txt 
chmod u=rwx,g=rx,o=rx a.txt
chmod 000 a.txt  / chmod 777 a.txt chown 变更文件或目录改文件所属用户和组
chown u1:public a.txt	：变更当前的目录或文件的所属用户和组
chown -R u1:public dir	：变更目录中的所有的子目录及文件的所属用户和组
```
