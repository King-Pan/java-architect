## 第六章之JPA缓存

JPA的一级缓存是通过EntityManager实现的,JPA的二级缓存可以跨EntityManager。

### 配置信息

>**需要在persistence.xml中开启二级缓存,配置二级缓存实现产品**

```
<!--class标签下面,properties标签上-->
<shared-cache-mode>ENABLE_SELECTIVE</shared-cache-mode>
```

>**shared-cache-mode**

* ALL：所有的实体类都被缓存
* NONE：所有的实体类都不被缓存.
* ENABLE_SELECTIVE：标识 @Cacheable(true) 注解的实体类将被缓存
* DISABLE_SELECTIVE：缓存除标识 @Cacheable(false) 以外的所有实体类
* UNSPECIFIED：默认值，JPA 产品默认值将被使用

>**在persistence.xml的properties标签内配置二级缓存相关配置**

```
<!-- 二级缓存相关 -->
<property name="hibernate.cache.use_second_level_cache" value="true"/>
<property name="hibernate.cache.region.factory_class" value="org.hibernate.cache.ehcache.EhCacheRegionFactory"/>
<property name="hibernate.cache.use_query_cache" value="true"/>
```


>**persistence.xml**

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0"
             xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">
    <persistence-unit name="jpa-cache" transaction-type="RESOURCE_LOCAL">
        <!--
        配置使用什么 ORM 产品来作为 JPA 的实现
        1. 实际上配置的是  javax.persistence.spi.PersistenceProvider 接口的实现类
        2. 若 JPA 项目中只有一个 JPA 的实现产品, 则也可以不配置该节点.
        -->
        <provider>org.hibernate.ejb.HibernatePersistence</provider>

        <!-- 添加持久化类 -->
        <class>club.javalearn.learn.jpa.cache.domain.User</class>
        <!--javax.persistence.PersistenceException: No Persistence provider for EntityManager named persistence.xml

        配置二级缓存的策略
        ALL：所有的实体类都被缓存
        NONE：所有的实体类都不被缓存.
        ENABLE_SELECTIVE：标识 @Cacheable(true) 注解的实体类将被缓存
        DISABLE_SELECTIVE：缓存除标识 @Cacheable(false) 以外的所有实体类
        UNSPECIFIED：默认值，JPA 产品默认值将被使用
        -->
        <shared-cache-mode>ENABLE_SELECTIVE</shared-cache-mode>
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

            <!-- 二级缓存相关 -->
            <property name="hibernate.cache.use_second_level_cache" value="true"/>
            <property name="hibernate.cache.region.factory_class" value="org.hibernate.cache.ehcache.EhCacheRegionFactory"/>
            <property name="hibernate.cache.use_query_cache" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

>**ehcache.xml**

详细配置请参考ehcache官网.

```
<ehcache>
    <diskStore path="java.io.tmpdir"/>
    <defaultCache
        maxElementsInMemory="10000"
        eternal="false"
        timeToIdleSeconds="120"
        timeToLiveSeconds="120"
        overflowToDisk="true"
        />
    <cache name="sampleCache1"
        maxElementsInMemory="10000"
        eternal="false"
        timeToIdleSeconds="300"
        timeToLiveSeconds="600"
        overflowToDisk="true"
        />

</ehcache>
```

### 持久化Bean

>**User**

```
package club.javalearn.learn.jpa.cache.domain;

import javax.persistence.*;
import java.util.Date;


@Cacheable
@Entity(name = "cache_user")
public class User {
    private Integer id;
    private String name;
    private String password;
    private String salt;
    private String locked;
    private Date lastLoginDate;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", locked='" + locked + '\'' +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                '}';
    }
}
```

### 测试

>**测试代码**

```
package test.jpa.cache;

import club.javalearn.learn.jpa.cache.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheTest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init() {
        String persistenceUnit = "jpa-cache";
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql", true); // 如果有log4j配置文件则debug打印sql,程序不打印sql
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit, properties);
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @After
    public void destory() {
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void testFirstLevelCache() {
        User user1 = entityManager.find(User.class, 1);
        User user2 = entityManager.find(User.class, 1);
        System.out.println(user1);
        System.out.println(user2);
    }

    @Test
    public void testSecondLevelCache() {
        User user1 = entityManager.find(User.class, 1);
        transaction.commit();
        entityManager.close();
        System.out.println("user1------>" + user1);
        System.out.println("-------------");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
        User user2 = entityManager.find(User.class, 1);
        System.out.println("user2------>" + user2);
    }

    @Test
    public void testPersist() {
        User user = new User();
        user.setName("AA");
        user.setLastLoginDate(new Date());
        user.setLocked("Y");
        user.setPassword("123456");
        user.setSalt("9002jkf");
        entityManager.persist(user);
    }
}
```

>**测试结果**

