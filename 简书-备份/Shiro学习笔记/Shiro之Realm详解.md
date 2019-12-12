## Shiro Realm 详解

### Realm 接口定义

```
package org.apache.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

public interface Realm {

    String getName();

    boolean supports(AuthenticationToken token);

    AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException;

}
```

> **getName()方法**

* 返回唯一的Realm名称
* 所有的realm都必须有唯一的名称

> **supports(AuthenticationToken token)方法**

* 判断该Realm是否支持该类型的AuthenticationToken
* 如果为null或者不支持,返回false

> **getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException**

* 验证用户账号
* 验证通过则返回AuthenticationToken
* 验证不通过则返回对于的AuthenticationException异常
* 常见的AuthenticationException异常有2中:
*  1. org.apache.shiro.authc.UnknownAccountException 账户不存在
*  2. org.apache.shiro.authc.IncorrectCredentialsException 密码错误
* 为了系统安全,我们会统一返回登录失败的错误,不会区分账号不存在和密码错误.
