# Dockerfile 体系结构

## Dockerfile关键字

* FROM：基础镜像，当前新镜像是基于哪个镜像的
* MAINTAINER：作者的姓名和邮箱
* RUN：容器构建时需要运行的命令
* EXPOSE：当前容器对外暴露出的端口，如果需要与宿主机映射需要run时-p指定映射关系
* WORKDIR：指定再创建容器后，终端默认登录进来的工作目录，一个落脚点
* ENV：用来在构建镜像过程中设置环境变量
* ADD：将宿主机目录下的文件拷贝进镜像且ADD命令会自动处理URL和解压tar压缩包
* COPY：类似ADD，拷贝文件和目录到镜像中，将从构建上下文目录中<源路径>的文件/目录复制到新的一层的镜像内的<目录路径>位置 COPY src dest  COPY ["src","dest"]
* VOLUME：容器数据卷，用于数据保存和持久化工作
* CMD：指定一个容器启动时要运行的命令；Dockerfile中可以有多个CMD指令，但只有最后一个生效，CMD会被docker run之后的参数替换
* ENTRYPOINT：指定一个容器启动时要运行的命令；ENTRYPOINT的目的和CMD一样，都是在指定容器启动程序及参数
* ONBUILD：当构建一个被继承的Dockerfile时运行命令，父镜像在被子镜像继承后父镜像的onbuild被触发

## Dockerfile案例-1

docker 官网的精简版的centos不支持vim,ifconfig命令，并且登录后默认的工作目录是/

> 改造目标

基于centos镜像，添加vim、ifconfig命令；修改登录后的工作目录

> dockerfile脚本

```dockerfile
FROM centos
MAINTAINER king-pan<pwpw1218@gmail.com>
ENV mypath /usr/local
WORKDIR $mypath
RUN yum -y install vim
RUN yum -y install net-tools
EXPOSE 80
CMD echo $mypath
CMD echo "build mycentos image success"
CMD /bin/bash
```

> 构建命令

```sh
cd /mydocker/mycentos
docker build -t mycentos:v2 .
```

	>构建结果

