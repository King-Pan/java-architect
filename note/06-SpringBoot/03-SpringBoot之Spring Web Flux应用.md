# Spring Web Flux应用

* Reactor基础: Java Lambda、Mono、Flux
* Web Flux核心: Web MVC注解、函数式声明、异步非阻塞
* 使用场景：优点、缺点

## Spring Web MVC注解兼容

* @Controller
* @RequestMapping
* @ResponseBody
* @RequestBody
* ...

## 函数式声明

* RouterFunction

## 异步非阻塞

* Servelt 3.1+
* Netty Reactor

## 使用场景



## 页面渲染

##  REST应用

## 性能测试

http://blog.ippon.tech/spring-5-webflux-performance-tests/



## Web Server应用

* 切换Web Server
* 自定义Servlet Web Server(定制Web Server)
* 自定义Reactive Web Server

### 切换Web Server

tomcat->Jetty

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <!--在web模块中排除默认的tomcat容器-->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--额外添加Jetty容器-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```



web flux容器

```xml
//注释掉web模块，添加web flux模块
<!--<dependencies>-->
<!--<dependency>-->
<!--<groupId>org.springframework.boot</groupId>-->
<!--<artifactId>spring-boot-starter</artifactId>-->
<!--</dependency>-->

<!--<dependency>-->
<!--<groupId>org.springframework.boot</groupId>-->
<!--<artifactId>spring-boot-starter-web</artifactId>-->
<!--<exclusions>-->
<!--<exclusion>-->
<!--<groupId>org.springframework.boot</groupId>-->
<!--<artifactId>spring-boot-starter-tomcat</artifactId>-->
<!--</exclusion>-->
<!--</exclusions>-->
<!--</dependency>-->

<!--<dependency>-->
<!--<groupId>org.springframework.boot</groupId>-->
<!--<artifactId>spring-boot-starter-jetty</artifactId>-->
<!--</dependency>-->

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

### 自定义Servlet Web Server

* WebServerFactoryCustomizer

### 自定义Reactive Web Server

* ReactiveWebServerFactoryCustomizer



