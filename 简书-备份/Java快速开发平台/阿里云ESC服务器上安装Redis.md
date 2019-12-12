### 阿里云ESC服务器上安装Redis

#### 安装包

```
redis-3.2.9.tar.gz
```

#### 编译redis

```
[root@iz8vbesbzj30ty24cvijhvz local]# cd redis-3.2.9/
[root@iz8vbesbzj30ty24cvijhvz redis-3.2.9]# make test
......

\o/ All tests passed without errors!

Cleanup: may take some time... OK
make[1]: Leaving directory `/usr/local/redis-3.2.9/src'
```

其他他们错误信息，按照提示按照软件就OK了.

```
\o/ All tests passed without errors!

Cleanup: may take some time... OK
``

#### 启动redis

```
[root@iz8vbesbzj30ty24cvijhvz redis-3.2.9]# cd src
[root@iz8vbesbzj30ty24cvijhvz src]# ./redis-server ../redis.conf 
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 3.2.9 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6379
 |    `-._   `._    /     _.-'    |     PID: 2035
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

2035:M 02 Jul 17:51:47.094 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
2035:M 02 Jul 17:51:47.094 # Server started, Redis version 3.2.9
2035:M 02 Jul 17:51:47.094 # WARNING overcommit_memory is set to 0! Background save may fail under low memory condition. To fix this issue add 'vm.overcommit_memory = 1' to /etc/sysctl.conf and then reboot or run the command 'sysctl vm.overcommit_memory=1' for this to take effect.
2035:M 02 Jul 17:51:47.094 # WARNING you have Transparent Huge Pages (THP) support enabled in your kernel. This will create latency and memory usage issues with Redis. To fix this issue run the command 'echo never > /sys/kernel/mm/transparent_hugepage/enabled' as root, and add it to your /etc/rc.local in order to retain the setting after a reboot. Redis must be restarted after THP is disabled.
2035:M 02 Jul 17:51:47.094 * The server is now ready to accept connections on port 6379
```

#### 修改配置文件

* bind 主机IP 远程连接
* daemonize yes 默认后台启动

```
[root@iz8vbesbzj30ty24cvijhvz src]# cd ..
[root@iz8vbesbzj30ty24cvijhvz redis-3.2.9]# cp redis.conf redis.conf.bak
[root@iz8vbesbzj30ty24cvijhvz redis-3.2.9]# vim redis.conf
# Redis configuration file example.
#
# Note that in order to read the configuration file, Redis must be
# started with the file path as first argument:
#
# ./redis-server /path/to/redis.conf
......
bind 主机IP
......
daemonize yes
.....
"redis.conf" 1052L, 46699C written    
```



```
[root@iz8vbesbzj30ty24cvijhvz redis-3.2.9]# cd src
[root@iz8vbesbzj30ty24cvijhvz src]# ./redis-server ../redis.conf
[root@iz8vbesbzj30ty24cvijhvz src]# 
```

> **错误信息**

```
You need tcl 8.5 or newer in order to run the Redis test
```

安装tcl8.6

```
cd /usr/local
wget http://downloads.sourceforge.net/tcl/tcl8.6.1-src.tar.gz  
sudo tar xzvf tcl8.6.1-src.tar.gz  -C /usr/local/  
cd  /usr/local/tcl8.6.1/unix/  
sudo ./configure  
sudo make  
sudo make install   
```
