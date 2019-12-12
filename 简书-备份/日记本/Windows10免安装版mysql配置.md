1. 新建my.ini文件
```
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8 
[mysqld]
#设置3306端口
port = 3306 
# 设置mysql的安装目录
basedir=E:\Program Files\mysql-5.7.22-winx64
# 设置mysql数据库的数据的存放目录
datadir=E:\Program Files\mysql-5.7.22-winx64\data
# 允许最大连接数
max_connections=200
# 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
```

注意:
> basedir=E:\Program Files\mysql-5.7.22-winx64
> datadir=E:\Program Files\mysql-5.7.22-winx64\data 
请根据实际填写

2. 然后配置环境变量

3. 使用管理员运行cmd

4. mysqld -install [服务名]   

5. 初始化
mysqld --initialize-insecure 
注：–initialize有两个-，后边没有空格 
初始化后，E:\Program Files\mysql-5.7.22-winx64目录下回出现data目录



https://blog.csdn.net/mr_green1024/article/details/53222526

