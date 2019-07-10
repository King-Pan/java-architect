# zipKin分布式链路追踪







接下来，重点解释一下日志中的 [appname,traceId,spanId,exportable] 各部分所代表的含义：

* appname：记录日志的应用的名称，即spring.application.name的值；

* traceId：Sleuth为一次请求链路生成的唯一ID，一个Trace中可以包含多个Span；

* spanId：请求链路基本的工作单元，代表发生一次特定的操作，例如：发送一个Http请求；

* exportable：是否需要将日志导出到 Zipkin；

```java
2019-07-10 23:48:36.177  INFO [order,5338a67b999060a3,11d0593a6be87e11,false] 11060 --- [derController-2] c.j.fresh.order.web.OrderController      : 调用商品服务...
```

