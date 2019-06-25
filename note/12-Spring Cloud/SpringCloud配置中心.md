# SpringCloud配置中心



​		

# Spring Cloud Config

https://blog.csdn.net/xlgen157387/article/details/82840553

## 为什么需要统一配置中心

* 不方便维护（一句不方便维护包含了多少苦和累啊）
  * 单机、集群、分布式导致服务器主机的剧增
  * 应用部署数的剧增都会导致维护工作增加
* 配置内容安全与权限
* 更新配置项目需要重启

![1561044807283](./images/config-server.png)



## 开发配置中心步骤

### 添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

		添加了Eureka Client、Config Server依赖

### 添加注解

		在Spring Boot的启动类上添加注解

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class EbuyConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbuyConfigServerApplication.class, args);
    }

}
```

* @EnableDiscoveryClient标注该项目是一个Eureka的Client端
* @EnableConfigServer 标注该项目是一个Config Server

### 添加Git配置

```yml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/King-Pan/config-repo
          username: pwpw1218@gmail.com
          password: xxx
          basedir: /home/ocdc/config-server/config   #需要注意会情况config目录
```

		添加git仓库信息，使用git拉取配置到本地路径下，默认会放入到Temp目录下，生产环境下，需要修改默认路径，需要注意，配置的路径会被config server清空。

 


### 请求测试



/{name}-{profiles}.yml

/{label}/{name}-{profiles}.yml



其中变量含义如下:

* name 服务名
* profiles 环境
* label 分支(branch)



## 动态刷新配置



```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-monitor</artifactId>
</dependency>
```

