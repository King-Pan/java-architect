# Git最简配置



```shell
git config --local   # 只对某个仓库有效
git config --global  # 只针对当前用户的所有仓库有效(【推荐使用】)
git config --system  # 对系统所有的登陆用户有效
```





> 显示config配置

```shell
git config --list
```







> 分离头指针 

在分离头指针下提交的commit，如果不新建分支，切换到其他分支下，提交会丢失.

在分离头指针下提交的commit是不重要的，会被git丢弃掉。

```shell
git checkout commit-id ## 
```

