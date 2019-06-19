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



## Spring Boot版本差异



> 差异配置属性列表

| 旧的属性                | 新的属性                               | 属性含义              |
| ----------------------- | -------------------------------------- | --------------------- |
| endpoints.<id>.*        | management.endpoint.<id>.*             | endpoint各属性配置    |
| endpoints.cors.*        | management.endpoints.web.cors.*        | web的cors相关配置     |
| endpoints.jmx.*         | management.endpoints.jmx.*             | jmx相关配置           |
| management.address      | management.server.address              | 地址                  |
| management.context-path | management.server.servlet.context-path | actuator路径配置      |
| management.ssl.*        | management.server.ssl.*                | actuator的ssl相关配置 |
| management.port         | management.server.port                 | actuator端口          |

> 请求路径

1. 所有 endpoints 默认情况下都已移至 `/actuator`。就是多了跟路径 `actuator` ；

2. 上个版本中的 `management/context-path:` 和 `management/port:` 改为 :

   ```yml
   management:
    server:
      port: 8004
      servlet:
        context-path: /xxx # 只有在设置了 management.server.port 时才有效 
   ```

​	另外，您还可以使用新的单独属性 `management.endpoints.web.base-path` 为管理端点设置基本路径。

例如，如果你设置management.server.servlet.context-path=/management和management.endpoints.web.base-path=/application，你就可以在下面的路径到达终点健康：/management/application/health。

1. 如果你想恢复 1.x 的行为（即具有`/health`代替`/actuator/health`），设置以下属性：`management.endpoints.web.base-path=/`

​	

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

**效果**

![1560913337923](C:\Users\king-pan\Documents\GitHub\java-architect\note\06-SpringBoot\images\health-all-info.png)

```
{
    status: "UP",
    details: {
        diskSpace: {
            status: "UP",
            details: {
                total: 127426097152,
                free: 77610815488,
                threshold: 10485760
            }
        }
    }
}
```

​		从上面的信息中我们可以得知，健康检查可以获取服务的状态、磁盘状态等信息。

> 默认配置解读

​		开启查看详细健康状态比较简单，通过配置参数`management.endpoint.health.show-details`来进行修改，该参数的值由`org.springframework.boot.actuate.health.ShowDetails`枚举提供配置值，`ShowDetails`源码如下所示：

```java
/**
 * Options for showing details in responses from the {@link HealthEndpoint} web
 * extensions.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
public enum ShowDetails {

    /**
     * Never show details in the response.
     */
    NEVER,

    /**
     * Show details in the response when accessed by an authorized user.
     */
    WHEN_AUTHORIZED,

    /**
     * Always show details in the response.
     */
    ALWAYS

}
```

​		在`spring-configuration-metadata.json`元数据文件内，配置的`showDetails`的默认值为`never`，也就是不显示详细信息，配置如下所示：

```java
//.....省略
{
  "sourceType": "org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties",
  "defaultValue": "never",
  "name": "management.endpoint.health.show-details",
  "description": "When to show full health details.",
  "type": "org.springframework.boot.actuate.health.ShowDetails"
},
//.....省略
```

### /info endpoint

​		info endpoint展示服务的基本信息， 默认是没有信息的。

​		它通过`META-INF/build-info.properties`来获得编译信息，通过`git.properties`来获得Git信息。它同时可以展示任何其他信息，只要这个环境property中含有`info`key。

```yml
# INFO ENDPOINT CONFIGURATION
info:
  app:
    name: @project.name@
    description: @project.description@
    version: @project.version@
    encoding: @project.build.sourceEncoding@
    java:
      version: @java.version@
```

