# RocketMQ常见错误



```java
2019-05-15 14:56:13 INFO brokerOutApi_thread_3 - register broker to name server localhost:9876 OK
2019-05-15 14:56:43 INFO brokerOutApi_thread_4 - register broker to name server localhost:9876 OK
2019-05-15 14:57:12 INFO BrokerControllerScheduledThread1 - dispatch behind commit log 0 bytes
2019-05-15 14:57:12 INFO BrokerControllerScheduledThread1 - Slave fall behind master: 399182 bytes
2019-05-15 14:57:13 INFO brokerOutApi_thread_1 - register broker to name server localhost:9876 OK
2019-05-15 14:57:43 INFO brokerOutApi_thread_2 - register broker to name server localhost:9876 OK
2019-05-15 14:58:12 INFO BrokerControllerScheduledThread1 - dispatch behind commit log 0 bytes
2019-05-15 14:58:12 INFO BrokerControllerScheduledThread1 - Slave fall behind master: 399182 bytes
2019-05-15 14:58:13 INFO brokerOutApi_thread_3 - register broker to name server localhost:9876 OK
2019-05-15 14:58:43 INFO brokerOutApi_thread_4 - register broker to name server localhost:9876 OK
2019-05-15 14:59:12 INFO BrokerControllerScheduledThread1 - dispatch behind commit log 0 bytes
2019-05-15 14:59:12 INFO B
2019-05-15 15:03:35 INFO HeartbeatThread_1 - new producer connected, group: example_group_name channel: ClientChannelInfo [channel=[id: 0xac573d5d, L:/172.24.78.17:10911 - R:/117.136.74.217:56876], clientId=192.168.43.222@24052, language=JAVA, version=293, lastUpdateTimestamp=1557903815993]
2019-05-15 15:03:35 INFO HeartbeatThread_1 - new producer connected, group: CLIENT_INNER_PRODUCER channel: ClientChannelInfo [channel=[id: 0xac573d5d, L:/172.24.78.17:10911 - R:/117.136.74.217:56876], clientId=192.168.43.222@24052, language=JAVA, version=293, lastUpdateTimestamp=1557903815993]
2019-05-15 15:03:43 INFO brokerOutApi_thread_2 - register broker to name server localhost:9876 OK
2019-05-15 15:03:45 INFO ClientManageThread_6 - unregister a producer[example_group_name] from groupChannelTable ClientChannelInfo [channel=[id: 0xac573d5d, L:/172.24.78.17:10911 - R:/117.136.74.217:56876], clientId=192.168.43.222@24052, language=JAVA, version=293, lastUpdateTimestamp=1557903825373]
2019-05-15 15:03:45 INFO ClientManageThread_6 - unregister a producer group[example_group_name] from groupChannelTable
2019-05-15 15:03:45 INFO ClientManageThread_7 - unregister a producer[CLIENT_INNER_PRODUCER] from groupChannelTable ClientChannelInfo [channel=[id: 0xac573d5d, L:/172.24.78.17:10911 - R:/117.136.74.217:56876], clientId=192.168.43.222@24052, language=JAVA, version=293, lastUpdateTimestamp=1557903825454]
2019-05-15 15:03:45 INFO ClientManageThread_7 - unregister a producer group[CLIENT_INNER_PRODUCER] from groupChannelTable
rokerControllerScheduledThread1 - Slave fall behind master: 399182 bytes
2019-05-15 14:59:13 INFO brokerOutApi_thread_1 - register broker to name server localhost:9876 OK
```



```java
SendResult [sendStatus=SEND_OK, msgId=AC140A06695418B4AAC24B7B8AF7003F, offsetMsgId=2768A44400002A9F0000000000084EEC, messageQueue=MessageQueue [topic=TopicTest, brokerName=broker-a, queueId=3], queueOffset=265]

```

