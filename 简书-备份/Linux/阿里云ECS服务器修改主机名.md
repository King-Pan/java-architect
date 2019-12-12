#### 修改Linux主机名


```
[root@iz2zedk116xwtvo0kuu5awz ~]# hostname
iz2zedk116xwtvo0kuu5awz
```

##### 老版本的Linux修改主机名方式

> **修改/etc/sysconfig/network**

```[root@iz2zedk116xwtvo0kuu5awz ~]# vim /etc/sysconfig/network
NETWORKING_IPV6=no
# Created by anaconda
HOSTNAME=Pan
NETWORKING_IPV6=no
PEERNTP=no
GATEWAY=127.0.0.1
```

> **修改/etc/hosts**

需要修改127.0.0.1 Pan

```
[root@iz2zedk116xwtvo0kuu5awz ~]# vim /etc/hosts
127.0.0.1   localhost Pan localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
ip1  Pan
ip2    King
```

> **重启服务器reboot**

```
[root@iz2zedk116xwtvo0kuu5awz ~]# reboot
```

> **验证是否修改成功**

```
[root@Pan ~]# hostname
Pan
```

##### 新版本的Linux修改主机名方式

> **修改/etc/hostname**

原始的文件内容是: iz2zedk116xwtvo0kuu5awz

```
[root@iz2zedk116xwtvo0kuu5awz ~]# vim /etc/hostname
Pan
```
