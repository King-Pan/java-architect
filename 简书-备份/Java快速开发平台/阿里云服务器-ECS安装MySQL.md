### 阿里云服务器 ECS安装MySQL

> **系统环境**

实例规格： ecs.n4.small
实例规格族： 共享计算型
镜像ID： alinux_7_01_64_40G_base_20170310.vhd

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# uname -a
Linux iZ8vbesbzj30ty24cvijhvZ 4.4.24-2.al7.x86_64 #1
 SMP Wed Jan 4 17:23:01 CST 2017 x86_64 x86_64 x86_64 GNU/Linux
```

> **下载mysql源安装包**

```
 wget http://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm
```

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# wget http://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm
--2017-07-01 15:26:39--  http://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm
Resolving dev.mysql.com (dev.mysql.com)... 137.254.60.11
Connecting to dev.mysql.com (dev.mysql.com)|137.254.60.11|:80... connected.
HTTP request sent, awaiting response... 301 Moved Permanently
Location: https://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm [following]
--2017-07-01 15:26:40--  https://dev.mysql.com/get/mysql57-community-release-el7-8.noarch.rpm
Connecting to dev.mysql.com (dev.mysql.com)|137.254.60.11|:443... connected.
HTTP request sent, awaiting response... 302 Found
Location: https://repo.mysql.com//mysql57-community-release-el7-8.noarch.rpm [following]
--2017-07-01 15:26:42--  https://repo.mysql.com//mysql57-community-release-el7-8.noarch.rpm
Resolving repo.mysql.com (repo.mysql.com)... 23.211.97.88
Connecting to repo.mysql.com (repo.mysql.com)|23.211.97.88|:443... connected.
HTTP request sent, awaiting response... 200 OK
Length: 9116 (8.9K) [application/x-redhat-package-manager]
Saving to: ‘mysql57-community-release-el7-8.noarch.rpm’

100%[===================================================================================================================================>] 9,116       --.-K/s   in 0s      

2017-07-01 15:26:43 (172 MB/s) - ‘mysql57-community-release-el7-8.noarch.rpm’ saved [9116/9116]
```
> **安装mysql源**

```

[root@iZ8vbesbzj30ty24cvijhvZ ~]# yum localinstall mysql57-community-release-el7-8.noarch.rpm
```

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# yum localinstall mysql57-community-release-el7-8.noarch.rpm
Loaded plugins: fastestmirror, langpacks
Examining mysql57-community-release-el7-8.noarch.rpm: mysql57-community-release-el7-8.noarch
Marking mysql57-community-release-el7-8.noarch.rpm to be installed
Resolving Dependencies
--> Running transaction check
---> Package mysql57-community-release.noarch 0:el7-8 will be installed
--> Finished Dependency Resolution

Dependencies Resolved

=============================================================================================================================================================================
 Package                                        Arch                        Version                       Repository                                                    Size
=============================================================================================================================================================================
Installing:
 mysql57-community-release                      noarch                      el7-8                         /mysql57-community-release-el7-8.noarch                      8.2 k

Transaction Summary
=============================================================================================================================================================================
Install  1 Package

Total size: 8.2 k
Installed size: 8.2 k
Is this ok [y/d/N]: y
Downloading packages:
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
  Installing : mysql57-community-release-el7-8.noarch                                                                                                                    1/1 
  Verifying  : mysql57-community-release-el7-8.noarch                                                                                                                    1/1 

Installed:
  mysql57-community-release.noarch 0:el7-8                                                                                                                                   

