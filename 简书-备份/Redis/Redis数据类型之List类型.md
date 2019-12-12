## Redis数据类型之List类型

**List类型(列表类型)**

Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素导列表的头部（左边）或者尾部（右边）。

它的底层实际是个链表

### List(列表类型)常用命令

#### 创建列表

**lpush左侧添加/rpush右侧添加**

> **lpush从左侧添加**


```
127.0.0.1:6379> lpush list01 1 2 3 4 5
(integer) 5
127.0.0.1:6379> lrange list01 0 -1
1) "5"
2) "4"
3) "3"
4) "2"
5) "1"
```

> **rpush从左侧添加**

```
127.0.0.1:6379> rpush list02 1 2 3 4 5
(integer) 5
127.0.0.1:6379> lrange list02 0 -1
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
```

### 查看List元素

* lrange key start end   start/end超出索引则忽略.
* lrange key 0 -1   查看list全部元素.
* lrange key 0  1   查看list 索引为0/1元素.
* lrange key 0 length+1  查看list 0 - (length-1) 的元素,超出的索引忽略.
* lrange key 1  3   查看list索引为1-3的元素. 


> **lrange key start end**

```
127.0.0.1:6379> lrange list02 0 -1
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
127.0.0.1:6379> lrange list02 0 3
1) "1"
2) "2"
3) "3"
4) "4"
127.0.0.1:6379> lrange list02 0 4
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
127.0.0.1:6379> lrange list02 0 5
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
127.0.0.1:6379> lrange list02 0 6
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
127.0.0.1:6379> lrange list02 6 9
(empty list or set)
```

### 弹出/取出list元素


> **lpop key(左侧弹出list元素)**

```
127.0.0.1:6379> lpop list01
"5"
127.0.0.1:6379>  lrange list01 0 -1
1) "4"
2) "3"
3) "2"
4) "1"
```

> **rpop key(右侧弹出list元素)**

```
127.0.0.1:6379> rpop list02
"5"
127.0.0.1:6379> lrange list02  0 -1
1) "1"
2) "2"
3) "3"
4) "4"
```

### 按照索引获取元素(从上到下)

> **lindex key index按照索引下标获得元素(从上到下)**

```
127.0.0.1:6379> lindex list01 2
"2"
127.0.0.1:6379> lrange list02 0 -1
1) "1"
2) "2"
3) "3"
4) "4"
```

### 获取List元素个数

> **llen key获取list元素个数**

```
127.0.0.1:6379> lrange list02 0 -1
1) "1"
2) "2"
3) "3"
4) "4"
127.0.0.1:6379> llen list02
(integer) 4
```

### 删除N个value值

* lrem key n value 如果没有n个value,有多少个删除多少个.
* lrem key n value 从上到下按照顺序删除.

> **lrem key n**

```
127.0.0.1:6379> lpush list03 1 1 1 2 2 2 3 3 3 4 4 4 4 5 3 4 4 5
(integer) 18
127.0.0.1:6379> llen list03
(integer) 18
127.0.0.1:6379> lrange list03 0 -1
 1) "5"
 2) "4"
 3) "4"
 4) "3"
 5) "5"
 6) "4"
 7) "4"
 8) "4"
 9) "4"
10) "3"
11) "3"
12) "3"
13) "2"
14) "2"
15) "2"
16) "1"
17) "1"
18) "1"
127.0.0.1:6379> lrem list03 4  3
(integer) 4
127.0.0.1:6379> lrange list03 0 -1
 1) "5"
 2) "4"
 3) "4"
 4) "5"
 5) "4"
 6) "4"
 7) "4"
 8) "4"
 9) "2"
10) "2"
11) "2"
12) "1"
13) "1"
14) "1"
127.0.0.1:6379> lrem list03 3 5
(integer) 2
127.0.0.1:6379> lrange list03 0 -1
 1) "4"
 2) "4"
 3) "4"
 4) "4"
 5) "4"
 6) "4"
 7) "2"
 8) "2"
 9) "2"
10) "1"
11) "1"
12) "1"
```
 
### 截取list元素并且赋值给list

* ltrim key start end 把 start end中的元素赋值给key
* ltrim key start end 是包含 start end 元素
* ltrim key start end 如果索引超出范围,则自动忽略.

> **ltrim key start end**

```
127.0.0.1:6379> lpush list04 1 2 3 4 5 6 7 8 9 0
(integer) 10
127.0.0.1:6379> lrange list04 0 -1
 1) "0"
 2) "9"
 3) "8"
 4) "7"
 5) "6"
 6) "5"
 7) "4"
 8) "3"
 9) "2"
10) "1"
127.0.0.1:6379> ltrim list04 3 5
OK
127.0.0.1:6379> lrange list04 0 -1
1) "7"
2) "6"
3) "5"
127.0.0.1:6379> ltrim list04 0 7
OK
127.0.0.1:6379> lrange list04 0 -1
1) "7"
2) "6"
3) "5"
```

### 在list元素前后添加新元素

* linsert key before/after value new_value  在value值前后添加new_value
* 从上到下找到第一个匹配
* 找不到则value 这返回-1 不作操作.

> **linsert key value new_value**

```
127.0.0.1:6379> lpush list05 x i a o m i
(integer) 6
127.0.0.1:6379> lrange list05 0 -1
1) "i"
2) "m"
3) "o"
4) "a"
5) "i"
6) "x"
127.0.0.1:6379> linsert list05 before i 1
(integer) 7
127.0.0.1:6379> linsert list05 after i 2
(integer) 8
127.0.0.1:6379> lrange list05 0 -1
1) "1"
2) "i"
3) "2"
4) "m"
5) "o"
6) "a"
7) "i"
8) "x"
127.0.0.1:6379> linsert list05 after v  100
(integer) -1
```

 
### 性能特点

1. 它是一个字符串链表，left、right都可以插入添加；
2. 如果键不存在，创建新的链表；
3. 如果键已存在，新增内容；
4. 如果值全移除，对应的键也就消失了。
5. 链表的操作无论是头和尾效率都极高，但假如是对中间元素进行操作，效率就很惨淡了。
