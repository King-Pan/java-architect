





> 创建一个新的容器,用-v创建一个volume /data



```sh
[root@linux mydocker]# docker run -it --name container-test -h container -v /data busybox /bin/sh
Unable to find image 'busybox:latest' locally
Trying to pull repository docker.io/library/busybox ...
latest: Pulling from docker.io/library/busybox
0f8c40e1270f: Pull complete
Digest: sha256:1303dbf110c57f3edf68d9f5a16c082ec06c4cf7604831669faf2c712260b5a0
Status: Downloaded newer image for docker.io/busybox:latest
```

> 查看容器状态

```sh
[root@linux mydocker]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
1875e0f3fd4b        busybox             "/bin/sh"                2 minutes ago       Up 5 seconds                            container-test
```

> 使用docker inspect container-test |grep -i  volume

使用docker inspect命令查看/data与宿主机的哪个目录关联

```sh

```

