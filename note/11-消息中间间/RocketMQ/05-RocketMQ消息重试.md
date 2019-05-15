# 05-RocketMQ消息重试



## 生产者消息重试

	>生产者默认重试次数为3，只有在同步发送的情况下才生效

```java
//但是只有同步情况下才有效，异步和SendOneWay情况下没有效果
//生产者重试次数,只有在同步情况下有效果(异步和SendOneWay下配置无效)
producer.setRetryTimesWhenSendFailed(3);
```

> 源码分析

**demo的生产者发送代码**

```java
public void send() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
    Message message = null;
    for(int i=0;i<5;i++){
        message = new Message("demo_topic","tag_a","key"+i,("我是第"+i+"条消息").getBytes());
        defaultMQProducer.send(message);
    }
}
```

**org.apache.rocketmq.client.producer.DefaultMQProducer#send(org.apache.rocketmq.common.message.Message)**

```JAVA
@Override
public SendResult send(
    Message msg) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return this.defaultMQProducerImpl.send(msg);
}
```

**org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#send(org.apache.rocketmq.common.message.Message)**

**可靠同步发送**

```JAVA
/**
* DEFAULT SYNC -------------------------------------------------------
*/
//send(Message message) 默认是同步
public SendResult send(
    Message msg) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return send(msg, this.defaultMQProducer.getSendMsgTimeout());
}
public SendResult send(Message msg,
                       long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
    return this.sendDefaultImpl(msg, CommunicationMode.SYNC, null, timeout);
}
```

**真正的生产者发送方法**