Complete!
```

> **检查mysql源是否安装成功**

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# yum repolist enabled | grep "mysql.*-community.*"
```

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# yum repolist enabled | grep "mysql.*-community.*"
mysql-connectors-community/x86_64 MySQL Connectors Community                  36
mysql-tools-community/x86_64      MySQL Tools Community                       47
mysql57-community/x86_64          MySQL 5.7 Community Server                 187
```

> **安装Mysql**

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# yum install mysql-community-server
```

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# yum install mysql-community-server
Loaded plugins: fastestmirror, langpacks
Loading mirror speeds from cached hostfile
Resolving Dependencies
--> Running transaction check
---> Package mysql-community-server.x86_64 0:5.7.18-1.el7 will be installed
--> Processing Dependency: mysql-community-common(x86-64) = 5.7.18-1.el7 for package: mysql-community-server-5.7.18-1.el7.x86_64
--> Processing Dependency: mysql-community-client(x86-64) >= 5.7.9 for package: mysql-community-server-5.7.18-1.el7.x86_64
--> Running transaction check
---> Package mysql-community-client.x86_64 0:5.7.18-1.el7 will be installed
--> Processing Dependency: mysql-community-libs(x86-64) >= 5.7.9 for package: mysql-community-client-5.7.18-1.el7.x86_64
---> Package mysql-community-common.x86_64 0:5.7.18-1.el7 will be installed
--> Running transaction check
---> Package mariadb-libs.x86_64 1:5.5.52-1.4.al7 will be obsoleted
--> Processing Dependency: libmysqlclient.so.18()(64bit) for package: 2:postfix-2.10.1-6.1.al7.x86_64
--> Processing Dependency: libmysqlclient.so.18(libmysqlclient_18)(64bit) for package: 2:postfix-2.10.1-6.1.al7.x86_64
---> Package mysql-community-libs.x86_64 0:5.7.18-1.el7 will be obsoleting
--> Running transaction check
---> Package mysql-community-libs-compat.x86_64 0:5.7.18-1.el7 will be obsoleting
--> Finished Dependency Resolution

Dependencies Resolved

=============================================================================================================================================================================
 Package                                              Arch                            Version                               Repository                                  Size
=============================================================================================================================================================================
Installing:
 mysql-community-libs                                 x86_64                          5.7.18-1.el7                          mysql57-community                          2.1 M
     replacing  mariadb-libs.x86_64 1:5.5.52-1.4.al7
 mysql-community-libs-compat                          x86_64                          5.7.18-1.el7                          mysql57-community                          2.0 M
     replacing  mariadb-libs.x86_64 1:5.5.52-1.4.al7
 mysql-community-server                               x86_64                          5.7.18-1.el7                          mysql57-community                          162 M
Installing for dependencies:
 mysql-community-client                               x86_64                          5.7.18-1.el7                          mysql57-community                           24 M
 mysql-community-common                               x86_64                          5.7.18-1.el7                          mysql57-community                          271 k

Transaction Summary
=============================================================================================================================================================================
Install  3 Packages (+2 Dependent packages)

Total download size: 190 M
Is this ok [y/d/N]: y
Downloading packages:
warning: /var/cache/yum/x86_64/17.01/mysql57-community/packages/mysql-community-common-5.7.18-1.el7.x86_64.rpm: Header V3 DSA/SHA1 Signature, key ID 5072e1f5: NOKEY-:-- ETA 
Public key for mysql-community-common-5.7.18-1.el7.x86_64.rpm is not installed
(1/5): mysql-community-common-5.7.18-1.el7.x86_64.rpm                                                                                                 | 271 kB  00:00:01     
(2/5): mysql-community-libs-5.7.18-1.el7.x86_64.rpm                                                                                                   | 2.1 MB  00:00:00     
(3/5): mysql-community-libs-compat-5.7.18-1.el7.x86_64.rpm                                                                                            | 2.0 MB  00:00:00     
(4/5): mysql-community-client-5.7.18-1.el7.x86_64.rpm                                                                                                 |  24 MB  00:00:04     
(5/5): mysql-community-server-5.7.18-1.el7.x86_64.rpm                                                                                                 | 162 MB  00:01:00     
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Total                                                                                                                                        3.0 MB/s | 190 MB  00:01:02     
Retrieving key from file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql
Importing GPG key 0x5072E1F5:
 Userid     : "MySQL Release Engineering <mysql-build@oss.oracle.com>"
 Fingerprint: a4a9 4068 76fc bd3c 4567 70c8 8c71 8d3b 5072 e1f5
 Package    : mysql57-community-release-el7-8.noarch (installed)
 From       : /etc/pki/rpm-gpg/RPM-GPG-KEY-mysql
