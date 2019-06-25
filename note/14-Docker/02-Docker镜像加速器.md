# 1、Docker国内镜像加速



## 1.1、阿里云镜像加速

阿里云加速器访问地址: [阿里云加速](https://dev.aliyun.com/search.html) https://dev.aliyun.com/search.html



![阿里云加速器](C:/Users/King-Pan/Documents/GitHub/javanote/Docker/imgs/%E9%98%BF%E9%87%8C%E4%BA%91%E9%95%9C%E5%83%8F%E5%8A%A0%E9%80%9F.png)

### 1.1.1、申请阿里云账号

​	如果有阿里云账号，可以直接使用阿里云账号登陆，或者使用淘宝账号登陆。

### 1.1.2、配置阿里云加速器

[镜像加速器](https://cr.console.aliyun.com/cn-hangzhou/mirrors)

镜像加速地址查询方法：登陆https://dev.aliyun.com/--> 管理中心-->容器镜像服务-->镜像加速器； 

![阿里云加速镜像](C:/Users/King-Pan/Documents/GitHub/javanote/Docker/.imgs/%E9%85%8D%E7%BD%AE.png)

> 针对Docker客户端版本大于 1.10.0 的用户和大于Centos7

您可以通过修改daemon配置文件/etc/docker/daemon.json来使用加速器

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://iht4xsyj.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```



centos7以前的

```
vim /etc/sysconfig/docker
#添加一行
other_args="--registry-mirror=https://iht4xsyj.mirror.aliyuncs.com"
```

重启docker：$ service docker restart

### 1.1.3、检查加速器是否生效

```
#查看docket是否生效
$ ps -ef|grep docker
# ps 查看到的docker 进程后面会有registry-mirror=https://iht4xsyj.mirror.aliyuncs.com相关内容
```



## 1.2 网易云加速器

​	原理同阿里云，只需要把阿里云的网络加速器地址改为网易云加速器地址即可。