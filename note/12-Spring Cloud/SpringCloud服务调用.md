# Spring Cloud服务调用



## HTTP VS RPC

​		dubbo是一款RPC调用框架，集成了服务发现、注册、负载均衡、路由等功能。

​		Spring Cloud是微服务一站式解决方案，主要通过HTTP进行服务间的通信





## SpringCloud中服务间两种restful调用方式

* RestTemplate
* Feign



## RestTemple的Restful调用



​		RestTemplate常用方法

![1561219538360](C:\Users\King-Pan\Documents\GitHub\java-architect\note\12-Spring Cloud\images\rest-cy-method.png)

### RestTemplate调用

```java
@GetMapping("/msg")
public String getMsg(){
    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject("http://localhost:8080/msg",String.class);
    return result;
}
```



### RestTemplate的优点

​		RestTemplate与HttpClient功能相似，虽然操作很复杂，但是容易理解。

###  RestTemplate的缺点

* 固定url，修改url后必须修改代码，属于硬编码
* 分布式部署，固定调用某一台，没有实现负载均衡（可以通过Ribbon实现负载均衡）
* 后期接口修改，也必须修改代码

### RestTemplate实现负载均衡

> 第一种方式

```java
@Autowired
private LoadBalancerClient loadBalancerClient;

@GetMapping("/msg")
public String getMsg(){
    RestTemplate restTemplate = new RestTemplate();
    ServiceInstance instance = loadBalancerClient.choose("PRODUCT");
    String url = String.format("http://%s:%s",instance.getHost(),instance.getPort())+"/msg";
    String result = restTemplate.getForObject(url,String.class);
    return result;
}
```

> 第二种方式

​		可以配置一个RestTemplate类型的javabean，然后在方法上添加 @LoadBalanced注解

```java
@Component
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```



调用，这儿需要注意在url里使用服务名称http://PRODUCT/msg中的PRODUCT

```java
@GetMapping("/msg")
public String getMsg(){
    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject("http://PRODUCT/msg",String.class);
    return result;
}
```