```sh
[root@linux mycentos]# docker build -t mycentos:v2 .
Sending build context to Docker daemon 2.048 kB
Step 1/10 : FROM centos
 ---> 0f3e07c0138f
Step 2/10 : MAINTAINER king-pan<pwpw1218@gmail.com>
 ---> Running in f7ef89f43afe
 ---> 2efa4b8fa719
Removing intermediate container f7ef89f43afe
Step 3/10 : ENV mypath /usr/local
 ---> Running in 9d61e2e771dc
 ---> 486af9820fe6
Removing intermediate container 9d61e2e771dc
Step 4/10 : WORKDIR $mypath
 ---> c6d890e47b60
Removing intermediate container 0eacfc0d1d67
Step 5/10 : RUN yum -y install vim
 ---> Running in 4048195ed2e2

CentOS-8 - AppStream                            1.4 MB/s | 6.3 MB     00:04
CentOS-8 - Base                                 160 kB/s | 7.9 MB     00:50
CentOS-8 - Extras                               597  B/s | 2.1 kB     00:03
Dependencies resolved.
================================================================================
 Package             Arch        Version                   Repository      Size
================================================================================
Installing:
 vim-enhanced        x86_64      2:8.0.1763-10.el8         AppStream      1.4 M
Installing dependencies:
 gpm-libs            x86_64      1.20.7-15.el8             AppStream       39 k
 vim-common          x86_64      2:8.0.1763-10.el8         AppStream      6.3 M
 vim-filesystem      noarch      2:8.0.1763-10.el8         AppStream       48 k
 which               x86_64      2.21-10.el8               BaseOS          49 k

Transaction Summary
================================================================================
Install  5 Packages

Total download size: 7.8 M
Installed size: 30 M
Downloading Packages:
(1/5): gpm-libs-1.20.7-15.el8.x86_64.rpm         33 kB/s |  39 kB     00:01
(2/5): vim-filesystem-8.0.1763-10.el8.noarch.rp 529 kB/s |  48 kB     00:00
(3/5): which-2.21-10.el8.x86_64.rpm             430 kB/s |  49 kB     00:00
(4/5): vim-enhanced-8.0.1763-10.el8.x86_64.rpm  892 kB/s | 1.4 MB     00:01
(5/5): vim-common-8.0.1763-10.el8.x86_64.rpm    2.8 MB/s | 6.3 MB     00:02
--------------------------------------------------------------------------------
Total                                           1.5 MB/s | 7.8 MB     00:05
warning: /var/cache/dnf/AppStream-02e86d1c976ab532/packages/gpm-libs-1.20.7-15.el8.x86_64.rpm: Header V3 RSA/SHA256 Signature, key                                                                                                                                                                 ID 8483c65d: NOKEY
CentOS-8 - AppStream                            122 kB/s | 1.6 kB     00:00
Importing GPG key 0x8483C65D:
 Userid     : "CentOS (CentOS Official Signing Key) <security@centos.org>"
 Fingerprint: 99DB 70FA E1D7 CE22 7FB6 4882 05B5 55B3 8483 C65D
 From       : /etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
Key imported successfully
Running transaction check
Transaction check succeeded.
Running transaction test
Transaction test succeeded.
Running transaction
  Preparing        :                                                        1/1
  Installing       : which-2.21-10.el8.x86_64                               1/5
  Installing       : vim-filesystem-2:8.0.1763-10.el8.noarch                2/5
  Installing       : vim-common-2:8.0.1763-10.el8.x86_64                    3/5
  Installing       : gpm-libs-1.20.7-15.el8.x86_64                          4/5
  Running scriptlet: gpm-libs-1.20.7-15.el8.x86_64                          4/5
  Installing       : vim-enhanced-2:8.0.1763-10.el8.x86_64                  5/5
  Running scriptlet: vim-enhanced-2:8.0.1763-10.el8.x86_64                  5/5
  Running scriptlet: vim-common-2:8.0.1763-10.el8.x86_64                    5/5
  Verifying        : gpm-libs-1.20.7-15.el8.x86_64                          1/5
  Verifying        : vim-common-2:8.0.1763-10.el8.x86_64                    2/5
  Verifying        : vim-enhanced-2:8.0.1763-10.el8.x86_64                  3/5
  Verifying        : vim-filesystem-2:8.0.1763-10.el8.noarch                4/5
  Verifying        : which-2.21-10.el8.x86_64                               5/5

Installed:
  vim-enhanced-2:8.0.1763-10.el8.x86_64 gpm-libs-1.20.7-15.el8.x86_64
  vim-common-2:8.0.1763-10.el8.x86_64   vim-filesystem-2:8.0.1763-10.el8.noarch
  which-2.21-10.el8.x86_64

Complete!
 ---> ae767f4c2981
Removing intermediate container 4048195ed2e2
Step 6/10 : RUN yum -y install net-tools
 ---> Running in 08cc66fc8b69

error: rpmdb: damaged header #175 retrieved -- skipping.
error: rpmdb: damaged header #175 retrieved -- skipping.
error: rpmdbNextIterator: skipping h#     175 blob size(13252): BAD, 8 + 16 * il(71) + dl(12108)
Last metadata expiration check: 0:00:13 ago on Wed Nov 27 01:00:36 2019.
Dependencies resolved.
================================================================================
 Package         Arch         Version                        Repository    Size
================================================================================
Installing:
 net-tools       x86_64       2.0-0.51.20160912git.el8       BaseOS       323 k

Transaction Summary
================================================================================
Install  1 Package

Total download size: 323 k
Installed size: 1.0 M
Downloading Packages:
net-tools-2.0-0.51.20160912git.el8.x86_64.rpm   227 kB/s | 323 kB     00:01
error: rpmdbNextIterator: skipping h#     175 blob size(13252): BAD, 8 + 16 * il(71) + dl(12108)
--------------------------------------------------------------------------------
Total                                           112 kB/s | 323 kB     00:02
warning: /var/cache/dnf/BaseOS-f6a80ba95cf937f2/packages/net-tools-2.0-0.51.20160912git.el8.x86_64.rpm: Header V3 RSA/SHA256 Signa                                                                                                                                                                ture, key ID 8483c65d: NOKEY
CentOS-8 - Base                                 1.6 MB/s | 1.6 kB     00:00
error: rpmdbNextIterator: skipping h#     175 blob size(13252): BAD, 8 + 16 * il(71) + dl(12108)
error: rpmdbNextIterator: skipping h#     175 blob size(13252): BAD, 8 + 16 * il(71) + dl(12108)
Importing GPG key 0x8483C65D:
 Userid     : "CentOS (CentOS Official Signing Key) <security@centos.org>"
 Fingerprint: 99DB 70FA E1D7 CE22 7FB6 4882 05B5 55B3 8483 C65D
 From       : /etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
error: rpmdbNextIterator: skipping h#     175 blob size(13252): BAD, 8 + 16 * il(71) + dl(12108)
Key imported successfully
error: rpmdbNextIterator: skipping h#     175 blob size(13252): BAD, 8 + 16 * il(71) + dl(12108)
Running transaction check
Transaction check succeeded.
Running transaction test
Transaction test succeeded.
Running transaction
  Preparing        :                                                        1/1
  Installing       : net-tools-2.0-0.51.20160912git.el8.x86_64              1/1
  Running scriptlet: net-tools-2.0-0.51.20160912git.el8.x86_64              1/1
  Verifying        : net-tools-2.0-0.51.20160912git.el8.x86_64              1/1

Installed:
  net-tools-2.0-0.51.20160912git.el8.x86_64

Complete!
 ---> 79de08e31664
Removing intermediate container 08cc66fc8b69
Step 7/10 : EXPOSE 80
 ---> Running in a3640fca3395
 ---> 2a10bd503fe9
Removing intermediate container a3640fca3395
Step 8/10 : CMD echo $mypath
 ---> Running in 6af73a24a6fd
 ---> 292cc41d876d
Removing intermediate container 6af73a24a6fd
Step 9/10 : CMD echo "build mycentos image success"
 ---> Running in e2fd8e902743
 ---> beb91f4b4ef1
Removing intermediate container e2fd8e902743
Step 10/10 : CMD /bin/bash
 ---> Running in 362c530b6b17
 ---> 76ecc19d917c
Removing intermediate container 362c530b6b17
Successfully built 76ecc19d917c
```