```java
private SendResult sendDefaultImpl(
    Message msg,
    final CommunicationMode communicationMode,
    final SendCallback sendCallback,
    final long timeout
    ) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
        this.makeSureStateOK();
        Validators.checkMessage(msg, this.defaultMQProducer);

        final long invokeID = random.nextLong();
        long beginTimestampFirst = System.currentTimeMillis();
        long beginTimestampPrev = beginTimestampFirst;
        long endTimestamp = beginTimestampFirst;
        TopicPublishInfo topicPublishInfo = this.tryToFindTopicPublishInfo(msg.getTopic());
        if (topicPublishInfo != null && topicPublishInfo.ok()) {
            boolean callTimeout = false;
            MessageQueue mq = null;
            Exception exception = null;
            SendResult sendResult = null;
            //这儿判断消息类型来决定消息重试次数
            //如果是同步1+设置的重试次数,否则重试次数为1，即为不重试
            int timesTotal = communicationMode == CommunicationMode.SYNC ? 1 + this.defaultMQProducer.getRetryTimesWhenSendFailed() : 1;
            int times = 0;
            String[] brokersSent = new String[timesTotal];
            //使用for循环来实现生产者消息重试
            for (; times < timesTotal; times++) {
                String lastBrokerName = null == mq ? null : mq.getBrokerName();
                MessageQueue mqSelected = this.selectOneMessageQueue(topicPublishInfo, lastBrokerName);
                if (mqSelected != null) {
                    mq = mqSelected;
                    brokersSent[times] = mq.getBrokerName();
                    try {
                        beginTimestampPrev = System.currentTimeMillis();
                        long costTime = beginTimestampPrev - beginTimestampFirst;
                        if (timeout < costTime) {
                            callTimeout = true;
                            break;
                        }

                        sendResult = this.sendKernelImpl(msg, mq, communicationMode, sendCallback, topicPublishInfo, timeout - costTime);
                        endTimestamp = System.currentTimeMillis();
                        this.updateFaultItem(mq.getBrokerName(), endTimestamp - beginTimestampPrev, false);
                        switch (communicationMode) {
                            case ASYNC:
                                return null;
                            case ONEWAY:
                                return null;
                            case SYNC:
                                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                                    if (this.defaultMQProducer.isRetryAnotherBrokerWhenNotStoreOK()) {
                                        continue;
                                    }
                                }

                                return sendResult;
                            default:
                                break;
                        }
                    } catch (RemotingException e) {
                        endTimestamp = System.currentTimeMillis();
                        this.updateFaultItem(mq.getBrokerName(), endTimestamp - beginTimestampPrev, true);
                        log.warn(String.format("sendKernelImpl exception, resend at once, InvokeID: %s, RT: %sms, Broker: %s", invokeID, endTimestamp - beginTimestampPrev, mq), e);
                        log.warn(msg.toString());
                        exception = e;
                        continue;
                    } catch (MQClientException e) {
                        endTimestamp = System.currentTimeMillis();
                        this.updateFaultItem(mq.getBrokerName(), endTimestamp - beginTimestampPrev, true);
                        log.warn(String.format("sendKernelImpl exception, resend at once, InvokeID: %s, RT: %sms, Broker: %s", invokeID, endTimestamp - beginTimestampPrev, mq), e);
                        log.warn(msg.toString());
                        exception = e;
                        continue;
                    } catch (MQBrokerException e) {
                        endTimestamp = System.currentTimeMillis();
                        this.updateFaultItem(mq.getBrokerName(), endTimestamp - beginTimestampPrev, true);
                        log.warn(String.format("sendKernelImpl exception, resend at once, InvokeID: %s, RT: %sms, Broker: %s", invokeID, endTimestamp - beginTimestampPrev, mq), e);
                        log.warn(msg.toString());
                        exception = e;
                        switch (e.getResponseCode()) {
                            case ResponseCode.TOPIC_NOT_EXIST:
                            case ResponseCode.SERVICE_NOT_AVAILABLE:
                            case ResponseCode.SYSTEM_ERROR:
                            case ResponseCode.NO_PERMISSION:
                            case ResponseCode.NO_BUYER_ID:
                            case ResponseCode.NOT_IN_CURRENT_UNIT:
                                continue;
                            default:
                                if (sendResult != null) {
                                    return sendResult;
                                }

                                throw e;
                        }
                    } catch (InterruptedException e) {
                        endTimestamp = System.currentTimeMillis();
                        this.updateFaultItem(mq.getBrokerName(), endTimestamp - beginTimestampPrev, false);
                        log.warn(String.format("sendKernelImpl exception, throw exception, InvokeID: %s, RT: %sms, Broker: %s", invokeID, endTimestamp - beginTimestampPrev, mq), e);
                        log.warn(msg.toString());

                        log.warn("sendKernelImpl exception", e);
                        log.warn(msg.toString());
                        throw e;
                    }
                } else {
                    break;
                }
            }

            if (sendResult != null) {
                return sendResult;
            }

            String info = String.format("Send [%d] times, still failed, cost [%d]ms, Topic: %s, BrokersSent: %s",
                times,
                System.currentTimeMillis() - beginTimestampFirst,
                msg.getTopic(),
                Arrays.toString(brokersSent));

            info += FAQUrl.suggestTodo(FAQUrl.SEND_MSG_FAILED);

            MQClientException mqClientException = new MQClientException(info, exception);
            if (callTimeout) {
                throw new RemotingTooMuchRequestException("sendDefaultImpl call timeout");
            }

            if (exception instanceof MQBrokerException) {
                mqClientException.setResponseCode(((MQBrokerException) exception).getResponseCode());
            } else if (exception instanceof RemotingConnectException) {
                mqClientException.setResponseCode(ClientErrorCode.CONNECT_BROKER_EXCEPTION);
            } else if (exception instanceof RemotingTimeoutException) {
                mqClientException.setResponseCode(ClientErrorCode.ACCESS_BROKER_TIMEOUT);
            } else if (exception instanceof MQClientException) {
                mqClientException.setResponseCode(ClientErrorCode.BROKER_NOT_EXIST_EXCEPTION);
            }

            throw mqClientException;
        }

        List<String> nsList = this.getmQClientFactory().getMQClientAPIImpl().getNameServerAddressList();
        if (null == nsList || nsList.isEmpty()) {
            throw new MQClientException(
                "No name server address, please set it." + FAQUrl.suggestTodo(FAQUrl.NAME_SERVER_ADDR_NOT_EXIST_URL), null).setResponseCode(ClientErrorCode.NO_NAME_SERVER_EXCEPTION);
        }

        throw new MQClientException("No route info of this topic, " + msg.getTopic() + FAQUrl.suggestTodo(FAQUrl.NO_TOPIC_ROUTE_INFO),
            null).setResponseCode(ClientErrorCode.NOT_FOUND_TOPIC_EXCEPTION);
    }
```

