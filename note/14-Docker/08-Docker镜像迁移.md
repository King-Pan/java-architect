> 导出镜像

```sh
[root@linux software]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
web_tomcat7_user    latest              12841564aa53        5 seconds ago       830 MB
docker.io/redis     latest              dcf9ec9265e0        23 hours ago        98.2 MB
docker.io/mysql     5.6                 e143ed325782        29 hours ago        302 MB
docker.io/nginx     latest              231d40e811cd        29 hours ago        126 MB
docker.io/centos    7                   5e35e350aded        12 days ago         203 MB
docker.io/centos    latest              0f3e07c0138f        7 weeks ago         220 MB
docker.io/tomcat    7-jre7              47c156f4d4e3        6 months ago        359 MB

```



> save

```sh
[root@linux software]# docker save 12841564aa53 > web_user_tomcat7.tar.gz
```

* 解决导入tag信息为空信息

```sh
docker save -o web_tomcat7_user:latest web_user_tomcat7.tar.gz  #可以解决tag为空问题 
```

> 删除仓库中的镜像

```sh
[root@linux software]# docker rmi 12841564aa53
```

> load加载镜像备份

```sh
[root@linux software]# docker load < web_user_tomcat7.tar.gz
c7525a8f976b: Loading layer [==================================================>] 614.3 MB/614.3 MB
Loaded image ID: sha256:12841564aa53143bac5a49fe0f2f5c3d96e6f7e51db3909402aca15345486af9
[root@linux software]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
<none>              <none>              12841564aa53        10 minutes ago      830 MB
docker.io/redis     latest              dcf9ec9265e0        23 hours ago        98.2 MB
docker.io/mysql     5.6                 e143ed325782        29 hours ago        302 MB
docker.io/nginx     latest              231d40e811cd        30 hours ago        126 MB
docker.io/centos    7                   5e35e350aded        12 days ago         203 MB
docker.io/centos    latest              0f3e07c0138f        7 weeks ago         220 MB
docker.io/tomcat    7-jre7              47c156f4d4e3        6 months ago        359 MB
```

* 注意

* 这样导入的镜像的REPOSITORY、TAG是为none的，需要手动的修改

* ```sh
  [root@linux software]# docker tag 12841564aa53 web_tomcat7_user:latest
  [root@linux software]# docker images
  REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
  web_tomcat7_user    latest              12841564aa53        12 minutes ago      830 MB
  docker.io/redis     latest              dcf9ec9265e0        23 hours ago        98.2 MB
  docker.io/mysql     5.6                 e143ed325782        29 hours ago        302 MB
  docker.io/nginx     latest              231d40e811cd        30 hours ago        126 MB
  docker.io/centos    7                   5e35e350aded        12 days ago         203 MB
  docker.io/centos    latest              0f3e07c0138f        7 weeks ago         220 MB
  docker.io/tomcat    7-jre7              47c156f4d4e3        6 months ago        359 MB
  ```

