## 修改虚拟机IP



vi /etc/sysconfig/network-scripts/i





```sh
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=no
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=ens18
UUID=89d02fe1-2845-4cf3-b8db-ea8e4612a34e
DEVICE=ens18
ONBOOT=yes
IPADDR=192.168.48.205
NETMASK=255.255.255.0
GATEWAY=192.168.48.254
DNS1=218.104.111.114
```