上面的源码中我们知道，判断发送模式是通过枚举类**==CommunicationMode==**判断的

```JAVA
public enum CommunicationMode {
    SYNC,
    ASYNC,
    ONEWAY,
}
```



**可靠异步发送**

org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#send(org.apache.rocketmq.common.message.Message, org.apache.rocketmq.client.producer.SendCallback)

```java
/**
* DEFAULT ASYNC -------------------------------------------------------
*/
//默认异步发送
public void send(Message msg,
                 SendCallback sendCallback) throws MQClientException, RemotingException, InterruptedException {
    send(msg, sendCallback, this.defaultMQProducer.getSendMsgTimeout());
}
@Deprecated
public void send(final Message msg, final SendCallback sendCallback, final long timeout)
    throws MQClientException, RemotingException, InterruptedException {
    final long beginStartTime = System.currentTimeMillis();
    ExecutorService executor = this.getAsyncSenderExecutor();
    try {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                long costTime = System.currentTimeMillis() - beginStartTime;
                if (timeout > costTime) {
                    try {
                        //最后发送的逻辑都是调用了sendDefaultImpl方法，进行重试次数判断
                        sendDefaultImpl方法，进行重试次数判断(msg, CommunicationMode.ASYNC, sendCallback, timeout - costTime);
                    } catch (Exception e) {
                        sendCallback.onException(e);
                    }
                } else {
                    sendCallback.onException(
                        new RemotingTooMuchRequestException("DEFAULT ASYNC send call timeout"));
                }
            }

        });
    } catch (RejectedExecutionException e) {
        throw new MQClientException("executor rejected ", e);
    }

}
```

**单向（Oneway）发送**

```java
/**
* DEFAULT ONEWAY -------------------------------------------------------
*/
//默认发送方式ONEWAY
public void sendOneway(Message msg) throws MQClientException, RemotingException, InterruptedException {
    try {
        this.sendDefaultImpl(msg, CommunicationMode.ONEWAY, null, this.defaultMQProducer.getSendMsgTimeout());
    } catch (MQBrokerException e) {
        throw new MQClientException("unknown exception", e);
    }
}
```

### 生产者消息重试中的问题

> 生产者消息发送重试了几次?

​	生产者只有是同步发送情况下才会进行消息重试，默认重试2次，可以通过producer.setRetryTimesWhenSendFailed(3);//进行设置，那么     **==最多执行次数= 1+重试次数==**



> 生产者什么时候进行消息重试

发生内部异常时才会进行消息重试: RemotingException、MQClientException、MQBrokerException、InterruptedException才会进行消息重试

> **怎么重试？间隔多久重试？**

​	每次重试都会重新进行负载均衡（会考虑发送失败的因素），重新选择MessageQueue，这样增大发送消息成功的可能性。

​	没有时间间隔，直接重试

## 消费者消息重试



```java
//DefaultMQPushConsumer类的messageModel默认值时集群方式，支持消息重试，广播方式不支持消息重试
private MessageModel messageModel = MessageModel.CLUSTERING;


//可以设置方式,集群方式
consumer.setMessageModel(MessageModel.CLUSTERING);
//广播方式
consumer.setMessageModel(MessageModel.BROADCASTING);
```



> 消费者重试时间间隔: 

```java
messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m
20m 30m 1h 2h
```

> ConsumeConcurrentlyStatus消费者消费消息的状态

```java
package org.apache.rocketmq.client.consumer.listener;

public enum ConsumeConcurrentlyStatus {
    /**
     * Success consumption
     */
    CONSUME_SUCCESS,
    /**
     * Failure consumption,later try to consume
     */
    RECONSUME_LATER;
}
```

* CONSUME_SUCCESS: 消费成功
* RECONSUME_LATER 消费失败重试，一般生产中重试一定次数后，把异常信息存储下来，运维手工处理，不会重试16次.



> 实例

