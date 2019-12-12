## 第一章Spring-data-jpa简介

介绍Spring-data-jpa之前，我们需要先了解一下Spring-data是什么?

>**Spring官网简介**

```
Spring Data’s mission is to provide a familiar and consistent, Spring-based programming model for data access while still retaining the special traits of the underlying data store. 

It makes it easy to use data access technologies,
 relational and non-relational databases, map-reduce frameworks, 
 and cloud-based data services. 
 This is an umbrella project which contains many subprojects that 
 are specific to a given database. The projects are developed by working 
 together with many of the companies and developers that are behind these 
 exciting technologies.
```

>**翻译**

```
Spring data的任务是提供一个熟悉的、一致的、基于Spring的数据访问编程模型，同时仍然保留底层数据存储的特殊特性。
它可以轻松地使用数据访问技术、关系数据库和非关系数据库、map-reduce框架和基于云的数据服务。这是一个大型项目，包含许多子项目是特定于一个给定的数据库。这些项目是通过与这些令人兴奋的技术背后的许多公司和开发人员合作开发的。
```

Spring Data : 

	Spring 的一个子项目。用于简化数据库访问，支持NoSQL 和 关系数据存储。
	其主要目标是使数据库的访问变得方便快捷。

>**SpringData 项目所支持 NoSQL 存储**

* MongoDB （文档数据库）
* Neo4j（图形数据库）
* Redis（键/值存储）
* Hbase（列族数据库）

>**SpringData 项目所支持的关系数据存储技术：**

* JDBC
* JPA

### Spring-data-jpa 简介

JPA Spring Data : 

	致力于减少数据访问层 (DAO) 的开发量. 开发者唯一要做的，
	就只是声明持久层的接口，其他都交给 Spring Data JPA 来帮你完成！

框架怎么可能代替开发者实现业务逻辑呢？

比如：当有一个 UserDao.findUserById()  这样一个方法声明，大致应该能判断出这是根据给定条件的 ID 查询出满足条件的 User  对象。

Spring Data JPA 做的便是规范方法的名字，根据符合规范的名字来确定方法需要实现什么样的逻辑。

