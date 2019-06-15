# RocketMQ分布式事务消息

## 分布式事务消息简介

> 什么时分布式事务

* 来源:单体应用->拆分为分布式应用
* 一个接口需要调用多个服务，且操作不同的数据库，数据一致性很难保障。

> 分布式事务常见的解决方案

* 2PC:两阶段提交，基于XA协议
* TCC: Try、Confirm、Cancel
  * 下单
* 事务消息最终一致性
* 其他

> 框架

* GTS->开源Fescar
* LCN



## RocketMQ实现分布式事务消息的流程

### 名词解析

> RocketMQ事务消息

​	RocketMQ提供分布式事务功能，通过RocketMQ事务消息能达到分布式事务的最终一致性

> 半消息Half Message

​	暂不能投递的消息(暂不能消费的消息)，Producer已经将消息成功发送到了Broker端，但是服务端未收到生产者对该消息的二次确认，此时该消息被标记为"暂不能投递"状态，处于该种状态下的消息即半消息。

> 消息回查

​	由于网络闪断、生产者应用重启等原因，导致某条消息的二次确认丢失，消息队列RocketMQ服务端通过扫描发现某条消息长期处于"半消息"时，需要主动向消息生产者询问该消息的最终状态(Commit或Rollback)，该过程即消息回查: 消息回查的实现是通过TransacationListener.checkLocalTransaction()方法

> 整体交互流程

![分布式事务图](./images/fenbushishiwu.png)



* Producer向broker端发送消息
* 服务端将消息持久化成功后，向发送方ACK确认消息已经发送成功，此时消息为半消息。
* 发送方开始执行本地事务逻辑
* 发送放根据本地事务执行结果向服务端提交二次确认(Commit或Rollback)，服务端收到Commit状态则将半消息标记为可投递，订阅放最终将收到该消息;服务端收到Rollback状态则删除半消息，订阅方将不会接受该消息。
* 在断网或者应用重启的特殊情况下，上述步骤4提交的二次确认最终未达到服务端，经过固定时间后服务端将对该消息发送消息回查。
* 发送放收到消息回查后，要检查对应消息的本地事务执行的最终结果，然后根据结果对消息进行状态回复，Commit或Rollback，然后服务端按照上面步骤4进行处理。

> RocketMQ事务消息的状态

* COMMIT_MESSAGE: 提交事务消息，消费者可以消费此消息
* ROLLBACK_MESSAGE: 回滚事务消息，消息会在broker中删除，消费者不能消费
* UNKNOW: broker需要回查确认该消息的状态

> 事务消息的消费

事务消息consumer端的消费方式和普通消息是一样的，RocketMQ能保证消息能被consumer收到(消息重试等机制，最后也存在consumer消费失败的情况，这种情况出现的概率极低)



## 分布式事务消息实现

### 事务消息生产者

```java
public class TrProducer {

    public static void main(String[] args) {
        //监听器，执行本地事务
        TransactionListener transactionListener = new TransactionListenerImpl();
        //事务消息发送者,需要区分普通消息的groupName，不能混用，不然容易出问题.
        TransactionMQProducer producer = new TransactionMQProducer("tx_group");
        producer.setNamesrvAddr(Config.NAME_ADDR);

        //一般自定义线程池时，需要给线程添加名称，出问题好跟踪问题
        //不能使用Executors工具创建线程池，容易出现OOM异常
        //2 corePoolSize 核心线程数
        //5 线程池最大线程数
        //100 非核心线程最大空闲存活时间
        //最大空闲存活时间的单位
        //new ArrayBlockingQueue<Runnable>(2000)创建一个2000定长的空闲队列防止OOM异常
        //ThreadFactory自定义线程池中线程的名称，方便跟踪问题
        //使用线程池默认的拒绝策略:
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        try {
            //消息回查的时候需要开启线程检查状态
            producer.setExecutorService(executorService);
            producer.setTransactionListener(transactionListener);
            producer.start();
            String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
            for (int i = 0; i < 10; i++) {
                try {
                    Message msg =
                            new Message("TopicTest1234", tags[i % tags.length], "KEY" + i,
                                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    //事务消息发送入口
                    SendResult sendResult = producer.sendMessageInTransaction(msg, i%3);
                    System.out.printf("%s%n", sendResult);

                    Thread.sleep(10);
                } catch (MQClientException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < 100000; i++) {
                Thread.sleep(1000);
            }
            producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
```

