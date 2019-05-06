# 02-SpringBoot-FileUpload



## 配置

```properties
#Spring Boot1.4版本后配置更改为:
spring.http.multipart.maxFileSize = 10Mb  
spring.http.multipart.maxRequestSize=100Mb 
#Spring Boot2.0之后的版本配置修改为:
spring.servlet.multipart.max-file-size = 10Mb  
spring.servlet.multipart.max-request-size=100Mb 
#Spring Boot2.1.4.RELEASE版本配置修改为:(只能是一个long类型的数字)
spring.servlet.multipart.max-file-size = 100000000  
spring.servlet.multipart.max-request-size=100000000 
```

> 常见错误

```java
Description:
Failed to bind properties under ‘spring.servlet.multipart.max-file-size’ to org.springframework.util.unit.DataSize:
Property: spring.servlet.multipart.max-file-size
Value: 10MB
Origin: class path resource [application.yml]:13:22
Reason: failed to convert java.lang.String to org.springframework.util.unit.DataSize

//springboot 2.1.x版本的max-file-size和max-request-size必须是一个long值
```







## 单文件上传



> java代码

```java
@GetMapping({"/", "/index", "/home"})
public ModelAndView upload() {
    return new ModelAndView("index");
}

@PostMapping("/upload")
public String upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
    log.info("正在处理上传");
    if (file.isEmpty()) {
        return "上传失败，请选择文件";
    }
    String fileName = file.getOriginalFilename();
    log.info("上传文件名称:{}", fileName);
    String filePath = path;
    log.info("文件上传路径:{}",filePath);

    File dest = new File(filePath + fileName);
    try {
        file.transferTo(dest);

        return "上传成功";
    } catch (IOException e) {
    }
    return "上传失败！";
}
```

> html代码

**index.html代码**

```html
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv=”Content-Type” content=”text/html; charset=utf-8″>
    <title>文件上传</title>
    <style>
        form{padding:20px 15px 15px 120px;}
        form{width:500px;height:300px;}
        form fieldset{border:0;align:center;}
        form label{display:inline-block;width:70px;text-align:right}
        input{width:240px;height:30px;}
        #submit{width:200px;height:35px;position:relative;left:70px;}
        #submit:hover{cursor: pointer;background:#D8D8D8;}
        body{width:100%;height:100%;background-image: url(img/1.jpg);}
        #box{position:fixed;left:0px;right:0px;width:706px;margin-left:auto;margin-right:auto;top:100px;}
        #code{width: 70px;height: 40px;font-size:18px;}
        #captcha_img{position:relative;bottom:-15px;}
        a{position:relative;bottom:-10px;}
    </style>
</head>
<body >
<div id="box">
    <form method="post" action="upload" enctype="multipart/form-data" style="background:#cccccc;border-radius:25px;">
        <fieldset>
            <div style="margin-left: 120px;font-size: 30px;font-family:'Microsoft YaHei UI'">
                文件上传
            </div>
            <!-- <p>
              <label>用户名:</label>
              <input type="text"name="username"placeholder="用户名"required="required"/>
            </p>
            <p>
              <label>密码:</label>
              <input type="password"name="password"placeholder="密码"required="required"/>
            </p> -->

            <p>
                <label>选择文件:</label>
                <input type="file" name="file">
            </p>
            <p>
                <label>保存路径:</label>
                <input type="text" name="path" id="path" value="L:\"/>
            </p>
            <p>
                <input type="submit" value="开始上传" name="submit" id="submit"/>
            </p>
        </fieldset>
    </form>
</div>
</body>
</html>
```



## 多文件上传

> java代码

```java
@GetMapping({"/multiUpload"})
public ModelAndView multiUpload() {
    return new ModelAndView("multiUpload");
}
@PostMapping("/multiUpload")
public String multiUpload(HttpServletRequest request) {
    List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
    String filePath = System.getProperty("user.dir");
    for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        if (file.isEmpty()) {
            return "上传第" + (i++) + "个文件失败";
        }
        String fileName = file.getOriginalFilename();

        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            log.info("第" + (i + 1) + "个文件上传成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
            return "上传第" + (i++) + "个文件失败";
        }
    }
    return "上传成功";
}
```

> html代码

**multiUpload.html**

```html
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>多文件上传</title>
</head>
<body>
<form method="post" action="/multiUpload" enctype="multipart/form-data">
    <input type="file" name="file"><br>
    <input type="file" name="file"><br>
    <input type="file" name="file"><br>
    <input type="submit" value="提交">
</form>
</body>
</html>
```

