# RocketMQ面试专题

## 为什么使用消息队列，怎么选择技术

> 为什么使用消息队列

* 异步
* 解耦
* 肖峰
* 缺点
  * 系统可用性降低： 外部依赖越多，出问题风险越大
  * 系统复杂性提高：需要考虑多种场景，重复消费、消息丢失
  * 需要更多的机器和人力：消息队列一般集群部署。而且需要运维和监控，例如topic申请等。

> 消息队列的选择

* ActiveMQ

  Apache出品，历史悠久，支持多种语言的客户端和协议。支持多种语言Java、.NET、C++等，基于JMS Provider的实现

  缺点：吞吐量不高，多队列的时候性能下降，存在消息丢失的情况，比较少大规模使用

* Kafka

  Apache开发的一个开源流处理平台，由Scala和java编写。Kafka是一种高吞吐量的分布式发布订阅消息系统，它可以处理大规模的网站中的所有动作流数据(网页搜索和其他用户的行动信息)，副本集机制，实现数据冗余，保障数据尽量不丢失；支持多个生产者和消费者。

  缺点：不支持批量和广播消息，运维难度大，文档比较少，需要掌握scala，二次开发难度大

* RabbitMQ

  是一个开源的AMQP实现，服务端使用Erlang语言编写，支持多种客户端，如: Python、Ruby、.NET、Java、JMS、C、用于在分布式系统中存储转发消息，在易用性、扩展性、高可用等方面表现不错

  缺点：使用Erlang开发，阅读和修改源码难度大

* RocketMQ

  阿里开源的一款消息中间件，现在已经捐赠给Apache了，纯Java开发，具有高吞吐量，高可用，适合大规模分布式系统应用的特定，性能强劲(零拷贝技术)，支持海量堆积，支持指定次数和时间间隔的失败消息重发，支持consumer端tag过滤，延迟消息等，在阿里内部进行大规模使用，适合在电商、互联网金融等领域使用。

  缺点：部分实现不是按照标注JMS规范，有些系统要迁移或者引入队列需要修改代码。

## 消息队列怎么样可以避免重复消费

​	RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己再业务端去重

> 接口幂等性保障，消费端处理业务消息要保持幂等性

* Redis

  * setNx()做消息去重，java版本目前不支持设置过期时间

  ```java
  boolean flag = jedis.setNX(key);
  if(flag){
      //第一次消费
  }else{
      //重复消费
  }
  ```

  如果再用expire则不是原子操作，可以用下面方式实现分布式锁

  ```java
  //加锁
  String result = jedis.set(key,value,"NX","PX",expireTime);
  //解锁(Lua脚本，先检查key，匹配再释放锁，lua可以保证原子性)
  String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
  Object result = jedis.eval(script,Collections.singletonList(locakKey),Collections.SingletonList(requestId));
  //locakKey可以是商品ID，requestId用于标示是同一个客户端
  ```

  * incr原子操作: key自增，返回值大于0则表示消费过

  ```java
  int num = jedis.incr(key);
  if(num == 1){
      //第一次消费
  }else{
      //重复消费
  }
  ```

* 上述两种方式都可以，但是不能用于分布式锁，考虑原子性问题，但是排重可以不考虑原子问题，数据量多需要设置过期时间
* 数据库去重表
  * 某个字段使用Message的key做唯一索引



### RocketMQ如何保证消息的可靠性传输



* producer端
  * 不采用oneway方式发送，使用同步或者异步方式发送，做好重试，但是重试的message key必须唯一
  * 投递的日志需要保存，关键字段，投递时间，投递状态，重试次数，请求体，相应体
* broker端
  * 双主双从架构，NameServer需要多节点
  * 同步双写，异步刷盘(同步刷盘则可靠性更高，但是性能差一点，根据业务选择)
* consumer端
  * 消息消费务必保留日志，即消息的元数据和消息体
  * 消费端务必做好幂等性处理
* 投递到broker端后
  * 机器断电重启: 异步刷盘，消息丢失；同步刷盘消息不丢失
  * 硬件故障:可能存在丢失，看队列架构



## RocketMQ如何处理大量堆积在broker里面的消息

线上故障了，怎么处理

* 消息堆积了10个小时，有几千万消息待处理，现在怎么办?
* 修复consumer然后慢慢消费？也需要几个小时才可以消费完成，新的消息怎么办?

> 正确方法

* 历史topic队列扩容，并提供消费者能力，但是如果增加consumer数量，但是堆积的topic里面的message queue数量固定，过多的consumer不能分配到message queue
* 编写临时处理分发程序，从旧topic快速取到临时新的topic中，新的topic的queue数量扩容多倍，然后再启动更多consumer进行临时新的topic里消费。



## RocketMQ高性能的原因分析

* MQ架构配置

  * 顺序写，随机读，零拷贝
  * 同步刷盘SYNC_FLUSH和异步刷盘ASYNC_FLUSH，通过flushDiskType配置
  * 同步复制和异步复制，通过brokerRole配置，ASYNC_MASTER,SYNC_MASTER,SLAVE
  * 推荐同步复制(双写)，异步刷盘

* 发送端高可用

  * 双主双从架构：创建topic对应的时间，MessageQueue创建在多个Broker上，即相同的Broker名称，不同的brokerid（即主从模式）；当一个Master不可用时，组内其他的Master任然可用

    但是机器资源不足时，需要手动把slave转成master，目前不支持自动转换，可用shell处理

* 消费高可用

  * 主从架构：broker角色，，Master提供读写，slave只支持读
  * consumer不用配置，当master不可用或者繁忙的时候，consumer会自动切换到slave节点进行读取

* 提供消息的消费能力

  * 并行消费
    * 增加多个节点
    * 增加单个consumer的并行度，修改consumerThreadMin和consumerThreadMax
    * 批量消费，设置consumer的consumerMessageBatchMaxSize，默认为1，如果为N，则消费多的时候，每次收到的消息为N条
  * 选择Linux EXT4文件系统，1G大小的文件通常耗时小于50ms而Ext3文件系统耗时需要1s，删除文件时磁盘IO压力极大，会导致IO操作超时等。



## 常见面试题汇总

为什么使用消息队列

消息队列技术选择

如果保证消息队列的高可用

消息传输的可靠性

消息的重复消费

消息的顺序消费

消息堆积怎么处理.

