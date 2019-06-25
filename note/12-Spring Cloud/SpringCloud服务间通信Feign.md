# SpringCloud服务间通信Feign

​		Feign是一个声明式REST客户端(伪RPC )，采用了基于接口的注解。

​		Feign内置了Ribbon做负载均衡。

## 使用步骤

> 第一步：添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

> 第二步：启动类添加注解

```java
@EnableFeignClients
```

> 第三步：定义接口

```java
@FeignClient("PRODUCT")
public interface ProductClient {
    @RequestMapping("/msg")
    public String getMsg();
}
```

参数详解:

* @FeignClient: 调用的应用服务名
* @RequestMapping具体哪个接口
* public String getMsg();方法定义，注意方法参数必须一致.

> 第四步：使用

```java
@Autowired
private ProductClient productClient;

@GetMapping("/msg")
public String getMsg(){
    String result = productClient.getMsg();
    return result;
}
```

## 修改负载均衡策略

```java
@Bean
public IRule ribbonRule() {
    // 负载均衡规则，改为随机
    return new RandomRule();
}
```

​		当然你也可能会向使用配置的方式而非代码方式。**但是记住，配置方式优先级要大于代码方式。**

