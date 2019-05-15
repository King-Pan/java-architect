# 01-RocketMQ初识



## RocketMQ安装

> 下载地址: 

[**http://mirrors.tuna.tsinghua.edu.cn/apache/rocketmq/4.4.0/rocketmq-all-4.4.0-source-release.zip**](http://mirrors.tuna.tsinghua.edu.cn/apache/rocketmq/4.4.0/rocketmq-all-4.4.0-source-release.zip)

> 安装步骤

```shell
//解压编译

> unzip rocketmq-all-4.4.0-source-release.zip
> cd rocketmq-all-4.4.0/
> mvn -Prelease-all -DskipTests clean install -U
> cd distribution/target/apache-rocketmq

//Start Name Server

> nohup sh bin/mqnamesrv &
> tail -f ~/logs/rocketmqlogs/namesrv.log
The Name Server boot success...

//Start Broker

> nohup sh bin/mqbroker -n localhost:9876 &
> tail -f ~/logs/rocketmqlogs/broker.log 
The broker[%s, 172.30.30.233:10911] boot success...

//Shutdown Servers

> sh bin/mqshutdown broker
The mqbroker(36695) is running...
Send shutdown request to mqbroker(36695) OK

> sh bin/mqshutdown namesrv
The mqnamesrv(36664) is running...
Send shutdown request to mqnamesrv(36664) OK
```

nohup sh bin/mqbroker -n localhost:9876 -c ./conf/broker.conf

## RocketMQ简介

​	RocketMQ是一款分布式，队列模型的消息中间件，由阿里巴巴研发，借鉴惨了JMS规范的MQ实现，更参考了优秀的开源消息中间件KAFKA，并且结合阿里实际业务需求，在天猫双十一的场景，实现义务消峰，分布式事务的优秀框架。目前开源稳定版本为4.4.0，最新版本为4.5.0。阿里巴巴把RocketMQ捐赠给了Apache。



## JMS消息服务介绍和使用场景

JMS: Java消息服务(JAVA Message Service),java平台中关于面向消息中间件的接口。

JMS是一种与厂商无关的API,用来访问消息收发系统消息，它类似于JDBC(Java Database Connectivity)。这里,JDBC是可以用来访问许多不同关系数据库的API.

使用场景:

* 核心应用
  * 解耦: 订单系统->物流系统
  * 异步: 用户注册->发送邮件->发送短信
  * 削峰: 秒杀、日志处理
* 跨平台、多语言
* 分布式事务、最终一致性
* RPC调用上下游对接、数据源变动->通知下属
* 

​	





## 启动报错

```java
/opt/rocketmq-all-4.4.0/distribution/target/apache-rocketmq/bin/runbroker.sh: line 62: 25326 Killed                  $JAVA ${JAVA_OPT} $@

    
    调整Xms参数
```



```java

```

