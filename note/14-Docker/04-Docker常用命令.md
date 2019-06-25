# Docker常用命令

## 1.1、Docker帮助文档

> $docker version

```shell
[king-pan@localhost ~]$ docker version
Client:
 Version:           18.06.0-ce
 API version:       1.38
 Go version:        go1.10.3
 Git commit:        0ffa825
 Built:             Wed Jul 18 19:08:18 2018
 OS/Arch:           linux/amd64
 Experimental:      false

Server:
 Engine:
  Version:          18.06.0-ce
  API version:      1.38 (minimum version 1.12)
  Go version:       go1.10.3
  Git commit:       0ffa825
  Built:            Wed Jul 18 19:10:42 2018
  OS/Arch:          linux/amd64
  Experimental:     false
```

> $docker info

```shell
[king-pan@localhost ~]$ docker info
Containers: 1
 Running: 0
 Paused: 0
 Stopped: 1
Images: 1
Server Version: 18.06.0-ce
Storage Driver: overlay2
 Backing Filesystem: xfs
 Supports d_type: true
 Native Overlay Diff: true
Logging Driver: json-file
Cgroup Driver: cgroupfs
Plugins:
 Volume: local
 Network: bridge host macvlan null overlay
 Log: awslogs fluentd gcplogs gelf journald json-file logentries splunk syslog
Swarm: inactive
Runtimes: runc
Default Runtime: runc
Init Binary: docker-init
containerd version: d64c661f1d51c48782c9cec8fda7604785f93587
runc version: 69663f0bd4b60df09991c08812a60108003fa340
init version: fec3683
Security Options:
 seccomp
  Profile: default
Kernel Version: 3.10.0-862.el7.x86_64
Operating System: CentOS Linux 7 (Core)
OSType: linux
Architecture: x86_64
CPUs: 2
Total Memory: 1.779GiB
Name: localhost.localdomain
ID: SPLE:NVFB:ZVSK:32BI:PRSO:PKGP:PZGU:SG2Y:2ZSD:O37X:FRF2:HAEI
Docker Root Dir: /var/lib/docker
Debug Mode (client): false
Debug Mode (server): false
Registry: https://index.docker.io/v1/
Labels:
Experimental: false
Insecure Registries:
 127.0.0.0/8
Registry Mirrors:
 https://iht4xsyj.mirror.aliyuncs.com/
Live Restore Enabled: false

```

> $docker --help

```shell
[king-pan@localhost ~]$ docker --help

Usage:  docker [OPTIONS] COMMAND

A self-sufficient runtime for containers

Options:
      --config string      Location of client config files (default "/home/king-pan/.docker")
  -D, --debug              Enable debug mode
  -H, --host list          Daemon socket(s) to connect to
  -l, --log-level string   Set the logging level ("debug"|"info"|"warn"|"error"|"fatal") (default "info")
      --tls                Use TLS; implied by --tlsverify
      --tlscacert string   Trust certs signed only by this CA (default "/home/king-pan/.docker/ca.pem")
      --tlscert string     Path to TLS certificate file (default "/home/king-pan/.docker/cert.pem")
      --tlskey string      Path to TLS key file (default "/home/king-pan/.docker/key.pem")
      --tlsverify          Use TLS and verify the remote
  -v, --version            Print version information and quit

Management Commands:
  config      Manage Docker configs
  container   Manage containers
  image       Manage images
  network     Manage networks
  node        Manage Swarm nodes
  plugin      Manage plugins
  secret      Manage Docker secrets
  service     Manage services
  stack       Manage Docker stacks
  swarm       Manage Swarm
  system      Manage Docker
  trust       Manage trust on Docker images
  volume      Manage volumes

Commands:
  attach      Attach local standard input, output, and error streams to a running container
  build       Build an image from a Dockerfile
  commit      Create a new image from a container's changes
  cp          Copy files/folders between a container and the local filesystem
  create      Create a new container
  diff        Inspect changes to files or directories on a container's filesystem
  events      Get real time events from the server
  exec        Run a command in a running container
  export      Export a container's filesystem as a tar archive
  history     Show the history of an image
  images      List images
  import      Import the contents from a tarball to create a filesystem image
  info        Display system-wide information
  inspect     Return low-level information on Docker objects
  kill        Kill one or more running containers
  load        Load an image from a tar archive or STDIN
  login       Log in to a Docker registry
  logout      Log out from a Docker registry
  logs        Fetch the logs of a container
  pause       Pause all processes within one or more containers
  port        List port mappings or a specific mapping for the container
  ps          List containers
  pull        Pull an image or a repository from a registry
  push        Push an image or a repository to a registry
  rename      Rename a container
  restart     Restart one or more containers
  rm          Remove one or more containers
  rmi         Remove one or more images
  run         Run a command in a new container
  save        Save one or more images to a tar archive (streamed to STDOUT by default)
  search      Search the Docker Hub for images
  start       Start one or more stopped containers
  stats       Display a live stream of container(s) resource usage statistics
  stop        Stop one or more running containers
  tag         Create a tag TARGET_IMAGE that refers to SOURCE_IMAGE
  top         Display the running processes of a container
  unpause     Unpause all processes within one or more containers
  update      Update configuration of one or more containers
  version     Show the Docker version information
  wait        Block until one or more containers stop, then print their exit codes

Run 'docker COMMAND --help' for more information on a command.
```



