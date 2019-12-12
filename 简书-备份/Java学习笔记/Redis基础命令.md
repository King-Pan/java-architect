# Redis 基本命令﻿

## list类型操作﻿﻿
﻿﻿
> **向列表两端添加元素**﻿﻿

```﻿
lpush key value [value ...]﻿
rpush key value [value ...]﻿
```﻿﻿﻿

列表左侧添加元素﻿﻿

```﻿
192.168.164.128:6379> lpush list:1 2 4 6﻿
(integer) 3﻿
```﻿﻿

列表右侧添加元素﻿﻿

```﻿
192.168.164.128:6379> rpush list:1 2 7 9﻿
(integer) 6﻿
```﻿﻿

> **查询列表元素**﻿﻿﻿

```﻿
lrange key start stop﻿
```﻿﻿

查询list所有元素﻿﻿

```﻿
lrange list:1 0 -1﻿
```﻿﻿

结果:﻿﻿

```﻿
192.168.164.128:6379> lrange list:1 0 -1﻿
1) "6"﻿
2) "4"﻿
3) "2"﻿
4) "2"﻿
5) "7"﻿
6) "9"﻿
```﻿﻿

> **从列表两端弹出元素**﻿﻿

```﻿
lpop key﻿
rpop key﻿
```﻿﻿

```﻿
192.168.164.128:6379> lpop list:1﻿
"6"﻿
192.168.164.128:6379> rpop list:1﻿
"9"﻿
```﻿﻿

* 注意lpop,rpop返回值为弹出元素﻿
﻿﻿﻿

## Set无序集合﻿﻿

### set添加元素﻿﻿

向名称为 key 的 set 中添加元素﻿﻿

```﻿
192.168.164.128:6379> sadd set1 1 2 3﻿
(integer) 3﻿
192.168.164.128:6379> sadd set1 2 4 5﻿
(integer) 2﻿
```﻿﻿

### 查看Set中的所有元素﻿﻿﻿

```﻿
192.168.164.128:6379> smembers set1﻿
1) "1"﻿
2) "2"﻿
3) "3"﻿
4) "4"﻿
5) "5"﻿
```﻿﻿

### 删除Set中的元素﻿﻿

删除名称为 key 的 set 中的元素 member﻿﻿

```﻿
srem key member﻿
```﻿﻿

```﻿
192.168.164.128:6379> srem set1 3﻿
(integer) 1﻿
192.168.164.128:6379> smembers set1﻿
1) "1"﻿
2) "2"﻿
3) "4"﻿
4) "5"﻿
```﻿﻿

### 判断元素是否存在﻿﻿

判断该元素是否存在于集合中,存在返回1,不存在返回0﻿﻿

```﻿
sismember key member﻿
```﻿﻿

```﻿
192.168.164.128:6379> sismember set1 1﻿
(integer) 1﻿
192.168.164.128:6379> sismember set1 3﻿
(integer) 0﻿
```﻿﻿

### 集合的运算﻿﻿﻿﻿

> **准备材料**﻿﻿

```﻿
192.168.164.128:6379> sadd skey1 1 2 3 4﻿
(integer) 4﻿
192.168.164.128:6379> sadd skey2 3 4 5 6﻿
(integer) 4﻿
192.168.164.128:6379> smembers skey1﻿
1) "1"﻿
2) "2"﻿
3) "3"﻿
4) "4"﻿
192.168.164.128:6379> smembers skey2﻿
1) "3"﻿
2) "4"﻿
3) "5"﻿
4) "6"﻿
192.168.164.128:6379>﻿
```﻿﻿﻿

#### 集合的差集﻿﻿

A-B  A中有的B中没有的﻿﻿

```﻿
sdiff key1 key2﻿
```﻿﻿

```﻿
192.168.164.128:6379> sdiff skey1 skey2﻿
1) "1"﻿
2) "2"﻿
192.168.164.128:6379> sdiff skey2 skey1﻿
1) "5"﻿
2) "6"﻿
192.168.164.128:6379>﻿
```﻿﻿﻿

#### 集合的交集∩﻿﻿

A ∩ B  A和B中重复的部分﻿﻿

```﻿
sinter key1 key2﻿
```﻿﻿

```﻿
192.168.164.128:6379> sinter skey1 skey2﻿
1) "3"﻿
2) "4"﻿
192.168.164.128:6379>﻿
```﻿﻿﻿

#### 集合的并集∪﻿﻿﻿

A∪B    A和B中的去掉重复的全部元素﻿

```﻿
sunion skey1 skey2﻿
```﻿﻿

```﻿
192.168.164.128:6379> sunion skey1 skey2﻿
1) "1"﻿
2) "2"﻿
3) "3"﻿
4) "4"﻿
5) "5"﻿
6) "6"﻿
```﻿﻿

## Sorted Set有序集合﻿﻿﻿

### Sorted Set 添加元素﻿﻿

格式: zdd 集合名称 索引值 元素﻿﻿

```﻿
zadd key score member﻿
```﻿﻿

```﻿
192.168.164.128:6379> zadd sortset 1 one﻿
(integer) 1﻿
192.168.164.128:6379> zadd sortset 2 two﻿
(integer) 1﻿
192.168.164.128:6379> zadd sortset 3 two﻿
(integer) 0﻿
192.168.164.128:6379> zrange sortset 0 -1 withscores﻿
1) "one"﻿
2) "1"﻿
3) "two"﻿
4) "3"﻿
192.168.164.128:6379>﻿
```﻿﻿

### 查询有序集合中的元素﻿﻿

```﻿
zrange key start stop [withscores]﻿
```
﻿﻿
```﻿
192.168.164.128:6379> zrange sortset 0 -1 withscores﻿
1) "one"﻿
2) "1"﻿
3) "two"﻿
4) "3"﻿
```﻿﻿

### 删除有序集合中的元素﻿﻿

```﻿
zrem key member﻿
```﻿﻿

```﻿
192.168.164.128:6379> zrem sortset two﻿
(integer) 1﻿
192.168.164.128:6379> zrange sortset 0 -1﻿
1) "one"﻿
```﻿﻿

### 有序集合的score increment﻿﻿

如果在名称为 key 的 zset 中已经存在元素 member，则该元素的 score 增加 increment；否则﻿

向集合中添加该元素，其 score 的值为 increment﻿﻿
﻿﻿﻿

```﻿
192.168.164.128:6379> zadd zset1 1 one﻿
(integer) 1﻿
192.168.164.128:6379> zadd zset 1 two﻿
(integer) 1﻿
192.168.164.128:6379> zincrby zset 1 two﻿
"2"﻿
192.168.164.128:6379> zrange zset 0 -1 withscores﻿
1) "two"﻿
2) "2"﻿
```﻿﻿﻿﻿﻿﻿﻿﻿
