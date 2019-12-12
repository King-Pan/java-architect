在使用git时，有些时候我们本地会有一些与操作系统或者项目配置相关的配置文件，我们不想让git管理，但是每次git status都会显示Untracked files ...，在你全量体积<code>git add -A</code>时，会把那些我们不想让git管理的文件提交到仓库。

这种情况，Git已经为大家考虑了,开发者只需要在自己仓库的根目录下创建一个.gitignore文件添加忽略规则就能让Git忽略那些文件或者目录

笔者使用eclipse做java开发，总结了一下几点忽略文件原则

> **忽略原则**
1.  忽略系统相关的文件   Mac系统目录下.DS_Store
2.  eclipse 的配置文件 .project/.classpath/settings目录
3.  maven的target目录
4. 应用产生的日志文件 *.log
5. 生成的.class文件

总结以上原则.gitignore文件内容如下:

```
*.class
*.log

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files #
*.jar
*.war
*.ear
.classpath
.project
.settings/
target/
.DS_Store
```

> **忽略规则**

1. *.class 忽略后缀为.class的文件
2. .project 忽略文件名称为 .project的文件
3. settings/ 忽略settings目录
4. /user  只忽略.gitignore文件所在目录的user目录，子目录中的user目录不忽略


> 不跟踪版本
* 1. 在.gitignore文件中添加排除规则
* 2. 在 git中删除相关的文件或者目录

```
$ git rm 我的文件
$ git rm -r 我的文件夹/
$ git commit -m '删除xxx'
$ git push origin 分支名
```
