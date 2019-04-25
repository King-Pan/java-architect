# 2、Redis API的理解和使用

## 2.1、通用命令

> keys [pattern]（不推荐生产中使用）

查看redis中所有的键

```shell
keys * # 打印所有的key
keys he* # 查询以he开头的key
keys he[h-l]* #查询以he开头，第三个字符在h-l中的key

```



> dbsize

查询redis数据库的大小

```shell
127.0.0.1:6379> dbsize
(integer) 2
```

> exists key

判断key是否存在

```shell
127.0.0.1:6379> get a
(nil)
127.0.0.1:6379> exists a
(integer) 0
127.0.0.1:6379> set a b
OK
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> exists a
(integer) 1
```



> del key

删除key-value

```shell
127.0.0.1:6379> get a
(nil)
127.0.0.1:6379> exists a
(integer) 0
127.0.0.1:6379> set a b
OK
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> exists a
(integer) 1
127.0.0.1:6379> del a
(integer) 1
127.0.0.1:6379> get a
(nil)
127.0.0.1:6379> exists a
(integer) 0
```

> expire key seconds

key在多少秒后过期

```shell
127.0.0.1:6379> exists a
(integer) 0
127.0.0.1:6379> set a b
OK
127.0.0.1:6379> expire a 10
(integer) 1
127.0.0.1:6379> ttl a
(integer) 7
127.0.0.1:6379> ttl a
(integer) 6
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> ttl a
(integer) -2
127.0.0.1:6379> get a
(nil)
```

> ttl key 

查看key剩余过期时间

```shell
127.0.0.1:6379> exists a
(integer) 0
127.0.0.1:6379> set a b
OK
127.0.0.1:6379> expire a 10
(integer) 1
127.0.0.1:6379> ttl a
(integer) 7
127.0.0.1:6379> ttl a
(integer) 6
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> get a
"b"
127.0.0.1:6379> ttl a
(integer) -2
127.0.0.1:6379> get a
(nil)
```

* 未过期显示剩余秒数
* 没有设置过期的显示-1
* 过期显示-2

> persist key 

去掉key的过期时间，使得key不过期

```shell
127.0.0.1:6379> set hello world
OK
127.0.0.1:6379> expire hello 20
(integer) 1
127.0.0.1:6379> ttl hello
(integer) 15
127.0.0.1:6379> persist hello
(integer) 1
127.0.0.1:6379> ttl hello
(integer) -1
127.0.0.1:6379> get hello
"world"
```

> type key

返回key的类型



### 2.1.1、命令时间复杂度

| 命令    | 命令作用              | 时间复杂度 |
| ------- | --------------------- | ---------- |
| keys    | 通过正则表达式查看key | O(n)       |
| dbsize  | 数据库大小            | O(1)       |
| del     | 删除key-value         | O(1)       |
| exists  | 判断key是否存在       | O(1)       |
| expire  | 对key进行过期设置     | O(1)       |
| ttl     | 查看key的剩余过期时间 | O(1)       |
| persist | 清除key的过期设置     | O(1)       |
| type    | 查看key的数据类型     | O(1)       |



## 2.2、数据结构



## 2.3、Redis单线程



### 2.3.1、Redis单线程简介

​    单线程指的是网络请求模块使用了一个线程（所以不需考虑并发安全性），即一个线程处理所有网络请求，其他模块仍用了多个线程。



### 2.3.1、Redis单线程为什么这么快

* 纯内存
* 非阻塞IO
* 单线程避免线程切换和竞态消耗

### 2.3.3、Redis单线程需要注意什么

- 一次只执行一条命令
- 拒绝长(慢)命令（例如keys、flushall、flushdb、slow lua script、mutil\exec、operate big value(collection)）

## 2.4、Redis字符串数据类型

### 2.4.1、字符串键值结构

key 是字符串，value可以是字符串，数字json串等...

value是二进制，value不能超过512MB，一般推荐100k以内

### 2.4.2、场景

> 缓存

主要功能缓存



> 计数器

incr函数 key自增1，如果key不存在自增后get(key)=1

incr userid:pageview   单线程：无竞争

decr key

自减1

incrby 增加n

decrby 减少n

> 分布式锁

sss

> 其他

### 2.4.3、实战

> 缓存系统

