```http
ab -n 1000 -c 200 -p 指定请求数据文件 -t 'application/x-www-form-urlencoded; charset=UTF-8' http://localhost:8080/kill
```

POST请求使用-T

```http
ab -n 1000 -c 200  -T 'application/x-www-form-urlencoded; charset=UTF-8' http://localhost:8080/kill
```





> 案例

```http
ab -n 1000 -c 200 -p /usr/test/a  -T 'application/x-www-form-urlencoded; charset=UTF-8' http://192.168.32.1:8080/kill
```

参数说明:

-n：执行的请求个数，默认时执行一个请求

-c：一次产生的请求个数，即并发个数

-p:模拟post请求，文件格式为gid=2&status=1,配合-T使用

-T:post数据所使用的Content-Type头信息，如果-T 'application/x-www-form-urlencoded'


