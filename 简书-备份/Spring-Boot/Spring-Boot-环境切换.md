1. 配置切换
application.yml
```
spring:
  profiles:
    active: prod
```
> **注意**
> **需要有对应的application-dev.yml和application-prod.yml文件**

2. 命令切换
```
java -jar target/xxx.jar --spring.profiles.activa=prod
```
