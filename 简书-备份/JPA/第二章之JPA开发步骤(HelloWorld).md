## JPA开发步骤(HelloWorld)

    上文已经说了，JPA与JDBC一样都是一套规范，并没有实现，所有本文中引用Hibernate对JPA的实现。

### JPA持久化Bean步骤

JPA持久化对象的步骤:
>**创建 persistence.xml, 在这个文件中配置持久化单元**

* 需要指定跟哪个数据库进行交互;
* 需要指定 JPA 使用哪个持久化的框架以及配置该框架的基本属性

>**创建实体类, 使用 annotation 来描述实体类跟数据库表之间的映射关系**

>**使用 JPA API 完成数据增加、删除、修改和查询操作**

* 创建 EntityManagerFactory (对应 Hibernate 中的 SessionFactory);
* 创建 EntityManager (对应 Hibernate 中的Session);
* 开启事务
* 进行持久化操作(查询可以不用事务)
* 提交事务
* 关闭EntityManager
* 关闭EntityManagerFactory

### 项目结构

```
+-jpa-parent
----+-jpa-helloworld
--------+-src
------------+-main
----------------+-java
----------------+-resources
--------------------+-META-INF
--------------------+-persistence.xml(jpa配置文件,默认放在此处)
--------------------log4j.xml
------------+-test
----------------+-java(包名以test开头)
----------------+-resources
----pom.xml
```
![IDEA项目结构图](http://upload-images.jianshu.io/upload_images/6331401-df1fc00f54d73af8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Maven依赖

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>jpa-parent</artifactId>
        <groupId>club.javalearn.learn</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>jpa-helloworld</artifactId>
    <packaging>jar</packaging>
    <dependencies>
        <!-- JPA API -->
        <dependency>
            <groupId>org.hibernate.javax.persistence</groupId>
            <artifactId>hibernate-jpa-2.0-api</artifactId>
        </dependency>

        <!-- Hibernate JPA Support -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
        </dependency>

        <!-- log4j -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!-- test -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

### JPA配置文件

>**JPA的配置文件默认放在classpath:META-INF/persistence.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0"
             xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">
    <persistence-unit name="jpa-helloworld" transaction-type="RESOURCE_LOCAL">
        <!--
        配置使用什么 ORM 产品来作为 JPA 的实现
        1. 实际上配置的是  javax.persistence.spi.PersistenceProvider 接口的实现类
        2. 若 JPA 项目中只有一个 JPA 的实现产品, 则也可以不配置该节点.
        -->
        <provider>org.hibernate.ejb.HibernatePersistence</provider>

        <!-- 添加持久化类 -->
        <class>club.javalearn.learn.jpa.domain.Customer</class>
        <!--javax.persistence.PersistenceException: 

        <properties>
            <!-- 连接数据库的基本信息 -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql:///jpa"/>
            <property name="javax.persistence.jdbc.user" value="root"/>
            <property name="javax.persistence.jdbc.password" value="123456"/>

            <!-- 配置 JPA 实现产品的基本属性. 配置 hibernate 的基本属性 -->
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
        </properties>
    </persistence-unit>
</persistence>
```

>**persistence.xml配置中的大坑**

* 配置文件根节点(persistence-unit)的name属性,这儿记住这个属性值,下面需要用，如果不一致会导致异常.

```
No Persistence provider for EntityManager named xxxx
```
### Log4j配置文件(推荐开启日志)

>**开发或者学习中开启日志可以最快最准确的找到错误信息**

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">

<!-- http://wiki.apache.org/logging-log4j/Log4jXmlFormat -->

<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

    <appender name="console" class="org.apache.log4j.ConsoleAppender">
        <param name="Target" value="System.out" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{ABSOLUTE} %5p %c{1}:%L - %m%n" />
        </layout>
    </appender>

    <appender name="file" class="org.apache.log4j.RollingFileAppender">
        <param name="file" value="logs/logger.log" />
        <param name="MaxFileSize" value="5MB" />
        <param name="MaxBackupIndex" value="10" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{MM-dd-yyyy hh:mm:ss,SSS a} %5p %c{1}:%L - %m%n" />
        </layout>

    </appender>

    <root>
        <priority value="debug" />
        <appender-ref ref="console" />
        <appender-ref ref="file" />
    </root>

</log4j:configuration>
```

### 持久化Bean

```
package club.javalearn.learn.jpa.domain;

import javax.persistence.*;

@Entity
@Table(name = "jpa_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String lastName;
    private String email;
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "last_name",length = 50)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
```

### JPA测试代码

```
package test.club.javalearn.learn;

import club.javalearn.learn.jpa.domain.Customer;
import org.junit.Test;

import javax.persistence.*;

public class JPATest {

    @Test
    public void testJpa() {
        String persistenceUnitName = "jpa-helloworld";
        //1. 创建EntityManagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

        //2. EntityManager
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //3. 开启事务
        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();

        //4. 进行持久化操作
        Customer customer = new Customer();
        customer.setEmail("aa@qq.com");
        customer.setLastName("king");
        customer.setAge(18);

        entityManager.persist(customer);

        //5. 提交事务
        entityTransaction.commit();

        //6. 关闭EntityManager
        entityManager.close();

        //7. 关闭EntityManagerFactory
        entityManagerFactory.close();

    }
}
```

>**大坑**

* 创建EntityManagerFactory的persistenceUnitName必须与persistence.xml的name一致.

```
String persistenceUnitName = "jpa-helloworld";
        //1. 创建EntityManagerFactory
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
```
