# 07-RocketMQ生产者之MessageQueueSelector

## 简介

生产者使用MessageQueueSelector投递到Topic下指定的queue

* 应用场景: 顺序消息，分摊负载
* 默认自动创建的topic下的queue数量是4，手动创建的topic的queue数量是8
* 支持范围: 支持同步、异步发送指定的MessageQueue
* 选择的queue数量必须小于配置的，否则会出错->IndexOutOfBoundsException

```java
package mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * @author king-pan
 * @date 2019/5/15
 * @Description ${DESCRIPTION}
 */
public class MessageQueueSelectorDemo {

    private String groupName = "queue_selector";

    private String topic = "selector_topic";


    private DefaultMQProducer producer;

    public MessageQueueSelectorDemo() {
        producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(Config.NAME_ADDR);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
    /**同步MessageQueueSelector*/	
    public void sendMessageQueueSelector() {
        Message message = new Message(topic, "queue_selector_tag", "k1", "Queue selector".getBytes());
        try {
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    return mqs.get((Integer) arg);
                }
            }, 0);
            System.out.println(sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**异步MessageQueueSelector*/
    public void sendAsyncMessageQueueSelector() {
        Message message = new Message(topic, "queue_selector_tag", "k1", "Queue selector".getBytes());
        try {
            producer.send(message, (mqs, msg, arg)->{
                int index = Integer.parseInt(arg.toString());
                return mqs.get(index);
            }, 0, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("消息发送成功:"+sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println("消息发送异常");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MessageQueueSelectorDemo selectorDemo = new MessageQueueSelectorDemo();
        selectorDemo.sendMessageQueueSelector();
        selectorDemo.sendMessageQueueSelector();
        selectorDemo.sendAsyncMessageQueueSelector();
    }
}
```