### 分布式事务消息的事务监听

在监听类中处理本地事务和回查消息的处理

```java
package mq.tran;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author king-pan
 * @date 2019/5/16
 * @Description ${DESCRIPTION}
 */
public class TransactionListenerImpl implements TransactionListener {

    /**
     * 事务监听器-执行本地事务
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        System.out.println("===============executeLocalTransaction================");
        String body = new String(msg.getBody());
        String key = msg.getKeys();
        String tags = msg.getTags();
        System.out.printf("id=%,body=%,key=%,tags=%", msg.getTransactionId(), body, key, tags);
        int status = Integer.parseInt(arg.toString());
        if(status==1){
            //提交消息，然后消费端可以消费
            return LocalTransactionState.COMMIT_MESSAGE;
        }else if(status ==2){
            //回退，broker会删除事务消息
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }else if(status==3){
            //broker端会进行回查消息
            return LocalTransactionState.UNKNOW;
        }
        //再或者什么都不发送,broker端会回查状态
        return null;
    }

    /**
     * 回查消息,要么commit，要么rollback，reconsumeTimes不生效
     *
     * @param msg
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        System.out.println("===============checkLocalTransaction================");
        String body = new String(msg.getBody());
        String key = msg.getKeys();
        String tags = msg.getTags();
        System.out.printf("id=%,body=%,key=%,tags=%", msg.getTransactionId(), body, key, tags);
        //要么commit要么rollback
        //执行回查操作，然后返回LocalTransactionState
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
```

### 分布式事务消息的消费者

​	分布式事务消息的消费者和普通消息的消费者是一样的。

## 分布式事务消息源码分析



**TransactionMQProducer.sendMessageInTransaction(Message msg,Object arg)是事务消息发送的入口**

