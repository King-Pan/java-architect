### 1. 在前台直接使用了

```
$(".form-inline").serialize();
```
获取参数值，
实际上jquery在获取form表达的值时，调用了encodeURIComponent对数据进行了编码，所有后台获取到的参数都是编码后的数据，不能直接解析

### 在前台传入参数之前，可以使用下面语句处理后，就OK了

```
var params = $(".form-inline").serialize();
params = decodeURIComponent(params)
```

在网上搜到了很多的decodeURIComponent(params,true)的用法，但是直接提示参数个数错误，亲测上面的可以用。
