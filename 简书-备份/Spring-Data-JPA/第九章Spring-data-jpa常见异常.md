## 第九章Spring-data-jpa常见异常

### 异常一


>**问题描述**

```
org.springframework.beans.factory.BeanCreationException: 
Error creating bean with name 'userRepository': Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: Validation failed for query for method public abstract java.util.List club.javalearn.learn.springdata.repository.UserRepository.findUsedUser(java.lang.String)!s
```
>**解决方案**

在Entity实体上不要使用@Table(name="tableName")

改为:
@Entity
@Table(name="tableName")
spring-data-jpa使用@Entity和@Table(name="tableName")

### 异常二

>**问题描述**

```
org.springframework.dao.InvalidDataAccessApiUsageException: 
	org.hibernate.hql.internal.QueryExecutionRequestException: 
	Not supported for DML operations 
	[UPDATE club.javalearn.learn.springdata.domain.User u set u.password = :password where u.id = :id]; nested exception is java.lang.IllegalStateException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [UPDATE club.javalearn.learn.springdata.domain.User u set u.password = :password where u.id = :id]
```

>**解决方案**

上面的错误的信息: QueryExecution 不能执行DML语句

1. 在@Query注解下面添加@Modifying注解
2. 此时需要Service层，并且在调用repository的update或者delete方法上面加上事务注解@Transactional(如果没有添加事务注解,将会报出异常三)


### 异常三

>**问题描述**

```
org.springframework.dao.InvalidDataAccessApiUsageException: 
	Executing an update/delete query; 
	nested exception is javax.persistence.TransactionRequiredException: 
	Executing an update/delete query
	
Caused by: javax.persistence.TransactionRequiredException: Executing an update/delete query
	at org.hibernate.query.internal.AbstractProducedQuery.executeUpdate(AbstractProducedQuery.java:1496)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
```

>**解决方案**

此时需要Service层，并且在调用repository的update或者delete方法上面加上事务注解@Transactional
