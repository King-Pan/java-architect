### Spring Boot中Spring Security牛刀小试

在spring boot项目中添加spring security依赖后，默认开启spring security功能，如果需要关闭，需要手动配置

```
security:
  basic:
    enabled: true # true开启，false关闭,默认开启
```

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

开启spring security默认功能后，会在项目启动时内置了一个user用户，密码在控制台找

```
2017-12-28 14:17:04.411  INFO 9961 --- [  restartedMain] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-12-28 14:17:04.556  INFO 9961 --- [  restartedMain] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-12-28 14:17:05.698  INFO 9961 --- [  restartedMain] b.a.s.AuthenticationManagerConfiguration : 

Using default security password: 2d0bd144-a093-4ead-a2d6-6ea0c2d98bf7

2017-12-28 14:17:05.830  INFO 9961 --- [  restartedMain] o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: OrRequestMatcher [requestMatchers=[Ant [pattern='/css/**'], Ant [pattern='/js/**'], Ant [pattern='/images/**'], Ant [pattern='/webjars/**'], Ant [pattern='/**/favicon.ico'], Ant [pattern='/error']]], []
```

默认的登录窗口为:

![默认登录窗口](http://upload-images.jianshu.io/upload_images/6331401-e4d5b17f29aa8477.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 问题
> 用户密码都是系统内置的，不符合生成需求

> 登录界面太丑，不忍直视
 
> 其他问题后续慢慢发现




