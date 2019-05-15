# 02-RocketMQ状态



```java
public enum SendStatus {
    SEND_OK,
    FLUSH_DISK_TIMEOUT,
    FLUSH_SLAVE_TIMEOUT,
    SLAVE_NOT_AVAILABLE,
}
```

​	java代码中SendStatus有四种状态



> SEND_OK 发送正常状态



> FLISH_NOT_AVAIVABLE

