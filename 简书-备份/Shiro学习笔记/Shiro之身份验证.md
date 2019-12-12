## Shiro身份验证

Authenticator 的职责是验证用户帐号，是 Shiro API 中身份验证核心的入口点

```
public AuthenticationInfo authenticate
	(AuthenticationToken authenticationToken)
		throws AuthenticationException;
```

如果验证成功，将返回 AuthenticationInfo 验证信息；此信息中包含了身份及凭证；如果验
证失败将抛出相应的 AuthenticationException 实现。

SecurityManager 接口继承了 Authenticator，另外还有一个 ModularRealmAuthenticator 实现，
其委托给多个 Realm 进行验证，验证规则通过 AuthenticationStrategy 接口指定，默认提供
的实现：
FirstSuccessfulStrategy: 只要有一个 Realm 验证成功即可，只返回第一个 Realm 身份验证
成功的认证信息，其他的忽略；
AtLeastOneSuccessfulStrategy: 只要有一个 Realm 验证成功即可，和 FirstSuccessfulStrategy
不同，返回所有 Realm 身份验证成功的认证信息；
AllSuccessfulStrategy: 所有 Realm 验证成功才算成功，且返回所有 Realm 身份验证成功的
认证信息，如果有一个失败就失败了。
ModularRealmAuthenticator 默认使用 AtLeastOneSuccessfulStrategy 策略。

### Shiro之HelloWorld

> **shiro.ini文件**

```
[users]
zhang=123
wang=123
```

> **测试代码**

```
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;
```

```
/**
 * 1. 使用ini文件存储账号密码
 * 2. 默认使用org.apache.shiro.realm.text.IniRealm
 * 3. 
 */
@Test
public void login(){
	//1. 获取SecurityManager工厂,使用ini文件实例化工厂
	//默认情况下找classpath下的shiro.ini文件
	Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
	//2. 获取SecurityManager,并且把SecurityManager注入到SecurityUtils中
	SecurityManager securityManager = factory.getInstance();
	SecurityUtils.setSecurityManager(securityManager);
	//3. 获取Subject
	Subject subject = SecurityUtils.getSubject();
	//4. 创建用户名/密码身份验证Token（即用户身份/凭证）
	UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
	try {
		//5. 登陆验证
		subject.login(token);
	}catch(IncorrectCredentialsException e2){
		System.out.println("密码错误");
	}catch(UnknownAccountException e1){
		System.out.println("账号不存在");
		e1.printStackTrace();
	} catch (AuthenticationException e) {
		e.printStackTrace();
	}
	//断言 登陆成功
	Assert.assertEquals(true, subject.isAuthenticated());
	
	//6. 登出
	subject.logout();
}
```


### Shiro之自定义Realm

> **MyRealm**

```
package club.javalearn.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class MyRealm implements Realm {

	@Override
	public String getName() {
		return "first-realm";
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		// 仅支持UsernamePasswordToken类型的Token
		return token instanceof UsernamePasswordToken;
	}
	
	/**
	 * 验证用户账号,是Shiro API中身份验证核心入口
	 */
	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal(); // 得到用户名
		String password = new String((char[]) token.getCredentials()); // 得到密码
		if (!"admin".equals(username)) {
			throw new UnknownAccountException(); // 如果用户名错误
		}
		if (!"123456".equals(password)) {
			throw new IncorrectCredentialsException(); // 如果密码错误
		}
		System.out.println("登陆成功--------------");
		// 如果身份认证验证成功，返回一个AuthenticationInfo实现；
		return new SimpleAuthenticationInfo(username, password, getName());
	}

}
```

> **shiro-realm.ini文件**

```
[main]
#定义一个realm
myRealm1=club.javalearn.shiro.realm.MyRealm
#指定securityManager的realms实现
#ini默认使用org.apache.shiro.realm.text.IniRealm
securityManager.realms=$myRealm1
```

> **测试代码**

