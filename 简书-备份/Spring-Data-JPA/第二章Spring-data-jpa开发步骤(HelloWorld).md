## 第二章Spring-data-jpa开发步骤(HelloWorld)

>**使用 Spring Data JPA 进行持久层开发需要的四个步骤:**

- 配置 Spring 整合 JPA
- 在 Spring 配置文件中配置 Spring Data，让 Spring 为声明的接口创建代理对象。配置了 <jpa:repositories> 后，Spring 初始化容器时将会扫描 base-package  指定的包目录及其子目录，为继承 Repository 或其子接口的接口创建代理对象，并将代理对象注册为 Spring Bean，业务层便可以通过 Spring 自动封装的特性来直接使用该对象。
- 声明持久层的接口，该接口继承  Repository，Repository 是一个标记型接口，它不包含任何方法，如必要，Spring Data 可实现 Repository 其他子接口，其中定义了一些常用的增删改查，以及分页相关的方法。
- 在接口中声明需要的方法。Spring Data 将根据给定的策略（具体策略稍后讲解）来为其生成实现代码。


### 环境搭建

>**Maven依赖**

```
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.11</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-jpa</artifactId>
        <version>${spring.data.jpa.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-beans</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-aop</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-tx</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-orm</artifactId>
        <version>${spring.version}</version>
    </dependency>

    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>${hibernate.version}</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-entitymanager</artifactId>
        <version>${hibernate.version}</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-ehcache</artifactId>
        <version>${hibernate.version}</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.35</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>${druid.version}</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>

</dependencies>
```

>**spring配置**

applicationContext.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
      http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
      http://www.springframework.org/schema/context
      http://www.springframework.org/schema/context/spring-context-4.3.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">
    <!--1.配置数据源-->
    <context:property-placeholder location="classpath:db.properties"/>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${jdbc.user}" />
        <property name="password" value="${jdbc.password}" />
        <property name="driverClassName" value="${jdbc.driverClass}" />
        <property name="url" value="${jdbc.url}" />
        <property name="initialSize" value="1" />
        <property name="maxActive" value="8" />
    </bean>
    <!--2.配置JPA的EnitityManagerFactory属性-->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter" />
        </property>
        <property name="packagesToScan" value="club.javalearn.learn.springdata" />
        <property name="jpaProperties">
            <props>
                <prop key="hibernate.show_sql">true</prop>
                <prop key="hibernate.format_sql">true</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
                <prop key="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</prop>
                <prop key="hibernate.ejb.naming_strategy">org.hibernate.cfg.ImprovedNamingStrategy</prop>

            </props>
        </property>
    </bean>
    <!--3.配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager" >
        <property name="entityManagerFactory" ref="entityManagerFactory"/>
    </bean>
    <!--4.配置支持注解事务-->
    <tx:annotation-driven transaction-manager="transactionManager" />
    <!--5.配置springdata-->
    <!--加入命名空间-->
    <!--base-package:扫描@Repository Bean-->
    <jpa:repositories base-package="club.javalearn.learn.springdata" entity-manager-factory-ref="entityManagerFactory">

    </jpa:repositories>
</beans>
```

>**db.properties**

```
jdbc.user=root
jdbc.password=123456
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql:///jpa
```

### 配置步骤

>**1.配置数据源**

```
<!--1.配置数据源-->
<context:property-placeholder location="classpath:db.properties"/>
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="username" value="${jdbc.user}" />
    <property name="password" value="${jdbc.password}" />
    <property name="driverClassName" value="${jdbc.driverClass}" />
    <property name="url" value="${jdbc.url}" />
    <property name="initialSize" value="1" />
    <property name="maxActive" value="8" />
</bean>
```

>**2.配置JPA的EnitityManagerFactory属性**

配置了该属性后,在Entity实体中的属性例如:userName会自动转换成数据库的user_name字段
不需要额外的配置@Column(name="user_name")
<prop key="hibernate.ejb.naming_strategy">org.hibernate.cfg.ImprovedNamingStrategy</prop>

```
<!--2.配置JPA的EnitityManagerFactory属性-->
<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="jpaVendorAdapter">
        <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter" />
    </property>
    <property name="packagesToScan" value="club.javalearn.learn.springdata" />
    <property name="jpaProperties">
        <props>
            <prop key="hibernate.show_sql">true</prop>
            <prop key="hibernate.format_sql">true</prop>
            <prop key="hibernate.hbm2ddl.auto">update</prop>
            <prop key="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</prop>
            <prop key="hibernate.ejb.naming_strategy">org.hibernate.cfg.ImprovedNamingStrategy</prop>

        </props>
    </property>