```java
package mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author king-pan
 * @date 2019/5/14
 * @Description ${DESCRIPTION}
 */
public class ConsumerDemo {

    private DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();

    public ConsumerDemo() {
        consumer.setNamesrvAddr("39.104.164.68:9876");
        consumer.setConsumerGroup("pay_group");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

    }

    public void start() {
        try {
            consumer.subscribe("demo_topic", "*");
            consumer.setMaxReconsumeTimes(5);
            consumer.setMessageModel(MessageModel.CLUSTERING);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    MessageExt msg = msgs.get(0);
                    int times = msg.getReconsumeTimes();
                    System.out.println("重试次数=" + times);
                    try {
                        String msgbody = new String(msg.getBody(), "utf-8");
                        System.out.println(msgbody + " Receive New Messages: " + msgs);
                        if (msgbody != null) {
                            System.out.println("======错误=======");
                            int a = 1 / 0;
                        }
                    } catch (Exception e) {
                        System.out.println("消费异常");
                        //如果重试2次不成功，则记录，人工介入
                        if (times >= 2) {
                            System.out.println("重试次数大于2，记录数据库，发短信通知开发人员或者运营人员");
                            //TODO 记录数据库，发短信通知开发人员或者运营人员
                            //告诉broker，消息成功
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                        e.printStackTrace();
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConsumerDemo consumerDemo = new ConsumerDemo();
        consumerDemo.start();
    }
}
```

**org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService.ConsumeRequest#run**

```java
@Override
public void run() {
    if (this.processQueue.isDropped()) {
        log.info("the message queue not be able to consume, because it's dropped. group={} {}", ConsumeMessageConcurrentlyService.this.consumerGroup, this.messageQueue);
        return;
    }

    MessageListenerConcurrently listener = ConsumeMessageConcurrentlyService.this.messageListener;
    ConsumeConcurrentlyContext context = new ConsumeConcurrentlyContext(messageQueue);
    ConsumeConcurrentlyStatus status = null;

    ConsumeMessageContext consumeMessageContext = null;
    if (ConsumeMessageConcurrentlyService.this.defaultMQPushConsumerImpl.hasHook()) {
        consumeMessageContext = new ConsumeMessageContext();
        consumeMessageContext.setConsumerGroup(defaultMQPushConsumer.getConsumerGroup());
        consumeMessageContext.setProps(new HashMap<String, String>());
        consumeMessageContext.setMq(messageQueue);
        consumeMessageContext.setMsgList(msgs);
        consumeMessageContext.setSuccess(false);
        ConsumeMessageConcurrentlyService.this.defaultMQPushConsumerImpl.executeHookBefore(consumeMessageContext);
    }

    long beginTimestamp = System.currentTimeMillis();
    boolean hasException = false;
    ConsumeReturnType returnType = ConsumeReturnType.SUCCESS;
    try {
        ConsumeMessageConcurrentlyService.this.resetRetryTopic(msgs);
        if (msgs != null && !msgs.isEmpty()) {
            for (MessageExt msg : msgs) {
                MessageAccessor.setConsumeStartTimeStamp(msg, String.valueOf(System.currentTimeMillis()));
            }
        }
         // 这个status是listener返回的，用户可以指定status，如果业务逻辑代码消费消息失败后可以返回org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus#RECONSUME_LATER
        // 来告诉RocketMQ需要重新消费
        // 如果是多个消息，用户还可以指定从哪一个消息开始需要重新消费
        status = listener.consumeMessage(Collections.unmodifiableList(msgs), context);
    } catch (Throwable e) {
        log.warn("consumeMessage exception: {} Group: {} Msgs: {} MQ: {}",
                 RemotingHelper.exceptionSimpleDesc(e),
                 ConsumeMessageConcurrentlyService.this.consumerGroup,
                 msgs,
                 messageQueue);
        hasException = true;
    }
    long consumeRT = System.currentTimeMillis() - beginTimestamp;
    if (null == status) {
        if (hasException) {
            returnType = ConsumeReturnType.EXCEPTION;
        } else {
            returnType = ConsumeReturnType.RETURNNULL;
        }
    } else if (consumeRT >= defaultMQPushConsumer.getConsumeTimeout() * 60 * 1000) {
        returnType = ConsumeReturnType.TIME_OUT;
    } else if (ConsumeConcurrentlyStatus.RECONSUME_LATER == status) {
        returnType = ConsumeReturnType.FAILED;
    } else if (ConsumeConcurrentlyStatus.CONSUME_SUCCESS == status) {
        returnType = ConsumeReturnType.SUCCESS;
    }

    if (ConsumeMessageConcurrentlyService.this.defaultMQPushConsumerImpl.hasHook()) {
        consumeMessageContext.getProps().put(MixAll.CONSUME_CONTEXT_TYPE, returnType.name());
    }

    if (null == status) {
        log.warn("consumeMessage return null, Group: {} Msgs: {} MQ: {}",
                 ConsumeMessageConcurrentlyService.this.consumerGroup,
                 msgs,
                 messageQueue);
        status = ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    if (ConsumeMessageConcurrentlyService.this.defaultMQPushConsumerImpl.hasHook()) {
        consumeMessageContext.setStatus(status.toString());
        consumeMessageContext.setSuccess(ConsumeConcurrentlyStatus.CONSUME_SUCCESS == status);
        ConsumeMessageConcurrentlyService.this.defaultMQPushConsumerImpl.executeHookAfter(consumeMessageContext);
    }

    ConsumeMessageConcurrentlyService.this.getConsumerStatsManager()
        .incConsumeRT(ConsumeMessageConcurrentlyService.this.consumerGroup, messageQueue.getTopic(), consumeRT);

    if (!processQueue.isDropped()) {
        ConsumeMessageConcurrentlyService.this.processConsumeResult(status, context, this);
    } else {
        log.warn("processQueue is dropped without process consume result. messageQueue={}, msgs={}", messageQueue, msgs);
    }
}
```





