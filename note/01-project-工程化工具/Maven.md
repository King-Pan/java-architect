# 工程化专题之Maven

## Maven的安装

[Maven官网](http://maven.apache.org/)下载安装包，解压到相应的路径下



> window系统

```shell
1. 下载安装包，解压到对于目录下，拷贝安装的根目录
2. 配置M2_HOME环境变量，目录为maven根目录
3. 配置JAVA_HOME环境变量
4. 修改PATH环境变量
5. 打开CMD 输出mvn -version
```



### Maven的配置

maven是从国外传过来的技术，maven的仓库中心也是国外的，从国内下载速度很慢，国内很多公司提供了镜像服务，可以通过修改maven/conf/settings.xml中修改仓库

```xml
<mirror>  
    <id>alimaven</id>  
    <name>aliyun maven</name>  
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
    <mirrorOf>central</mirrorOf>          
</mirror> 
<mirror>
    <id>ui</id>
    <mirrorOf>central</mirrorOf>
    <name>Human Readable Name for this Mirror.</name>
    <url>http://uk.maven.org/maven2/</url>
</mirror>
<mirror>
    <id>osc</id>
    <mirrorOf>central</mirrorOf>
    <url>http://maven.oschina.net/content/groups/public/</url>
</mirror>
<mirror>
    <id>osc_thirdparty</id>
    <mirrorOf>thirdparty</mirrorOf>
    <url>http://maven.oschina.net/content/repositories/thirdparty/</url>
</mirror>
```





## Maven基础概念