​			注意，我使用了Spring Boot的[Automatic property expansion](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-properties-and-configuration.html#howto-automatic-expansion) 特征来扩展来自maven工程的properties。

一旦你增加上面的properties，`info` endpoint将展示如下信息：

```json
{
    app: {
        name: "actuator",
        description: "Demo project for Spring Boot",
        version: "0.0.1-SNAPSHOT",
        encoding: "UTF-8",
        java: {
        	version: "1.8.0_131"
        }
    }
}
```

### /loggers endpoint

### 

`loggers` endpoint，可以通过访问<http://localhost:8080/actuator/loggers>来进入。它展示了应用中可配置的loggers的列表和相关的日志等级。

你同样能够使用[http://localhost:8080/actuator/loggers/{name}](http://localhost:8080/actuator/loggers/{name)来展示特定logger的细节。

举个例子，为了获得`root` logger的细节，你可以使用<http://localhost:8080/actuator/loggers/root>：



```json
{
   "configuredLevel":"INFO",
   "effectiveLevel":"INFO"
}
```

#### 在运行时改变日志等级

`loggers` endpoint也允许你在运行时改变应用的日志等级。

举个例子，为了改变`root` logger的等级为`DEBUG` ，发送一个`POST`请求到<http://localhost:8080/actuator/loggers/root>，加入如下参数

```json
{
   "configuredLevel": "DEBUG"
}
```

* 这个功能对于线上问题的排查非常有用。

* 同时，你可以通过传递`null`值给`configuredLevel`来重置日志等级。

### /metrics endpoint

`metrics` endpoint展示了你可以追踪的所有的度量。

```json
{
    names: [
        "jvm.threads.states",
        "jvm.gc.memory.promoted",
        "jvm.memory.committed",
        "jvm.memory.used",
        "jvm.gc.max.data.size",
        "system.cpu.count",
        "logback.events",
        "tomcat.global.sent",
        "jvm.buffer.memory.used",
        "tomcat.sessions.created",
        "jvm.memory.max",
        "jvm.threads.daemon",
        "system.cpu.usage",
        "jvm.gc.memory.allocated",
        "tomcat.global.request.max",
        "tomcat.global.request",
        "tomcat.sessions.expired",
        "jvm.threads.live",
        "jvm.threads.peak",
        "tomcat.global.received",
        "process.uptime",
        "tomcat.sessions.rejected",
        "process.cpu.usage",
        "tomcat.threads.config.max",
        "jvm.classes.loaded",
        "http.server.requests",
        "jvm.gc.pause",
        "jvm.classes.unloaded",
        "tomcat.global.error",
        "tomcat.sessions.active.current",
        "tomcat.sessions.alive.max",
        "jvm.gc.live.data.size",
        "tomcat.threads.current",
        "jvm.buffer.count",
        "jvm.buffer.total.capacity",
        "tomcat.sessions.active.max",
        "tomcat.threads.busy",
        "process.start.time"
    ]
}
```

​		想要获得每个度量的详细信息，你需要传递度量的名称到URL中，像[http://localhost:8080/actuator/metrics/{MetricName}](http://localhost:8080/actuator/metrics/{MetricName)

​		举个例子，获得`systems.cpu.usage`的详细信息，使用以下URL<http://localhost:8080/actuator/metrics/system.cpu.usage>。它将显示如下内容:

```json
{
    name: "system.cpu.usage",
    description: "The "recent cpu usage" for the whole system",
    baseUnit: null,
    measurements: [
        {
            statistic: "VALUE",
            value: 0
        }
    ],
    availableTags: [ ]
}
```

### 其他endpoint

​		其他endpoint请参考 [actuator](http://localhost:8080/actuator)



## 自定义监控项



### java代码

```java
package club.javalearn.actuator.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class HbaseHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up()
                .withDetail("ip", "127.0.0.1")
                .withDetail("time", "100ms");
    }
}
```

### 注意事项

> AbstractHealthIndicator实现类的命名与在http请求结果的返回项有对应关系

例如：

* Java类名: HbaseHealthIndicator
* 返回结果: details.hbase  等于HbaseHealthIndicator 的HealthIndicator前缀

```json
{
    status: "UP",
    details: {
        hbase: {
            status: "UP",
            details: {
                app: "Alive and Kicking",
                error: "Nothing! I'm good."
            }
        },
        diskSpace: {
            status: "UP",
            details: {
                total: 127426097152,
                free: 78129496064,
                threshold: 10485760
            }
        }
    }
}
```

