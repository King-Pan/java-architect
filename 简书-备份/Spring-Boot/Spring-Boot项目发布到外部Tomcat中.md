### 1. 打包方式改为 war
### 2. 将spring-boot-starter-tomcat的范围设置为provided
```
<dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-tomcat</artifactId>
         <scope>provided</scope>
      </dependency>
```
### 3. 修改代码设置自动启动

 需要集成SpringBootServletInitializer，然后重写configure，将Spring Boot的入口类设置进去。
```
public class StartApplication extends SpringBootServletInitializer{
    @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(StartApplication.class);
    } 
}
```
### 4. 执行maven命令打war包: mvn clean package
### 5. 部署到tomcat的webapps目录下
