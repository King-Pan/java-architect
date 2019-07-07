# Docker安装RabbitMQ



## 下载RabbitMQ



```shell
docker pull rabbitmq:3.7.3-management
```





## 启动命令



```shell
docker run -d --hostname my-rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.7.3-management
```





## 常见问题



### 启动报错



```shell
D:\MYSQL\mysql-8.0.16-winx64\bin>docker run -d --hostname my-rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.7.3-management
5e00c638e5fd82fd01aac3d7ebcb2365e5b6e32e3fc0e39f7e5a9b6a53d2269e
docker: Error response from daemon: driver failed programming external connectivity on endpoint friendly_morse (0ff9970b817d43ef569177d1b3f51fd9938d041fcfd11ed9179226bf2cab9f84): Error starting userland proxy: mkdir /port/tcp:0.0.0.0:15672:tcp:172.17.0.2:15672: input/output error.
```



> 解决方法

```shell
重启docker
```