> 查询镜像验证

```sh
[root@linux mycentos]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
mycentos            v2                  76ecc19d917c        2 minutes ago       316 MB
mycentos/v1         latest              02dfc0e4cae6        9 hours ago         220 MB
test/mycentos       latest              58b4e62a0de4        12 hours ago        220 MB
web_tomcat7_user    latest              12841564aa53        2 days ago          830 MB
docker.io/redis     latest              dcf9ec9265e0        3 days ago          98.2 MB
docker.io/mysql     5.6                 e143ed325782        3 days ago          302 MB
docker.io/nginx     latest              231d40e811cd        3 days ago          126 MB
docker.io/centos    7                   5e35e350aded        2 weeks ago         203 MB
docker.io/busybox   latest              020584afccce        3 weeks ago         1.22 MB
docker.io/centos    latest              0f3e07c0138f        8 weeks ago         220 MB
docker.io/tomcat    7-jre7              47c156f4d4e3        6 months ago        359 MB
docker.io/java      8                   d23bdf5b1b1b        2 years ago         643 MB
```

> 运行镜像

```sh
# 默认登录后的工作空间修改成功
[root@linux mycentos]# docker run -it mycentos:v2
[root@a0ca8ddb7f59 local]# pwd
/usr/local
# vim命令可以使用
[root@a0ca8ddb7f59 local]# vim a.txt
[root@a0ca8ddb7f59 local]# ls
bin  etc  games  include  lib  lib64  libexec  sbin  share  src
# ifconfig命令可以使用
[root@a0ca8ddb7f59 local]# ifconfig
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.17.0.2  netmask 255.255.0.0  broadcast 0.0.0.0
        inet6 fe80::42:acff:fe11:2  prefixlen 64  scopeid 0x20<link>
        ether 02:42:ac:11:00:02  txqueuelen 0  (Ethernet)
        RX packets 8  bytes 656 (656.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 8  bytes 656 (656.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

[root@a0ca8ddb7f59 local]#

```

