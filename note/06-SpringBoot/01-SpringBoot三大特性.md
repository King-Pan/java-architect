# Spring Boot三大特性



## 组件自动装配: Web MVC、Web Flux、JDBC、其他集成模块等

### 组件自动装配

spring-boot-autoconfigure/2.0.2.RELEASE/spring-boot-autoconfigure-2.0.2.RELEASE.jar!/META-INF/spring.factories

> 激活

@EnableAutoConfiguration

> 配置 :/META-INF/spring.factories

classpath目录下的/META-INF/spring.factories

> 实现

XXXAutoConfiguration

springboot2.x 是通过@SpringBootApplication



### Web应用



#### Servlet注册: Servelt注册、Spring Bean、RegistrationBean

#### 异步非阻塞: 异步Servelt、非阻塞Servlet



#### 传统Web项目

> 添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

#### Servlet组件: Servlet、Filter、Listener



> Servelt注册



```
package com.example.demo.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author king-pan
 * @date 2019/5/19
 * @Description ${DESCRIPTION}
 */
@WebServlet(urlPatterns = "/my/Servlet")
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Hello World  get method");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Hello World post method");
    }
}



//
@SpringBootApplication
@ServletComponentScan(basePackages = "com.example.demo.web.**")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

* 使用@WebServelt注解实现
* @WebServelt(urlPatterns="/my/servelt")映射
* @ServletComponentScan(basePackages = "com.example.demo.web.**") 注册



## 嵌入式Web容器: Tomcat、Jetty以及Undertow和Netty Server

Web Servlet：Tomcat、Jetty、Undertow

Web Reactive:	Netty Web Server

## 



## 生产特性

> 指标: /actuator/metrics 

> 健康检查: /actuator/health

> 外部化配置: /actuator/configprops







## Web应用

###



