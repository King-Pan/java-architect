## jenkins安装



### 安装java





### 下载jenkins

https://mirrors.tuna.tsinghua.edu.cn/jenkins/war/2.257/jenkins.war



### 配置jenkins



```sh
docker network create jenkins
docker volume create jenkins-docker-certs
docker volume create jenkins-data
```



###  启动jenkins





```sh
docker container run \
  --name jenkins-blueocean \
  --rm \
  --detach \
  --network jenkins \
  --env DOCKER_HOST=tcp://docker:2376 \
  --env DOCKER_CERT_PATH=/certs/client \
  --env DOCKER_TLS_VERIFY=1 \
  --publish 8080:8080 \
  --publish 50000:50000 \
  --volume jenkins-data:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  jenkinsci/blueocean
```



### 修改配置



```sh
vi /data/docker/volumes/jenkins-data/_data/hudson.model.UpdateCenter.xml


把默认插件地址：https://updates.jenkins.io/update-center.json
修改为清华大学镜像地址: https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json
```







