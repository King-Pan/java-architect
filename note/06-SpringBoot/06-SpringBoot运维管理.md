# Spring Boot运维管理

## Spring Boot Actuator

* 端点： 各类Web和JMX Endpoints
* 健康检查: Health、HeadlthIndicator
* 指标: 内建Metrics、自定义Metrics

## 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## 端点

### Web Endpoints 

默认只开启Health、info

```properties
management.endpoints.web.exposure.include=info,health
//使用通配符暴露
management.endpoints.web.exposure.include=*
//虽然开启了endpoints，但是由于安全问题，只能显示状态

http://localhost:8080/actuator/metrics/jvm.memory.used
//查看jvm内存使用量
```



## JMX Endpoints



## 健康检查

> Health

> HealthIndicator

## 指标(Metrics)

### 内建Metrics

* Web Endpoints : /actuator/metrics

### 自定义Metrics

