## 认证流程解读



```java
#登录操作
subject.login(token);
org.apache.shiro.subject.support.DelegatingSubject#login
org.apache.shiro.mgt.DefaultSecurityManager#login
org.apache.shiro.mgt.AuthenticatingSecurityManager#authenticate
org.apache.shiro.authc.AbstractAuthenticator#authenticate
org.apache.shiro.authc.pam.ModularRealmAuthenticator#doAuthenticate
org.apache.shiro.authc.pam.ModularRealmAuthenticator#doSingleRealmAuthentication
org.apache.shiro.realm.Realm#getAuthenticationInfo
```





## 授权流程解读



```java
#授权操作
subject.checkRole("user");
org.apache.shiro.subject.support.DelegatingSubject#checkRole
org.apache.shiro.mgt.AuthorizingSecurityManager#checkRoles(org.apache.shiro.subject.PrincipalCollection, java.util.Collection<java.lang.String>)
org.apache.shiro.realm.AuthorizingRealm#checkRoles(org.apache.shiro.subject.PrincipalCollection, java.util.Collection<java.lang.String>)
org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo
```



## shiro内置的filter过滤器



核心过滤器类: DefaultFilter 配置哪个路径对应哪个拦截器进行处理

```java
anon(AnonymousFilter.class),
authc(FormAuthenticationFilter.class),
authcBasic(BasicHttpAuthenticationFilter.class),
logout(LogoutFilter.class),
noSessionCreation(NoSessionCreationFilter.class),
perms(PermissionsAuthorizationFilter.class),
port(PortFilter.class),
rest(HttpMethodPermissionFilter.class),
roles(RolesAuthorizationFilter.class),
ssl(SslFilter.class),
user(UserFilter.class);
```



* authc:  org.apache.shiro.web.filter.authc.FormAuthenticationFilter  需要认证登录才能访问
* user: org.apache.shiro.web.filter.authc.UserFilter  用户拦截器，表示必须存在用户
* anon：org.apache.shiro.web.filter.authc.AnonymousFilter  匿名拦截器，不需要登录即可访问的资源，匿名用户或游客，一般用于过滤静态资源
* roles：org.apache.shiro.web.filter.authz.RolesAuthorizationFilter 
  * 角色授权拦截器，验证用户是否拥有角色
  * 参数可写多个，表示某些角色才能通过，多个参数时写roles["admin,user"]，当有多个参数时必须多个参数都通过了才算通过
* perms：org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter
  * 权限授权拦截器，验证用户是否拥有权限
  * 参数可写多个，表示需要某些权限才能通过，多个参数时写perms["user:add,admin:delete"] ，当有多个参数时必须每个参数都通过才算可以。
* authcBasic：org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter  httpBasic身份验证拦截器
* logout：org.apache.shiro.web.filter.authc.LogoutFilter 推出拦截器，执行后会直接调转到shiroFilterFactoryBean.setLogoutUrl();设置的url
* port：org.apache.shiro.web.filter.authz.PortFilter 端口拦截器，可通过的端口
* ssl：org.apache.shiro.web.filter.authz.SslFilter ssl拦截器，只有请求协议时https才能通过





## Shiro数据安全之数据加解密



* 为什么需要加解密

明文数据容易泄露，比如密码明文存储，万一泄露则会造成严重后果

* 什么时散列算法

  一般叫hash，简单的说就是一种将任意长度的消息压缩到某一固定长度消息摘要的函数算法，时候存储密码，比如MD5

* 什么时salt（盐）

如果需要通过散列函数得到加密数据，容易被对应解密网站暴力破解，一般会在应用程序里面加特殊的字符进行处理，比如用户id，例子： 加密数据=MD5(明文密码+用户ID)，破解难度会更大，也可以使用多重散列，比如多次md5

* Shiro里面CredentialsMatcher用来验证密码是否正确

```java
org.apache.shiro.realm.AuthenticatingRealm#assertCredentialsMatch

一般会自定义验证规则

@Bean
public HashedCredentialsMatcher hashedCredentialsMatcher(){
   HashedCredentialsMatcher hashedCredentialsMatcher = new 			HashedCredentialsMatcher();

    //散列算法，使用MD5算法;
    hashedCredentialsMatcher.setHashAlgorithmName("md5");

    //散列的次数，比如散列两次，相当于 md5(md5("xxx"));
    hashedCredentialsMatcher.setHashIterations(2);

    return hashedCredentialsMatcher;
}
```

