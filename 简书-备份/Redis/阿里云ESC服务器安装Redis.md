# 阿里云ESC服务器安装Redis

## 1. 安装redis

> 详细步骤可以参考官方文档: https://redis.io/download

### 1.1 下载软件包

> 使用wget命令下载redis 包

```
$ wget http://download.redis.io/releases/redis-4.0.8.tar.gz
```


### 1.2 解压软件包

```
 $ tar xzf redis-4.0.8.tar.gz
```

### 1.3 建立软连接

```
$ ln -s redis-4.0.8 redis
```

### 1.4 进入redis目录

```
$ cd redis
```

### 1.5 编译redis

```
> $ make
```

### 1.6 安装redis

```
$ make install
```

> 安装完成后可以直接在系统变量中使用redis相关命令

## 2. 设置redis

### 2.1 备份redis.conf文件

```
$ cd ~/redis
$ mkdir /etc/redis
$ cp redis.conf /etc/redis/redis.conf
$ cp /etc/redis/redis.conf /etc/redis/redis.conf.bak
```

后面的操作都是对/etc/redis/redis.conf文件进行修改

```
vim /etc/redis/redis.conf
```


### 2.2 设置redis为后台进程

```
daemonize no 改成 daemonize yes
```

> 设置redis可以一直在后台运行，以守护进程方式运行，
即关闭SSH工具程序也在运行。 daemonize no 改成 daemonize yes


### 2.3 修改redis加载备份文件路径

```
dir ./ 改成 dir /usr/local/redis/
```

> 默认的话redis-server启动时会在当前目录生成或读取dump.rdb，
可以指定生成的路径 dir ./ 改成 dir /usr/local/redis/

### 2.4 启动AOF

```
appendonly no 改成 appendonly yes
```

> 指定是否在每次更新操作后进行日志记录,
Redis在默认情况下是异步的把数据写入磁盘,如果不开启，
可能会在断电时导致一段时间内的数据丢失。 
因为 redis本身同步数据文件是按上面save条件来同步的，
所以有的数据会在一段时间内只存在于内存中。默认为no 
appendonly no 改成 appendonly yes 

### 2.5 设置外网绑定

```
bind 127.0.0.1 改成 bind 0.0.0.0 
```

> 默认redis是只能内网127.0.0.0访问,如果想外网访问需要修改绑定的地
址,bind 127.0.0.1 改成 bind 0.0.0.0 改成任何IP都能访问的，当然这里也可以绑定单独的IP


>## 注意： 阿里云ESC服务器不需要其他的设置就可以远程连接redis，亲测可用

### 2.6 修改保护模式

```
protected-mode yes 改成 protected-mode no
```

> 修改保护模式，不修改保护模式也是只能内网访问的
protected-mode yes 改成 protected-mode no

### 2.7 设置密码

```
将"# requirepass foobared" 取掉注释改成 requirepass 123456
```

> 设置redis登录密码为123456

## 3. 系统设置

### 3.1 开机启动

```
$ cd ~/reids/utils
$ cp redis_init_script /etc/init.d/redis
$ vim /etc/init.d/redis
```

#### 3.1.1 /etc/init.d/redis内容

```
#!/bin/sh
# chkconfig: 2345 10 90  
# description: Start and Stop redis



#
# Simple Redis init.d script conceived to work on Linux systems
# as it does use of the /proc filesystem.

REDISPORT=6379
EXEC=/usr/local/bin/redis-server
CLIEXEC=/usr/local/bin/redis-cli

PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF=/etc/redis/redis.conf
AUTH="123456"

case "$1" in
    start)
        if [ -f $PIDFILE ]
        then
                echo "$PIDFILE exists, process is already running or crashed"
        else
                echo "Starting Redis server..."
                $EXEC $CONF
        fi
        ;;
    stop)
        if [ ! -f $PIDFILE ]
        then
                echo "$PIDFILE does not exist, process is not running"
        else
                PID=$(cat $PIDFILE)
                echo "Stopping ..."
                $CLIEXEC -p $REDISPORT -a $AUTH shutdown
                while [ -x /proc/${PID} ]
                do
                    echo "Waiting for Redis to shutdown ..."
                    sleep 1
                done
                echo "Redis stopped"
        fi
        ;;
    *)
        echo "Please use start or stop as first argument"
        ;;
esac
```

##### 3.1.2 需要修改的参数

```
## 端口
REDISPORT=6379   
## 路径
EXEC=/usr/local/bin/redis-server
## 路径
CLIEXEC=/usr/local/bin/redis-cli

PIDFILE=/var/run/redis_${REDISPORT}.pid
## 配置文件路径
CONF=/etc/redis/redis.conf
## 密码，没有设置密码的可以不用设置
AUTH="123456"
```

##### 3.1.3 设置密码的例外配置


> 有密码

```
$CLIEXEC -p $REDISPORT -a $AUTH shutdown
```

> 没密码

```
$CLIEXEC -p $REDISPORT shutdown
```

#### 3.2 设置系统命令

```
$ chmod 755 /etc/init.d/redis
$ chkconfig redis on
$ reboot
```

> 第一行： 设置redis可执行
> 第二行:  设置开机启动
> 第三行:  服务器重启(慎重)


## 4. redis命令

### 4.1 redis启动

> 单独启动redis

```
$ redis-server /etc/redis/redis.conf &
```

> 指定配置文件启动redis

```
$ redis-server /etc/redis/redis.conf &
```

> 系统命令启动redis

```
$ service redis start
```

### 4.2 redis关闭

> 单独关闭redis

没有密码

```
$ redis-cli shutdown
```
有密码
```
$ redis-cli -a 密码 shutdown
```

> 系统命令关闭redis

需要在/etc/init.d/redis文件中配置密码,请参考3.1.3

```
$ service redis stop
```

### 4.3 redis连接

```
$ redis-cli -h host -p port -a 密码 
```

## 5. 测试

### 5.1 本地测试

```
$ redis-cli -a 123456
127.0.0.1:6379> keys *
1) "foo"
127.0.0.1:6379> set k1 v1
OK
127.0.0.1:6379> get k1
"v1"
127.0.0.1:6379> 
```

### 5.2 远程测试

```
$ redis-cli -h host -p port -a 123456
host:port> keys *
1) "foo"
host:port> set k1 v1
OK
host:port> get k1
"v1"
host:port> 
```
