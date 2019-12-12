#	什么是Nginx

Nginx ("engine x") 是一个高性能的 HTTP和反向代理服务器，也是一个 IMAP/POP3/SMTP 服务器。

正向代理与反向代理：

![正反向代理](https://coding.net/u/javaBlog/p/markdown_img/git/raw/master/nginx_img/nginx_zf_proxy.png)


很多大网站都是使用nginx做反向代理，应用非常广泛。
Nginx是一款高性能的http 服务器/反向代理服务器及电子邮件（IMAP/POP3）代理服务器。由俄罗斯的程序设计师Igor Sysoev所开发，官方测试nginx能够支支撑5万并发链接，并且cpu、内存等资源消耗却非常低，运行非常稳定。

#	应用场景
* http服务器，可以做静态网页的http服务器。
* 配置虚拟机。
一个域名可以被多个ip绑定。可以根据域名的不同吧请求转发给运行在不同端口的服务器。
* 反向代理，负载均衡。把请求转发给不同的服务器。

#	安装及配置
##	下载

官方网站：http://nginx.org/


```
#wget -c https://nginx.org/download/nginx-1.12.1.tar.gz 
``` 

##	安装
###	环境要求

nginx是C语言开发，建议在linux上运行，本教程使用Centos6.4作为安装环境。

> **gcc**

安装nginx需要先将官网下载的源码进行编译，编译依赖gcc环境，如果没有gcc环境，需要安装gcc：yum install gcc-c++ 

> **PCRE**

PCRE(Perl Compatible Regular Expressions)是一个Perl库，包括 perl 兼容的正则表达式库。nginx的http模块使用pcre来解析正则表达式，所以需要在linux上安装pcre库。

```
#yum install -y pcre pcre-devel
```

**注：pcre-devel是使用pcre开发的一个二次开发库。nginx也需要此库。**

> **zlib**

zlib库提供了很多种压缩和解压缩的方式，nginx使用zlib对http包的内容进行gzip，所以需要在linux上安装zlib库。

```
#yum install -y zlib zlib-devel
```

> **openssl**

OpenSSL 是一个强大的安全套接字层密码库，囊括主要的密码算法、常用的密钥和证书封装管理功能及SSL协议，并提供丰富的应用程序供测试或其它目的使用。
nginx不仅支持http协议，还支持https（即在ssl协议上传输http），所以需要在linux安装openssl库。

```
#yum install -y openssl openssl-devel
```


###	编译及安装

* 第一步：把nginx的源码包上传至linux服务器
* 第二步：解压源码包。 tar -zxf nginx-1.12.1.tar.gz 
* 第三步：进入nginx-1.12.1文件夹。使用configure命令创建makefile。
* 第四步：参数设置如下：

```
./configure \
--prefix=/usr/local/nginx \
--pid-path=/var/run/nginx/nginx.pid \
--lock-path=/var/lock/nginx.lock \
--error-log-path=/var/log/nginx/error.log \
--http-log-path=/var/log/nginx/access.log \
--with-http_gzip_static_module \
--http-client-body-temp-path=/var/temp/nginx/client \
--http-proxy-temp-path=/var/temp/nginx/proxy \
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
--http-scgi-temp-path=/var/temp/nginx/scgi
```

**注意：上边将临时文件目录指定为/var/temp/nginx，需要在/var下创建temp及nginx目录**
* 第五步：make
* 第六步 make install
 

##	Nginx的启动及关闭
###	启动
在nginx目录下有一个sbin目录，sbin目录下有一个nginx可执行程序。

```
#./nginx
```

 
 
###	关闭nginx
关闭命令：相当于找到nginx进程kill。

```
#./nginx -s stop
```
退出命令：
```
#./nginx -s quit
```

等程序执行完毕后关闭，建议使用此命令。

###	动态加载配置文件

```
#./nginx -s reload
```
可以不关闭nginx的情况下更新配置文件。


### 测试启动成功

在浏览器地址栏输入:
```
localhost
```

![Nginx启动成功](https://coding.net/u/javaBlog/p/markdown_img/git/raw/master/nginx_img/nginx_success.png)

