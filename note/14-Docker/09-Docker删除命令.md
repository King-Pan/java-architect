# Docker删除名



## Docker删除镜像

```sh
docker rmi repository:tag/imageId
```

## Docker删除容器

### Docker删除停止运行的容器

```sh
docker rm container_name/container_id
```

### Docker删除正在运行的容器

```sh
docker rm -f container_name/container_id
```