```
@Test
public void testRealm(){
	//在shiro-realm.ini 中找到realm配置
	//在shiro.ini找到用户密码
	//1. 获取SecurityManager 通过ini文件实例化SecurityManager工厂
	Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
	//2. 获取SecurityManager并注入到SecurityUtils中
	SecurityManager securityManager = factory.getInstance();
	SecurityUtils.setSecurityManager(securityManager);
	//3. 获取Subject对象
	Subject subject = SecurityUtils.getSubject();
	//4. 获取token
	UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
	try {
		//5. 登陆
		subject.login(token);
	}catch(IncorrectCredentialsException e2){
		System.out.println("密码错误");
	}catch(UnknownAccountException e1){
		System.out.println("账号不存在");
		e1.printStackTrace();
	} catch (AuthenticationException e) {
		e.printStackTrace();
	}
	//断言 登陆成功
	Assert.assertEquals(true, subject.isAuthenticated());
	
	//6. 登出
	subject.logout();
}
```
注意看控制台输出: 登陆成功--------------

### Shiro之JdbcRealm

> **shiro-jdbc-realm.ini**

如果数据库没有密码,请注释掉dataSource.password=xxxx

```
[main]
jdbcRealm=org.apache.shiro.realm.jdbc.JdbcRealm
dataSource=com.alibaba.druid.pool.DruidDataSource
dataSource.driverClassName=com.mysql.jdbc.Driver
dataSource.url=jdbc:mysql://localhost:3306/shiro
dataSource.username=root
dataSource.password=xxxx
#如果数据库没有密码,请注释掉dataSource.password=xxxx
jdbcRealm.dataSource=$dataSource
securityManager.realms=$jdbcRealm
```

> **JdbcRealm**

JdbcRealm是Shiro API中默认的Realm实现之一

