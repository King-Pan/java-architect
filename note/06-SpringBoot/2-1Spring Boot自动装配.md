# Spring Boot自动装配

* Spring Framework手动装配
* Spring Boot自动装配
* 课堂总结



## Spring Framework手动装配

### Spring模式注解装配

* 定义:一种用于声明在应用中扮演"组件"角色的注解
* 举例：@Component、@Service、@Configuration等
* 装配: <context:component-scan> 



### 模式注解举例

| Spring Framework 注解 | 场景说明           | 起始版本 |
| --------------------- | ------------------ | -------- |
| @Repository           | 数据仓储模式注解   | 2.0      |
| @Component            | 通用组件模式注解   | 2.5      |
| @Service              | 服务模式注解       | 2.5      |
| @Controller           | Web 控制器模式注解 | 2.5      |
| @Configuration        | 配置类模式注解     | 3.0      |

### 装配方式

<context:component-scan> 方

<?xml version="1.0" encoding="UTF-8"?> <beans xmlns="http://www.springframework.org/schema/beans"       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"

@ComponentScan 方式 
  自定义模式注解 

@Component “派生性” 
@Component
@Repository
FirstLevelRepository


    <?xml version="1.0" encoding="UTF-8"?> 
    <beans xmlns="http://www.springframework.org/schema/beans"       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xmlns:context="http://www.springframework.org/schema/context"       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/springcontext.xsd">
     
    <!-- 激活注解驱动特性 -->    <context:annotation-config />
     
    <!-- 找寻被 @Component 或者其派生 Annotation 标记的类（Class），将它们注册为 Spring Bean -->    <context:component-scan base-package="com.imooc.dive.in.spring.boot" />
    </beans>

