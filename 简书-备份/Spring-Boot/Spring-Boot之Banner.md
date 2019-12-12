## Spring Boot之Banner

### Spring Boot默认Banner效果图

![Spring Boot默认Banner效果图](http://upload-images.jianshu.io/upload_images/6331401-25e450900e525c8a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Spring Boot简介

[Spring Boot官方文档地址](https://docs.spring.io/spring-boot/docs/1.5.7.RELEASE/reference/htmlsingle/)

下面是来自官网关于Banner部分的文档，硬翻译的，不当之处请见谅.

#### Customizing the Banner(自定义Banner banner翻译成横幅感觉缺少点意境)

The banner that is printed on start up can be changed by adding a **banner.txt** file to your classpath, or by setting **banner.location** to the location of such a file.
可以通过在在classpath路径下添加**bannner.txt**文件改变控制台打印的Banner,或者在application.yml/application.properties文件中设置**banner.location**属性来指定bannner文件.

 If the file has an unusual encoding you can set **banner.charset** (default is UTF-8). In addition to a text file, you can also add a banner.gif, banner.jpg or banner.png image file to your classpath, or set a **banner.image.location** property. 
 如果文件中有一个不寻常的编码可以设置**banner.charset**来指定编码(默认为UTF-8)除了一个文本文件，你还可以添加一个banner.gif，banner.jpg或banner.png 图像文件到您的类路径，或者设置**banner.image.location**属性指定图片位置。
 
 Images will be converted into an ASCII art representation and printed above any text banner.
图像将被转换成ASCII艺术表现和任何文本旗帜，并且在bannner.txt文本上方打印。

>**Inside your banner.txt file you can use any of the following placeholders(在你的bannner.txt文件中，你可以使用下面的变量)**


|变量|描述|
| -----------| -----------------------------------------------------:|
|**${application.version}**|在声明的应用程序的版本号MANIFEST.MF。例如 Implementation-Version: 1.0打印为1.0|
|**${application.formatted-version}**|在声明的应用程序的版本号MANIFEST.MF格式化显示（周围有括号和前缀v）。例如(v1.0)。|
|**${spring-boot.version}**|您正在使用Spring Boot版本。例如1.5.7.RELEASE。|
|**${spring-boot.formatted-version}**|您正在使用Spring Boot版本。例如1.5.7.RELEASE。|
|**${Ansi.NAME}（或${AnsiColor.NAME}，${AnsiBackground.NAME}，${AnsiStyle.NAME}）**|NAME是一个ANSI转义代码名称。请参阅 AnsiPropertySource有关详情。|
|**${application.title}**|在声明的应用程序的标题MANIFEST.MF。例如 Implementation-Title: MyApp打印为MyApp。|
|**其他application.yml变量**|声明在application.yml/properties中的变量都可以使用${}来获取值|




### 生成Bannner

Bannner的生成需要有工具来生成，大神自己手动生成也是可以的。


工具地址：
* [http://patorjk.com/software/taag](http://patorjk.com/software/taag) 【亲测好用👌】
* [http://www.network-science.de/ascii/](http://www.network-science.de/ascii/)
* [http://www.degraeve.com/img2txt.php](http://www.degraeve.com/img2txt.php)

### 自定义Banner

>**Bannner文件**

```
${AnsiColor.BRIGHT_YELLOW}
 /$$$$$$$$                     /$$             /$$      /$$
|__  $$__/                    | $$            | $$$    /$$$
   | $$     /$$$$$$   /$$$$$$$| $$   /$$      | $$$$  /$$$$  /$$$$$$  /$$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$
   | $$    |____  $$ /$$_____/| $$  /$$/      | $$ $$/$$ $$ |____  $$| $$__  $$ |____  $$ /$$__  $$ /$$__  $$ /$$__  $$
   | $$     /$$$$$$$|  $$$$$$ | $$$$$$/       | $$  $$$| $$  /$$$$$$$| $$  \ $$  /$$$$$$$| $$  \ $$| $$$$$$$$| $$  \__/
   | $$    /$$__  $$ \____  $$| $$_  $$       | $$\  $ | $$ /$$__  $$| $$  | $$ /$$__  $$| $$  | $$| $$_____/| $$
   | $$   |  $$$$$$$ /$$$$$$$/| $$ \  $$      | $$ \/  | $$|  $$$$$$$| $$  | $$|  $$$$$$$|  $$$$$$$|  $$$$$$$| $$
   |__/    \_______/|_______/ |__/  \__/      |__/     |__/ \_______/|__/  |__/ \_______/ \____  $$ \_______/|__/
                                                                                          /$$  \ $$
                                                                                         |  $$$$$$/
                                                                                          \______/
                                                                                          
${AnsiColor.BRIGHT_RED}
Application Name: ${spring.application.name}
Application Version: ${spring.application.version}${spring.application.formatted-version}
Spring Boot Version: ${spring-boot.version}${spring-boot.formatted-version}

===========================================================================================================================                                                                                          
===========================================================================================================================



```

>**效果图**

![自定义Banner相关图](http://upload-images.jianshu.io/upload_images/6331401-a7d50fad97651484.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>**常用属性解释**

* **${AnsiColor.BRIGHT_YELLOW}：**设置控制台中banner内容的颜色
* **${application.version}：**用来获取MANIFEST.MF文件中的版本号
* **${application.formatted-version}：**格式化后的${application.version}版本信息
* **${spring-boot.version}：**Spring Boot的版本号
* **${spring-boot.formatted-version}：**格式化后的${spring-boot.version}版本信息

### 代码定制Banner

```
SpringApplication bootstrap = new SpringApplication(SampleController.class);
 bootstrap.setBanner(new Banner() {
   @Override
   public void printBanner(Environment environment, Class<?> aClass, PrintStream printStream) {
     //往 printStream 中输出吧, 可从 environment 中取属性配置 
   }
 });
 bootstrap.run(args);
```

### 开关Bannner

Banner.Mode 有三个值 CONSOLE, LOG, OFF, 默认为 CONSOLE, 可以用 OFF 关闭 Banner 显示, 或是 LOG 让 Banner 在应用 log 中显示

>**配置文件**

```
spring.main.banner-mode: off/console/log
```

>**代码**

```
new SpringApplicationBuilder(Xxx.class)
  .bannerMode(Banner.Mode.OFF)
  .run(args);
```
