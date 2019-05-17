



## 下载源码

> 下载源码

[**http://mirror.bit.edu.cn/apache/rocketmq/4.4.0/rocketmq-all-4.4.0-source-release.zip**](http://mirror.bit.edu.cn/apache/rocketmq/4.4.0/rocketmq-all-4.4.0-source-release.zip)

> 解压

> 导入IDEA



## 分析生产者

```java
package mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author king-pan
 * @date 2019/5/13
 * @Description ${DESCRIPTION}
 */
public class ProductDemo {

    private DefaultMQProducer defaultMQProducer;

    public void start(){
        defaultMQProducer = new DefaultMQProducer();
        //设置生产者组
        defaultMQProducer.setProducerGroup("producer_demo");
        defaultMQProducer.setNamesrvAddr("39.104.164.68:9876");
        try {
            //必须调用producer的start()方法
            defaultMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void send() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = null;
        for(int i=0;i<5;i++){
            message = new Message("demo_topic","tag_a","key"+i,("我是第"+i+"条消息").getBytes());
            defaultMQProducer.send(message);
        }
    }

    

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        ProductDemo productDemo = new ProductDemo();
        productDemo.start();
        //productDemo.send();
    }
}
```

### 生产者启动流程

​	在上面的例子中我们可以知道，配置了DefaultMQProducer生产者组和nameAddr，我们调用了DefaultMQProducer的start()方法



```java
public class DefaultMQProducer extends ClientConfig implements MQProducer {
    /**
     * Wrapping internal implementations for virtually all methods presented in this class.
     */
    protected final transient DefaultMQProducerImpl defaultMQProducerImpl;
    
 	@Override
    public void start() throws MQClientException {
        //调用了defaultMQProducerImpl.start();
        this.defaultMQProducerImpl.start();
        if (null != traceDispatcher) {
            try {
                traceDispatcher.start(this.getNamesrvAddr());
            } catch (MQClientException e) {
                log.warn("trace dispatcher start failed ", e);
            }
        }
    }
}
```

>org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl#start()

```java
public class DefaultMQProducerImpl implements MQProducerInner {
    
    private ServiceState serviceState = ServiceState.CREATE_JUST;
    //调用了重载的start(boolean)方法
	 public void start() throws MQClientException {
        this.start(true);
    }

    public void start(final boolean startFactory) throws MQClientException {
        //判断状态，防止重复启动
        switch (this.serviceState) {
            //默认CREATE_JUST
            case CREATE_JUST:
                //更改状态防止多次启动
                this.serviceState = ServiceState.START_FAILED;
				//验证groupName
                this.checkConfig();
				//获取了jvm进程id作为producer的intancename名。
                if (!this.defaultMQProducer.getProducerGroup().equals(MixAll.CLIENT_INNER_PRODUCER_GROUP)) {
                    this.defaultMQProducer.changeInstanceNameToPID();
                }
			//获取MQClientManager单例对象，调用getAndCreateMQClientInstance，获取MQClientInstance(mq客户端对象实例) 

                this.mQClientFactory = MQClientManager.getInstance().getAndCreateMQClientInstance(this.defaultMQProducer, rpcHook);
            //查询groupname是否注册过，有就会false，没有就添加到map中，返回true

                boolean registerOK = mQClientFactory.registerProducer(this.defaultMQProducer.getProducerGroup(), this);
                            //如果groupname创建过则报错

                if (!registerOK) {
                    this.serviceState = ServiceState.CREATE_JUST;
                    throw new MQClientException("The producer group[" + this.defaultMQProducer.getProducerGroup()
                        + "] has been created before, specify another name please." + FAQUrl.suggestTodo(FAQUrl.GROUP_NAME_DUPLICATE_URL),
                        null);
                }

                               //新建TopicPublishInfo放入本地缓存变量—topicPublishInfoTable，key是默认的Topic（“TBW102”）
 this.topicPublishInfoTable.put(this.defaultMQProducer.getCreateTopicKey(), new TopicPublishInfo());
                //如果该方法入参是true，则会调用MQClientInstance的start方法

                if (startFactory) {
                    mQClientFactory.start();
                }

                log.info("the producer [{}] start OK. sendMessageWithVIPChannel={}", this.defaultMQProducer.getProducerGroup(),
                    this.defaultMQProducer.isSendMessageWithVIPChannel());
                //更新状态为RUNNING
                this.serviceState = ServiceState.RUNNING;
                break;
            case RUNNING:
            case START_FAILED:
            case SHUTDOWN_ALREADY:
                throw new MQClientException("The producer service state not OK, maybe started once, "
                    + this.serviceState
                    + FAQUrl.suggestTodo(FAQUrl.CLIENT_SERVICE_NOT_OK),
                    null);
            default:
                break;
        }
        //向所有的broker master发送心跳
        this.mQClientFactory.sendHeartbeatToAllBrokerWithLock();
    }
    
    private void checkConfig() throws MQClientException {
    	//首先验证ProducerGroup名称的 有效性。包括，非空，正则是否合法，最长字符不能超过255
        Validators.checkGroup(this.defaultMQProducer.getProducerGroup());
        //组名不能为空且不能是生产组名“DEFAULT_PRODUCER”
        if (null == this.defaultMQProducer.getProducerGroup()) {
            throw new MQClientException("producerGroup is null", (Throwable)null);
        } else if (this.defaultMQProducer.getProducerGroup().equals("DEFAULT_PRODUCER")) {
            throw new MQClientException("producerGroup can not equal DEFAULT_PRODUCER, please specify another one.", (Throwable)null);
        }
    }
}
```

>org.apache.rocketmq.common.ServiceState

```java
public enum ServiceState {
    /**
     * Service just created,not start
     */
    CREATE_JUST,
    /**
     * Service Running
     */
    RUNNING,
    /**
     * Service shutdown
     */
    SHUTDOWN_ALREADY,
    /**
     * Service Start failure
     */
    START_FAILED;
}
```



>org.apache.rocketmq.client.trace.AsyncTraceDispatcher#start

```java
public void start(String nameSrvAddr) throws MQClientException {
    if (isStarted.compareAndSet(false, true)) {
        traceProducer.setNamesrvAddr(nameSrvAddr);
        traceProducer.setInstanceName(TRACE_INSTANCE_NAME + "_" + nameSrvAddr);
        traceProducer.start();
    }
    this.worker = new Thread(new AsyncRunnable(), "MQ-AsyncTraceDispatcher-Thread-" + dispatcherId);
    this.worker.setDaemon(true);
    this.worker.start();
    this.registerShutDownHook();
}
```