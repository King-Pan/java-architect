```shell

[zk: localhost:2181(CONNECTED) 0] ls /locks
[0000000045, 0000000044, 0000000047, 0000000046, 0000000049, 0000000048, 0000000050, 0000000041, 0000000043, 0000000042]
[zk: localhost:2181(CONNECTED) 1] delete /locks/0000000041
[zk: localhost:2181(CONNECTED) 2] delete /locks/0000000042
[zk: localhost:2181(CONNECTED) 3] delete /locks/0000000043
[zk: localhost:2181(CONNECTED) 4] delete /locks/0000000044
[zk: localhost:2181(CONNECTED) 5]

```


```java
Thread-4->/locks/0000000041,尝试竞争锁
Thread-1->/locks/0000000042,尝试竞争锁
Thread-0->/locks/0000000045,尝试竞争锁
Thread-3->/locks/0000000047,尝试竞争锁
Thread-7->/locks/0000000043,尝试竞争锁
Thread-5->/locks/0000000046,尝试竞争锁
Thread-2->/locks/0000000048,尝试竞争锁
Thread-6->/locks/0000000044,尝试竞争锁
Thread-8->/locks/0000000049,尝试竞争锁
Thread-9->/locks/0000000050,尝试竞争锁
Thread-4->/locks/0000000041->获取锁成功
Thread-1->等待锁/locks//locks/0000000041释放
Thread-7->等待锁/locks//locks/0000000042释放
Thread-3->等待锁/locks//locks/0000000046释放
Thread-5->等待锁/locks//locks/0000000045释放
Thread-0->等待锁/locks//locks/0000000044释放
Thread-2->等待锁/locks//locks/0000000047释放
Thread-6->等待锁/locks//locks/0000000043释放
Thread-8->等待锁/locks//locks/0000000048释放
Thread-9->等待锁/locks//locks/0000000049释放
Thread-1->获得锁成功
Thread-7->获得锁成功
Thread-6->获得锁成功
Thread-0->获得锁成功
```

