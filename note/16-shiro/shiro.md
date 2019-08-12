```
user
```

![SimpleAccountRealm](./images/SimpleAccountRealm.png)



自定义Realm



realm作用：shiro从Realm获取安全数据



默认自带的realm：有默认实现和自定义继承的realm



* principal：主体标识，可以有多个，但是需要具有唯一性，常见的有用户名，手机号，邮箱等
* credential：凭证，一般就是密码
* 所以一般我们说principal+credential 就是账号+密码

开发中，我们都是自定义Realm，既继承AuthorizingRealm

