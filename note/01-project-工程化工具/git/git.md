建git仓库的两种场景



1. 已有的项目

```shell
cd project_dir
git init
```



1. 未创建项目

   ```shell
   git init project_dir
   cd project_dir
   ```



> git global配置

```shell
git config --global user.name ""
git config --global user.email ""
```

> git local配置

```shell
git config --local user.name ""
git config --local user.email ""
# local的优先级比global高
```

> 查看git配置

```shell
git config --list #查看所有配置
git config --list --global #查看global级别配置
git config --list --global 

git config --local user.name #只查看某一个属性值
```



> 工作目录  git add files-》暂存区   git commit -m ""--》版本区



git cat-file -t hashcode    识别是什么类型



commit tag等





```shell
git add -u  #暂存区的修改一起提交
git add -A  #所有的变动都提交

```





> git重命名

1. 方式一

   ```shell
   mv readme readme.md
   git add readme.md
   git rm readme
   
   
   # 还原
   git reset --hard
   ```

   

2. 方式二

   ```shell
   git mv readme readme.md
   
   git commit -m 'rename readme to readme.md'
   ```

   

## 查看git版本演变历史 git log



git log 只查看当前分支的变更历史



git log --all 查看所有分支的变更历史



git log 分支名 查看某个分支



git log --all --graph







git log --oneline 简洁查看变更历史





git log -n4 --oneline 最近4条记录



git log -n2 --oneline 最近2条记录



```shell
[root@iZ8vbesbzj30ty24cvijhvZ git_learning]# git log --oneline
5a0abc4 rename readme to readme.md
6c2d99c wm
cb384c0 add a js file
8de8922 add a style file
15663ea add index.html
640e025 add a readme file

```



git log --oneline 分支名



git log --oneline --all 查看所有分支的变更历史





git checkout -b temp 创建并切换到temp分支



git branch -v 查看分支



git 自带了图形界面



```shell
gitk
```



gitk



## git查看分支 git branch -v







## .git目录

```shell
[root@iZ8vbesbzj30ty24cvijhvZ .git]# cd objects/
[root@iZ8vbesbzj30ty24cvijhvZ objects]# ll
total 84
drwxr-xr-x 2 root root 4096 Mar  1 20:55 12
drwxr-xr-x 2 root root 4096 Mar  1 20:53 15
drwxr-xr-x 2 root root 4096 Mar  1 21:05 29
drwxr-xr-x 2 root root 4096 Mar  1 21:05 2e
drwxr-xr-x 2 root root 4096 Mar  1 21:08 5a
drwxr-xr-x 2 root root 4096 Mar  1 21:15 61
drwxr-xr-x 2 root root 4096 Mar  1 20:43 64
drwxr-xr-x 2 root root 4096 Mar  1 21:05 6c
drwxr-xr-x 2 root root 4096 Mar  1 20:52 7f
drwxr-xr-x 2 root root 4096 Mar  1 21:08 85
drwxr-xr-x 2 root root 4096 Mar  1 20:55 8b
drwxr-xr-x 2 root root 4096 Mar  1 20:55 8d
drwxr-xr-x 2 root root 4096 Mar  1 21:15 a1
drwxr-xr-x 2 root root 4096 Mar  1 20:53 a3
drwxr-xr-x 2 root root 4096 Mar  1 20:58 cb
drwxr-xr-x 2 root root 4096 Mar  1 20:58 d2
drwxr-xr-x 2 root root 4096 Mar  1 20:42 e6
drwxr-xr-x 2 root root 4096 Mar  1 20:58 e8
drwxr-xr-x 2 root root 4096 Mar  1 20:55 f8
drwxr-xr-x 2 root root 4096 Mar  1 20:37 info
drwxr-xr-x 2 root root 4096 Mar  1 20:37 pack
[root@iZ8vbesbzj30ty24cvijhvZ objects]# cd 8d
[root@iZ8vbesbzj30ty24cvijhvZ 8d]# ll
total 4
-r--r--r-- 1 root root 154 Mar  1 20:55 e8922a0d5fc70ef08a8ef1ffbc6ccb6bd67c05
[root@iZ8vbesbzj30ty24cvijhvZ 8d]# git  cat-file -t 8de8922a0d5fc70ef08a8ef1ffbc6ccb6bd67c05
commit
[root@iZ8vbesbzj30ty24cvijhvZ 8d]# cd ..
[root@iZ8vbesbzj30ty24cvijhvZ objects]# cd f8
[root@iZ8vbesbzj30ty24cvijhvZ f8]# ll
total 4
-r--r--r-- 1 root root 119 Mar  1 20:55 9495ccea7a03297c304dd097b300aed0e2376a
[root@iZ8vbesbzj30ty24cvijhvZ f8]# git cat-file -t f89495ccea7a03297c304dd097b300aed0e2376a
tree
[root@iZ8vbesbzj30ty24cvijhvZ f8]# cd ..
[root@iZ8vbesbzj30ty24cvijhvZ objects]# cd d2
[root@iZ8vbesbzj30ty24cvijhvZ d2]# ll
total 4
-r--r--r-- 1 root root 36 Mar  1 20:58 aef921a44e0243cddde3f6c349e37efafeb0f4
[root@iZ8vbesbzj30ty24cvijhvZ d2]# git cat-file -t d2aef921a44e0243cddde3f6c349e37efafeb0f4
blob
[root@iZ8vbesbzj30ty24cvijhvZ d2]# git cat-file -p d2aef921a44e0243cddde3f6c349e37efafeb0f4
alert("hello git");

```







## git 把文件提交到版本区

### 新增的文件，未受暂存区、版本区管理的文件

```shell
git add .
git commit -m 'xxx'
```



### 已经在暂存区、版本区管理的文件

```shell
#方式一
可以使用上面的方式
#方式二
git commit -am 'xxx' #直接提交到版本区
```























