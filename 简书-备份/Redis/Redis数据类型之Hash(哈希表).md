## Redis数据类型之Hash(哈希表)

KV模式不变,但V是一个键值对.

### Hash(哈希Map)常用命令

#### 设置字段值

> **hset key field value**

将哈希表key中的字段field设值为value

```
127.0.0.1:6379> hset user name king
(integer) 1
```

#### 获取字段值

获取哈希表key中字段field的值

> **hget key field**

```
127.0.0.1:6379> hget user name
"king"
```


#### 设置多个字段值

> **hmset key field1 v1 field2 v2 ...**

同时设置多个field-value键值对

```
127.0.0.1:6379> hmset customer id 1 name zhangsan age 25 dept  it-developer
OK
```

#### 获取多个字段值

> **hmget key field1 field2 field3**

```
127.0.0.1:6379> hmget customer id name age dept
1) "1"
2) "zhangsan"
3) "25"
4) "it-developer"
```

#### 获取哈希表中所有字段-值

> **hgetall key**

```
127.0.0.1:6379> hgetall customer
1) "id"
2) "1"
3) "name"
4) "zhangsan"
5) "age"
6) "25"
7) "dept"
8) "it-developer"
```

#### 删除哈希表中某个字段

> **hdel key field**

```
127.0.0.1:6379> hgetall customer
1) "id"
2) "1"
3) "name"
4) "zhangsan"
5) "age"
6) "25"
7) "dept"
8) "it-developer"
127.0.0.1:6379> hdel customer dept
(integer) 1
127.0.0.1:6379> hgetall customer
1) "id"
2) "1"
3) "name"
4) "zhangsan"
5) "age"
6) "25"
```

#### 查看哈希表中key的键值对个数

> **hlen key**

```
127.0.0.1:6379> hgetall customer
1) "id"
2) "1"
3) "name"
4) "zhangsan"
5) "age"
6) "25"
127.0.0.1:6379> hgetall user
1) "id"
2) "11"
3) "name"
4) "king"
127.0.0.1:6379> hlen customer
(integer) 3
127.0.0.1:6379> hlen user
(integer) 2
```

#### 查看哈希表中key的所有的value

> **hvals key**

```
127.0.0.1:6379> hvals user
1) "11"
2) "king"
127.0.0.1:6379> hvals customer
1) "1"
2) "zhangsan"
3) "25"
```

#### 查看哈希表中key的所有的key

> **hkeys key**

```
127.0.0.1:6379> hkeys user
1) "id"
2) "name"
127.0.0.1:6379> hkeys customer
1) "id"
2) "name"
3) "age"
```

#### 是否存在某个field

> **hexists key field**

* 存在返回1
* 不存在返回0

```
127.0.0.1:6379> hexists customer id
(integer) 1
127.0.0.1:6379> hexists customer score
(integer) 0
```


#### field值增加整数

> **hincrby key field(field必须是数字) int_value**

```
127.0.0.1:6379> hget customer age
"29"
127.0.0.1:6379> hincrby customer age 2
(integer) 31
127.0.0.1:6379> hincrby customer age 2
(integer) 33
```

#### field值增加小数

> **hincrbyfloat key field float_value**

```
127.0.0.1:6379> hset customer score 88.5
(integer) 1
127.0.0.1:6379> hincrbyfloat customer score 10.5
"99"
```

#### 不存在field赋值存在不作任何操作

> **hsetnx key field value**

* 如果field不存在则设置field的值为value
* 如果field存在则忽略,不作任何操作.

```
127.0.0.1:6379> hsetnx customer dept it
(integer) 1
127.0.0.1:6379> hsetnx customer dept it-2
(integer) 0
```
