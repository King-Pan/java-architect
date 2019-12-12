#### SSH免密登录配置

> **配置/etc/hosts文件**

```
ip1  Pan
ip2   King
```

```
[root@iz2zedk116xwtvo0kuu5awz ~]# ping Pan
PING Pan (ip1) 56(84) bytes of data.
64 bytes from Pan (ip1): icmp_seq=1 ttl=64 time=0.898 ms
64 bytes from Pan (ip1): icmp_seq=2 ttl=64 time=0.895 ms
^C
--- Pan ping statistics ---
2 packets transmitted, 2 received, 0% packet loss, time 1000ms
rtt min/avg/max/mdev = 0.895/0.896/0.898/0.029 ms
[root@iz2zedk116xwtvo0kuu5awz ~]# ping King
PING King (ip2) 56(84) bytes of data.
64 bytes from King (ip2): icmp_seq=1 ttl=56 time=6.14 ms
64 bytes from King (ip2): icmp_seq=2 ttl=56 time=6.14 ms
64 bytes from King (ip2): icmp_seq=3 ttl=56 time=6.17 ms
^C
--- King ping statistics ---
3 packets transmitted, 3 received, 0% packet loss, time 2002ms
rtt min/avg/max/mdev = 6.147/6.156/6.172/0.011 ms
[root@iz2zedk116xwtvo0kuu5awz ~]# 
```

> **生成秘钥**

```
[root@iz2zedk116xwtvo0kuu5awz ~]# ssh-keygen -t rsa
Generating public/private rsa key pair.
Enter file in which to save the key (/root/.ssh/id_rsa): 
Enter passphrase (empty for no passphrase): 
Enter same passphrase again: 
Your identification has been saved in /root/.ssh/id_rsa.
Your public key has been saved in /root/.ssh/id_rsa.pub.
The key fingerprint is:
83:73:55:ed:f7:a1:96:83:bc:00:ee:04:bd:32:c0:c7 root@iz2zedk116xwtvo0kuu5awz
The key's randomart image is:
+--[ RSA 2048]----+
|            ..   |
|           .  .  |
|  . . .   .  .   |
|   o E + .    ...|
|    o = S . . o.o|
|     o * o o =  .|
|      =   . o .  |
|       .   .     |
|                 |
+-----------------+
[root@iz2zedk116xwtvo0kuu5awz ~]# 
```

> **拷贝秘钥**

```
[root@iz2zedk116xwtvo0kuu5awz ~]# ssh-copy-id -i ~/.ssh/id_rsa.pub root@King
The authenticity of host 'king (ip2)' can't be established.
ECDSA key fingerprint is ac:dc:91:1b:e8:d6:2e:ca:db:4a:d0:7a:41:30:9f:34.
Are you sure you want to continue connecting (yes/no)? yes
/usr/bin/ssh-copy-id: INFO: attempting to log in with the new key(s), to filter out any that are already installed
/usr/bin/ssh-copy-id: INFO: 1 key(s) remain to be installed -- if you are prompted now it is to install the new keys
root@king's password: 

Number of key(s) added: 1

Now try logging into the machine, with:   "ssh 'root@King'"
and check to make sure that only the key(s) you wanted were added.
```

> **登录测试**

```
[root@iz2zedk116xwtvo0kuu5awz ~]# ssh root@King
Last login: Tue Jul  4 22:07:31 2017 from ip1

Welcome to Alibaba Cloud Elastic Compute Service !

[root@iz8vbesbzj30ty24cvijhvz ~]# exit
logout
Connection to king closed.
[root@iz2zedk116xwtvo0kuu5awz ~]# 
```
