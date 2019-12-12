### Redis基础命令

#### 数据库操作命令

Redis 默认有16个库,默认使用0号库,通过SELECT命令可以切换数据库,数据库索引是从[0,15]

> **切换数据库**

```

127.0.0.1:6379> select 0

OK

127.0.0.1:6379> select 1

OK

```

> **清空数据库**

> **慎用清空数据库命令**

* 清空当前数据库: flushdb

* 清空所有数据库: flushall

```

127.0.0.1:6379> flushdb

OK

```

```

127.0.0.1:6379> flushall

OK

```

#### 键Key操作命令

> **查看库中的key**

* keys pattern  * 匹配任意字符 ; ? 匹配一个字符 []可以匹配[]中的任意一个字符  \x匹配字符x 用于转义字符 \? 匹配?

* keys * 查看库中所有的key

* keys a?  查看库中以a开头2个长度的key

* keys a[b-d] 可以匹配ab,ac,ad

* keys \?  可以匹配?

```

keys *

keys a?  --

```

> **查看库中key的数量**

```

127.0.0.1:6379> dbsize

(integer) 0

```

> **删除键Key**

```

127.0.0.1:6379> del a1

(integer) 0

```

> **移动key**

* 格式: move key db_index

* 把当前数据库中的key移动到其他数据库中

```

127.0.0.1:6379> move a2 2

(integer) 1

127.0.0.1:6379> select 2

OK

127.0.0.1:6379[2]> get a2

"2"

```

### Redis字符串命令

#### 设置单值

> **格式: set key value**

* key不存在时设置新增

* key存在时,替换新值

* setnx 只有在key不存在时才能设置成功.

```

127.0.0.1:6379> set str1 value1

OK

```

#### 设置不存在的key

> **格式: setnx key value**

* key不存在时设置新增

* key存在时,setnx不做任何操作

```

127.0.0.1:6379[2]> get k1

(nil)

127.0.0.1:6379[2]> set k1 1

OK

127.0.0.1:6379[2]> setnx k1 2

(integer) 0

127.0.0.1:6379[2]> get k2

(nil)

127.0.0.1:6379[2]> setnx k2 vk2

(integer) 1

```

#### 设置多个值

> **格式: mset key1 value1 key2 v2[...]**

```

127.0.0.1:6379> mset a1 1 a2 2 a3 3

OK

127.0.0.1:6379> keys *

1) "a1"

2) "a2"

3) "a3"

```

#### 设置新值返回旧值

> **格式: getset key value**

* key存在则返回旧值,set新值

* key不存在,返回空值,set新值

```

127.0.0.1:6379> get a1

"1"

127.0.0.1:6379> getset a1 value1

"1"

127.0.0.1:6379> get an

(nil)

127.0.0.1:6379> getset an valuen

(nil)

127.0.0.1:6379> get an

"valuen"

```

#### 从索引出添加字符串

> **格式: setrange key offset value**

* 对非空字符串进行setrange

```

127.0.0.1:6379> set greeting "hello world"

OK

127.0.0.1:6379> setrange greeting 6 "redis"

(integer) 11

127.0.0.1:6379> get greeting

"hello redis"

```

* 对非空字符串进行setrange,使用\x00填充

```

127.0.0.1:6379> get gt

(nil)

127.0.0.1:6379> setrange gt 4 "say hello"

(integer) 13

127.0.0.1:6379> get gt

"\x00\x00\x00\x00say hello"

```

#### 设置超时失效

> **格式: setex key seconds value**

* 设置值并且设置超时时间,单位是秒

* psetex 单位是毫米，其他相同

* 查询剩余时间使用ttl key

* -2 表示已经超时,失效状态

```

127.0.0.1:6379[2]> setex qg 100 1

OK

127.0.0.1:6379[2]> get qg

"1"

127.0.0.1:6379[2]> ttl qg

(integer) 65

127.0.0.1:6379[2]> ttl qg

(integer) 60

127.0.0.1:6379[2]> ttl qg

(integer) -2

```

#### 获取单值

> **格式: get key**

```

127.0.0.1:6379> get str1

"value1"

```

#### 获取多值

> **格式: mget key1 key2[...]**

```

127.0.0.1:6379> mget a1 a2 a3

1) "1"

2) "2"

3) "3"

```

#### 获取字符串范围值

> **格式: getrange key1 start end**

```

127.0.0.1:6379> get gt

"\x00\x00\x00\x00say hello"

127.0.0.1:6379> getrange gt 4 13

"say hello"

127.0.0.1:6379> getrange gt 2 13

"\x00\x00say hello"

```

#### 删除key

> **格式: del key**

* 删除成功返回1,删除失败返回0

```

127.0.0.1:6379> del str1

(integer) 1

```

#### 追加value

> **格式: append key value**

* 如果key存,则追加

* 如果key不存在,则执行 set key value

```

127.0.0.1:6379> append ak47 ak

(integer) 2

127.0.0.1:6379> get ak47

"ak"

127.0.0.1:6379> append ak47 47

(integer) 4

127.0.0.1:6379> get ak47

"ak47"

```

#### 数值递减1

> **格式: decr key**

* 对value值减一

```

127.0.0.1:6379> set last 100

OK

127.0.0.1:6379> decr last

(integer) 99

127.0.0.1:6379> decr last

(integer) 98

127.0.0.1:6379> decr last

(integer) 97

```

#### 数值减N

> **格式: decrby key decrement**

* 对value值减decrement

```

127.0.0.1:6379> decrby last 5

(integer) 92

```

#### 数值递增1

> **格式: incr key**

* 对value值加一

```

127.0.0.1:6379> set download 0

OK

127.0.0.1:6379> incr download

(integer) 1

127.0.0.1:6379> incr download

(integer) 2

127.0.0.1:6379> incr download

(integer) 3

```

#### 数值加N

> **格式: incrby key decrement**

* 对value值加decrement

* 只能加正整数

```

127.0.0.1:6379> incrby download 10

(integer) 13

```

```

127.0.0.1:6379> incrby download 3.3

(error) ERR value is not an integer or out of range

```

#### 数值加浮点数

> **格式: incrbyfloat key decrement**

* 对value值加decrement

```

127.0.0.1:6379> get download

"13"

127.0.0.1:6379> incrbyfloat download 11.5

"24.5"

```

#### 字符串长度

> **格式: strlen key**

```

127.0.0.1:6379> get ak47

"ak47"

127.0.0.1:6379> strlen ak47

(integer) 4

127.0.0.1:6379> get a1

"value1"

127.0.0.1:6379> strlen a1

(integer) 6

```
