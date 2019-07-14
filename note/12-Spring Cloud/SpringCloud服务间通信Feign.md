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



## Feign处理Header

### Feign转发请求头

> 配置类

```java
@Slf4j
@Configuration
public class FeignConfiguration implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                requestTemplate.header(name, values);
            }
            log.info("feign interceptor header:{}", requestTemplate);
        }
    }
}
```

> 配置

```java
@FeignClient(name = "ORDER", configuration = FeignConfiguration.class, fallback = OrderServiceFallBack.class)
public interface OrderService {

    @GetMapping("/orders")
    String getAllOrder();
}

@Slf4j
public class OrderServiceFallBack implements OrderService {


    @Override
    public String getAllOrder() {
        log.warn("使用了feignClient注解中的fallback方法");
        return "订单接口异常，请稍后重试";
    }
}

@RestController
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public Object getAllOrder() {
        return orderService.getAllOrder();
    }
}
```

> 配置文件

```yml
hystrix:
  command:
    default:
      execution:
        timeout:
          enabled: false
        isolation:
          strategy: SEMAPHORE
```

### Feign自定义添加header

```java
@FeignClient(name = "ORDER", configuration = FeignConfiguration.class, fallback = OrderServiceFallBack.class)
public interface OrderService {
    @GetMapping(value = "/orders", headers = {"Content-Type=application/json", "a=aaa111"})
    String getAllOrder();
}
```

> 注意

​		***Header的key和value需要用“=”进行分割***

