# SpringBoot之数据相关

* JDBC: 数据源、JdbcTemplate、自动装配
* JPA:实体映射关系、实体操作、自动装配
* 事务: Spring事务抽象、JDBC事务抽象、自动装配

## JDBC

依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

SpringBoot2.x默认依赖于一个新的数据源

```xml
<groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>2.7.9</version>
    <scope>compile</scope>
</dependency>
```

数据源

* javax.sql.DataSource

JdbcTemplate

自动装配

* DataSourceAutoConfiguration



## JPA

> 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

> 实体映射关系

* @javax.persistence.OneToOne
* @javax.persistence.OneToMany
* @javax.persistence.ManyToOne
* @javax.persistence.ManyToMany

> 实体操作

javax.persistence.EntityManager



```java
package javax.persistence;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;

/**
 * Interface used to interact with the persistence context.
 */
public interface EntityManager {

    /**
     * 持久化对象,并且管理该对象
     */
    public void persist(Object entity);

    /**
     * 管理并合并对象
     */
    public <T> T merge(T entity);

    /**
     * 删除对象 
     */
    public void remove(Object entity);

    /**
     * Find by primary key.
     */
    public <T> T find(Class<T> entityClass, Object primaryKey);

    /**
     * Find by primary key, using the specified properties.
     */
    public <T> T find(Class<T> entityClass, Object primaryKey,
                      Map<String, Object> properties);

    /**
     * Find by primary key and lock.
     */
    public <T> T find(Class<T> entityClass, Object primaryKey,
                      LockModeType lockMode);

    /**
     * Find by primary key and lock, using the specified properties.
     */
    public <T> T find(Class<T> entityClass, Object primaryKey,
                      LockModeType lockMode,
                      Map<String, Object> properties);

    /**
     * Get an instance, whose state may be lazily fetched
     */
    public <T> T getReference(Class<T> entityClass,
                                  Object primaryKey);

    /**
     * Synchronize the persistence context to the
     */
    public void flush();

    /**
     * Set the flush mode that applies to all objects contained
     * in the persistence context.
     * @param flushMode  flush mode
     */
    public void setFlushMode(FlushModeType flushMode);

    /**
     * Get the flush mode that applies to all objects contained
     * in the persistence context.
     * @return flushMode
     */
    public FlushModeType getFlushMode();

    /**
     * Lock an entity instance that is contained in the persistenc
     */
    public void lock(Object entity, LockModeType lockMode);

    /**
     * Lock an entity instance that is contained in the persistenc
     */
    public void lock(Object entity, LockModeType lockMode,
                     Map<String, Object> properties);

    /**
     * Refresh the state of the instance from the database,
     */
    public void refresh(Object entity);

    /**
     * Refresh the state of the instance from the database, using
     */
    public void refresh(Object entity,
                            Map<String, Object> properties);

    /**
     * Refresh the state of the instance from the database,
     */
    public void refresh(Object entity, LockModeType lockMode);

    /**
     * Refresh the state of the instance from the database
     */
    public void refresh(Object entity, LockModeType lockMode,
                        Map<String, Object> properties);

    /**
     * Clear the persistence context, causing all managed
     * entities to become detached. Changes made to entities that
     * have not been flushed to the database will not be
     * persisted.
     */
    public void clear();

    /**
     * Remove the given entity from the persistence context, causing
     */
    public void detach(Object entity);

    /**
     * Check if the instance is a managed entity instance belonging
     */
    public boolean contains(Object entity);

    /**
     * Get the current lock mode for the entity instance.
     */
    public LockModeType getLockMode(Object entity);

    /**
     * Set an entity manager property or hint.
     */
    public void setProperty(String propertyName, Object value);

    /**
     * Get the properties and hints and associated values that are in effect
     */
    public Map<String, Object> getProperties();

    /**
     * Create an instance of <code>Query</code> for executing a
     */
    public Query createQuery(String qlString);

    /**
     * Create an instance of <code>TypedQuery</code> for executing a
     */
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);

	/**
	 * Create an instance of Query for executing a criteria
	 */
	public Query createQuery(CriteriaUpdate updateQuery);

	/**
	 * Create an instance of Query for executing a criteria
	 */
	public Query createQuery(CriteriaDelete deleteQuery);

	/**
     * Create an instance of <code>TypedQuery</code> for executing a
     */
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass);

    /**
     * Create an instance of <code>Query</code> for executing a named query
     */
    public Query createNamedQuery(String name);

    /**
     * Create an instance of <code>TypedQuery</code> for executing a
     */
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass);

    /**
     * Create an instance of <code>Query</code> for executing
     */
    public Query createNativeQuery(String sqlString);

    /**
     * Create an instance of <code>Query</code> for executing
     */
    public Query createNativeQuery(String sqlString, Class resultClass);

    /**
     * Create an instance of <code>Query</code> for executing
     */
    public Query createNativeQuery(String sqlString, String resultSetMapping);

	/**
	 * Create an instance of StoredProcedureQuery for executing a
	 */
	public StoredProcedureQuery createNamedStoredProcedureQuery(
			String name);

	/**
	 * Create an instance of StoredProcedureQuery for executing a
	 */
	public StoredProcedureQuery createStoredProcedureQuery(
			String procedureName);

	/**
	 * Create an instance of StoredProcedureQuery for executing a
	 */
	public StoredProcedureQuery createStoredProcedureQuery(
			String procedureName, Class... resultClasses);

	/**
	 * Create an instance of StoredProcedureQuery for executing a
	 */
	public StoredProcedureQuery createStoredProcedureQuery(
			String procedureName, String... resultSetMappings);

	/**
     * Indicate to the entity manager that a JTA transaction is
     */
    public void joinTransaction();

	/**
	 * Determine whether the entity manager is joined to the
	 */
	public boolean isJoinedToTransaction();

	/**
     * Return an object of the specified type to allow access to the
     */
    public <T> T unwrap(Class<T> cls);

    /**
     * Return the underlying provider object for the <code>EntityManager</code>,
     * if available. The result of this method is implementation
     * specific. The <code>unwrap</code> method is to be preferred for new
     * applications.
     * @return underlying provider object for EntityManager
     */
    public Object getDelegate();

    /**
     * Close an application-managed entity manager.
     */
    public void close();

    /**
     * Determine whether the entity manager is open.
     * @return true until the entity manager has been closed
     */
    public boolean isOpen();

    /**
     * Return the resource-level <code>EntityTransaction</code> object.
     */
    public EntityTransaction getTransaction();

    /**
     * Return the entity manager factory for the entity manager.
     */
    public EntityManagerFactory getEntityManagerFactory();

    /**
     * Return an instance of <code>CriteriaBuilder</code> for the creation of
     */
    public CriteriaBuilder getCriteriaBuilder();

    /**
     * Return an instance of <code>Metamodel</code> interface for access to the
     */
    public Metamodel getMetamodel();

	/**
	 * Return a mutable EntityGraph that can be used to dynamically create an EntityGraph.
	 */
	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType);

	/**
	 * Return a mutable copy of the named EntityGraph.  If there is no entity graph with the 
	 */
	public EntityGraph<?> createEntityGraph(String graphName);

	/**
	 * Return a named EntityGraph. The returned EntityGraph should be considered immutable.
	 */
	public EntityGraph<?> getEntityGraph(String graphName);

	/**
	 * Return all named EntityGraphs that have been defined for the provided class type.
	 */
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass);

}
```



> 自动装配

* HibernateJpaAutoConfiguration

## 事务(Transaction)

> 依赖

```java
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-tx</artifactId>
</dependency>
```

> Spring事务抽象

* PlatformTransactionManager

> JDBC事务处理

* DataSourceTransactionManager

> 自动装配

* TransactionAutoConfiguration



