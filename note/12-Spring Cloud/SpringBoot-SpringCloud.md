### 一. 什么是springboot

1.用来简化spring应用的初始搭建以及开发过程 使用特定的方式来进行配置（properties或yml文件）
 2.创建独立的spring引用程序 main方法运行
 3.嵌入的Tomcat 无需部署war文件
 4.简化maven配置
 5.自动配置spring添加对应功能starter自动化配置



### 二.springboot常用的starter有哪些

可以去Maven仓库中搜索以下插件，pom文件依赖即可
 Maven仓库官网：[http://mvnrepository.com/](https://link.jianshu.com?t=http%3A%2F%2Fmvnrepository.com%2F)
 1.spring-boot-starter-web (嵌入tomcat和web开发需要servlet与jsp支持)
 2.spring-boot-starter-data-jpa (数据库支持)
 3.spring-boot-starter-data-redis (redis数据库支持)
 4.spring-boot-starter-data-solr (solr搜索应用框架支持)
 5.mybatis-spring-boot-starter (第三方的mybatis集成starter)

### springboot自动配置的原理

1.@EnableAutoConfiguration这个注释告诉SpringBoot“猜”你将如何想配置Spring,基于你已经添加jar依赖项。如果spring-boot-starter-web已经添加Tomcat和Spring MVC,这个注释自动将假设您正在开发一个web应用程序并添加相应的spring设置。会自动去maven中读取每个starter中的spring.factories文件  该文件里配置了所有需要被创建spring容器中的bean

2.使用@SpringbootApplication注解  可以解决根类或者配置类（我自己的说法，就是main所在类）头上注解过多的问题，一个@SpringbootApplication相当于@Configuration,@EnableAutoConfiguration和 @ComponentScan 并具有他们的默认属性值

### 九.springcloud断路器的作用

当一个服务调用另一个服务由于网络原因或者自身原因出现问题时 调用者就会等待被调用
 者的响应 当更多的服务请求到这些资源时
 导致更多的请求等待 这样就会发生连锁效应（雪崩效应） 断路器就是解决这一问题断路器
 有完全打开状态
 一定时间内 达到一定的次数无法调用 并且多次检测没有恢复的迹象 断路器完全打开，那
 么下次请求就不会请求到该服务半开,短时间内 有恢复迹象 断路器会将部分请求发给该服务 当
 能正常调用时 断路器关闭,当服务一直处于正常状态 能正常调用 断路器关闭

