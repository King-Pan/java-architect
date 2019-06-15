# 03-RocketMQ延时消息

`rocketmq`提供一种延时消息的解决方案，就是在特定的时间到了，消息才会被投递出去供`consumer`消费。

*总体来是简单的场景是满足了，但是需要注意的是延时的时间是需要按照默认配置的延时级别去配置的，而不是随意设置消息的延时时间。*

如果想不受延时级别的约束 可以参考之前的一遍文章<http://blog.seoui.com/2017/08/19/delayqueue/>

默认的延迟级别

rocketmq-store->MessageStoreConfig.java属性messageDelayLevel

```java
private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
```

这个配置下标从1开始 比如级别2是延时5秒、级别5是延时1分钟。默认配置在不满足需求的情况下，可以在`broker`配置文件加入`messageDelayLevel`参数覆盖默认的延时级别配置。

#### 示例

和普通的消息不同之处在于`Producer`在发送消息的时候 需要设置`message.setDelayTimeLevel();`延迟级别方法。其他参数和消费端的写法并与不同之处。

```java
//延时的级别为3 对应的时间为10s 就是发送后延时10S在把消息投递出去,其他并没有不同，实际上是RocketMQ底层对延时消息做了处理
message.setDelayTimeLevel(3);
// org.apache.rocketmq.common.message.Message
public void setDelayTimeLevel(int level) {
    this.putProperty(MessageConst.PROPERTY_DELAY_TIME_LEVEL, String.valueOf(level));
}
// org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#sendKernelImpl 

if (msg.getProperty("__STARTDELIVERTIME") != null || msg.getProperty(MessageConst.PROPERTY_DELAY_TIME_LEVEL) != null) {
    //设置消息类型为延时消息
    context.setMsgType(MessageType.Delay_Msg);
}
```

broker处理延时消息

broker收到延时消息和正常消息在前置的处理流程是一致的，对于延时消息的特殊处理体现在将消息写入存储(内存或文件)的时候

```java
// org.apache.rocketmq.store.CommitLog#putMessage
public PutMessageResult putMessage(final MessageExtBrokerInner msg) {
    // 省略中间代码...
    StoreStatsService storeStatsService = this.defaultMessageStore.getStoreStatsService();

    // 拿到原始topic和对应的queueId
    String topic = msg.getTopic();
    int queueId = msg.getQueueId();

    final int tranType = MessageSysFlag.getTransactionValue(msg.getSysFlag());
    // 非事务消息和事务的commit消息才会进一步判断delayLevel
    if (tranType == MessageSysFlag.TRANSACTION_NOT_TYPE
        || tranType == MessageSysFlag.TRANSACTION_COMMIT_TYPE) {
        // Delay Delivery
        if (msg.getDelayTimeLevel() > 0) {
            // 纠正设置过大的level，就是delayLevel设置都大于延时时间等级的最大级
            if (msg.getDelayTimeLevel() > this.defaultMessageStore.getScheduleMessageService().getMaxDelayLevel()) {
                msg.setDelayTimeLevel(this.defaultMessageStore.getScheduleMessageService().getMaxDelayLevel());
            }

            // 设置为延时队列的topic
            topic = ScheduleMessageService.SCHEDULE_TOPIC;
            // 每一个延时等级一个queue，queueId = delayLevel - 1
            queueId = ScheduleMessageService.delayLevel2QueueId(msg.getDelayTimeLevel());

            // Backup real topic, queueId
            // 备份原始的topic和queueId
            MessageAccessor.putProperty(msg, MessageConst.PROPERTY_REAL_TOPIC, msg.getTopic());
            MessageAccessor.putProperty(msg, MessageConst.PROPERTY_REAL_QUEUE_ID, String.valueOf(msg.getQueueId()));
            // 更新properties
            msg.setPropertiesString(MessageDecoder.messageProperties2String(msg.getProperties()));

            msg.setTopic(topic);
            msg.setQueueId(queueId);
        }
    }
    // 省略中间代码...
}
```

上面的SCHEDULE_TOPIC是：

```
public static final String SCHEDULE_TOPIC = "SCHEDULE_TOPIC_XXXX";
```

​	这个topic是一个特殊的topic，和正常的topic不同的地方是：

* 1、不会创建TopicConfig，因为也不需要consumer直接消费这个topic下的消息。
* 2、不会将topic注册到namesrv。
* 3、这个topic的队列个数和延时等级的个数是相同的。

​	后面消息写入的过程和普通的又是一致的。上面将消息写入延时队列中了，接下来就是处理延时队列中的消息，然后重新发送回原始topic的队列中。
​	在此之前先说明下至今还有疑问的一个个概念——delayLevel。这个概念和我们接下要需要用到的的类ScheduleMessageService有关，这个类的字段delayLevelTable里面保存了具体的延时等级。

```java
private final ConcurrentMap<Integer /* level */, Long/* delay timeMillis */> delayLevelTable = new ConcurrentHashMap<Integer, Long>(32);
```