```java
public VideoInfo get(Long id){
    String redisKey = redisPrefix + id;
    VideoInfo videoInfo = redis.get(redisKey);
    if(videoInfo == null){
        videoInfo = mysql.get(id);
        //序列号
        redis.set(redisKey,serialize(videoInfo));
    }
}
```



> 分布式ID生成器

使用了redis单线程原子性的特性 incr id



### 2.4.4 常用API

> set key value

 不管key是否存在，都设置

```shell
127.0.0.1:6379> exists setkey
(integer) 0
127.0.0.1:6379> set setkey v1
OK
127.0.0.1:6379> get setkey
"v1"
127.0.0.1:6379> set setkey v2
OK
127.0.0.1:6379> get setkey
"v2"
```

> setnx key value 

key不存在才设置

```shell
127.0.0.1:6379> setnx setkey v3
(integer) 0
127.0.0.1:6379> get setkey
"v2"
127.0.0.1:6379> setnx setkey1 v3
(integer) 1
127.0.0.1:6379> get setkey1
"v3"
```

> set key value nx|xx

set key value nx 等同于setnx key value

set key value xx key存在，才设置

```shell
127.0.0.1:6379> set pingpang pg xx
(nil)
127.0.0.1:6379> set pingpang v1
OK
127.0.0.1:6379> get pingpang
"v1"
127.0.0.1:6379> set pingpang v2 xx
OK
127.0.0.1:6379> get pingpang
"v2"
```



> mset key1 v1 k2 v2

设置多个key-value

```shell
127.0.0.1:6379> mget h1 h2 h3 h4
1) "v1"
2) "v2"
3) "v3"
4) (nil)
```

> mget k1 k2 k3

获取多个key的值

```shell
127.0.0.1:6379> mget php java j1
1) "laji"
2) "xxx"
3) "v2"
```

> getset key newvalue

设置key新的value，返回旧的value

```shell
127.0.0.1:6379> get php 
"good"
127.0.0.1:6379> getset php laji
"good"
127.0.0.1:6379> get php
"laji"
```

> append key value

将value追加到旧的value上,如果key不存在直接set key value，并且返回value的长度

```shell
127.0.0.1:6379> get key
(nil)
127.0.0.1:6379> append key value
(integer) 5
127.0.0.1:6379> append key -001
(integer) 9
```

> strlen key

计算value字符串的长度(注意中文)

```shell
127.0.0.1:6379> strlen hello
(integer) 5
127.0.0.1:6379> set name 我是中国人
OK
127.0.0.1:6379> get name
"\xe6\x88\x91\xe6\x98\xaf\xe4\xb8\xad\xe5\x9b\xbd\xe4\xba\xba"
127.0.0.1:6379> strlen name
(integer) 15
```



> incr 自增1

使得数字自增1，如果key不存在，set key=1并返回

```shell
127.0.0.1:6379> exists id
(integer) 0
127.0.0.1:6379> incr id
(integer) 1
127.0.0.1:6379> get id
"1"
127.0.0.1:6379> incr id
(integer) 2
127.0.0.1:6379> get id
"2"
```

> incrby key number

设置步长自增

```shell
127.0.0.1:6379> get id
"2"
127.0.0.1:6379> incrby id 2
(integer) 4
127.0.0.1:6379> get id
"4"
```

> decr key

对key的value自减1，如果没有key，则设置set key =-1 然后返回-1

```shell
127.0.0.1:6379> decr nb
(integer) -1
127.0.0.1:6379> decr nb
(integer) -2
```

> decrby key number

对key的value自减步长

```shell
127.0.0.1:6379> decr nb
(integer) -1
127.0.0.1:6379> decr nb
(integer) -2
127.0.0.1:6379> decrby nb 10
(integer) -12
```

> incrbyfloat key 3.5

对key对应值+3.5

```shell
127.0.0.1:6379> incrby id 10
(integer) 10
127.0.0.1:6379> incrbyfloat id 22.8
"32.8"
```



> getrange key start end

获取key指定下标值，下标是从0开始

```shell
127.0.0.1:6379> set hello helloworld
OK
127.0.0.1:6379> strlen hello
(integer) 10
127.0.0.1:6379> getrange hello 0 4
"hello"
```

> setrang key index value