```
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.shiro.realm.jdbc;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Realm that allows authentication and authorization via JDBC calls.  The default queries suggest a potential schema
 * for retrieving the user's password for authentication, and querying for a user's roles and permissions.  The
 * default queries can be overridden by setting the query properties of the realm.
 * <p/>
 * If the default implementation
 * of authentication and authorization cannot handle your schema, this class can be subclassed and the
 * appropriate methods overridden. (usually {@link #doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)},
 * {@link #getRoleNamesForUser(java.sql.Connection,String)}, and/or {@link #getPermissions(java.sql.Connection,String,java.util.Collection)}
 * <p/>
 * This realm supports caching by extending from {@link org.apache.shiro.realm.AuthorizingRealm}.
 *
 * @since 0.2
 */
public class JdbcRealm extends AuthorizingRealm {

    //TODO - complete JavaDoc

    /*--------------------------------------------
    |             C O N S T A N T S             |
    ============================================*/
    /**
     * The default query used to retrieve account data for the user.
     */
    protected static final String DEFAULT_AUTHENTICATION_QUERY = "select password from users where username = ?";
    
    /**
     * The default query used to retrieve account data for the user when {@link #saltStyle} is COLUMN.
     */
    protected static final String DEFAULT_SALTED_AUTHENTICATION_QUERY = "select password, password_salt from users where username = ?";

    /**
     * The default query used to retrieve the roles that apply to a user.
     */
    protected static final String DEFAULT_USER_ROLES_QUERY = "select role_name from user_roles where username = ?";

    /**
     * The default query used to retrieve permissions that apply to a particular role.
     */
    protected static final String DEFAULT_PERMISSIONS_QUERY = "select permission from roles_permissions where role_name = ?";

    private static final Logger log = LoggerFactory.getLogger(JdbcRealm.class);
    
    /**
     * Password hash salt configuration. <ul>
     *   <li>NO_SALT - password hashes are not salted.</li>
     *   <li>CRYPT - password hashes are stored in unix crypt format.</li>
     *   <li>COLUMN - salt is in a separate column in the database.</li> 
     *   <li>EXTERNAL - salt is not stored in the database. {@link #getSaltForUser(String)} will be called
     *       to get the salt</li></ul>
     */
    public enum SaltStyle {NO_SALT, CRYPT, COLUMN, EXTERNAL};

    /*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    protected DataSource dataSource;

    protected String authenticationQuery = DEFAULT_AUTHENTICATION_QUERY;

    protected String userRolesQuery = DEFAULT_USER_ROLES_QUERY;

    protected String permissionsQuery = DEFAULT_PERMISSIONS_QUERY;

    protected boolean permissionsLookupEnabled = false;
    
    protected SaltStyle saltStyle = SaltStyle.NO_SALT;

    /*--------------------------------------------
    |         C O N S T R U C T O R S           |
    ============================================*/

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/
    
    /**
     * Sets the datasource that should be used to retrieve connections used by this realm.
     *
     * @param dataSource the SQL data source.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Overrides the default query used to retrieve a user's password during authentication.  When using the default
     * implementation, this query must take the user's username as a single parameter and return a single result
     * with the user's password as the first column.  If you require a solution that does not match this query
     * structure, you can override {@link #doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)} or
     * just {@link #getPasswordForUser(java.sql.Connection,String)}
     *
     * @param authenticationQuery the query to use for authentication.
     * @see #DEFAULT_AUTHENTICATION_QUERY
     */
    public void setAuthenticationQuery(String authenticationQuery) {
        this.authenticationQuery = authenticationQuery;
    }

    /**
     * Overrides the default query used to retrieve a user's roles during authorization.  When using the default
     * implementation, this query must take the user's username as a single parameter and return a row
     * per role with a single column containing the role name.  If you require a solution that does not match this query
     * structure, you can override {@link #doGetAuthorizationInfo(PrincipalCollection)} or just
     * {@link #getRoleNamesForUser(java.sql.Connection,String)}
     *
     * @param userRolesQuery the query to use for retrieving a user's roles.
     * @see #DEFAULT_USER_ROLES_QUERY
     */
    public void setUserRolesQuery(String userRolesQuery) {
        this.userRolesQuery = userRolesQuery;
    }

    /**
     * Overrides the default query used to retrieve a user's permissions during authorization.  When using the default
     * implementation, this query must take a role name as the single parameter and return a row
     * per permission with three columns containing the fully qualified name of the permission class, the permission
     * name, and the permission actions (in that order).  If you require a solution that does not match this query
     * structure, you can override {@link #doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)} or just
     * {@link #getPermissions(java.sql.Connection,String,java.util.Collection)}</p>
     * <p/>
     * <b>Permissions are only retrieved if you set {@link #permissionsLookupEnabled} to true.  Otherwise,
     * this query is ignored.</b>
     *
     * @param permissionsQuery the query to use for retrieving permissions for a role.
     * @see #DEFAULT_PERMISSIONS_QUERY
     * @see #setPermissionsLookupEnabled(boolean)
     */
    public void setPermissionsQuery(String permissionsQuery) {
        this.permissionsQuery = permissionsQuery;
    }

    /**
     * Enables lookup of permissions during authorization.  The default is "false" - meaning that only roles
     * are associated with a user.  Set this to true in order to lookup roles <b>and</b> permissions.
     *
     * @param permissionsLookupEnabled true if permissions should be looked up during authorization, or false if only
     *                                 roles should be looked up.
     */
    public void setPermissionsLookupEnabled(boolean permissionsLookupEnabled) {
        this.permissionsLookupEnabled = permissionsLookupEnabled;
    }
    
    /**
     * Sets the salt style.  See {@link #saltStyle}.
     * 
     * @param saltStyle new SaltStyle to set.
     */
    public void setSaltStyle(SaltStyle saltStyle) {
        this.saltStyle = saltStyle;
        if (saltStyle == SaltStyle.COLUMN && authenticationQuery.equals(DEFAULT_AUTHENTICATION_QUERY)) {
            authenticationQuery = DEFAULT_SALTED_AUTHENTICATION_QUERY;
        }
    }

    /*--------------------------------------------
    |               M E T H O D S               |
    ============================================*/

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        Connection conn = null;
        SimpleAuthenticationInfo info = null;
        try {
            conn = dataSource.getConnection();

            String password = null;
            String salt = null;
            switch (saltStyle) {
            case NO_SALT:
                password = getPasswordForUser(conn, username)[0];
                break;
            case CRYPT:
                // TODO: separate password and hash from getPasswordForUser[0]
                throw new ConfigurationException("Not implemented yet");
                //break;
            case COLUMN:
                String[] queryResults = getPasswordForUser(conn, username);
                password = queryResults[0];
                salt = queryResults[1];
                break;
            case EXTERNAL:
                password = getPasswordForUser(conn, username)[0];
                salt = getSaltForUser(username);
            }

            if (password == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }

            info = new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
            
            if (salt != null) {
                info.setCredentialsSalt(ByteSource.Util.bytes(salt));
            }

        } catch (SQLException e) {
            final String message = "There was a SQL error while authenticating user [" + username + "]";
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }

            // Rethrow any SQL errors as an authentication exception
            throw new AuthenticationException(message, e);
        } finally {
            JdbcUtils.closeConnection(conn);
        }

        return info;
    }

    private String[] getPasswordForUser(Connection conn, String username) throws SQLException {

        String[] result;
        boolean returningSeparatedSalt = false;
        switch (saltStyle) {
        case NO_SALT:
        case CRYPT:
        case EXTERNAL:
            result = new String[1];
            break;
        default:
            result = new String[2];
            returningSeparatedSalt = true;
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(authenticationQuery);
            ps.setString(1, username);

            // Execute query
            rs = ps.executeQuery();

            // Loop over results - although we are only expecting one result, since usernames should be unique
            boolean foundResult = false;
            while (rs.next()) {

                // Check to ensure only one row is processed
                if (foundResult) {
                    throw new AuthenticationException("More than one user row found for user [" + username + "]. Usernames must be unique.");
                }

                result[0] = rs.getString(1);
                if (returningSeparatedSalt) {
                    result[1] = rs.getString(2);
                }

                foundResult = true;
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }

        return result;
    }

    /**
     * This implementation of the interface expects the principals collection to return a String username keyed off of
     * this realm's {@link #getName() name}
     *
     * @see #getAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);

        Connection conn = null;
        Set<String> roleNames = null;
        Set<String> permissions = null;
        try {
            conn = dataSource.getConnection();

            // Retrieve roles and permissions from database
            roleNames = getRoleNamesForUser(conn, username);
            if (permissionsLookupEnabled) {
                permissions = getPermissions(conn, username, roleNames);
            }

        } catch (SQLException e) {
            final String message = "There was a SQL error while authorizing user [" + username + "]";
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }

            // Rethrow any SQL errors as an authorization exception
            throw new AuthorizationException(message, e);
        } finally {
            JdbcUtils.closeConnection(conn);
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        return info;

    }

    protected Set<String> getRoleNamesForUser(Connection conn, String username) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<String> roleNames = new LinkedHashSet<String>();
        try {
            ps = conn.prepareStatement(userRolesQuery);
            ps.setString(1, username);

            // Execute query
            rs = ps.executeQuery();

            // Loop over results and add each returned role to a set
            while (rs.next()) {

                String roleName = rs.getString(1);

                // Add the role to the list of names if it isn't null
                if (roleName != null) {
                    roleNames.add(roleName);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Null role name found while retrieving role names for user [" + username + "]");
                    }
                }
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }
        return roleNames;
    }

    protected Set<String> getPermissions(Connection conn, String username, Collection<String> roleNames) throws SQLException {
        PreparedStatement ps = null;
        Set<String> permissions = new LinkedHashSet<String>();
        try {
            ps = conn.prepareStatement(permissionsQuery);
            for (String roleName : roleNames) {

                ps.setString(1, roleName);

                ResultSet rs = null;

                try {
                    // Execute query
                    rs = ps.executeQuery();

                    // Loop over results and add each returned role to a set
                    while (rs.next()) {

                        String permissionString = rs.getString(1);

                        // Add the permission to the set of permissions
                        permissions.add(permissionString);
                    }
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }

            }
        } finally {
            JdbcUtils.closeStatement(ps);
        }

        return permissions;
    }
    
    protected String getSaltForUser(String username) {
        return username;
    }

}
```
> **sql脚本**

