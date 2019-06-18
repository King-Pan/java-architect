# Spring Boot Actuator



## Actuator简介

​		Spring Boot Actuator可以帮助你监控和管理Spring Boot应用，比如健康检查、审计、统计和HTTP追踪等。所有的这些特性可以通过JMX或者HTTP endpoints来获得。

​		Actuator同时还可以与外部应用监控系统整合，比如 [Prometheus](https://prometheus.io/), [Graphite](https://graphiteapp.org/), [DataDog](https://www.datadoghq.com/), [Influx](https://www.influxdata.com/), [Wavefront](https://www.wavefront.com/), [New Relic](https://newrelic.com/)等。这些系统提供了非常好的仪表盘、图标、分析和告警等功能，使得你可以通过统一的接口轻松的监控和管理你的应用。

​		Actuator使用[Micrometer](http://micrometer.io/)来整合上面提到的外部应用监控系统。这使得只要通过非常小的配置就可以集成任何应用监控系统。

​		Actuator创建了所谓的**endpoint**来暴露HTTP或者JMX来监控和管理应用。因为HTTP方式的不安全性，默认只暴露了health、info节点，其他节点需要手动开启。

## 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## 默认开放节点

启动actuator项目时，默认开启的HTTP节点如下:

* health 健康监测节点
* info 基本信息查看节点

```java
2019-06-18 17:25:37.908  INFO 10548 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 2 endpoint(s) beneath base path '/actuator'
```

[访问actuator](http://localhost:8080/actuator/)

![1560850650171](C:\Users\king-pan\Desktop\面试\Spring Boot\images\fw-actuator.png)



[访问health](http://localhost:8080/actuator/health)

![1560851242502](C:\Users\king-pan\Desktop\面试\Spring Boot\images\health.png)

```json
{
	status: "UP"
}
```





> 因为安全问题，默认只开启了HTTP的health、info节点，那么其他节点需要我们手动开启。



## endpoint列表

> 具体有如下节点

| Endpoint ID    | Description                                                  |
| -------------- | ------------------------------------------------------------ |
| auditevents    | 显示应用暴露的审计事件 (比如认证进入、订单失败)              |
| info           | 显示应用的基本信息                                           |
| health         | 显示应用的健康状态                                           |
| metrics        | 显示应用多样的度量信息                                       |
| loggers        | 显示和修改配置的loggers                                      |
| logfile        | 返回log file中的内容(如果logging.file或者logging.path被设置) |
| httptrace      | 显示HTTP足迹，最近100个HTTP request/repsponse                |
| env            | 显示当前的环境特性                                           |
| flyway         | 显示数据库迁移路径的详细信息                                 |
| liquidbase     | 显示Liquibase 数据库迁移的纤细信息                           |
| shutdown       | 让你逐步关闭应用                                             |
| mappings       | 显示所有的@RequestMapping路径                                |
| scheduledtasks | 显示应用中的调度任务                                         |
| threaddump     | 执行一个线程dump                                             |
| heapdump       | 返回一个GZip压缩的JVM堆dump                                  |

## 打开和关闭Actuator Endpoints

​		默认所以的actuator endpoint通过JMX被暴露，而通过HTTP暴露的只有`health`和`info`。

以下是你可以通过应用的properties可以通过HTTP和JMX暴露的actuator endpoint。

- 通过HTTP暴露Actuator endpoints

  * 全部暴露

  ```yml
  management:
    endpoints:
      web:
        exposure:
          include: '*'
  ```

  开启所以HTTP节点后，[访问actuator](http://localhost:8080/actuator/)

  ![1560851651640](C:\Users\king-pan\Desktop\面试\Spring Boot\images\http-inclube-all.png)

  * 指定暴露

  ```yml
  management:
    endpoints:
      web:
        exposure:
          include: beans
  ```

  指定只暴露beans后，[访问actuator](http://localhost:8080/actuator/)

  ![1560851793436](C:\Users\king-pan\Desktop\面试\Spring Boot\images\include-beans.png)

  

- 通过JMX暴露Actuator endpoints

> 默认开启JMX的所以节点，如果需要修改可以手动修改

```yml
management:
  endpoints:
    web:
      exposure:
        include: '*'
    jmx:
      exposure:
        include: '*'
```



JMX主要是通过jconsole来远程查看

> 打开jconsole，选择一个应用

![1560851987617](C:\Users\king-pan\Desktop\面试\Spring Boot\images\one-select-a-application.png)

> 确定不安全的连接

![1560852024937](C:\Users\king-pan\Desktop\面试\Spring Boot\images\two-no-safe-link.png)

> 进入概览

![1560852073896](C:\Users\king-pan\Desktop\面试\Spring Boot\images\enter-gl.png)

> 选择MBean->org.springframework.boot.Endpoint

![1560852117792](C:\Users\king-pan\Desktop\面试\Spring Boot\images\jmx-look.png)







## 常用endpoint



### /health endpoint

`health` endpoint通过合并几个健康指数检查应用的健康情况。

Spring Boot Actuator有几个预定义的健康指标比如[`DataSourceHealthIndicator`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/jdbc/DataSourceHealthIndicator.html), [`DiskSpaceHealthIndicator`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/system/DiskSpaceHealthIndicator.html), [`MongoHealthIndicator`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/mongo/MongoHealthIndicator.html), [`RedisHealthIndicator`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/redis/RedisHealthIndicator.html), [`CassandraHealthIndicator`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/cassandra/CassandraHealthIndicator.html)等。它使用这些健康指标作为健康检查的一部分。

举个例子，如果你的应用使用`Redis`，`RedisHealthindicator`将被当作检查的一部分。如果使用`MongoDB`，那么`MongoHealthIndicator`将被当作检查的一部分。

你也可以关闭特定的健康检查指标，比如在prpperties中使用如下命令：

```yml
management:
  health:
    mongo:
      enabled: false
```

默认，所有的这些健康指标被当作健康检查的一部分。

> 显示详细的健康信息

默认情况下，`health` endpoint只展示了简单的`UP`和`DOWN`状态。为了获得健康检查中所有指标的详细信息，你可以通过在`application.yaml`中增加如下内容：

```
management:
  endpoint:
    health:
      enable: true
      show-details: always
```