**org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService#processConsumeResult**



```java
public void processConsumeResult(
    final ConsumeConcurrentlyStatus status,
    final ConsumeConcurrentlyContext context,
    final ConsumeRequest consumeRequest
    ) {
        int ackIndex = context.getAckIndex();

        if (consumeRequest.getMsgs().isEmpty())
            return;

        switch (status) {
            case CONSUME_SUCCESS:
                if (ackIndex >= consumeRequest.getMsgs().size()) {
                    ackIndex = consumeRequest.getMsgs().size() - 1;
                }
                int ok = ackIndex + 1;
                int failed = consumeRequest.getMsgs().size() - ok;
                this.getConsumerStatsManager().incConsumeOKTPS(consumerGroup, consumeRequest.getMessageQueue().getTopic(), ok);
                this.getConsumerStatsManager().incConsumeFailedTPS(consumerGroup, consumeRequest.getMessageQueue().getTopic(), failed);
                break;
                //消费者消息重试处理流程
            case RECONSUME_LATER:
                ackIndex = -1;
                this.getConsumerStatsManager().incConsumeFailedTPS(consumerGroup, consumeRequest.getMessageQueue().getTopic(),
                    consumeRequest.getMsgs().size());
                break;
            default:
                break;
        }

        switch (this.defaultMQPushConsumer.getMessageModel()) {
            case BROADCASTING:
                for (int i = ackIndex + 1; i < consumeRequest.getMsgs().size(); i++) {
                    MessageExt msg = consumeRequest.getMsgs().get(i);
                    log.warn("BROADCASTING, the message consume failed, drop it, {}", msg.toString());
                }
                break;
                //集群方式，进行消息回退
            case CLUSTERING:
                List<MessageExt> msgBackFailed = new ArrayList<MessageExt>(consumeRequest.getMsgs().size());
                for (int i = ackIndex + 1; i < consumeRequest.getMsgs().size(); i++) {
                    MessageExt msg = consumeRequest.getMsgs().get(i);
                    //把消费者消息回退
                    boolean result = this.sendMessageBack(msg, context);
                    if (!result) {
                    	//上面的sendMessageBack失败后，添加到回退list中，下面再发送一次
                        msg.setReconsumeTimes(msg.getReconsumeTimes() + 1);
                        msgBackFailed.add(msg);
                    }
                }

                if (!msgBackFailed.isEmpty()) {
                    //msgBackFailed不为空，上面的回退失败，再回退一次
                    consumeRequest.getMsgs().removeAll(msgBackFailed);
					//
                    this.submitConsumeRequestLater(msgBackFailed, consumeRequest.getProcessQueue(), consumeRequest.getMessageQueue());
                }
                break;
            default:
                break;
        }

        long offset = consumeRequest.getProcessQueue().removeMessage(consumeRequest.getMsgs());
        if (offset >= 0 && !consumeRequest.getProcessQueue().isDropped()) {
                   // 更新消费进度
 this.defaultMQPushConsumerImpl.getOffsetStore().updateOffset(consumeRequest.getMessageQueue(), offset, true);
        }
    }

```