</bean>
```

>**3.配置事务管理器**

```
<!--3.配置事务管理器-->
<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager" >
    <property name="entityManagerFactory" ref="entityManagerFactory"/>
</bean>
```

>**4.配置支持注解事务**

```
<!--4.配置支持注解事务-->
<tx:annotation-driven transaction-manager="transactionManager" />
```

>**5.配置spring-data-jpa**

```
<!--5.配置springdata-->
<!--加入命名空间-->
<!--base-package:扫描@Repository Bean-->
<jpa:repositories base-package="club.javalearn.learn.springdata" entity-manager-factory-ref="entityManagerFactory">

</jpa:repositories>
```

### Java代码

>**User**

```
package club.javalearn.learn.springdata.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "springdata_user")
public class User {
    private Long id;
    private String userName;
    private String password;
    private String salt;
    private String locked;
    private Date lastLoginDate;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    @Column(name = "last_login_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", locked='" + locked + '\'' +
                ", lastLoginDate=" + lastLoginDate +
                '}';
    }
}
```

>**UserRepository**

这儿需要注意的是UserRepository接口不需要使用Repository注解来标识的,因为SpringData已经把继承Repository接口的类纳入了Spring IOC容器了.

```
package club.javalearn.learn.springdata.repository;

import club.javalearn.learn.springdata.domain.User;
import org.springframework.data.repository.Repository;


/**
 * 1. Repository 是一个空接口,即是一个标记接口
 * 2. 若我们定义的接口继承了Repository,则该接口会被IOC容器识别为一个Repository Bean. 纳入到IOC容器中,进而可以在该接口中定义满足一定规范的方法.
 */
public interface UserRepository extends Repository<User,Long> {
    User getByUserName(String userName);
}
```


### 测试

>**测试用例**

```
package test.springdata;

import club.javalearn.learn.springdata.domain.User;
import club.javalearn.learn.springdata.repository.UserRepository;
import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import javax.sql.DataSource;

public class ApplicationTest {
    private ApplicationContext cxt =null;

