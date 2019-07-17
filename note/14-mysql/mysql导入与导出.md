# Mysql导入与导出



## Mysql导出库表结构和数据

### 命令格式



```shell
mysqldump -h ip -P port -u用户名 -p 数据库名  表名 > 文件名
```

常见选项：

* --alldatabases, -A： 备份所有数据库

* --databases, -B： 用于备份多个数据库，如果没有该选项。

* mysqldump把第一个名字参数作为数据库名，后面的作为表名 多个表名用空格隔开。使用该选项，mysqldum把每个名字都当作为数据库名。

* mysqldump -u用戶名 -p密码  数据库名 表名1 表名2> 脚本名;

* --force, -f：即使发现sql错误，仍然继续备份
* --host, -h host_name：备份主机名，默认为localhost
* --no-data, -d：只导出表结构
* --password, -p[password]：密码
* --port, -P port_num：制定TCP/IP连接时的端口号
* --quick, -q：快速导出
* --tables：覆盖 --databases or -B选项，后面所跟参数被视作表名
* --user, -u user_name：用户名
* --xml, -X：导出为xml文件



### 实例

```shell
mysqldump  -h 127.0.0.1 -uroot -p test user > user.sql
```

> 把本地test库中的user表结构和数据导出到user.sql文件中

## Mysql导入数据

脚本中有创建库直接进mysql环境 ：source filePath；

没有创建库，进mysql环境然后create database databse_name； use database database_name；source filePath；

注意文件路径filePath----  不能使用\ 导致Failed to open file  