看下这个字段的初始化过程：

```java
// org.apache.rocketmq.store.schedule.ScheduleMessageService#parseDelayLevel
public boolean parseDelayLevel() {
    HashMap<String, Long> timeUnitTable = new HashMap<String, Long>();
    // 每个延时等级延时时间的单位对应的ms数
    timeUnitTable.put("s", 1000L);
    timeUnitTable.put("m", 1000L * 60);
    timeUnitTable.put("h", 1000L * 60 * 60);
    timeUnitTable.put("d", 1000L * 60 * 60 * 24);

    // 延时等级在MessageStoreConfig中配置
    // private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
    String levelString = this.defaultMessageStore.getMessageStoreConfig().getMessageDelayLevel();
    try {
        // 根据空格将配置分隔出每个等级
        String[] levelArray = levelString.split(" ");
        for (int i = 0; i < levelArray.length; i++) {
            String value = levelArray[i];
            String ch = value.substring(value.length() - 1);
            // 时间单位对应的ms数
            Long tu = timeUnitTable.get(ch);

            // 延时等级从1开始
            int level = i + 1;
            if (level > this.maxDelayLevel) {
                // 找出最大的延时等级
                this.maxDelayLevel = level;
            }
            long num = Long.parseLong(value.substring(0, value.length() - 1));
            long delayTimeMillis = tu * num;
            this.delayLevelTable.put(level, delayTimeMillis);
    // 省略部分代码...
}
```

上面这个load方法在broker启动的时候DefaultMessageStore会调用来初始化延时等级。接下来就应该解决怎么处理延时消息队列中的消息的问题了。处理延时消息的服务是：**ScheduleMessageService**。是broker启动的时候DefaultMessageStore会调用ScheduleMessageService#start来启动处理延时消息队列的服务：

```java
public void start() {
    for (Map.Entry<Integer, Long> entry : this.delayLevelTable.entrySet()) {
        Integer level = entry.getKey();
        Long timeDelay = entry.getValue();
        // 记录队列的处理进度
        Long offset = this.offsetTable.get(level);
        if (null == offset) {
            offset = 0L;
        }

        if (timeDelay != null) {
            // 每个延时队列启动一个定时任务来处理该队列的延时消息
            this.timer.schedule(new DeliverDelayedMessageTimerTask(level, offset), FIRST_DELAY_TIME);
        }
    }

    this.timer.scheduleAtFixedRate(new TimerTask() {

        @Override
        public void run() {
            try {
                // 持久化offsetTable(保存了每个延时队列对应的处理进度offset)
                ScheduleMessageService.this.persist();
            } catch (Throwable e) {
                log.error("scheduleAtFixedRate flush exception", e);
            }
        }
    }, 10000, this.defaultMessageStore.getMessageStoreConfig().getFlushDelayOffsetInterval());
}
```

DeliverDelayedMessageTimerTask是一个TimerTask，启动以后不断处理延时队列中的消息，直到出现异常则终止该线程重新启动一个新的TimerTask，它会调用executeOnTimeup方法。