**org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService#sendMessageBack**

```java
 public boolean sendMessageBack(final MessageExt msg, final ConsumeConcurrentlyContext context) {
        int delayLevel = context.getDelayLevelWhenNextConsume();

        try {
            this.defaultMQPushConsumerImpl.sendMessageBack(msg, delayLevel, context.getMessageQueue().getBrokerName());
            return true;
        } catch (Exception e) {
            log.error("sendMessageBack exception, group: " + this.consumerGroup + " msg: " + msg.toString(), e);
        }

        return false;
    }
```

**org.apache.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl#sendMessageBack**

```java
public void sendMessageBack(MessageExt msg, int delayLevel, final String brokerName)
    throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
    try {
        String brokerAddr = (null != brokerName) ? this.mQClientFactory.findBrokerAddressInPublish(brokerName)
            : RemotingHelper.parseSocketAddressAddr(msg.getStoreHost());
        //消费者发送消息返回broker
        this.mQClientFactory.getMQClientAPIImpl().consumerSendMessageBack(brokerAddr, msg,
                                                                          this.defaultMQPushConsumer.getConsumerGroup(), delayLevel, 5000, getMaxReconsumeTimes());
    } catch (Exception e) {
        log.error("sendMessageBack Exception, " + this.defaultMQPushConsumer.getConsumerGroup(), e);
		 // 如果消费失败的消息发送回broker失败了，会再重试一次，和try里面的方法不一样的地方是这里直接修改topic
        // 为重试topic然后和producer发送消息的方法一样发送到broker
        Message newMsg = new Message(MixAll.getRetryTopic(this.defaultMQPushConsumer.getConsumerGroup()), msg.getBody());

        String originMsgId = MessageAccessor.getOriginMessageId(msg);
        MessageAccessor.setOriginMessageId(newMsg, UtilAll.isBlank(originMsgId) ? msg.getMsgId() : originMsgId);

        newMsg.setFlag(msg.getFlag());
        MessageAccessor.setProperties(newMsg, msg.getProperties());
        MessageAccessor.putProperty(newMsg, MessageConst.PROPERTY_RETRY_TOPIC, msg.getTopic());
        MessageAccessor.setReconsumeTime(newMsg, String.valueOf(msg.getReconsumeTimes() + 1));
        MessageAccessor.setMaxReconsumeTimes(newMsg, String.valueOf(getMaxReconsumeTimes()));
        newMsg.setDelayTimeLevel(3 + msg.getReconsumeTimes());

        this.mQClientFactory.getDefaultMQProducer().send(newMsg);
    }
}
```

**org.apache.rocketmq.client.impl.MQClientAPIImpl#consumerSendMessageBack**

```java
public void consumerSendMessageBack(
    final String addr,
    final MessageExt msg,
    final String consumerGroup,
    final int delayLevel,
    final long timeoutMillis,
    final int maxConsumeRetryTimes
    ) throws RemotingException, MQBrokerException, InterruptedException {
        ConsumerSendMsgBackRequestHeader requestHeader = new ConsumerSendMsgBackRequestHeader();
        // 和普通的发送消息的RequestCode不一样，broker处理的方法也不一样
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.CONSUMER_SEND_MSG_BACK, requestHeader);

        requestHeader.setGroup(consumerGroup);
        //因为重试的消息被broker拿到后会修改topic，所以这里设置原始的topic
        requestHeader.setOriginTopic(msg.getTopic());
        //broker会根据offset查询原始的消息
        requestHeader.setOffset(msg.getCommitLogOffset());
        //设置delayLevel,这个值决定了该消息是否被延时消费，延时多久
        //用户可以设置延时等级，默认是0,不延时(broker端会有逻辑如果为0会设置成3)
        requestHeader.setDelayLevel(delayLevel);
        //设置最初的msgId，保证同一条消息的msgId一致
        requestHeader.setOriginMsgId(msg.getMsgId());
        //设置重试次数，默认为16次
        requestHeader.setMaxReconsumeTimes(maxConsumeRetryTimes);

        RemotingCommand response = this.remotingClient.invokeSync(MixAll.brokerVIPChannel(this.clientConfig.isVipChannelEnabled(), addr),
            request, timeoutMillis);
        assert response != null;
        switch (response.getCode()) {
            case ResponseCode.SUCCESS: {
                return;
            }
            default:
                break;
        }

        throw new MQBrokerException(response.getCode(), response.getRemark());
    }
```