```shell
127.0.0.1:6379> get hello
"helloworld"
127.0.0.1:6379> setrange hello 10 haha
(integer) 14
127.0.0.1:6379> get hello
"helloworldhaha"
127.0.0.1:6379> setrange hello 0 F
(integer) 14
127.0.0.1:6379> get hello
"Felloworldhaha"
```

### 2.4.5 字符串API时间复杂度

| 命令                                                 | 含义                         | 时间复杂度 |
| ---------------------------------------------------- | ---------------------------- | ---------- |
| set key value                                        | 设置key-value                | o(1)       |
| get key                                              | 获取key-value                | o(1)       |
| del key                                              | 删除key-value                | o(1)       |
| setnx setxx                                          | 根据key是否存在设置key-value | o(1)       |
| incr、incrby、incrbyfloat、decr、decrby、decrbyfloat | 计数                         | o(1)       |
| mget、mset                                           | 起来操作key-value            | o(n)       |



## 2.5、Redis list数据类型

### 2.5.1、List数据结构

​	List是一个有序的、可以重复的列表

​     <https://processon.com/diagraming/5cb739ade4b0bab9095c969a>

<https://www.cnblogs.com/knowledgesea/p/4972771.html>



> 提示

* Lpush + lpop = stack 后进先出
* lpush + rpop = queue 先进先出
* lpush + ltrim = capped collection 固定集合
* lpush + brpop = message queue 阻塞队列

### 2.5.2、场景

### 2.5.3、实战

TimeLine、微博评论、朋友圈评论、论坛回复等

### 2.5.4、常用API

> lpush key value

向list列表左侧添加元素

```shell
127.0.0.1:6379> lpush list 1
(integer) 1
```

> rpush key value

向List列表右侧添加元素

```shell
127.0.0.1:6379> rpush list 2
(integer) 2
```

> lrange key start end(从0开始,-1代表所有的list项)

通过范围查询List列表中的元素

```shell
127.0.0.1:6379> lrange list 0 1
1) "1"
2) "2"
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "2"
127.0.0.1:6379> lpush list 0
(integer) 3
127.0.0.1:6379> lrange list 0 -1
1) "0"
2) "1"
3) "2"
127.0.0.1:6379> lrange list 0 1
1) "0"
2) "1"
```

> lpop key

从List列表左侧移除一个元素，并且返回该元素

```shell
127.0.0.1:6379> lrange list 0 -1
1) "0"
2) "1"
3) "2"
127.0.0.1:6379> lpop list
"0"
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "2"
```

> rpop key

从List列表右侧移除一个元素，并且返回该元素

```shell
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "2"
127.0.0.1:6379> rpop list
"2"
127.0.0.1:6379> lrange list 0 -1
1) "1"
```

> lrange key start end:时间复杂度O(n)

获取指定索引范围内的元素，0 -1查看全部

```shell
127.0.0.1:6379> rpush list a b c d e
(integer) 6
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "a"
3) "b"
4) "c"
5) "d"
6) "e"
```

> ltrim key start end: 时间复杂度:O(n)

保留列表中索引范围内的元素，删除其他元素

```shell
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "a"
3) "b"
4) "c"
5) "d"
6) "e"
127.0.0.1:6379> ltrim list 0 4
OK
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "a"
3) "b"
4) "c"
5) "d"

```

> brpop key

从列表的右侧弹出一个元素，如果没有元素则阻塞

timeout是阻塞超时时间，timeout=0 y一直阻塞直到从右侧弹出元素

***使用lpush + brpop 实现阻塞消息队列***

```shell
127.0.0.1:6379> lpush blist 1
(integer) 1
127.0.0.1:6379> lrange blist 0 -1
1) "1"
127.0.0.1:6379> brpop blist 0
1) "blist"
2) "1"
127.0.0.1:6379> brpop blist 0  # 阻塞在这儿，直到列表中有元素，阻塞消息队列的实现
```

> linsert key before|after value newValue

在value前后插入新的元素: 时间复杂度o(n)

```shell
127.0.0.1:6379> rpush l1 a b c d
(integer) 4
127.0.0.1:6379> lrange l1 0 -1
1) "a"
2) "b"
3) "c"
4) "d"
127.0.0.1:6379> linsert l1 before b java
(integer) 5
127.0.0.1:6379> lrange l1 0 -1
1) "a"
2) "java"
3) "b"
4) "c"
5) "d"
127.0.0.1:6379> linsert l1 after b php
(integer) 6
127.0.0.1:6379> lrange l1 0 -1
1) "a"
2) "java"
3) "b"
4) "php"
5) "c"
6) "d"
```

