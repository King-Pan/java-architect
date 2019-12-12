## Spring Boot之Thymeleaf模板

>**Spring Boot支持多种模版引擎包括:**

* FreeMarker
* Groovy
* Thymeleaf(官方推荐)
* Mustache

>**JSP技术Spring Boot官方是不推荐的，原因有三:**

* tomcat只支持war的打包方式，不支持可执行的jar。
* Jetty 嵌套的容器不支持jsp
* Undertow(JBoss下面的内嵌服务器)嵌套的容器不支持jsp
* 创建自定义error.jsp页面不会覆盖错误处理的默认视图，而应该使用自定义错误页面
当你使用上述模板引擎中的任何一个，它们默认的模板配置路径为：src/main/resources/templates。当然也可以修改这个路径，具体如何修改，可在后续各模板引擎的配置属性中查询并修改。

### pom.xml配置

>**父类依赖**

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.1.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

>**使用其他版本Thymeleaf模板**

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
    <thymeleaf.version>3.0.2.RELEASE</thymeleaf.version>
    <thymeleaf-layout-dialect.version>2.0.4</thymeleaf-layout-dialect.version>
</properties>
```

>**添加依赖**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### yml配置

>**yml文件配置**

```yml
spring:
  thymeleaf:
    # 开启模板缓存 (默认开启)
    cache: false
    # 检查文档
    check-template: true
    # 检查文档位置
    check-template-location: true
    # context-type的值
    content-type: text/html
    # 开启MVC Thymeleaf 模板解析(默认为true)
    enabled: true
    # 字符编码
    encoding: UTF-8
    # 排除在解析器之外的视图名称列表,用逗号分隔
    excluded-view-names:
    # 模板模式
    mode: HTML5
    # 视图名称前缀
    prefix: classpath:/templates/
    # 视图名称后缀
    suffix: .html
    # 模板解析器在解析器链的顺序
    template-resolver-order:
```

>**默认情况下不要配置view-names,除非有必要,不然会报404**

```YAML
spring:
  thymeleaf:
    # 可解析的视图名称列表,用逗号隔开
    # view-names:
```

### 实体

```

public class Person {
    private String id;
    private String userName;
    private String password;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```

### 控制器

```
package club.javalearn.controller;

import club.javalearn.domain.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ThymeleafController {
    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView view = new ModelAndView("success");
        Person person = new Person();
        person.setUserName("King-Pan");
        view.addObject("person",person);
        return  view;
    }
}

```

### Thymeleaf模板

>**Thymeleaf默认属性**
>**spring.thymeleaf.prefix=classpath:/templates前缀**
>**spring.thymeleaf.suffix=.html**

*本案例修改为spring.thymeleaf.prefix=classpath:/templates/*

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>主页</title>
</head>
<body>
    <h1>
        <div>欢迎你,<span th:text="${person.userName}">Pan</span></div>
    </h1>
</body>
</html>
```

### 测试

```
http://localhost:8080/login
```


![测试结果](https://coding.net/u/javaBlog/p/markdown_img/git/raw/master/thymeleaf_img/thymeleaf.png)


