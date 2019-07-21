# 1、Redis认识

## 1.1、Redis是什么

* 开源 
* 多种数据结构

* 基于键值的存储服务系统
* 高性能，功能丰富

## 1.2、Redis的特性

* 速度快
* 持久化
* 多种数据结构
* 支持多种编程语言
* 功能丰富
* 简单
* 主从复制
* 高可用、分布式

### 1.2.1、Redis速度快

50000行代码(c语言)

10wops

数据存储在内存中(Redis快的主要原因)

redis单线程

### 1.2.2、Redis持久化

Redis的数据保存在内存中，出现异常后数据丢失，所有Redis需要把内存中的数据持久化操作。

Redis所有数据保持在内存中，对数据的操作将异步地保存到磁盘上。

### 1.2.3、Redis多中数据结构

* key-value
* hash
* linked list
* set
* sorted set
* BitMaps 位图
* HyperLogLog 超小内存唯一值计数
* GEO 地理信息定位



### 1.2.4、Redis支持多种编程语言

java、php、python、Ruby、Lua、nodejs...支持Redis



### 1.2.5、Redis功能丰富

* 发布订阅
* Lua脚本，自定义命令
* 事务
* pipeline

### 1.2.6、Redis简单

* 单机版23000行C语言代码

* 分布式版本50000行C语言代码
* 不依赖外部库
* 单线程模型，服务端、客户端都很简单

### 1.2.7、Redis主从复制

从服务器从主服务器上复制数据



### 1.2.8、Redis高可用、分布式

* Redis从2.8版本实现了Redis-sentinel支持高可用
* Redis从3.0版本实现Redis-Cluster支持分布式



## 1.3、Redis初识

### 1.3.1、Redis的典型使用场景

* 缓存系统
* 计数器
* 消息队列系统
* 排行榜
* 社交网络
* 实时系统



## 1.4、Redis安装文件解析

### 1.4.1、Redis可执行文件说明

* Redis-server redis服务器
* redis-cli Redis命令行客户端
* redis-benchmark Redis性能测试
* redis-check-aof   aof文件修复工具
* redis-check-dump   dump文件修复工具
* redis-sentinel sentinel服务器

### 1.4.2、Redis三种启动方式

#### 1.4.2.1、Redis最简启动

```shell
./redis-server   使用默认的配置文件启动
```



#### 1.4.2.2、Redis动态参数启动

```shell
./redis-server --port 6380
```

#### 1.4.2.3、Redis指定配置文件启动

```shell
./redis-server configPath
```

### 1.4.3、Redis三种启动方式的比较

* 生产环境选择配置启动
* 单机多实例配置文件可以用端口区分开



### 1.4.4、Redis客户端连接

> 客户端连接参数

* -h ip地址
* -p 端口默认6379
* -a 密码

```shell
redis-cli -h 127.0.0.1 -p 6384 
```

### 1.4.5、Redis客户端返回值

> 状态回复

ping - pong 

> 错误提示

对key-vue进行hget操作

> 整数回复

incr hello

> 字符串回复

get hello

> 多行字符串回复

mget hello name





### 1.4.6、Redis启动验证

```shell
ps -ef|grep redis  ## 查看进程
netstat -antp|grep redis   ## 查看端口
redis-cli -h ip -p port ping  ## redis客户端ping
```



## 1.5、常用配置

> daemonize守护线程配置

是否以守护线程启动 no|yes

> port 默认端口

默认端口6379,老款手机的6379对应MERZ意大利歌手Alessia Merz的名字

> logfile日志文件

Redis系统日志

> dir   工作目录

Redis工作目录







