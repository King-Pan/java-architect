### 问题截图
![image.png](http://upload-images.jianshu.io/upload_images/6331401-ceb4e105e4f36ceb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 问题文字描述

```
git push 
remote: Permission to a/a.git denied to b.
fatal: unable to access 'https://github.com/a/a.git/': The requested URL returned error: 403
```

问题原因主要是电脑上有两个github账号时，每个账号对应不同的ssh，比如：

```
 king-pan@King-Pan-PC  ~/.ssh  ll
total 72
drwx------  11 king-pan  staff   352 12 28 10:31 ./
drwxr-xr-x+ 74 king-pan  staff  2368 12 28 10:38 ../
-rw-r--r--   1 king-pan  staff   163  9 11 11:10 aliyun01
-rw-r--r--   1 king-pan  staff   400  8 24 14:46 authorized_keys
-rw-r--r--   1 king-pan  staff   150 12 28 10:28 config
-rw-------   1 king-pan  staff  1679 11 17 00:16 gmail_github
-rw-r--r--   1 king-pan  staff   400 11 17 00:16 gmail_github.pub
-rw-------   1 king-pan  staff  1679 12 28 10:05 id_rsa
-rw-r--r--   1 king-pan  staff   397 12 28 10:05 id_rsa.pub
-rw-r--r--   1 king-pan  staff   149  9 11 11:07 king-pan
-rw-r--r--   1 king-pan  staff  1890 10  1 16:10 known_hosts
```

#### config配置

```
 king-pan@King-Pan-PC  ~/.ssh  cat config
# pwpw1218@qq.com
Host github-qq
HostName github.com
User pwpw1218@qq.com
PreferredAuthentications publickey
#github对应的私钥
IdentityFile ~/.ssh/qq_github


# pwpw1218@gmail.com
Host github-gmail
HostName github.com
User pwpw1218@gmail.com
PreferredAuthentications publickey
#github对应的私钥
IdentityFile ~/.ssh/gmail_github
```
qq_github是pwpw1218@qq.com对应的秘钥
gmail_github是pwpw1218@gmail.com对应的秘钥

### 解决方法

切换到对应的git项目根目录下

```
 cd git_repository/bass
 git remote rm origin
 git remote add origin git@github-qq:HubeiAsiainfo/bass.git
 git push
fatal: The current branch master has no upstream branch.
To push the current branch and set the remote as upstream, use

    git push --set-upstream origin master
git push origin master
Counting objects: 92, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (87/87), done.
Writing objects: 100% (92/92), 68.55 KiB | 3.26 MiB/s, done.
Total 92 (delta 21), reused 0 (delta 0)
remote: Resolving deltas: 100% (21/21), completed with 6 local objects.
To github-qq:HubeiAsiainfo/bass.git
   b7def168..c3020a96  master -> master
```

### 大工告成，谢谢各位网友的支持，本文纯粹是从网上拷贝改写，如有侵权，请联系我。