> lrem key count value: 时间复杂度O(n)

* count = 0 删除所有value相等的项
* count > 0 从左到右最多删除count个value项
* count <0 从右向左最多删除count个value项

```sh
127.0.0.1:6379> rpush l2 1 2 3 3 2 3 4 6 3 2
(integer) 10
127.0.0.1:6379> lrange l2 0 -1
 1) "1"
 2) "2"
 3) "3"
 4) "3"
 5) "2"
 6) "3"
 7) "4"
 8) "6"
 9) "3"
10) "2"
127.0.0.1:6379> lrem l2 2 3
(integer) 2
127.0.0.1:6379> lrange l2 0 -1
1) "1"
2) "2"
3) "2"
4) "3"
5) "4"
6) "6"
7) "3"
8) "2"
127.0.0.1:6379> rpush l3 1 2 3 3 2 3 4 6 3 2
(integer) 10
127.0.0.1:6379> lrem l3 0 3
(integer) 4
127.0.0.1:6379> lrange l3 0 -1
1) "1"
2) "2"
3) "2"
4) "4"
5) "6"
6) "2"
127.0.0.1:6379> rpush l4 1 2 3 3 2 3 4 6 3 2
(integer) 10
127.0.0.1:6379> lrem l4 -3 3
(integer) 3
127.0.0.1:6379> lrange l4 0 -1
1) "1"
2) "2"
3) "3"
4) "2"
5) "4"
6) "6"
7) "2"
```

> lindex key index:时间复杂度O(n)

获取指定索引下的元素

```shell
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "a"
3) "b"
4) "c"
5) "d"
127.0.0.1:6379> lindex list 1
"a"
127.0.0.1:6379> lindex list 3
"c"

```

> llen key:时间复杂度O(1)

获取列表的长度

```shell
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "a"
3) "b"
4) "c"
5) "d"
127.0.0.1:6379> llen list
(integer) 5
```

> lset key index newValue: 时间复杂度O(n)

设置列表指定索引的值为newValue

```shell
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "a"
3) "b"
4) "c"
5) "d"
127.0.0.1:6379> lset list 1 java
OK
127.0.0.1:6379> lrange list 0 -1
1) "1"
2) "java"
3) "b"
4) "c"
5) "d"

```

### 2.5.5、List API时间复杂度



## 2.6、Redis Hash数据类型

### 2.6.1、Hash哈希特点

哈希键值结构



key-value     value 是  field1 value1 field2 value2

对应java中的Map<String,Map<String,String>>结构



### 2.6.2、场景



### 2.6.3、实战

### 2.6.4、常用API

> hset key field value

设置hash key对应field的value

```shell
127.0.0.1:6379> hset user:1:info age 23
(integer) 1
127.0.0.1:6379> hget user:1:info age
"23"
```

> hget key field 

获取hash key中对应field的值

```shell
127.0.0.1:6379> hset user:1:info age 23
(integer) 1
127.0.0.1:6379> hget user:1:info age
"23"
127.0.0.1:6379> hset user:1:info name king
(integer) 1
127.0.0.1:6379> hget user:1:info name
"king"
```

> hdel key field

删除hash key对应field的value

```shell
127.0.0.1:6379> hdel user:1:info age
(integer) 1
127.0.0.1:6379> hget user:1:info age
(nil)
```

> hexists key field

判断hash key是否有field

```shell
127.0.0.1:6379> hget user:1:info name
"king"
127.0.0.1:6379> hget user:1:info age
(nil)
127.0.0.1:6379> hexists user:1:info age
(integer) 0
127.0.0.1:6379> hexists user:1:info name
(integer) 1
```

> hlen key

获取hash key field的数量

```shell
127.0.0.1:6379> hlen user:1:info
(integer) 1
127.0.0.1:6379> hset user:1:info sex man
(integer) 1
127.0.0.1:6379> hlen user:1:info
(integer) 2
```

>hmget key field1 field2

获取hash key多个field的值

```shell
127.0.0.1:6379> hmget user:1:info age name sex
1) (nil)
2) "king"
3) "man"
```

> hmset key field1 value1 field2 value2 ...

设置hash key 多个field的值

```sh

```

### 2.6.5、Hash API时间复杂度