Is this ok [y/N]: y
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
  Installing : mysql-community-common-5.7.18-1.el7.x86_64                                                                                                                1/6 
  Installing : mysql-community-libs-5.7.18-1.el7.x86_64                                                                                                                  2/6 
  Installing : mysql-community-client-5.7.18-1.el7.x86_64                                                                                                                3/6 
  Installing : mysql-community-server-5.7.18-1.el7.x86_64                                                                                                                4/6 
  Installing : mysql-community-libs-compat-5.7.18-1.el7.x86_64                                                                                                           5/6 
  Erasing    : 1:mariadb-libs-5.5.52-1.4.al7.x86_64                                                                                                                      6/6 
  Verifying  : mysql-community-server-5.7.18-1.el7.x86_64                                                                                                                1/6 
  Verifying  : mysql-community-common-5.7.18-1.el7.x86_64                                                                                                                2/6 
  Verifying  : mysql-community-client-5.7.18-1.el7.x86_64                                                                                                                3/6 
  Verifying  : mysql-community-libs-compat-5.7.18-1.el7.x86_64                                                                                                           4/6 
  Verifying  : mysql-community-libs-5.7.18-1.el7.x86_64                                                                                                                  5/6 
  Verifying  : 1:mariadb-libs-5.5.52-1.4.al7.x86_64                                                                                                                      6/6 

Installed:
  mysql-community-libs.x86_64 0:5.7.18-1.el7            mysql-community-libs-compat.x86_64 0:5.7.18-1.el7            mysql-community-server.x86_64 0:5.7.18-1.el7           

Dependency Installed:
  mysql-community-client.x86_64 0:5.7.18-1.el7                                          mysql-community-common.x86_64 0:5.7.18-1.el7                                         

Replaced:
  mariadb-libs.x86_64 1:5.5.52-1.4.al7                                                                                                                                       

Complete!
```

> **启动MySQL服务**

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl start mysqld
```

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl start mysqld
[root@iZ8vbesbzj30ty24cvijhvZ ~]# 
```

> **查看MySQL启动状态**

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl status mysqld
```

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl status mysqld
● mysqld.service - MySQL Server
   Loaded: loaded (/usr/lib/systemd/system/mysqld.service; enabled; vendor preset: disabled)
   Active: active (running) since Sat 2017-07-01 15:34:34 CST; 55s ago
     Docs: man:mysqld(8)
           http://dev.mysql.com/doc/refman/en/using-systemd.html
  Process: 3880 ExecStart=/usr/sbin/mysqld --daemonize --pid-file=/var/run/mysqld/mysqld.pid $MYSQLD_OPTS (code=exited, status=0/SUCCESS)
  Process: 3806 ExecStartPre=/usr/bin/mysqld_pre_systemd (code=exited, status=0/SUCCESS)
 Main PID: 3884 (mysqld)
   CGroup: /system.slice/mysqld.service
           └─3884 /usr/sbin/mysqld --daemonize --pid-file=/var/run/mysqld/mysqld.pid

Jul 01 15:34:27 iZ8vbesbzj30ty24cvijhvZ systemd[1]: Starting MySQL Server...
Jul 01 15:34:34 iZ8vbesbzj30ty24cvijhvZ systemd[1]: Started MySQL Server.
```

> **设置MySQL开机启动**

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl enable mysqld
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl daemon-reload
```

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl enable mysqld
[root@iZ8vbesbzj30ty24cvijhvZ ~]# systemctl daemon-reload
[root@iZ8vbesbzj30ty24cvijhvZ ~]#
```

> **修改root密码**


查看默认密码:d16Db,F?Io;*

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]#  grep 'temporary password' /var/log/mysqld.log
2017-07-01T07:34:30.181714Z 1 [Note] A temporary password is generated for root@localhost: d16Db,F?Io;*
```

修改密码：

