# 一、 Spring Boot入门

## 1、 Spring Boot简介

> 简化Spring应用开发的一个框架

> 整个Spring技术栈的一个大整合

> J2EE开发的一站式解决方案

## 2、微服务

2014,martin fowler

微服务: 架构风格

一个应用应该是一组小型服务；一个通过HTTP的方式进行互通

每一个功能元素最终都是一个可独立替换和独立升级的软件

[详细参照微服务文档](https://martinfowler.com/articles/microservices.html#MicroservicesAndSoa)

 单体应用： all in one

优点: 开发、测试简单，部署，扩展简单

## 3、环境准备

> JDK1.8

```
king-pan@King-Pan-PC  ~  java -version
java version "1.8.0_121"
Java(TM) SE Runtime Environment (build 1.8.0_121-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.121-b13, mixed mode)
```



> Apache Maven 3.3.9

```
king-pan@King-Pan-PC  ~  mvn -version
Apache Maven 3.5.0 (ff8f5e7444045639af65f6095c62210b5713f426; 2017-04-04T03:39:06+08:00)
Maven home: /Users/king-pan/tools/maven-3.5.0
Java version: 1.8.0_121, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre
Default locale: zh_CN, platform encoding: UTF-8
OS name: "mac os x", version: "10.13.3", arch: "x86_64", family: "mac"
```



> IDEAIU 2017.3

![IDEA开发工具](https://upload-images.jianshu.io/upload_images/6331401-67bc2058fdcaaa36.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> Spring Boot版本2.0.0.RELEASE

> 配置IDEA

![配置IDEA Maven插件](https://upload-images.jianshu.io/upload_images/6331401-930f05303d9c0306.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


## 4、Spring Boot HelloWorld

一个功能:

​     浏览器发送hello请求，服务器接受请求并处理，相应Hello World字符串

### 1、创建一个maven工程(jar)

> 新建项目

![image-201803211514284.png](https://upload-images.jianshu.io/upload_images/6331401-e5c092b4d82999de.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image-201803211515336.png](https://upload-images.jianshu.io/upload_images/6331401-babec3a7d9c1ad6a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image-201803211516114.png](https://upload-images.jianshu.io/upload_images/6331401-1f531525889b4c7e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image-201803211516242.png](https://upload-images.jianshu.io/upload_images/6331401-794ea44c617e8db8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![初始化项目结构](https://upload-images.jianshu.io/upload_images/6331401-7ca9cb10c4a155a6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




### 2、导入依赖spring boot相关的依赖

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.0.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```



### 3、编写Spring Boot主程序类

```
package club.javalearn.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author king-pan 
 */
@SpringBootApplication
public class HelloworldApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloworldApplication.class, args);
	}
}
```



> @SpringBootApplication注解来标注一个主程序类，说明这是一个Spring Boot应用

> 老版本中的Spring Boot是使用三个注解
>
> @SpringBootConfiguration
>
> @EnableAutoConfiguration
>
> @ComponentScan

> SpringApplication.run(HelloworldApplication.class, args); Spring Boot应用启动起来

### 4、编写相关业务代码

```
package club.javalearn.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * hello-world
 *
 * @author king-pan
 * @date 2018-03-21
 **/
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello Spring Boot";
    }
}
```



> @RestController 等同于@Controller

也可以把主程序与Controller合二为一

```
package club.javalearn.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author king-pan 
 */
@SpringBootApplication
public class HelloworldApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloworldApplication.class, args);
	}
    
    @GetMapping("/hello")
    public String hello(){
        return "Hello Spring Boot";
    }
}
```





## 5、HelloWorld探究

### 1、POM文件

#### 1、父项目

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.0.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```



> spring-boot-starter-parent的父项目是spring-boot-dependencies

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.0.0.RELEASE</version>
    <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
```



> spring-boot-dependencies来真正管理Spring Boot应用里面的所有依赖版本，是Spring Boot的版本仲裁中心
>
> 以后我们导入依赖默认是不需要写版本的(没有在spring-boot-dependencies管理的需要些版本号version)

#### 2、启动器

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```



**spring-boot-starter**-`web`: Spring web模块的启动器

spring-boot-starter: Spring Boot场景启动器，帮我们导入了web模块正常依赖的组件;

**spring-boot-starter**-`test` Spring 测试模块的启动器

Spring Boot将所有的功能场景都抽取出来，做成一个个starter(启动器) ，只需要在项目中引入这些stater，相关场景的所有依赖都会导入进来。需要用什么功能就导入什么场景的starter(启动器)



#### 2、主程序类、主入口类

```
package club.javalearn.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author king-pan 
 */
@SpringBootApplication
public class HelloworldApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloworldApplication.class, args);
	}
}
```



@SpringBootApplication：Spring Boot应用标注在某个类上说明这个类是Spring Boot的主配置类，Spring Boot就应该这个类的main方法来启动SpringBoot应用