## 1.2、Docker镜像命令

> $docker images

**列出本地所以的docker镜像**

```shell
[king-pan@localhost ~]$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
tomcat              latest              f3159377bac1        21 hours ago        463MB
hello-world         latest              2cb0d9787c4d        3 weeks ago         1.85kB
```

REPOSITORY: 镜像的仓库源

TAG: 镜像的标签

IMAGE ID: 镜像ID

CREATED: 镜像创建时间

SIZE: 镜像大小

​	*`同一仓库源可以有多个TAG,代表这个仓库源的不同版本，我们使用REPOSITORY:TAG来定义不同的镜像。如果不指定一个镜像的版本标签，例如你只使用ubuntu，docker将默认使用ubuntu:lastest镜像。`*



**参数(OPTIONS):**

- -a  列出本地所有的镜像(含中间映像层) eg:  docker images -a
- -q  只显示镜像ID      eg: docker images -aq
- --digests 显示镜像的摘要信息  eg: docker images --digests
- --no-trunc 显示完整的镜像信息   eg: docker images --no-trunc     
- 完整命令: $docker images -aq --digests --no-trunc 



> $docker search   img_name(镜像名)

​	docker search是从https://hub.docker.com/ 查找镜像，下载镜像是通过我们配置的镜像加速器的地址下载。

![Docker hub](G:\javanote\Docker\imgs\hub-docker.png)



```shell
[king-pan@localhost ~]$ docker search nginx
NAME                                                   DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
nginx                                                  Official build of Nginx.                        9145                [OK]                
jwilder/nginx-proxy                                    Automated Nginx reverse proxy for docker con…   1374                                    [OK]
```

​	一般情况下，常用的镜像有很多不的仓库源，我们有时候只需要官方的最基础的镜像，这样我们就需要使用一些参数来过滤镜像。

**参数(OPTIONS):**

- -s  number    : stars 数量大于等于number才显示.   eg: $docker search -s 20 --no-trunc nginx
- --no-trunc : 显示完整的镜像描述                   eg: $docker search --no-trunc
- --automated : 只列出automated类型的镜像      eg: $docker search --automated



> docker pull 镜像名[:tag]   下载某个镜像

 	下载镜像，Docker官网下载太慢，使用国内的镜像加速器。

```shell
[king-pan@localhost ~]$ docker pull nginx
Using default tag: latest
latest: Pulling from library/nginx
be8881be8156: Pull complete 
32d9726baeef: Pull complete 
87e5e6f71297: Pull complete 
Digest: sha256:d85914d547a6c92faa39ce7058bd7529baacab7e0cd4255442b04577c4d1f424
Status: Downloaded newer image for nginx:latest
[king-pan@localhost ~]$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
tomcat              latest              f3159377bac1        22 hours ago        463MB
nginx               latest              c82521676580        7 days ago          109MB
hello-world         latest              2cb0d9787c4d        3 weeks ago         1.85kB
[king-pan@localhost ~]$ 
```

**参数(OPTIONS):**

- 指定版本镜像下载:       eg: $docker pull nginx:1.14.0

![查询镜像版本](G:\javanote\Docker\imgs\docker-pull-tag.png)