```
drop database if exists shiro;
create database shiro;
use shiro;

create table users (
  id bigint auto_increment,
  username varchar(100),
  password varchar(100),
  password_salt varchar(100),
  constraint pk_users primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_users_username on users(username);

create table user_roles(
  id bigint auto_increment,
  username varchar(100),
  role_name varchar(100),
  constraint pk_user_roles primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_user_roles on user_roles(username, role_name);

create table roles_permissions(
  id bigint auto_increment,
  role_name varchar(100),
  permission varchar(100),
  constraint pk_roles_permissions primary key(id)
) charset=utf8 ENGINE=InnoDB;
create unique index idx_roles_permissions on roles_permissions(role_name, permission);

insert into users(username,password)values('zhang','123');
```

> **测试代码**

```
@Test
public void testJdbcRealm(){
	//在shiro-realm.ini 中找到realm配置
	//在shiro.ini找到用户密码
	//1. 获取SecurityManager 通过ini文件实例化SecurityManager工厂
	Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");
	//2. 获取SecurityManager并注入到SecurityUtils中
	SecurityManager securityManager = factory.getInstance();
	SecurityUtils.setSecurityManager(securityManager);
	//3. 获取Subject对象
	Subject subject = SecurityUtils.getSubject();
	//4. 获取token
	UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
	try {
		//5. 登陆
		subject.login(token);
	}catch(IncorrectCredentialsException e2){
		System.out.println("密码错误");
	}catch(UnknownAccountException e1){
		System.out.println("账号不存在");
		e1.printStackTrace();
	} catch (AuthenticationException e) {
		e.printStackTrace();
	}
	//断言 登陆成功
	Assert.assertEquals(true, subject.isAuthenticated());
	
	//6. 登出
	subject.logout();
}
```