```java
public void executeOnTimeup() {
    // 找到该延时等级对应的ConsumeQueue
    ConsumeQueue cq =
        ScheduleMessageService.this.defaultMessageStore.findConsumeQueue(SCHEDULE_TOPIC,
            delayLevel2QueueId(delayLevel));
    // 记录异常情况下一次启动TimerTask开始处理的offset
    long failScheduleOffset = offset;

    if (cq != null) {
        // 找到offset所处的MappedFile中offset后面的buffer
        SelectMappedBufferResult bufferCQ = cq.getIndexBuffer(this.offset);
        if (bufferCQ != null) {
            try {
                long nextOffset = offset;
                int i = 0;
                ConsumeQueueExt.CqExtUnit cqExtUnit = new ConsumeQueueExt.CqExtUnit();
                for (; i < bufferCQ.getSize(); i += ConsumeQueue.CQ_STORE_UNIT_SIZE) {
                    // 下面三个字段信息是ConsumeQueue物理存储的信息
                    long offsetPy = bufferCQ.getByteBuffer().getLong();
                    int sizePy = bufferCQ.getByteBuffer().getInt();
                    // 注意这个tagCode，不再是普通的tag的hashCode，而是该延时消息到期的时间
                    long tagsCode = bufferCQ.getByteBuffer().getLong();
                    // 省略中间代码....
                    long now = System.currentTimeMillis();
                    // 计算应该投递该消息的时间，如果已经超时则立即投递
                    long deliverTimestamp = this.correctDeliverTimestamp(now, tagsCode);
                    // 计算下一个消息的开始位置，用来寻找下一个消息位置(如果有的话)
                    nextOffset = offset + (i / ConsumeQueue.CQ_STORE_UNIT_SIZE);
                    // 判断延时消息是否到期
                    long countdown = deliverTimestamp - now;

                    if (countdown <= 0) {
                        MessageExt msgExt =
                            ScheduleMessageService.this.defaultMessageStore.lookMessageByOffset(
                                offsetPy, sizePy);

                        if (msgExt != null) {
                            try {
                                // 将消息恢复到原始消息的格式，恢复topic、queueId、tagCode等，清除属性"DELAY"
                                MessageExtBrokerInner msgInner = this.messageTimeup(msgExt);
                                PutMessageResult putMessageResult =
                                    ScheduleMessageService.this.defaultMessageStore
                                        .putMessage(msgInner);

                                if (putMessageResult != null
                                    && putMessageResult.getPutMessageStatus() == PutMessageStatus.PUT_OK) {
                                    // 投递成功，处理下一个
                                    continue;
                                } else {
                                    // XXX: warn and notify me
                                    log.error(
                                        "ScheduleMessageService, a message time up, but reput it failed, topic: {} msgId {}",
                                        msgExt.getTopic(), msgExt.getMsgId());
                                    // 投递失败，结束当前task，重新启动TimerTask，从下一个消息开始处理，也就是说当前消息丢弃
                                    // 更新offsetTable中当前队列的offset为下一个消息的offset
                                    ScheduleMessageService.this.timer.schedule(
                                        new DeliverDelayedMessageTimerTask(this.delayLevel,
                                            nextOffset), DELAY_FOR_A_PERIOD);
                                    ScheduleMessageService.this.updateOffset(this.delayLevel,
                                        nextOffset);
                                    return;
                                }
                            } catch (Exception e) {
                                // 重新投递期间出现任何异常，结束当前task，重新启动TimerTask，从当前消息开始重试
                                /*
                                 * XXX: warn and notify me
                                 */
                                log.error(
                                    "ScheduleMessageService, messageTimeup execute error, drop it. msgExt="
                                        + msgExt + ", nextOffset=" + nextOffset + ",offsetPy="
                                        + offsetPy + ",sizePy=" + sizePy, e);
                            }
                        }
                    } else {
                        ScheduleMessageService.this.timer.schedule(
                            new DeliverDelayedMessageTimerTask(this.delayLevel, nextOffset),
                            countdown);
                        ScheduleMessageService.this.updateOffset(this.delayLevel, nextOffset);
                        return;
                    }
                } // end of for
                // 处理完当前MappedFile中的消息后，重新启动TimerTask，从下一个消息开始处理
                // 更新offsetTable中当前队列的offset为下一个消息的offset
                nextOffset = offset + (i / ConsumeQueue.CQ_STORE_UNIT_SIZE);
                ScheduleMessageService.this.timer.schedule(new DeliverDelayedMessageTimerTask(
                    this.delayLevel, nextOffset), DELAY_FOR_A_WHILE);
                ScheduleMessageService.this.updateOffset(this.delayLevel, nextOffset);
                return;
            } finally {

                bufferCQ.release();
            }
        } // end of if (bufferCQ != null)
        else {
            // 如果根据offsetTable中的offset没有找到对应的消息(可能被删除了)，则按照当前ConsumeQueue的最小offset开始处理
            long cqMinOffset = cq.getMinOffsetInQueue();
            if (offset < cqMinOffset) {
                failScheduleOffset = cqMinOffset;
                log.error("schedule CQ offset invalid. offset=" + offset + ", cqMinOffset="
                    + cqMinOffset + ", queueId=" + cq.getQueueId());
            }
        }
    } // end of if (cq != null)
    
    ScheduleMessageService.this.timer.schedule(new DeliverDelayedMessageTimerTask(this.delayLevel,
        failScheduleOffset), DELAY_FOR_A_WHILE);
}
```

对于上面的tagCode做一下特别说明，延时消息的tagCode和普通消息不一样：

- 延时消息的tagCode：存储的是消息到期的时间。
- 非延时消息的tagCode：tags字符串的hashCode。

总结：

​		以上就是RocketMQ延时消息的实现方式，上面没有详说的是重试消息的延时是怎么实现的，其实就是在consumer将延时消息发送回broker的时候设置了（用户可以自己设置，如果没有自己设置默认是0）delayLevel，到了broker处理重试消息的时候如果delayLevel是0（也就是说是默认的延时等级）的时候会在原来的基础上加3，后面的处理就和上面说的延时消息一样了，存储的时候将消息投递到延时队列，等待延时到期后再重新投递到原始topic队列中等到consumer消费。



## 使用场景

* 通过消息触发一些定时任务，比如在某一固定时间点向用户发送提醒消息
* 消息生产和消费有时间窗口要求: 比如在天猫电商交易中超时未支付关闭订单的场景，在订单创建时会发送一条延时消息。这条消息将会在30分钟以后投递给消费者，消费者收到此消息后需要判断对应的订单是否已经完成。如支付未完成，则关闭订单。如已完成支付，则忽略。
* 

## 延时消息的原理