    @Before
    public void init(){
        cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void testDataSource(){
        DataSource dataSource = cxt.getBean(DruidDataSource.class);
        try {
            System.out.println(dataSource.getConnection());
        }catch (Exception e){
            e.printStackTrace();
        }
        Assert.notNull(dataSource,"数据源不为空");
    }

    @Test
    public void testJpa(){

    }

    @Test
    public void testUser(){
        UserRepository userRepository = cxt.getBean(UserRepository.class);
        User user = userRepository.getByUserName("root");
        System.out.println(user);
    }
}
```

>**测试结果**

```
/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/bin/java -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=52134:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar:/Applications/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit-rt.jar:/Applications/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit5-rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/tools.jar:/Users/king-pan/git_repository/springdata-parent/springdata-helloworld/target/test-classes:/Users/king-pan/git_repository/springdata-parent/springdata-helloworld/target/classes:/Users/king-pan/.m2/repository/junit/junit/4.11/junit-4.11.jar:/Users/king-pan/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/king-pan/.m2/repository/org/springframework/data/spring-data-jpa/1.11.1.RELEASE/spring-data-jpa-1.11.1.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/data/spring-data-commons/1.13.1.RELEASE/spring-data-commons-1.13.1.RELEASE.jar:/Users/king-pan/.m2/repository/org/aspectj/aspectjrt/1.8.10/aspectjrt-1.8.10.jar:/Users/king-pan/.m2/repository/org/slf4j/slf4j-api/1.7.24/slf4j-api-1.7.24.jar:/Users/king-pan/.m2/repository/org/slf4j/jcl-over-slf4j/1.7.24/jcl-over-slf4j-1.7.24.jar:/Users/king-pan/.m2/repository/org/springframework/spring-webmvc/4.3.10.RELEASE/spring-webmvc-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-expression/4.3.10.RELEASE/spring-expression-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-context/4.3.10.RELEASE/spring-context-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-beans/4.3.10.RELEASE/spring-beans-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-web/4.3.10.RELEASE/spring-web-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-jdbc/4.3.10.RELEASE/spring-jdbc-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-aop/4.3.10.RELEASE/spring-aop-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-tx/4.3.10.RELEASE/spring-tx-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-core/4.3.10.RELEASE/spring-core-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar:/Users/king-pan/.m2/repository/org/springframework/spring-orm/4.3.10.RELEASE/spring-orm-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-core/5.2.10.Final/hibernate-core-5.2.10.Final.jar:/Users/king-pan/.m2/repository/org/jboss/logging/jboss-logging/3.3.0.Final/jboss-logging-3.3.0.Final.jar:/Users/king-pan/.m2/repository/org/hibernate/javax/persistence/hibernate-jpa-2.1-api/1.0.0.Final/hibernate-jpa-2.1-api-1.0.0.Final.jar:/Users/king-pan/.m2/repository/org/javassist/javassist/3.20.0-GA/javassist-3.20.0-GA.jar:/Users/king-pan/.m2/repository/antlr/antlr/2.7.7/antlr-2.7.7.jar:/Users/king-pan/.m2/repository/org/jboss/spec/javax/transaction/jboss-transaction-api_1.2_spec/1.0.1.Final/jboss-transaction-api_1.2_spec-1.0.1.Final.jar:/Users/king-pan/.m2/repository/org/jboss/jandex/2.0.3.Final/jandex-2.0.3.Final.jar:/Users/king-pan/.m2/repository/com/fasterxml/classmate/1.3.0/classmate-1.3.0.jar:/Users/king-pan/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:/Users/king-pan/.m2/repository/org/hibernate/common/hibernate-commons-annotations/5.0.1.Final/hibernate-commons-annotations-5.0.1.Final.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-entitymanager/5.2.10.Final/hibernate-entitymanager-5.2.10.Final.jar:/Users/king-pan/.m2/repository/net/bytebuddy/byte-buddy/1.6.6/byte-buddy-1.6.6.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-ehcache/5.2.10.Final/hibernate-ehcache-5.2.10.Final.jar:/Users/king-pan/.m2/repository/net/sf/ehcache/ehcache/2.10.3/ehcache-2.10.3.jar:/Users/king-pan/.m2/repository/mysql/mysql-connector-java/5.1.35/mysql-connector-java-5.1.35.jar:/Users/king-pan/.m2/repository/com/alibaba/druid/1.1.2/druid-1.1.2.jar:/Users/king-pan/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar:/Users/king-pan/.m2/repository/javax/servlet/jstl/1.2/jstl-1.2.jar" com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit4 test.springdata.ApplicationTest,testUser
objc[1320]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/bin/java (0x10dde74c0) and /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/libinstrument.dylib (0x10ee654e0). One of the two will be used. Which one is undefined.
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
八月 12, 2017 12:38:21 下午 org.hibernate.jpa.internal.util.LogHelper logPersistenceUnitInformation
INFO: HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
八月 12, 2017 12:38:21 下午 org.hibernate.Version logVersion
INFO: HHH000412: Hibernate Core {5.2.10.Final}
八月 12, 2017 12:38:21 下午 org.hibernate.cfg.Environment <clinit>
INFO: HHH000206: hibernate.properties not found
八月 12, 2017 12:38:21 下午 org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
八月 12, 2017 12:38:22 下午 org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.MySQL5InnoDBDialect
Hibernate: 
    
    create table springdata_user (
       id bigint not null,
        last_login_date datetime,
        locked varchar(255),
        password varchar(255),
        salt varchar(255),
        user_name varchar(255),
        primary key (id)
    ) engine=InnoDB
八月 12, 2017 12:38:24 下午 org.hibernate.hql.internal.QueryTranslatorFactoryInitiator initiateService
INFO: HHH000397: Using ASTQueryTranslatorFactory
Hibernate: 
    select
        user0_.id as id1_0_,
        user0_.last_login_date as last_log2_0_,
        user0_.locked as locked3_0_,
        user0_.password as password4_0_,
        user0_.salt as salt5_0_,
        user0_.user_name as user_nam6_0_ 
    from
        springdata_user user0_ 
    where
        user0_.user_name=?
null

Process finished with exit code 0
```