```
Hibernate: 
    select
        user0_.id as id1_0_0_,
        user0_.last_login_date as last_log2_0_0_,
        user0_.locked as locked3_0_0_,
        user0_.name as name4_0_0_,
        user0_.password as password5_0_0_,
        user0_.salt as salt6_0_0_ 
    from
        cache_user user0_ 
    where
        user0_.id=?
16:22:10,425 DEBUG Loader:942 - Result set row: 0
16:22:10,426 DEBUG Loader:1476 - Result row: EntityKey[club.javalearn.learn.jpa.cache.domain.User#1]
16:22:10,447 DEBUG TwoPhaseLoad:158 - Resolving associations for [club.javalearn.learn.jpa.cache.domain.User#1]
16:22:10,449 DEBUG TwoPhaseLoad:192 - Adding entity to second-level cache: [club.javalearn.learn.jpa.cache.domain.User#1]
16:22:10,460 DEBUG TwoPhaseLoad:277 - Done materializing entity [club.javalearn.learn.jpa.cache.domain.User#1]
16:22:10,462 DEBUG Loader:2133 - Done entity load
16:22:10,464 DEBUG AbstractTransactionImpl:173 - committing
16:22:10,465 DEBUG AbstractFlushingEventListener:144 - Processing flush-time cascades
16:22:10,468 DEBUG AbstractFlushingEventListener:185 - Dirty checking collections
16:22:10,471 DEBUG AbstractFlushingEventListener:118 - Flushed: 0 insertions, 0 updates, 0 deletions to 1 objects
16:22:10,471 DEBUG AbstractFlushingEventListener:125 - Flushed: 0 (re)creations, 0 updates, 0 removals to 0 collections
16:22:10,473 DEBUG EntityPrinter:114 - Listing entities:
16:22:10,473 DEBUG EntityPrinter:121 - club.javalearn.learn.jpa.cache.domain.User{password=123456, salt=9002jkf, name=AA, id=1, lastLoginDate=2017-08-09 14:48:52.0, locked=Y}
16:22:10,475 DEBUG JdbcTransaction:113 - committed JDBC Connection
16:22:10,475 DEBUG JdbcTransaction:126 - re-enabling autocommit
16:22:10,478 DEBUG LogicalConnectionImpl:232 - Releasing JDBC connection
16:22:10,478 DEBUG LogicalConnectionImpl:250 - Released JDBC connection
user1------>User{id=1, name='AA', password='123456', salt='9002jkf', locked='Y', lastLoginDate='2017-08-09 14:48:52.0'}
-------------
16:22:10,481 DEBUG AbstractTransactionImpl:158 - begin
16:22:10,481 DEBUG LogicalConnectionImpl:212 - Obtaining JDBC connection
16:22:10,481 DEBUG LogicalConnectionImpl:218 - Obtained JDBC connection
16:22:10,481 DEBUG JdbcTransaction:69 - initial autocommit status: true
16:22:10,481 DEBUG JdbcTransaction:71 - disabling autocommit
user2------>User{id=1, name='AA', password='123456', salt='9002jkf', locked='Y', lastLoginDate='2017-08-09 14:48:52.0'}
16:22:10,483 DEBUG AbstractTransactionImpl:173 - committing
16:22:10,483 DEBUG AbstractFlushingEventListener:144 - Processing flush-time cascades
16:22:10,483 DEBUG AbstractFlushingEventListener:185 - Dirty checking collections
16:22:10,483 DEBUG AbstractFlushingEventListener:118 - Flushed: 0 insertions, 0 updates, 0 deletions to 1 objects
16:22:10,484 DEBUG AbstractFlushingEventListener:125 - Flushed: 0 (re)creations, 0 updates, 0 removals to 0 collections
16:22:10,484 DEBUG EntityPrinter:114 - Listing entities:
16:22:10,484 DEBUG EntityPrinter:121 - club.javalearn.learn.jpa.cache.domain.User{password=123456, salt=9002jkf, name=AA, id=1, lastLoginDate=2017-08-09 14:48:52.0, locked=Y}
16:22:10,485 DEBUG JdbcTransaction:113 - committed JDBC Connection
16:22:10,485 DEBUG JdbcTransaction:126 - re-enabling autocommit
16:22:10,485 DEBUG LogicalConnectionImpl:232 - Releasing JDBC connection
16:22:10,486 DEBUG LogicalConnectionImpl:250 - Released JDBC connection
16:22:10,486 DEBUG SessionFactoryImpl:1369 - HHH000031: Closing
16:22:10,490  INFO DriverManagerConnectionProviderImpl:160 - HHH000030: Cleaning up connection pool [jdbc:mysql:///jpa]
16:22:10,493 DEBUG EntityManagerFactoryRegistry:108 - Remove: name=jpa-cache
```



