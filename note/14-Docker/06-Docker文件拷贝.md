> docker容器内部创建目录

```sh
mkdir /shard/txt -p
```

> 宿主机拷贝文件到docker容器

```sh
docker cp /root/shard/a.txt mycentos7:/shard/txt/
```

> docker容器拷贝文件到宿主机

```sh
docker cp mycentos7:/shard/txt/docker.md /root/shard/
```

可以指定宿主机的相对路径



***上面的mycentos7代指容器名称***





docker ps /root/software/apache-tomcat-7.0.96.tar mycentos7:/server/software/