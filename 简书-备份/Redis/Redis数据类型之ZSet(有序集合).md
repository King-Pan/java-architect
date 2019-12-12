## Redis数据类型之ZSet(有序集合)


在set基础上，加一个score值。
之前set是k1 v1 v2 v3，
现在zset是k1 score1 v1 score2 v2

### ZSet(有序集合)常用命令


#### 向有序集合中添加一个或多个成员,或者更新已有成员的分数

> **zadd key score1 member1 [score2 member2 ...]**

```
127.0.0.1:6379> zadd zset01 60 v1 70 v2 80 v3 90 v4 100 v5
(integer) 5
```

#### 查看有序集合中的元素或者withscores

> **zrange zset01 start end [withscores]**

```
127.0.0.1:6379> zrange zset01 0 -1
1) "v1"
2) "v2"
3) "v3"
4) "v4"
5) "v5"
127.0.0.1:6379> zrange zset01 0 -1 withscores
 1) "v1"
 2) "60"
 3) "v2"
 4) "70"
 5) "v3"
 6) "80"
 7) "v4"
 8) "90"
 9) "v5"
10) "100"
```

#### 有序集合分数之间的成员

> **zrangebyscore zset01 start_score end_score**

```
127.0.0.1:6379> zrangebyscore zset01 60 90
1) "v1"
2) "v2"
3) "v3"
4) "v4"
```

```
127.0.0.1:6379> zrangebyscore zset01 60 (90
1) "v1"
2) "v2"
3) "v3"
127.0.0.1:6379> zrangebyscore zset01 (60 (90
1) "v2"
2) "v3"
```

```
127.0.0.1:6379> zrangebyscore zset01 60 90 limit 0 2
1) "v1"
2) "v2"
127.0.0.1:6379> zrangebyscore zset01 60 90 limit 2 2
1) "v3"
2) "v4"
```

#### 删除有序集合的成员

> **zrem key value**

```
127.0.0.1:6379> zrem zset01 v5
(integer) 1
127.0.0.1:6379> zrange zset01 0 -1 withscores
1) "v1"
2) "60"
3) "v2"
4) "70"
5) "v3"
6) "80"
7) "v4"
8) "90"
```

#### 统计有序集合成员个数

> **zcard key**

```
127.0.0.1:6379> zcard zset01
(integer) 4
```

#### 统计有序集合分数范围内的成员个数

> **zcount key start_score end_score**

```
127.0.0.1:6379> zcount zset01 60 80
(integer) 3
127.0.0.1:6379> zcount zset01 60 (70
(integer) 1
```

#### 返回指定成员的索引

> **zrank key value**

```
127.0.0.1:6379> zrank zset01 v4
(integer) 3
127.0.0.1:6379> zrank zset01 v1
(integer) 0
```

#### 获取有序集合成员的score

> **zscore key value**

```
127.0.0.1:6379> zscore zset01 v1
"60"
127.0.0.1:6379> zscore zset01 v3
"80"
```

#### 有序集合成员分数从高到低排序

> **zrevrange key start end**

```
127.0.0.1:6379> zrevrange zset01 0 -1
1) "v4"
2) "v3"
3) "v2"
4) "v1"
```

```
127.0.0.1:6379> zrevrange zset01 0 -1 withscores
1) "v4"
2) "90"
3) "v3"
4) "80"
5) "v2"
6) "70"
7) "v1"
8) "60"
```

####  分数从高到低返回有序集合的元素


> **zrevrangebyscore  key 结束score(高分) 开始score(低分)**

```
127.0.0.1:6379> zrevrangebyscore zset01 90 60
1) "v4"
2) "v3"
3) "v2"
4) "v1"
127.0.0.1:6379> zrevrangebyscore zset01 90 80
1) "v4"
2) "v3"
```


