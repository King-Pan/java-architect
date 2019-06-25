# Docker安装RabbitMQ



## 下载RabbitMQ



```shell
docker pull rabbitmq:3.7.3-management
```





## 启动命令



```shell
docker run -d --hostname my-rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.7.3-management
```

