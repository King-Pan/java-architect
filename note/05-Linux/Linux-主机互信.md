# Linux主机互信

## 1、生成公钥私钥

```shell
ssh-keygen -t rsa
```

分别在两台主机上执行上面命令生成公钥私钥对*==**(一路回车)**==*

```shell
[zqmobile@CPCC .ssh]$ ssh-keygen -t rsa
Generating public/private rsa key pair.

Enter file in which to save the key (/home/zqmobile/.ssh/id_rsa): Enter passphrase (empty for no passphrase): 
Enter same passphrase again: 
Your identification has been saved in /home/zqmobile/.ssh/id_rsa.
Your public key has been saved in /home/zqmobile/.ssh/id_rsa.pub.
The key fingerprint is:
SHA256:LJdrAPEBdTuophXrtPE+YeHiykn5nUxnH7efV7AzlHE zqmobile@CPCC
The key's randomart image is:
+---[RSA 2048]----+
|    ooo .        |
|     o + .    . E|
|    o o o      + |
|     =.. o    +  |
|    B.o.S    . o |
|   B.+++ .    + .|
|  +.ooo.= . .  o.|
| o o.=.= . o . ..|
|  +.. =.  . ..o. |
+----[SHA256]-----+
```

## 2、A->B免密登陆

如果A->B免密登陆，那么就需要把A的公钥给B

```shell
$ cat ~/.ssh/id_rsa.pub # 把A主机上的id_ras.pub最加到B主机的~/.ssh/authorized_keys文件中,这样A登陆B就可以直接登陆，不需要密码
```

## 3、B->A免密登陆

同上，A<->B互信就是  分别把各自的公钥给对方，这样ssh登陆对方就可以不要密码

