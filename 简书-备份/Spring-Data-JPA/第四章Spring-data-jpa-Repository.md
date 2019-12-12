## 第四章Spring-data-jpa Repository


![Repository类继承关系图](http://upload-images.jianshu.io/upload_images/6331401-237938b67821ab97.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Repository接口概述

Repository 接口是 Spring Data 的一个核心接口，它不提供任何方法，开发者需要在自己定义的接口中声明需要的方法 

```
public interface Repository<T, ID extends Serializable> { }
```
 
Spring Data可以让我们只定义接口，只要遵循 Spring Data的规范，就无需写实现类。  
与继承 Repository 等价的一种方式，就是在持久层接口上使用 @RepositoryDefinition 注解，并为其指定 domainClass 和 idClass 属性。如下两种方式是完全等价的


### Repository子接口

>**基础的 Repository 提供了最基本的数据访问功能，其几个子接口则扩展了一些功能。它们的继承关系如下：**
 

* Repository： 仅仅是一个标识，表明任何继承它的均为仓库接口类
* CrudRepository： 继承 Repository，实现了一组 CRUD 相关的方法 
* PagingAndSortingRepository： 继承 CrudRepository，实现了一组分页排序相关的方法 
* JpaRepository： 继承 PagingAndSortingRepository，实现一组 JPA 规范相关的方法 
* 自定义的 XxxxRepository 需要继承 JpaRepository，这样的 XxxxRepository 接口就具备了通用的数据访问控制层的能力。
* JpaSpecificationExecutor： 不属于Repository体系，实现一组 JPA Criteria 查询相关的方法 


### CrudRepository子接口

CrudRepository子接口实现了一些常用的增删改查的方法

```
package org.springframework.data.repository;

import java.io.Serializable;

@NoRepositoryBean
public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {

	<S extends T> S save(S entity);

	<S extends T> Iterable<S> save(Iterable<S> entities);

	T findOne(ID id);

	boolean exists(ID id);

	Iterable<T> findAll();

	Iterable<T> findAll(Iterable<ID> ids);

	long count();

	void delete(ID id);

	void delete(T entity);

	void delete(Iterable<? extends T> entities);

	void deleteAll();
}
```

>**CrudRepository 接口提供了最基本的对实体类的添删改查操作**

* T save(T entity);//保存单个实体 
* Iterable<T> save(Iterable<? extends T> entities);//保存集合        
* T findOne(ID id);//根据id查找实体         
* boolean exists(ID id);//根据id判断实体是否存在         
* Iterable<T> findAll();//查询所有实体,不用或慎用!         
* long count();//查询实体数量         
* void delete(ID id);//根据Id删除实体         
* void delete(T entity);//删除一个实体 
* void delete(Iterable<? extends T> entities);//删除一个实体的集合         
* void deleteAll();//删除所有实体,不用或慎用! 

### PagingAndSortingRepository子接口

PagingAndSortingRepository子接口增加了分页和排序方法

```
package org.springframework.data.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoRepositoryBean
public interface PagingAndSortingRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

	/**
	 * Returns all entities sorted by the given options.
	 * 
	 * @param sort
	 * @return all entities sorted by the given options
	 */
	Iterable<T> findAll(Sort sort);

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 * 
	 * @param pageable
	 * @return a page of entities
	 */
	Page<T> findAll(Pageable pageable);
}
```


>**该接口提供了分页与排序功能**
 
* Iterable<T> findAll(Sort sort); //排序 
* Page<T> findAll(Pageable pageable); //分页查询（含排序功能） 


### JpaRepository接口

```
/*
 * Copyright 2008-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * JPA specific extension of {@link org.springframework.data.repository.Repository}.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Mark Paluch
 */
@NoRepositoryBean
public interface JpaRepository<T, ID extends Serializable>
		extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {

	List<T> findAll();

	List<T> findAll(Sort sort);

	List<T> findAll(Iterable<ID> ids);

	<S extends T> List<S> save(Iterable<S> entities);

	void flush();

	<S extends T> S saveAndFlush(S entity);

	void deleteInBatch(Iterable<T> entities);

	void deleteAllInBatch();

	T getOne(ID id);

	@Override
	<S extends T> List<S> findAll(Example<S> example);

	@Override
	<S extends T> List<S> findAll(Example<S> example, Sort sort);
}
``` 

>**该接口提供了JPA的相关功能**

* List<T> findAll(); //查找所有实体 
* List<T> findAll(Sort sort); //排序、查找所有实体 
* List<T> save(Iterable<? extends T> entities);//保存集合 
* void flush();//执行缓存与数据库同步 
* T saveAndFlush(T entity);//强制执行持久化 
* void deleteInBatch(Iterable<T> entities);//删除一个实体集合 


### JpaSpecificationExecutor接口

JpaSpecificationExecutor接口不是Repository接口的子接口，实现一组 JPA Criteria 查询相关的方法 

```
package org.springframework.data.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface to allow execution of {@link Specification}s based on the JPA criteria API.
 * 
 * @author Oliver Gierke
 */
public interface JpaSpecificationExecutor<T> {

	/**
	 * Returns a single entity matching the given {@link Specification}.
	 * 
	 * @param spec
	 * @return
	 */
	T findOne(Specification<T> spec);

	/**
	 * Returns all entities matching the given {@link Specification}.
	 * 
	 * @param spec
	 * @return
	 */
	List<T> findAll(Specification<T> spec);

	/**
	 * Returns a {@link Page} of entities matching the given {@link Specification}.
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<T> findAll(Specification<T> spec, Pageable pageable);

	/**
	 * Returns all entities matching the given {@link Specification} and {@link Sort}.
	 * 
	 * @param spec
	 * @param sort
	 * @return
	 */
	List<T> findAll(Specification<T> spec, Sort sort);

	/**
	 * Returns the number of instances that the given {@link Specification} will return.
	 * 
	 * @param spec the {@link Specification} to count instances for
	 * @return the number of instances
	 */
	long count(Specification<T> spec);
}
```

Specification：封装  JPA Criteria 查询条件。通常使用匿名内部类的方式来创建该接口的对象

后面会单独分析JpaSpecificationExecutor接口