```
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
		@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

	@AliasFor(annotation = EnableAutoConfiguration.class)
	Class<?>[] exclude() default {};

	@AliasFor(annotation = EnableAutoConfiguration.class)
	String[] excludeName() default {};

	@AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
	String[] scanBasePackages() default {};

	@AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
	Class<?>[] scanBasePackageClasses() default {};

}
```



@SpringBootConfiguration: Spring Boot的配置类，标注在某个类上，表示这是一个Spring Boot的配置类

```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootConfiguration {
}
```



@Configuration:  配置类上标注这个注解；配置类等同与之前的配置类，配置类也是容器中的一个组件@Component

> @EnableAutoConfiguration: 开启自动配置功能；以前我们需要配置的东西，Sping Boot帮我们自动配置；@EnableAutoConfiguration告诉Spring Boot开启自动配置功能，这样自动配置才能生效。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {

	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

	Class<?>[] exclude() default {};

	String[] excludeName() default {};
}
```

> @AutoConfigurationPackage: 自动配置包,==将我们主配置类所在的包已经子包中的所有的组件扫描到spring容器中==

> @Import(AutoConfigurationImportSelector.class): Spring的底层注解@Import给容器导入一个组件；导入的组件由AutoConfigurationImportSelector.class类制定

> AutoConfigurationImportSelector: 导入哪些组件的选择器，将所有需要导入的组件以全类名的方式返回，这些组件就会被添加到容器中；会给容器中导入非常多的自动配置类；就是给容器中导入这个场景需要的所有组件，有了自动配置类，就免了手动编写配置注入功能组件等工作；

```java
@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return NO_IMPORTS;
		}
		try {
			AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
					.loadMetadata(this.beanClassLoader);
			AnnotationAttributes attributes = getAttributes(annotationMetadata);
			List<String> configurations = getCandidateConfigurations(annotationMetadata,
					attributes);
			configurations = removeDuplicates(configurations);
			configurations = sort(configurations, autoConfigurationMetadata);
			Set<String> exclusions = getExclusions(annotationMetadata, attributes);
			checkExcludedClasses(configurations, exclusions);
			configurations.removeAll(exclusions);
			configurations = filter(configurations, autoConfigurationMetadata);
			fireAutoConfigurationImportEvents(configurations, exclusions);
			return StringUtils.toStringArray(configurations);
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}
```



```
List<String> configurations = SpringFactoriesLoader.loadFactoryNames(
      getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());
```

```java
META-INF/spring.factories
spring-boot-autoconfigure-2.0.0.RELEASE.jar!\META-INF\spring.factories
```

==Spring Boot在启动的时候，在类路径在的META-INF/spring.factories中获取EnableAutoConfiguration指定的值，将这些值作为自动配置类导入到容器中，自动配置类就生效了，帮我们进行自动配置工作。==以前我们需要自己配置的东西，自动配置类帮我们自动配置。

J2EE的整体解决方案和自动配置都在spring-boot-autoconfigure-2.0.0.RELEASE.jar的org.springframework.boot.autoconfigure包中

## 6、使用Spring Initializer快速创建Spring Boot项目

STS/IDEA都支持Spring的项目创建向导快速创建Spring Boot项目

![快速创建spring boot项目第一步](https://upload-images.jianshu.io/upload_images/6331401-b761281182847958.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![快速创建spring boot项目第二步](https://upload-images.jianshu.io/upload_images/6331401-b3043368bf5dc010.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


> Project Type:如果选择Maven那么生成的就是一个完整的Spring Boot项目，有启动类（参考1-4-1）
>
> Project Type: 如果选择了Maven POM那么就只会生成一个pom.xml文件，不会生成src目录

![快速创建spring boot项目第三步](https://upload-images.jianshu.io/upload_images/6331401-660e95aa33893434.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![快速创建spring boot项目第四步](https://upload-images.jianshu.io/upload_images/6331401-e299163b3fc631ab.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![快速创建spring boot项目第五步](https://upload-images.jianshu.io/upload_images/6331401-a36f6f50c46b92d9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 依赖Spring Boot有两种方式

- 第一种就是直接引用(参考1-4-2)

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.0.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

- 第二种就是pom导入(需要放在project->dependencyManagement->dependencies节点下面)

```
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>2.0.0.RELEASE</version>
     <type>pom</type>
     <scope>import</scope>
 </dependency>
```



默认生成的Spring Boot项目：

> 主程序已经生成好了，我们只需要写我们自己的逻辑

- 默认Project Type = Maven
- 如果Project Type = Maven POM那么只会生成一个pom.xml文件

> resource文件夹中目录结构

- static文件夹: 保存所有的静态资源js、css、images
- templates文件夹: 保存所有的模板页面(Spring Boot默认jar包使用潜入式的Tomcat，默认不支持JSP页面，但是我们可以使用模板引擎[freemarker|thymeleaf])
- application.properties:Spring Boot应用的配置文件，可以修改一些默认配置。