> 上面方法的关键属性

* originTopic: 因为重试的消息被broker拿到后会修改topic，所以这里设置原始的topic

**org.apache.rocketmq.remoting.netty.NettyRemotingClient#invokeSync**

```java
@Override
public RemotingCommand invokeSync(String addr, final RemotingCommand request, long timeoutMillis)
    throws InterruptedException, RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException {
    long beginStartTime = System.currentTimeMillis();
    final Channel channel = this.getAndCreateChannel(addr);
    if (channel != null && channel.isActive()) {
        try {
            doBeforeRpcHooks(addr, request);
            long costTime = System.currentTimeMillis() - beginStartTime;
            if (timeoutMillis < costTime) {
                throw new RemotingTimeoutException("invokeSync call timeout");
            }
            RemotingCommand response = this.invokeSyncImpl(channel, request, timeoutMillis - costTime);
            doAfterRpcHooks(RemotingHelper.parseChannelRemoteAddr(channel), request, response);
            return response;
        } catch (RemotingSendRequestException e) {
            log.warn("invokeSync: send request exception, so close the channel[{}]", addr);
            this.closeChannel(addr, channel);
            throw e;
        } catch (RemotingTimeoutException e) {
            if (nettyClientConfig.isClientCloseSocketIfTimeout()) {
                this.closeChannel(addr, channel);
                log.warn("invokeSync: close socket because of timeout, {}ms, {}", timeoutMillis, addr);
            }
            log.warn("invokeSync: wait response timeout exception, the channel[{}]", addr);
            throw e;
        }
    } else {
        this.closeChannel(addr, channel);
        throw new RemotingConnectException(addr);
    }
}
```



​	在介绍Consumer的消费重试机制之前，需要先来说下“重试队列”和“死信队列”两个概念。
​	**(1) 重试队列：** 如果Consumer端因为各种类型异常导致本次消费失败，为防止该消息丢失而需要将其重新回发给Broker端保存，保存这种因为异常无法正常消费而回发给MQ的消息队列称之为重试队列。 RocketMQ会为每个消费组都设置一个Topic名称为“**%RETRY%+consumerGroup**”的重试队列（这里需要注意的是，**这个Topic的重试队列是针对消费组，而不是针对每个Topic设置的**），用于暂时保存因为各种异常而导致Consumer端无法消费的消息。考虑到异常恢复起来需要一些时间，会为重试队列设置多个重试级别，每个重试级别都有与之对应的重新投递延时，重试次数越多投递延时就越大。RocketMQ对于重试消息的处理是先保存至Topic名称为“SCHEDULETOPICXXXX”的延迟队列中，后台定时任务按照对应的时间进行Delay后重新保存至“%RETRY%+consumerGroup”的重试队列中（具体细节后面会详细阐述）；
​	**(2) 死信队列：** 由于有些原因导致Consumer端长时间的无法正常消费从Broker端Pull过来的业务消息，为了确保消息不会被无故的丢弃，那么超过配置的“最大重试消费次数”后就会移入到这个死信队列中。在RocketMQ中，SubscriptionGroupConfig配置常量默认地设置了两个参数，一个是retryQueueNums为1（重试队列数量为1个），另外一个是retryMaxTimes为16（最大重试消费的次数为16次）。Broker端通过校验判断，如果超过了最大重试消费次数则会将消息移至这里所说的死信队列。这里，RocketMQ会为每个消费组都设置一个Topic命名为“%DLQ%+consumerGroup"的死信队列。一般在实际应用中，移入至死信队列的消息，需要人工干预处理；

