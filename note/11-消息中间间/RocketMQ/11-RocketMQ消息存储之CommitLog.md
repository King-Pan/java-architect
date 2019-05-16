# 11-RocketMQ消息存储之CommitLog

消息队列存储CommitLog分析

消息存储事由ConsumeQueue和CommitLog配合完成

* ConsumeQueue: 是逻辑队列，CommitLog是真正存储消息文件的，存储的是指向物理存储的地址Topic下的每个message queue都有对应的ConsumeQueue文件，内容也会被持久化到磁盘，默认地址: store/consume/topicName/{queueid}/fileName

![consumeQueue文件](./images/consumer-queue-file.png)

什么是CommitLog

​	消息文件的存储地址

​	生成规则:

​		每个文件的默认1G=**==1024x1024x1024==**，commitLog的文件名fileName，名字长度为20位，左边补零，剩余未起始偏移量:比如00000000000000000000代表第一个文件起始偏移量为0，文件大小为1G = 1073741824Byte，当这个文件满了，第二个文件名为00000000001073741824，消息存储的时候会顺序写入文件，当文件满了写入下一个文件。

判断消息存储在哪个CommitLog上，

例如1073742827为物理偏移量，则对应的相对偏移量为1003 =10773742827 - 1073741824，并且该偏移量位于第二个CommitLog上。



Broker里面的一个Topic 

里面有多个MessageQueue，每个MessageQueue对应一个ConsumeQueue，ConsumeQueue里面记录的是消息在CommitLog里面的物理存储地址。

