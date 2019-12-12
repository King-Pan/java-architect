## 第九章之JPA异常


>**4.2以上的版本都直接@Table(name="tableName")来注解表名**

>**4.3以上版本不行了,只能通过@Entity(name="tableName")来注解表名**

### 异常一

>**异常描述**

4.2 版本使用@Entity(name="tableName") 会导致二级缓存时找不到实体

```
异常信息:java.lang.NoSuchMethodError: javax.persistence.Table.indexes()[Ljavax/persistence/Index;
```

>**解决方案**


4.2 版本使用@Entity 和@Table(name="tableName")

### 异常二

```
Caused by: java.lang.ClassNotFoundException: org.hibernate.cache.ehcache.EhCacheRegionFactory
```

>**解决方案**

使用Hibernate-4.3.11.Final版本时报错,换成Hibernate-4.2.4.Final版本正常







### 异常三

>**异常描述**

本文使用的是hibernate-4.2.4.Final版本,所有@Entity(name="tableName")可以替代
@Table(name="tableName"),但是在使用JPQL语句时,会出现如下错误:

```
java.lang.IllegalArgumentException: 
org.hibernate.hql.internal.ast.QuerySyntaxException: 
User is not mapped [FROM User u]
...
Caused by: org.hibernate.hql.internal.ast.QuerySyntaxException: 
User is not mapped [FROM User u]
...
```

>**解决方案**

4.2以上的版本都直接@Table(name="tableName")来注解表名,
4.3以上版本不行了,只能通过@Entity(name="tableName")来注解表名

不使用Entity(name="tableName")这种方式,个人猜测可能是Hibernate版本升级过程中,过度期出现的各种问题。
或者hibernate升级到4.3.x版本以上

```
@Entity
@Table(name = "jpql_user")
```

### 异常四


>**异常描述**

Associations marked as mappedBy must not define database mappings like @JoinTable or 
@JoinColumn: club.javalearn.learn.jpa.mapping.domain.dmo.Customer.orders

```
Caused by: org.hibernate.AnnotationException: Associations marked as mappedBy must not define database mappings like @JoinTable or @JoinColumn: club.javalearn.learn.jpa.mapping.domain.dmo.Customer.orders
	at org.hibernate.cfg.annotations.CollectionBinder.bind(CollectionBinder.java:526)
	at org.hibernate.cfg.AnnotationBinder.processElementAnnotations(AnnotationBinder.java:1956)
	at org.hibernate.cfg.AnnotationBinder.processIdPropertiesIfNotAlready(AnnotationBinder.java:767)
	at org.hibernate.cfg.AnnotationBinder.bindClass(AnnotationBinder.java:686)
	at org.hibernate.cfg.Configuration$MetadataSourceQueue.processAnnotatedClassesQueue(Configuration.java:3466)
	at org.hibernate.cfg.Configuration$MetadataSourceQueue.processMetadata(Configuration.java:3420)
	at org.hibernate.cfg.Configuration.secondPassCompile(Configuration.java:1348)
	at org.hibernate.cfg.Configuration.buildSessionFactory(Configuration.java:1747)
	at org.hibernate.ejb.EntityManagerFactoryImpl.<init>(EntityManagerFactoryImpl.java:96)
	at org.hibernate.ejb.Ejb3Configuration.buildEntityManagerFactory(Ejb3Configuration.java:913)
```

>**解决方案**

```
在1端删除@JoinColumn注解
```

### 异常五

> **异常描述**

```
javax.persistence.TransactionRequiredException: No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call
```

>**解决方案**

**在Dao层或者Service加上事务**