### Shiro之多种验证策略

#### Shiro多重验证策略之AtLeastOneSuccessfulStrategy(默认)

通过ModularRealmAuthenticator的doAuthenticate来实现用户验证策略的


```
protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
    assertRealmsConfigured();
    Collection<Realm> realms = getRealms();
    if (realms.size() == 1) {
        return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
    } else {
        return doMultiRealmAuthentication(realms, authenticationToken);
    }
}
```


如果传入的realm集合个数为1,则调用doSingleRealmAuthentication方法

```
protected AuthenticationInfo doSingleRealmAuthentication(Realm realm, AuthenticationToken token) {
    if (!realm.supports(token)) {
        String msg = "Realm [" + realm + "] does not support authentication token [" +
                token + "].  Please ensure that the appropriate Realm implementation is " +
                "configured correctly or that the realm accepts AuthenticationTokens of this type.";
        throw new UnsupportedTokenException(msg);
    }
    AuthenticationInfo info = realm.getAuthenticationInfo(token);
    if (info == null) {
        String msg = "Realm [" + realm + "] was unable to find account data for the " +
                "submitted AuthenticationToken [" + token + "].";
        throw new UnknownAccountException(msg);
    }
    return info;
}
```

如果传入的realm集合个数大于1,则调用doMultiRealmAuthentication方法

```
protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {

    AuthenticationStrategy strategy = getAuthenticationStrategy();

    AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

    if (log.isTraceEnabled()) {
        log.trace("Iterating through {} realms for PAM authentication", realms.size());
    }

    for (Realm realm : realms) {

        aggregate = strategy.beforeAttempt(realm, token, aggregate);

        if (realm.supports(token)) {

            log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);

            AuthenticationInfo info = null;
            Throwable t = null;
            try {
                info = realm.getAuthenticationInfo(token);
            } catch (Throwable throwable) {
                t = throwable;
                if (log.isDebugEnabled()) {
                    String msg = "Realm [" + realm + "] threw an exception during a multi-realm authentication attempt:";
                    log.debug(msg, t);
                }
            }

            aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);

        } else {
            log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
        }
    }

    aggregate = strategy.afterAllAttempts(token, aggregate);

    return aggregate;
}
```

ModularRealmAuthenticator类的默认策略是AtLeastOneSuccessfulStrategy

```
public ModularRealmAuthenticator() {
	this.authenticationStrategy = new AtLeastOneSuccessfulStrategy();
}
```

> **shiro-multi-realm.ini**

```
[main]
#声明一个realm
myRealm1=club.javalearn.shiro.realm.MyRealm
myRealm2=club.javalearn.shiro.realm.MyRealm2
#指定securityManager的realms实现
securityManager.realms=$myRealm1,$myRealm2
```

> **MyRealm**

