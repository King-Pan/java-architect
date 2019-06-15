# RocketMQ核心配置



## 生产者核心配置



## 消费者核心配置

> consumerFromWhere

​	consumerFromWhere配置(某些请求下失效:https://blog.csdn.net/a417930422/article/details/83585397)

```java
public enum ConsumeFromWhere {
    CONSUME_FROM_LAST_OFFSET,

    @Deprecated
    CONSUME_FROM_LAST_OFFSET_AND_FROM_MIN_WHEN_BOOT_FIRST,
    @Deprecated
    CONSUME_FROM_MIN_OFFSET,
    @Deprecated
    CONSUME_FROM_MAX_OFFSET,
    CONSUME_FROM_FIRST_OFFSET,
    CONSUME_FROM_TIMESTAMP,
}
```

去掉过期的三个，主要有三个配置选项: CONSUME_FROM_LAST_OFFSET、CONSUME_FROM_FIRST_OFFSET、CONSUME_FROM_TIMESTAMP

* CONSUME_FROM_LAST_OFFSET

  默认策略：初次从该队列最尾开始消费、即跳过历史消息、后续再启动接着上次消费的进度开始消费

* CONSUME_FROM_FIRST_OFFSET

  初次从消息队列头部开始消费，即历史消息(还存储再broker上的)全部消费一遍，后续再启动接着上次消费的进度开始消费。

* CONSUME_FROM_TIMESTAMP

  从某个时间点开始消费，默认是半个小时以前，后续再接着上次消费的进度开始消费。

> allocateMessageQueueStrategy

​		负载均衡策略算法，即消费者分配到queue的算法，默认是AllocateMessageQueueAveragely即取模平均分配

> offsetStore 

​		消息消费进度存储器，offsetStore有两个策略：

* LocalFileOffsetStore 广播模式默认使用该策略
* RemoteBrokerOffsetStore集群模式默认使用该策略

> consumeThreadMin

​	消费者线程池最小线程数量

> consumeThreadMax

​	消费者线程池最大线程数量

> pullBatchSize

​	消费者去broker拉取消息时，一次拉取多少条。可选配置

> consumeMessageBatchMaxSize

​	单次消费者最多可以拉取的消息条数，只有批量消费接口才生效，可选配置

> messageModel

​	消费者模式，有两种模式:

* CLUSTERING 集群模式，默认该模式

* BROADCASTING 广播模式，**==需要注意广播模式下，消费端不支持消息重试==**

### 集群模式下消费端消费消息流程

> Topic下队列的奇偶数会影响Consumer的消费个数

* 如果是4个队列，8个消息，4个节点则会给消费2条，如果不对等，则负载均衡会分配不均；
* 如果consumer实例的数量比message queue的总数量还多的话，多出来的consumer实例将无法分到queue，也就无法消费到消息，也就无法祈祷分摊负载的作用，所以需要控制queue的数量大于等于consumer的数量。

> 集群模式下消息消费流程

* consumer实例平均分摊消费生产者发送的消息
* 例子: 订单消息，一般只被消费一次

> 广播模式下消息消费流程

* 广播模式下消费消息:投递到Broker的消息会被每个Consumer进行消费，一条消息被多个consumer消费，广播消费中ConsumerGroup暂时无用。
* 例子: 群公告，每个人都需要消费这个消息

> 消费端切换消费模式

```java
//设置消费模式为广播
consumer.setMessageModel(MessageModel.BROADCASTING);
//设置消费模式为集群(默认)
consumer.setMessageModel(MessageModel.CLUSTERING);

//org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
private MessageModel messageModel = MessageModel.CLUSTERING;

```