```shell
[king-pan@localhost ~]$ docker pull nginx:1.14.0
1.14.0: Pulling from library/nginx
be8881be8156: Already exists 
b4babd36efe5: Pull complete 
f4eba7658e18: Pull complete 
Digest: sha256:9b52c379180db988f7ff5f90ea851bf502fdf35c6b96b3bf64911c7183dec22e
Status: Downloaded newer image for nginx:1.14.0
[king-pan@localhost ~]$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
tomcat              latest              f3159377bac1        22 hours ago        463MB
nginx               latest              c82521676580        7 days ago          109MB
nginx               1.14.0              8ae4d16b741a        2 weeks ago         109MB
hello-world         latest              2cb0d9787c4d        3 weeks ago         1.85kB
```



> $docker rmi 镜像名字/ID   删除镜像

​	如果通过镜像名称删除镜像，不指定版本号，默认删除   镜像名:latest 版本。

```shell

```

**参数(OPTIONS):**

- -f   强制删除镜像

```shell
[king-pan@localhost ~]$ docker run hello-world

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/engine/userguide/

[king-pan@localhost ~]$ docker rmi hello-world
Error response from daemon: conflict: unable to remove repository reference "hello-world" (must force) - container 7f81352ff1a9 is using its referenced image 2cb0d9787c4d
[king-pan@localhost ~]$ docker rmi -f hello-world
Untagged: hello-world:latest
Untagged: hello-world@sha256:4b8ff392a12ed9ea17784bd3c9a8b1fa3299cac44aca35a85c90c5e3c7afacdc
Deleted: sha256:2cb0d9787c4dd17ef9eb03e512923bc4db10add190d3f84af63b744e353a9b34
```



- 删除单个      eg: $docker rmi -f  镜像名称/ID
- 删除多个      eg: $docker rmi -f 镜像名1[:tag]  镜像名2[:tag]
- 删除全部      eg: docker rmi -f ${docker imags -qa}

## 1.3、容器命令

> $ docker ps 查看docker容器

- docker ps -a 查看已创建的容器，包括运行的和停止的
- docker ps -s 查看正在运行的容器

```shell
 king-pan@King-Pan-PC ~ docker ps -a
CONTAINER ID        IMAGE               COMMAND             CREATED              STATUS                       PORTS               NAMES
8e8fdba46999        centos              "/bin/bash"         About a minute ago   Exited (137) 8 seconds ago                       mycentos
 king-pan@King-Pan-PC ~ docker ps -s
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
 king-pan@King-Pan-PC ~ docker start 8e8fdba46999
8e8fdba46999
 king-pan@King-Pan-PC ~ docker ps -s
CONTAINER ID        IMAGE               COMMAND             CREATED              STATUS              PORTS               NAMES               SIZE
8e8fdba46999        centos              "/bin/bash"         About a minute ago   Up 9 seconds                            mycentos            0B (virtual 199MB)
```



> $ docker run 创建容器

- -i  交互的方式创建一个容器

- -t  返回一个伪终端

- -d 守护模式启动容器

- —— name 给终端命名

- — — rm exit容器后自动删除容器

- 其他命令请参考 docker run — — help 命令

  

  ```shell
  ~ docker ps -s
  CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
  ~ docker ps -a
  CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                       PORTS               NAMES
  8e8fdba46999        centos              "/bin/bash"         6 minutes ago       Exited (137) 4 minutes ago                       mycentos
   ~ docker run -it --name mycentos02 centos
  [root@b2667c0419b8 /]# ls
  anaconda-post.log  bin  dev  etc  home  lib  lib64  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
  [root@b2667c0419b8 /]#
  
  ```



> $ docker stop 停止容器

- -t 多少秒后停止容器
- 容器以伪终端交互形式启动的可以通过exit直接停止容器
- 容器以伪终端交互形式启动的可以通过ctrl+q+p 来退出伪终端交互模式，可以通过docker stop 容器ID/名称停止容器，可以通过docker attach 容器ID/名称进入容器

```shell
~ docker ps -s
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
~ docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS                        PORTS               NAMES
b2667c0419b8        centos              "/bin/bash"              About a minute ago   Exited (127) 2 minutes ago                        mycentos02
7dedf3f34471        mysql               "docker-entrypoint.s…"   About a minute ago   Exited (1) 5 minutes ago                          mysql
8e8fdba46999        centos              "/bin/bash"              12 minutes ago       Exited (137) 10 minutes ago                       mycentos
~ docker start 8e8fdba46999
8e8fdba46999
~ docker attach 8e8fdba46999
[root@8e8fdba46999 /]# ls
anaconda-post.log  bin  dev  etc  home  lib  lib64  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
[root@8e8fdba46999 /]# read escape sequence
~ docker ps -s
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
8e8fdba46999        centos              "/bin/bash"         13 minutes ago      Up 46 seconds                           mycentos            0B (virtual 199MB)
~ docker attach mycentos
[root@8e8fdba46999 /]# exit
exit
~ docker ps -s
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
```



