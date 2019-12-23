## window 下nginx操作名



```shell


# 1. 进入nginx目录
cd ../nginx
# 2. 启动nginx
start nginx.exe
# 3. 重新加载配置文件
./nginx.exe -s reload
# 4. 检查配置文件是否正常

//5. 关闭nginx


进入到nginx.exe所在的目录
cd c:
cd nginx-1.15.2
然后在nginx根目录下运行 start nginx

在根目录下的操作:
nginx -t 检查配置文件的有效性
nginx -s stop立即关闭
nginx -s quit	处理完当前的请求后关闭
nginx -s reload	修改完配置文件后重新加载
nginx -s reopen	打开日志文件
```