# RocketMQ顺序消息

## 什么是顺序消息

​	消息的生产和消费顺序一致。

​	[官方顺序消息实例](http://rocketmq.apache.org/docs/order-example/)

## 顺序消息的分类

​	顺序消息可以分为：全局顺序、局部顺序

### 全局顺序

​	topic下面全部消息都要有序(很少用、并行度不够)

* 性能要求不高，仅适用所有的消息严格按照FIFO原则进行消息发布和消费的场景，并行度成为消息系统的瓶颈、吞吐量不够。
* 在证券处理中，以人民币兑换美元为例，在价格相同的情况下，先出价者优先处理，则可以通过全局顺序的方式按照FIFO的方式进行发布和消费。

### 局部顺序

​	只要保证一组消息被顺序消费即可(RocketMQ使用)

* 性能要求高
* 电商系统订单的创建，同一订单相关的创建订单消息、订单支付消息、订单退款消息、订单物流消息、订单交易成功消息都会按照先后顺序来发布和消费。（阿里巴巴内部电商系统均使用顺序消息，既保证业务的顺序性，同时又能保证业务的高性能）

## 顺序发布

​	对于指定的一个topic、客户端将按照一定的先后顺序发送消息

```java
public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer();

        producer.setNamesrvAddr("39.104.164.68:9876");
        producer.setProducerGroup("order_producer_group");
        try {
            producer.start();
            String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
            for (int i = 0; i < 100; i++) {
                int orderId = i % 4;
                Message msg = new Message("order_topic", tags[i % tags.length], "KEY" + i,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer) arg;
                        int index = id % mqs.size();
                        return mqs.get(index);
                    }
                }, orderId);

                System.out.printf("%s%n", sendResult);
            }

            producer.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```



## 顺序消费

对于指定的一个topic，按照一定的先后顺序接受消息，即先发送的消息一定会先被客户端接收到。

```java
public static void main(String[] args) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setConsumerGroup("order_consumer_group");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr("39.104.164.68:9876");

        try {
            consumer.subscribe("order_topic", "TagA || TagC || TagD");
            consumer.registerMessageListener(new MessageListenerOrderly() {

                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    //context.setAutoCommit(false);
                    System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                    return ConsumeOrderlyStatus.SUCCESS;
                }


            });
            consumer.start();
            System.out.printf("Consumer Started.%n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```



## 注意

	>顺序消息暂不支持广播模式

> 顺序消息不支持异步发送方式，否则将无法严格保证顺序