> $ docket start 启动容器

- -a 等同于—attach  启动容器并且进入容器

```shell
~ docker start --help

Usage:	docker start [OPTIONS] CONTAINER [CONTAINER...]

Start one or more stopped containers

Options:
  -a, --attach                  Attach STDOUT/STDERR and forward signals
      --checkpoint string       Restore from this checkpoint
      --checkpoint-dir string   Use a custom checkpoint storage directory
      --detach-keys string      Override the key sequence for detaching a container
  -i, --interactive             Attach container's STDIN
~ docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                       PORTS               NAMES
b2667c0419b8        centos              "/bin/bash"              6 minutes ago       Exited (127) 7 minutes ago                       mycentos02
7dedf3f34471        mysql               "docker-entrypoint.s…"   6 minutes ago       Exited (1) 10 minutes ago                        mysql
8e8fdba46999        centos              "/bin/bash"              17 minutes ago      Exited (0) 2 minutes ago                         mycentos
~ docker start -a b2667c0419b8
[root@b2667c0419b8 /]#
```

> $ docker restart 容器ID/名称     重启容器

- -t --time int second  多少秒后重启容器

```shell
[king-pan@localhost ~]$ docker restart --help

Usage:  docker restart [OPTIONS] CONTAINER [CONTAINER...]

Restart one or more containers

Options:
  -t, --time int   Seconds to wait for stop before killing the container (default 10)
```

```shell
[king-pan@localhost ~]$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
tomcat              latest              f3159377bac1        5 days ago          463MB
nginx               latest              c82521676580        12 days ago         109MB
nginx               1.14.0              8ae4d16b741a        2 weeks ago         109MB
hello-world         latest              2cb0d9787c4d        3 weeks ago         1.85kB
[king-pan@localhost ~]$ docker ps -s
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
[king-pan@localhost ~]$ docker run -it --name mytomcat tomcat
Using CATALINA_BASE:   /usr/local/tomcat
Using CATALINA_HOME:   /usr/local/tomcat
Using CATALINA_TMPDIR: /usr/local/tomcat/temp
Using JRE_HOME:        /docker-java-home/jre
......
06-Aug-2018 14:52:54.971 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 1102 ms
[king-pan@localhost ~]$ docker ps -s
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
19f950cd7b4b        tomcat              "catalina.sh run"   24 seconds ago      Up 23 seconds       8080/tcp            mytomcat            40.1kB (virtual 463MB)
[king-pan@localhost ~]$ docker restart ^C
[king-pan@localhost ~]$ docker restart 19f950cd7b4b
19f950cd7b4b
[king-pan@localhost ~]$ 
```



> $ docker rm 删除容器