```
[root@iZ8vbesbzj30ty24cvijhvZ ~]# mysql -uroot -p
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 4
Server version: 5.7.18

Copyright (c) 2000, 2017, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> 
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '1QAZ@wsx*!'; 
Query OK, 0 rows affected (0.00 sec)
```

或者

```
mysql> set password for 'root'@'localhost'=password('1QAZ@wsx*!'); 
```

密码不符合规范报错:

* mysql5.7默认安装了密码安全检查插件（validate_password），默认密码检查策略要求密码必须包含：大小写字母、数字和特殊符号，并且长度不能少于8位。否则会提示ERROR 1819 (HY000)

```
ERROR 1819 (HY000): Your password does not satisfy the current policy requirements
```

查看密码策略:

```
mysql> show variables like '%password%';
+---------------------------------------+--------+
| Variable_name                         | Value  |
+---------------------------------------+--------+
| default_password_lifetime             | 0      |
| disconnect_on_expired_password        | ON     |
| log_builtin_as_identified_by_password | OFF    |
| mysql_native_password_proxy_users     | OFF    |
| old_passwords                         | 0      |
| report_password                       |        |
| sha256_password_proxy_users           | OFF    |
| validate_password_check_user_name     | OFF    |
| validate_password_dictionary_file     |        |
| validate_password_length              | 8      |
| validate_password_mixed_case_count    | 1      |
| validate_password_number_count        | 1      |
| validate_password_policy              | MEDIUM |
| validate_password_special_char_count  | 1      |
+---------------------------------------+--------+
14 rows in set (0.00 sec)
```

```
validate_password_policy：密码策略，默认为MEDIUM策略  
validate_password_dictionary_file：密码策略文件，策略为STRONG才需要  
validate_password_length：密码最少长度  
validate_password_mixed_case_count：大小写字符长度，至少1个  
validate_password_number_count ：数字至少1个  
validate_password_special_char_count：特殊字符至少1个  上述参数是默认策略MEDIUM的密码检查规则。
```

如果不需要密码策略，添加my.cnf文件中添加如下配置禁用即可：

```
validate_password = off
```

重新启动mysql服务使配置生效：

```
systemctl restart mysqld
```


> **添加远程登录用户**

默认只允许root帐户在本地登录，如果要在其它机器上连接mysql，必须修改root允许远程连接，或者添加一个允许远程连接的帐户，为了安全起见，我添加一个新的帐户：

```
mysql> GRANT ALL PRIVILEGES ON *.* TO 'pwpw1218'@'%' IDENTIFIED BY 'Pwpw1218@**!' WITH GRANT OPTION;
Query OK, 0 rows affected, 1 warning (0.00 sec)
```

> **配置默认编码为utf8**

修改/etc/my.cnf配置文件，在[mysqld]下添加编码配置，如下所示：

```
[root@iZ8vbesbzj30ty24cvijhvZ etc]# vim /etc/my.cnf
# For advice on how to change settings please see
# http://dev.mysql.com/doc/refman/5.7/en/server-configuration-defaults.html

[mysqld]
character_set_server=utf8
init_connect='SET NAMES utf8'
```
esc :qw!

重启mysql

```
systemctl restart mysqld
[root@iZ8vbesbzj30ty24cvijhvZ etc]# mysql -u pwpw1218 -p
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 4
Server version: 5.7.18 MySQL Community Server (GPL)

Copyright (c) 2000, 2017, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show variables like '%character%';
+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | utf8                       |
| character_set_connection | utf8                       |
| character_set_database   | utf8                       |
| character_set_filesystem | binary                     |
| character_set_results    | utf8                       |
| character_set_server     | utf8                       |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+
8 rows in set (0.00 sec)
```


默认配置文件路径：
 配置文件：/etc/my.cnf  日志文件：/var/log//var/log/mysqld.log  
 服务启动脚本：/usr/lib/systemd/system/mysqld.service  
socket文件：/var/run/mysqld/mysqld.pid


至此,阿里云服务器上安装mysql结束.
