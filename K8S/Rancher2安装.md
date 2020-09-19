# Rancher2安装



## 单节点安装



### 更新软件包和系统内核 (如无必要该步可以省略).

```sh

yum update
```



### 2 清空历史 (如无必要该步可以省略).

```
sudo yum remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-selinux docker-engine-selinux docker-engine
```



### 3 安装必要的包

```
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

4 添加yum源

```
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

### 5 更新 yum 缓存

```
yum makecache
```



6 查询可用的docker版本

```
yum install docker-ce-19.03.9 docker-ce-cli-19.03.9 containerd.io
```





```sh
[root@root-master ~]# yum install docker-ce-19.03.9 docker-ce-cli-19.03.9 containerd.io
Last metadata expiration check: 0:03:45 ago on Fri 18 Sep 2020 12:11:15 AM CST.
Error: 
 Problem: package docker-ce-3:19.03.9-3.el7.x86_64 requires containerd.io >= 1.2.2-3, but none of the providers can be installed
  - conflicting requests
  - package containerd.io-1.2.10-3.2.el7.x86_64 is filtered out by modular filtering
  - package containerd.io-1.2.13-3.1.el7.x86_64 is filtered out by modular filtering
  - package containerd.io-1.2.13-3.2.el7.x86_64 is filtered out by modular filtering
  - package containerd.io-1.2.2-3.3.el7.x86_64 is filtered out by modular filtering
  - package containerd.io-1.2.2-3.el7.x86_64 is filtered out by modular filtering
  - package containerd.io-1.2.4-3.1.el7.x86_64 is filtered out by modular filtering
  - package containerd.io-1.2.5-3.1.el7.x86_64 is filtered out by modular filtering
  - package containerd.io-1.2.6-3.3.el7.x86_64 is filtered out by modular filtering
(try to add '--skip-broken' to skip uninstallable packages or '--nobest' to use not only best candidate packages)
```



#### 更新 containerd.io 的版本



```sh
wget https://download.docker.com/linux/centos/7/x86_64/edge/Packages/containerd.io-1.2.6-3.3.el7.x86_64.rpm
yum install -y  containerd.io-1.2.6-3.3.el7.x86_64.rpm
```



### 启动docker并设置开机启动

```sh
service docker start
systemctl entable docker
```



### 配置阿里镜像加速

```sh
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://iht4xsyj.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```



## 拉去rancher2 镜像



```sh
docker pull rancher/rancher
```



## 创建两个挂载目录



```sh
mkdir -p /rancher/rancher
mkdir -p /rancher/auditlog
```





## 启动rancher2



```sh
docker run -d --restart=unless-stopped   \

  -p 80:80 -p 443:443   \
  
  -v /rancher/rancher:/var/lib/rancher    \
  
  -v /rancher/auditlog:/var/log/auditlog  \
  -e AUDIT_LEVEL=3 \
  -v /etc/secret/4342198_www.1buy.club.pem:/etc/rancher/ssl/cert.pem \
  -v /etc/secret/4342198_www.1buy.club.key:/etc/rancher/ssl/key.pem \
  rancher/rancher:latest --no-cacerts
  
  
   docker run -d --restart=unless-stopped \
  -p 80:80 -p 443:443  \
  -v /rancher/rancher:/var/lib/rancher \
  -v /rancher/auditlog:/var/log/auditlog \
  -e AUDIT_LEVEL=3 \
  -v /etc/secret/4342198_www.1buy.club.pem:/etc/rancher/ssl/cert.pem \
  -v /etc/secret/4342198_www.1buy.club.key:/etc/rancher/ssl/key.pem \
  rancher/rancher:latest --no-cacerts
  
  
    
   docker run -d --restart=unless-stopped \
  -p 80:80 -p 443:443  \
  -v /rancher/rancher:/var/lib/rancher \
  -v /rancher/auditlog:/var/log/auditlog \
  rancher/rancher:latest --no-cacerts
  
  
  sudo docker run -d --restart=unless-stopped -p 80:80 -p 443:443 rancher/rancher
```





```sh
docker run -itd -p 80:80 -p 443:443 \*

​    --restart=unless-stopped \

​    -e CATTLE_AGENT_IMAGE="registry.cn-hangzhou.aliyuncs.com/rancher/rancher-agent:v2.4.8" \

​    registry.cn-hangzhou.aliyuncs.com/rancher/rancher:v2.4.8
```