- -f    在删除镜像的时候，我们使用-f(force)强制删除镜像，同理docker rm -f 容器ID/名称 强制删除容器

  

  ```shell
  ~ docker ps -a
  CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                      PORTS               NAMES
  b2667c0419b8        centos              "/bin/bash"              18 minutes ago      Up 11 minutes                                   mycentos02
  7dedf3f34471        mysql               "docker-entrypoint.s…"   18 minutes ago      Exited (1) 22 minutes ago                       mysql
  8e8fdba46999        centos              "/bin/bash"              29 minutes ago      Exited (0) 14 minutes ago                       mycentos
  ~ docker start b2667c0419b8
  b2667c0419b8
  ~ docker ps -s
  CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
  b2667c0419b8        centos              "/bin/bash"         18 minutes ago      Up 12 minutes                           mycentos02          27B (virtual 199MB)
  ~ docker rm b2667c0419b8
  Error response from daemon: You cannot remove a running container b2667c0419b8dfed8da14b7c13fd7779bd4e5abf8112bc9f95e3fa55323295f9. Stop the container before attempting removal or force remove
  ~ docker rm -f b2667c0419b8
  b2667c0419b8
  ~ docker ps -s
  CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES               SIZE
  ```

  

  > $ docker logs 容器ID/名称   查看容器日志

  - -f  跟随最新的日志打印
  - -t 显示时间戳
  - --details 显示详情
  - --tail number 显示最后n条
  - --since string 从某个时间段开始的的或者多少分钟以前。

  ```shell
  [king-pan@localhost ~]$ docker logs --help
  
  Usage:  docker logs [OPTIONS] CONTAINER
  
  Fetch the logs of a container
  
  Options:
        --details        Show extra details provided to logs
    -f, --follow         Follow log output
        --since string   Show logs since timestamp (e.g. 2013-01-02T13:23:37) or relative (e.g. 42m for 42 minutes)
        --tail string    Number of lines to show from the end of the logs (default "all")
    -t, --timestamps     Show timestamps
        --until string   Show logs before a timestamp (e.g. 2013-01-02T13:23:37) or relative (e.g. 42m for 42 minutes)
  [king-pan@localhost ~]$
  
  ```

  ```shell
  [king-pan@localhost ~]$ docker logs -tf a1777e6d6234
  2018-08-06T15:08:19.189281190Z hello centos
  2018-08-06T15:08:21.198816713Z hello centos
  ......
  
  ```

  ```shell
  [king-pan@localhost ~]$ docker logs -f a1777e6d6234
  hello centos
  hello centos
  ......
  
  ```

  ```shell
  [king-pan@localhost ~]$ docker logs -tf --tail 5 a1777e6d6234
  2018-08-06T15:19:46.596591947Z hello centos
  2018-08-06T15:19:48.599787749Z hello centos
  2018-08-06T15:19:50.603624298Z hello centos
  2018-08-06T15:19:52.606892475Z hello centos
  2018-08-06T15:19:54.609847748Z hello centos
  --上面5条是显示最后的五条，下面的2条最新输出的2条
  2018-08-06T15:19:56.612308851Z hello centos
  2018-08-06T15:19:58.615083665Z hello centos
  
  ```

  > $ docker top 容器ID/名称 查看容器内运行的进程

  - docker top 容器ID/名称

  ```shell
  [king-pan@localhost ~]$ docker ps -s
  CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES               SIZE
  a1777e6d6234        49f7960eb7e4        "/bin/sh -c 'while t…"   19 minutes ago      Up 19 minutes                           mycentos            0B (virtual 200MB)
  [king-pan@localhost ~]$ docker top a1777e6d6234
  UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
  root                5294                5277                0                   23:08               pts/0               00:00:00            /bin/sh -c while true;do echo hello centos;sleep 2;done
  root                6242                5294                0                   23:28               pts/0               00:00:00            sleep 2
  [king-pan@localhost ~]$ 
  
  ```

  

  > $ docker inspect 容器ID/名称  查看容器内部细节

  - docker inspect 容器ID/名称

  ```shell
  [king-pan@localhost ~]$ docker inspect a1777e6d6234
  [
      {
          "Id": "a1777e6d6234e601c64e2771eb92137b81854e98b9226bc918a3c5816f660a00",
          "Created": "2018-08-06T15:08:18.11800433Z",
          "Path": "/bin/sh",
          "Args": [
              "-c",
              "while true;do echo hello centos;sleep 2;done"
          ],
          "State": {
              "Status": "running",
              "Running": true,
              "Paused": false,
              "Restarting": false,
              "OOMKilled": false,
              "Dead": false,
              "Pid": 5294,
              "ExitCode": 0,
              "Error": "",
              "StartedAt": "2018-08-06T15:08:19.174137771Z",
              "FinishedAt": "0001-01-01T00:00:00Z"
          },
          "Image": "sha256:49f7960eb7e4cb46f1a02c1f8174c6fac07ebf1eb6d8deffbcb5c695f1c9edd5",
          "ResolvConfPath": "/var/lib/docker/containers/a1777e6d6234e601c64e2771eb92137b81854e98b9226bc918a3c5816f660a00/resolv.conf",
          "HostnamePath": "/var/lib/docker/containers/a1777e6d6234e601c64e2771eb92137b81854e98b9226bc918a3c5816f660a00/hostname",
          "HostsPath": "/var/lib/docker/containers/a1777e6d6234e601c64e2771eb92137b81854e98b9226bc918a3c5816f660a00/hosts",
          "LogPath": "/var/lib/docker/containers/a1777e6d6234e601c64e2771eb92137b81854e98b9226bc918a3c5816f660a00/a1777e6d6234e601c64e2771eb92137b81854e98b9226bc918a3c5816f660a00-json.log",
          "Name": "/mycentos",
          "RestartCount": 0,
          "Driver": "overlay2",
          "Platform": "linux",
          "MountLabel": "",
          "ProcessLabel": "",
          "AppArmorProfile": "",
          "ExecIDs": null,
          "HostConfig": {
              "Binds": null,
              "ContainerIDFile": "",
              "LogConfig": {
                  "Type": "json-file",
                  "Config": {}
              },
              "NetworkMode": "default",
              "PortBindings": {},
              "RestartPolicy": {
                  "Name": "no",
                  "MaximumRetryCount": 0
              },
              "AutoRemove": false,
              "VolumeDriver": "",
              "VolumesFrom": null,
              "CapAdd": null,
              "CapDrop": null,
              "Dns": [],
              "DnsOptions": [],
              "DnsSearch": [],
              "ExtraHosts": null,
              "GroupAdd": null,
              "IpcMode": "shareable",
              "Cgroup": "",
              "Links": null,
              "OomScoreAdj": 0,
              "PidMode": "",
              "Privileged": false,
              "PublishAllPorts": false,
              "ReadonlyRootfs": false,
              "SecurityOpt": null,
              "UTSMode": "",
              "UsernsMode": "",
              "ShmSize": 67108864,
              "Runtime": "runc",
              "ConsoleSize": [
                  0,
                  0
              ],
              "Isolation": "",
              "CpuShares": 0,
              "Memory": 0,
              "NanoCpus": 0,
              "CgroupParent": "",
              "BlkioWeight": 0,
              "BlkioWeightDevice": [],
              "BlkioDeviceReadBps": null,
              "BlkioDeviceWriteBps": null,
              "BlkioDeviceReadIOps": null,
              "BlkioDeviceWriteIOps": null,
              "CpuPeriod": 0,
              "CpuQuota": 0,
              "CpuRealtimePeriod": 0,
              "CpuRealtimeRuntime": 0,
              "CpusetCpus": "",
              "CpusetMems": "",
              "Devices": [],
              "DeviceCgroupRules": null,
              "DiskQuota": 0,
              "KernelMemory": 0,
              "MemoryReservation": 0,
              "MemorySwap": 0,
              "MemorySwappiness": null,
              "OomKillDisable": false,
              "PidsLimit": 0,
              "Ulimits": null,
              "CpuCount": 0,
              "CpuPercent": 0,
              "IOMaximumIOps": 0,
              "IOMaximumBandwidth": 0,
              "MaskedPaths": [
                  "/proc/acpi",
                  "/proc/kcore",
                  "/proc/keys",
                  "/proc/latency_stats",
                  "/proc/timer_list",
                  "/proc/timer_stats",
                  "/proc/sched_debug",
                  "/proc/scsi",
                  "/sys/firmware"
              ],
              "ReadonlyPaths": [
                  "/proc/asound",
                  "/proc/bus",
                  "/proc/fs",
                  "/proc/irq",
                  "/proc/sys",
                  "/proc/sysrq-trigger"
              ]
          },
          "GraphDriver": {
              "Data": {
                  "LowerDir": "/var/lib/docker/overlay2/a3b50d936b18f53c411d11af5105c9d49bd8066777f6adac6918edd3d15f0ce9-init/diff:/var/lib/docker/overlay2/7db963326383e330ec662a09ebe3f52967bfeb3e94c4b0ec1d682487dbe8082e/diff",
                  "MergedDir": "/var/lib/docker/overlay2/a3b50d936b18f53c411d11af5105c9d49bd8066777f6adac6918edd3d15f0ce9/merged",
                  "UpperDir": "/var/lib/docker/overlay2/a3b50d936b18f53c411d11af5105c9d49bd8066777f6adac6918edd3d15f0ce9/diff",
                  "WorkDir": "/var/lib/docker/overlay2/a3b50d936b18f53c411d11af5105c9d49bd8066777f6adac6918edd3d15f0ce9/work"
              },
              "Name": "overlay2"
          },
          "Mounts": [],
          "Config": {
              "Hostname": "a1777e6d6234",
              "Domainname": "",
              "User": "",
              "AttachStdin": true,
              "AttachStdout": true,
              "AttachStderr": true,
              "Tty": true,
              "OpenStdin": true,
              "StdinOnce": true,
              "Env": [
                  "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
              ],
              "Cmd": [
                  "/bin/sh",
                  "-c",
                  "while true;do echo hello centos;sleep 2;done"
              ],
              "Image": "49f7960eb7e4",
              "Volumes": null,
              "WorkingDir": "",
              "Entrypoint": null,
              "OnBuild": null,
              "Labels": {
                  "org.label-schema.schema-version": "= 1.0     org.label-schema.name=CentOS Base Image     org.label-schema.vendor=CentOS     org.label-schema.license=GPLv2     org.label-schema.build-date=20180531"
              }
          },
          "NetworkSettings": {
              "Bridge": "",
              "SandboxID": "8159d3d704af87b4923b89e2a71e496e2bc887aa9e951e67eb42fe29a0a36a5d",
              "HairpinMode": false,
              "LinkLocalIPv6Address": "",
              "LinkLocalIPv6PrefixLen": 0,
              "Ports": {},
              "SandboxKey": "/var/run/docker/netns/8159d3d704af",
              "SecondaryIPAddresses": null,
              "SecondaryIPv6Addresses": null,
              "EndpointID": "6f0c016c5604851c7ac44d1aa4804cfa4d499cc150be71b78277fd902fd8baed",
              "Gateway": "172.17.0.1",
              "GlobalIPv6Address": "",
              "GlobalIPv6PrefixLen": 0,
              "IPAddress": "172.17.0.2",
              "IPPrefixLen": 16,
              "IPv6Gateway": "",
              "MacAddress": "02:42:ac:11:00:02",
              "Networks": {
                  "bridge": {
                      "IPAMConfig": null,
                      "Links": null,
                      "Aliases": null,
                      "NetworkID": "8bfdd7be8f06d0a46ea7a0b50e331b6caaa4307387161fd75e7f79b2ece4410f",
                      "EndpointID": "6f0c016c5604851c7ac44d1aa4804cfa4d499cc150be71b78277fd902fd8baed",
                      "Gateway": "172.17.0.1",
                      "IPAddress": "172.17.0.2",
                      "IPPrefixLen": 16,
                      "IPv6Gateway": "",
                      "GlobalIPv6Address": "",
                      "GlobalIPv6PrefixLen": 0,
                      "MacAddress": "02:42:ac:11:00:02",
                      "DriverOpts": null
                  }
              }
          }
      }
  ]
  [king-pan@localhost ~]$ 
  
  ```

  > $ docker attach 容器ID/名称 进入到启动的容器中

  - docker attach  只是进入到容器中
  - docker exec -it 容器ID/名称  cmd  直接执行命令并把结果返回到宿主机上

  ```shell
  [king-pan@localhost ~]$ docker ps
  CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
  a1777e6d6234        49f7960eb7e4        "/bin/sh -c 'while t…"   31 minutes ago      Up 31 minutes                           mycentos
  [king-pan@localhost ~]$ docker exec -it a1777e6d6234 ls -l /tmp
  total 4
  -rwx------. 1 root root 836 May 31 18:03 ks-script-3QMvMi
  -rw-------. 1 root root   0 May 31 18:01 yum.log
  [king-pan@localhost ~]$ docker attach a1777e6d6234
  hello centos
  hello centos
  hello centos
  ^Chello centos
  hello centos
  ^Chello centos
  hello centos
  hello centos
  read escape sequence
  [king-pan@localhost ~]$ docker attach a1777e6d6234
  
  ```

  > $ docker cp 容器ID:容器内路径 目的主机路径   把容器中的文件拷贝到本地

  ```shell
  [king-pan@localhost ~]$ docker run -it --name mycentos centos
  [root@829afaa5024f /]# cd /tmp
  [root@829afaa5024f tmp]# ls -l
  total 4
  -rwx------. 1 root root 836 May 31 18:03 ks-script-3QMvMi
  -rw-------. 1 root root   0 May 31 18:01 yum.log
  [root@829afaa5024f tmp]# [king-pan@localhost ~]$ 
  [king-pan@localhost ~]$ ll
  总用量 0
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 公共
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 模板
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 视频
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 图片
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 文档
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 下载
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 音乐
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 桌面
  [king-pan@localhost ~]$ docker cp mycentos:/tmp/yum.log .
  [king-pan@localhost ~]$ ls -l
  总用量 0
  -rw-------. 1 king-pan king-pan 0 6月   1 02:01 yum.log
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 公共
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 模板
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 视频
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 图片
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 文档
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 下载
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 音乐
  drwxr-xr-x. 2 king-pan king-pan 6 7月  31 00:53 桌面
  
  ```

  

   

## 1.4、总结