```
package club.javalearn.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class MyRealm implements Realm {

	@Override
	public String getName() {
		return "first-realm";
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		// 仅支持UsernamePasswordToken类型的Token
		return token instanceof UsernamePasswordToken;
	}
	
	/**
	 * 验证用户账号,是Shiro API中身份验证核心入口
	 */
	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal(); // 得到用户名
		String password = new String((char[]) token.getCredentials()); // 得到密码
		if (!"admin2".equals(username)) {
			System.out.println(getName()+"登陆失败--------------");
			throw new UnknownAccountException(); // 如果用户名错误
		}
		if (!"123456".equals(password)) {
			throw new IncorrectCredentialsException(); // 如果密码错误
		}
		
		// 如果身份认证验证成功，返回一个AuthenticationInfo实现；
		return new SimpleAuthenticationInfo(username, password, getName());
	}

}
```

> **MyRealm2**

```
package club.javalearn.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class MyRealm2 implements Realm {

	@Override
	public String getName() {
		return "second-realm";
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token!=null && token instanceof UsernamePasswordToken;
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal(); // 得到用户名
		String password = new String((char[]) token.getCredentials()); // 得到密码
		if (!"admin".equals(username)) {
			throw new UnknownAccountException(); // 如果用户名错误
		}
		if (!"123456".equals(password)) {
			throw new IncorrectCredentialsException(); // 如果密码错误
		}
		System.out.println(getName()+"登陆成功--------------");
		// 如果身份认证验证成功，返回一个AuthenticationInfo实现；
		return new SimpleAuthenticationInfo(username, password, getName());
	}
	
}
```





> **测试代码**

MultiRealm默认实现策略是:只要有一个成功,即为成功.后文会详细说明
ModularRealmAuthenticator 默认使用 AtLeastOneSuccessfulStrategy 策略。

```
@Test
public void testMultiRealm(){
	//在shiro-realm.ini 中找到realm配置
	//在shiro.ini找到用户密码
	//1. 获取SecurityManager 通过ini文件实例化SecurityManager工厂
	Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-multi-realm.ini");
	//2. 获取SecurityManager并注入到SecurityUtils中
	SecurityManager securityManager = factory.getInstance();
	SecurityUtils.setSecurityManager(securityManager);
	//3. 获取Subject对象
	Subject subject = SecurityUtils.getSubject();
	//4. 获取token
	UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456");
	try {
		//5. 登陆
		subject.login(token);
	}catch(IncorrectCredentialsException e2){
		System.out.println("密码错误");
	}catch(UnknownAccountException e1){
		System.out.println("账号不存在");
		e1.printStackTrace();
	} catch (AuthenticationException e) {
		e.printStackTrace();
	}
	//断言 登陆成功
	Assert.assertEquals(true, subject.isAuthenticated());
	
	//6. 登出
	subject.logout();
}
```

#### Shiro多重验证策略之FirstSuccessfulStrategy(默认)

 FirstSuccessfulStrategy: 只要有一个 Realm 验证成功即可,
 只返回第一个 Realm 身份验证 成功的认证信息，其他的忽略.
 
 
 AuthenticationStrategy 主要有4个方法
 
```
package org.apache.shiro.authc.pam;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

public interface AuthenticationStrategy {

    AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException;

    AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException;

    AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t)
            throws AuthenticationException;

    AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException;
}

```

* beforeAllAttempts 在所有用户验证尝试之前
* beforeAttempt 用户验证尝试之前
* afterAttempt 用户尝试之后
* afterAllAttempts 所有的用户尝试之后
* 不同的策略模式重写了上面不同的方法,从而实现不同的用户验证策略

```
package org.apache.shiro.authc.pam;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

public abstract class AbstractAuthenticationStrategy implements AuthenticationStrategy {

    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();
    }

    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }

    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        AuthenticationInfo info;
        if (singleRealmInfo == null) {
            info = aggregateInfo;
        } else {
            if (aggregateInfo == null) {
                info = singleRealmInfo;
            } else {
                info = merge(singleRealmInfo, aggregateInfo);
            }
        }

        return info;
    }

    protected AuthenticationInfo merge(AuthenticationInfo info, AuthenticationInfo aggregate) {
        if( aggregate instanceof MergableAuthenticationInfo ) {
            ((MergableAuthenticationInfo)aggregate).merge(info);
            return aggregate;
        } else {
            throw new IllegalArgumentException( "Attempt to merge authentication info from multiple realms, but aggregate " +
                      "AuthenticationInfo is not of type MergableAuthenticationInfo." );
        }
    }

    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }
}
```