```JAVA
//org.apache.rocketmq.client.producer.TransactionMQProducer#sendMessageInTransaction(org.apache.rocketmq.common.message.Message, java.lang.Object)
@Override
public TransactionSendResult sendMessageInTransaction(final Message msg,
                                                      final Object arg) throws MQClientException {
    //判断transactionListener是否为空
    if (null == this.transactionListener) {
        throw new MQClientException("TransactionListener is null", null);
    }
	//发送事务消息
    return this.defaultMQProducerImpl.sendMessageInTransaction(msg, null, arg);
}

//org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#sendMessageInTransaction
public TransactionSendResult sendMessageInTransaction(final Message msg,
                                                      final LocalTransactionExecuter localTransactionExecuter, final Object arg)
    throws MQClientException {
    TransactionListener transactionListener = getCheckListener();
    if (null == localTransactionExecuter && null == transactionListener) {
        throw new MQClientException("tranExecutor is null", null);
    }
    Validators.checkMessage(msg, this.defaultMQProducer);

    SendResult sendResult = null;
    MessageAccessor.putProperty(msg, MessageConst.PROPERTY_TRANSACTION_PREPARED, "true");
    MessageAccessor.putProperty(msg, MessageConst.PROPERTY_PRODUCER_GROUP, this.defaultMQProducer.getProducerGroup());
    try {
        //发送消息
        sendResult = this.send(msg);
    } catch (Exception e) {
        throw new MQClientException("send message Exception", e);
    }

    LocalTransactionState localTransactionState = LocalTransactionState.UNKNOW;
    Throwable localException = null;
    switch (sendResult.getSendStatus()) {
            //只有发送成功才会处理,其他状态忽略
        case SEND_OK: {
            try {
                if (sendResult.getTransactionId() != null) {
                    msg.putUserProperty("__transactionId__", sendResult.getTransactionId());
                }
                String transactionId = msg.getProperty(MessageConst.PROPERTY_UNIQ_CLIENT_MESSAGE_ID_KEYIDX);
                if (null != transactionId && !"".equals(transactionId)) {
                    msg.setTransactionId(transactionId);
                }
                if (null != localTransactionExecuter) {
                    localTransactionState = localTransactionExecuter.executeLocalTransactionBranch(msg, arg);
                } else if (transactionListener != null) {
                    log.debug("Used new transaction API");
                    //执行事务监视器中的执行本地事务方法.
                    localTransactionState = transactionListener.executeLocalTransaction(msg, arg);
                }
                if (null == localTransactionState) {
                    localTransactionState = LocalTransactionState.UNKNOW;
                }

                if (localTransactionState != LocalTransactionState.COMMIT_MESSAGE) {
                    log.info("executeLocalTransactionBranch return {}", localTransactionState);
                    log.info(msg.toString());
                }
            } catch (Throwable e) {
                log.info("executeLocalTransactionBranch exception", e);
                log.info(msg.toString());
                localException = e;
            }
        }
            break;
        case FLUSH_DISK_TIMEOUT:
        case FLUSH_SLAVE_TIMEOUT:
        case SLAVE_NOT_AVAILABLE:
            localTransactionState = LocalTransactionState.ROLLBACK_MESSAGE;
            break;
        default:
            break;
    }

    try {
        this.endTransaction(sendResult, localTransactionState, localException);
    } catch (Exception e) {
        log.warn("local transaction execute " + localTransactionState + ", but end broker transaction failed", e);
    }

    TransactionSendResult transactionSendResult = new TransactionSendResult();
    transactionSendResult.setSendStatus(sendResult.getSendStatus());
    transactionSendResult.setMessageQueue(sendResult.getMessageQueue());
    transactionSendResult.setMsgId(sendResult.getMsgId());
    transactionSendResult.setQueueOffset(sendResult.getQueueOffset());
    transactionSendResult.setTransactionId(sendResult.getTransactionId());
    transactionSendResult.setLocalTransactionState(localTransactionState);
    return transactionSendResult;
}
//org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#endTransaction
public void endTransaction(
    final SendResult sendResult,
    final LocalTransactionState localTransactionState,
    final Throwable localException) throws RemotingException, MQBrokerException, InterruptedException, UnknownHostException {
    final MessageId id;
    if (sendResult.getOffsetMsgId() != null) {
        id = MessageDecoder.decodeMessageId(sendResult.getOffsetMsgId());
    } else {
        id = MessageDecoder.decodeMessageId(sendResult.getMsgId());
    }
    String transactionId = sendResult.getTransactionId();
    final String brokerAddr = this.mQClientFactory.findBrokerAddressInPublish(sendResult.getMessageQueue().getBrokerName());
    EndTransactionRequestHeader requestHeader = new EndTransactionRequestHeader();
    requestHeader.setTransactionId(transactionId);
    requestHeader.setCommitLogOffset(id.getOffset());
    switch (localTransactionState) {
        case COMMIT_MESSAGE:
            requestHeader.setCommitOrRollback(MessageSysFlag.TRANSACTION_COMMIT_TYPE);
            break;
        case ROLLBACK_MESSAGE:
            requestHeader.setCommitOrRollback(MessageSysFlag.TRANSACTION_ROLLBACK_TYPE);
            break;
        case UNKNOW:
            requestHeader.setCommitOrRollback(MessageSysFlag.TRANSACTION_NOT_TYPE);
            break;
        default:
            break;
    }

    requestHeader.setProducerGroup(this.defaultMQProducer.getProducerGroup());
    requestHeader.setTranStateTableOffset(sendResult.getQueueOffset());
    requestHeader.setMsgId(sendResult.getMsgId());
    String remark = localException != null ? ("executeLocalTransactionBranch exception: " + localException.toString()) : null;
    //调用远程MQClientAPI
    this.mQClientFactory.getMQClientAPIImpl().endTransactionOneway(brokerAddr, requestHeader, remark,
                                                                   this.defaultMQProducer.getSendMsgTimeout());
}

```



## 注意

TransactionMQProducer的groupName要唯一，不能和普通的MQProducer的groupName一致。

producer调用的是org.apache.rocketmq.client.producer.TransactionMQProducer#sendMessageInTransaction(org.apache.rocketmq.common.message.Message, java.lang.Object)