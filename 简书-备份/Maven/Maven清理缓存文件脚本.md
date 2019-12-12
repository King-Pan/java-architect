## Maven清理缓存文件脚本

### 新建一个cleanMaven.bat文件

cleanMaven.bat

### 脚本内容

> **注意**

set REPOSITORY_PATH=F:\work\reps 这儿配置的是你自己的仓库路径

```
@echo off
rem create by sunhao(sunhao.java@gmail.com)
rem crazy coder
  
rem 这里写你的仓库路径
set REPOSITORY_PATH=F:\work\reps
rem 正在搜索...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    del /s /q %%i
)
rem 搜索完毕
pause
```

### 右键管理员运行脚本.
