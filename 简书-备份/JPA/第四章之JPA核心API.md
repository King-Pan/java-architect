## JPA核心API

jpa主要包含以下接口和类:
   Persistence、EntityManagerFactory、EntityManager、EntityTransaction

### Persistence

Persistence 类是用于获取 EntityManagerFactory 实例。

该类包含一个名为 createEntityManagerFactory 的 静态方法 。
createEntityManagerFactory 方法有如下两个重载版本。
带有一个参数的方法以 JPA 配置文件 persistence.xml 中的持久化单元名为参数
带有两个参数的方法：前一个参数含义相同，后一个参数 Map类型，用于设置 JPA 的相关属性，这时将忽略其它地方设置的属性。Map 对象的属性名必须是 JPA 实现库提供商的名字空间约定的属性名。

```
//第一种
String persistenceUnit = "jpa-annotation";
EntityManagerFactory entityManagerFactory = 
	Persistence.createEntityManagerFactory(persistenceUnit);
//第二种
 String persistenceUnit = "jpa-api";
Map<String,Object> properties = new HashMap<>();
// 如果有log4j配置文件则debug打印sql,程序不打印sql
properties.put("hibernate.show_sql",false); 
EntityManagerFactory entityManagerFactory = 
	Persistence.createEntityManagerFactory(persistenceUnit,properties);
```

### EntityManagerFactory

EntityManagerFactory 接口主要用来创建 EntityManager 实例。

>**该接口约定了如下4个方法：**

* createEntityManager()：用于创建实体管理器对象实例。
* createEntityManager(Map map)：用于创建实体管理器对象实例的重载方法，Map 参数用于提供 EntityManager 的属性。
* isOpen()：检查 EntityManagerFactory 是否处于打开状态。实体管理器工厂创建后一直处于打开状态，除非调用close()方法将其关闭。
* close()：关闭 EntityManagerFactory 。 EntityManagerFactory 关闭后将释放所有资源，isOpen()方法测试将返回 false，其它方法将不能调用，否则将导致IllegalStateException异常。
* 其他的方法

```
String persistenceUnit = "jpa-api";
Map<String,Object> properties = new HashMap<>();
//配置的必须是jpa实现产品中的属性,如果不是，则忽略.
// 如果有log4j配置文件则debug打印sql,程序不打印sql
properties.put("hibernate.show_sql",false); 
EntityManagerFactory entityManagerFactory = 
	Persistence.createEntityManagerFactory(persistenceUnit,properties);
EntityManager entityManager = entityManagerFactory.createEntityManager();
```


### EntityManager
在 JPA 规范中, EntityManager 是完成持久化操作的核心对象。实体作为普通 Java 对象，只有在调用 EntityManager 将其持久化后才会变成持久化对象。EntityManager 对象在一组实体类与底层数据源之间进行 O/R 映射的管理。它可以用来管理和更新 Entity Bean, 根椐主键查找 Entity Bean, 还可以通过JPQL语句查询实体。

>**实体的状态:**

* 新建状态:   新创建的对象，尚未拥有持久性主键。
* 持久化状态：已经拥有持久性主键并和持久化建立了上下文环境
* 游离状态：拥有持久化主键，但是没有与持久化建立上下文环境
* 删除状态:  拥有持久化主键，已经和持久化建立上下文环境，但是从数据库中删除。

>**持久化Bean**

```
package club.javalearn.learn.jpa.api.domain;

import javax.persistence.*;
import java.util.Date;

@Table(name = "sys_user")
@Entity
public class User {
    private Long id;
    private String userName;
    private String password;
    private String salt;
    private String locked;
    private Date lastLoginDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "user_name",length = 50)
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

>**Junit测试用例**

```
package test.club.javalearn.learn.jpa.api;

import club.javalearn.learn.jpa.api.domain.User;
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

public class JPAApiTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init(){
        String persistenceUnit = "jpa-api";
        Map<String,Object> properties = new HashMap<>();
        // 如果有log4j配置文件则debug打印sql,程序不打印sql
        properties.put("hibernate.show_sql",false); 
        properties.put("a","aaaa");
        entityManagerFactory = 
        		Persistence.createEntityManagerFactory(persistenceUnit,properties);
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();
    }

    @After
    public void destory(){
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }
}
```

#### find(等同于hibernate的get方法)

>**find**

*find (Class<T> entityClass,Object primaryKey)：*

返回指定的 OID 对应的实体类对象，如果这个实体存在于当前的持久化环境，则返回一个被缓存的对象；否则会创建一个新的 Entity, 并加载数据库中相关信息；若 OID 不存在于数据库中，则返回一个 null。第一个参数为被查询的实体类类型，第二个参数为待查找实体的主键值。

>**测试用例**

```
@Test
public void testFind(){
    User user = entityManager.find(User.class,1L);
    System.out.println("---------------");
    System.out.println(user);
}
```

>**测试结果**

```
16:05:09,933 DEBUG Loader:2129 - Done entity load
---------------
User{id=1, userName='test', password='123456', salt='1111', locked='null', lastLoginDate=2017-08-07 23:26:11.0}
```

>**find方法特点**

* find方法与hibernate的get方法功能相同,
* 如果查询不到数据返回null

#### getReference(等同于hibernate的load方法)

>**getReference**

*getReference (Class<T> entityClass,Object primaryKey)：*

与find()方法类似，不同的是：如果缓存中不存在指定的 Entity, EntityManager 会创建一个 Entity 类的代理，但是不会立即加载数据库中的信息，只有第一次真正使用此 Entity 的属性才加载，所以如果此 OID 在数据库不存在，getReference() 不会返回 null 值, 而是抛出EntityNotFoundException(如果连接关闭会导致延迟加载错误)

>**测试用例**

```
@Test
    public void testGetReference(){
        User user = entityManager.getReference(User.class,1L);
        System.out.println("<-------testGetReference-------->");
        System.out.println(user);
        System.out.println(user.getClass());
    }
```

>**测试结果**

```
<-------testGetReference-------->
16:10:20,306 DEBUG SessionImpl:1003 - Initializing proxy: [club.javalearn.learn.jpa.api.domain.User#1]
16:10:20,307 DEBUG Loader:2105 - Loading entity: [club.javalearn.learn.jpa.api.domain.User#1]
16:10:20,323 DEBUG SQL:104 - 
    select
        user0_.id as id1_0_0_,
        user0_.last_login_date as last2_0_0_,
        user0_.locked as locked3_0_0_,
        user0_.password as password4_0_0_,
        user0_.salt as salt5_0_0_,
        user0_.user_name as user6_0_0_ 
    from
        sys_user user0_ 
    where
        user0_.id=?
16:10:20,358 DEBUG Loader:941 - Result set row: 0
16:10:20,358 DEBUG Loader:1475 - Result row: EntityKey[club.javalearn.learn.jpa.api.domain.User#1]
16:10:20,376 DEBUG TwoPhaseLoad:158 - Resolving associations for [club.javalearn.learn.jpa.api.domain.User#1]
16:10:20,382 DEBUG TwoPhaseLoad:277 - Done materializing entity [club.javalearn.learn.jpa.api.domain.User#1]
16:10:20,383 DEBUG Loader:2129 - Done entity load
User{id=1, userName='test', password='123456', salt='1111', locked='null', lastLoginDate=2017-08-07 23:26:11.0}
class club.javalearn.learn.jpa.api.domain.User_$$_javassist_0
```

>**getReference方法特点**

* 功能与hibernate的load方法相同,延迟加载
* 返回的是一个代理对象
* 如果不存在则抛出异常
* 关闭连接后可能导致懒加载异常.

#### persistence(等同于hibernate的save方法)

>**persist**

*persist (Object entity)：*

用于将新创建的 Entity 纳入到 EntityManager 的管理。该方法执行后，传入 persist() 方法的 Entity 对象转换成持久化状态。
如果传入 persist() 方法的 Entity 对象已经处于持久化状态，则 persist() 方法什么都不做。
如果对删除状态的 Entity 进行 persist() 操作，会转换为持久化状态。
如果对游离状态的实体执行 persist() 操作，可能会在 persist() 方法抛出 EntityExistException(也有可能是在flush或事务提交后抛出)。

>**测试用例**

```
@Test
public void testPersist(){
    User user = new User();
    user.setUserName("admin");
    user.setPassword("admin");
    user.setLastLoginDate(new Date());
    user.setSalt("haha");
    user.setLocked("N");
    //user.setId(1000L);
    entityManager.persist(user);
    System.out.println(user.getId());
}
```

>**测试结果**

```
16:18:53,221 DEBUG SQL:104 - 
    insert 
    into
        sys_user
        (last_login_date, locked, password, salt, user_name) 
    values
        (?, ?, ?, ?, ?)
16:18:53,259 DEBUG IdentifierGeneratorHelper:93 - Natively generated identity: 5
5
```

>**persist方法特点**

* 功能与hibernate的save方法相同
* persist方法不能设置id属性,否则抛出异常:javax.persistence.PersistenceException: org.hibernate.PersistentObjectException


#### remove(等同于Hibernate的delete方法)

>**remove**

*remove (Object entity)：*

删除实例。如果实例是被管理的，即与数据库实体记录关联，则同时会删除关联的数据库记录。

>**测试用例**

```
@Test
public void testRemove(){
    //User user = new User();
    //user.setId(5);
    User user = entityManager.find(User.class,5L);
    entityManager.remove(user);
}
```

>**测试结果**

```
16:26:08,897 DEBUG SQL:104 - 
    delete 
    from
        sys_user 
    where
        id=?
```

>**remove方法特点**

* 功能与hibernate的delete方法相同
* remove方法只能删除持久化对象，不能通过id删除对象,否则抛出异常:java.lang.IllegalArgumentException: Removing a detached instance xxx.User


#### merge(等同于Hibernate的saveOrUpdate方法)

>**merge**

*merge (T entity)：*

merge() 用于处理 Entity 的同步。即数据库的插入和更新操作

>**测试用例**

```
@Test
public void testMerge(){
    User user = new User();
    user.setLocked("Y");
    user.setSalt("aaa");
    user.setLastLoginDate(new Date());
    user.setUserName("mayun");
    user.setPassword("i am mayun");
    User user2 = entityManager.merge(user);
    System.out.println("user.id="+user.getId());
    System.out.println("user2.id="+user2.getId());
}
```

>**测试结果**

```
16:41:59,929 DEBUG SQL:104 - 
    insert 
    into
        sys_user
        (last_login_date, locked, password, salt, user_name) 
    values
        (?, ?, ?, ?, ?)
16:41:59,980 DEBUG IdentifierGeneratorHelper:93 - Natively generated identity: 6
user.id=null
user2.id=6
```

>**merge方法流程图**

![](file:////Users/king-pan/Pictures/jpa_merge.png)

>**merge方法特点**

* 功能与Hibernate的saveOrUpdate方法相同
* 1. 临时对象-->创建一个新的对象拷贝临时对象的属性-->jpa进行持久化操作(insert)
* 2. 游离对象(即传入的对象有 OID)-->缓存中存在-->数据库中存在-->新建新对象拷贝游离对象的属性-->jpa进行持久化操作(update)
* 3. 游离对象-->缓存中不存在-->数据库中存在-->从数据库中加载对象-->游离对象的属性拷贝到持久化对象上-->jpa进行持久化操作(update)
* 4. 游离对象-->缓存中不存在-->数据库中不存在-->创建一个新的对象拷贝临时对象的属性-->jpa进行持久化操作(insert)



#### 创建Query方法

>**createQuery**

*createQuery (String qlString)：*

创建一个查询对象。

>**createNamedQuery**

*createNamedQuery (String name)：*

根据命名的查询语句块创建查询对象。参数为命名的查询语句。

>**createNativeQuery**

*createNativeQuery (String sqlString)：*

使用标准 SQL语句创建查询对象。参数为标准SQL语句字符串。

*createNativeQuery (String sqls, String resultSetMapping)：*

使用标准SQL语句创建查询对象，并指定返回结果集 Map的 名称。

#### 其他方法

>**flush**

*flush ()：*

同步持久上下文环境，即将持久上下文环境的所有未保存实体的状态信息保存到数据库中。

transcation.commit()方法会调用EntityManager.flush()方法,也可以手动刷新

>**refresh**

*refresh (Object entity)：*

用数据库实体记录的值更新实体对象的状态，即更新实例的属性值。

>**setFlushMode**

*setFlushMode (FlushModeType flushMode)：*

设置持久上下文环境的Flush模式。参数可以取2个枚举
* FlushModeType.AUTO 为自动更新数据库实体，
* FlushModeType.COMMIT 为直到提交事务时才更新数据库记录。

>**getFlushMode**

*getFlushMode ()：*

获取持久上下文环境的Flush模式。返回FlushModeType类的枚举值。(默认是自动提交)

*测试用例*

```
@Test
public void testGetFlushMode(){
    System.out.println(entityManager.getFlushMode());
}
```



>**clear**

*clear ()：*

清除持久上下文环境，断开所有关联的实体。如果这时还有未提交的更新则会被撤消。

>**contains**

*contains (Object entity)：*

判断一个实例是否属于当前持久上下文环境管理的实体。

>**isOpen**

*isOpen ()：*

判断当前的实体管理器是否是打开状态。

>**getTransaction**

*getTransaction ()：*

返回资源层的事务对象。EntityTransaction实例可以用于开始和提交多个事务。

>**close**

*close ()：*

关闭实体管理器。之后若调用实体管理器实例的方法或其派生的查询对象的方法都将抛出 IllegalstateException 异常，除了getTransaction 和 isOpen方法(返回 false)。不过，当与实体管理器关联的事务处于活动状态时，调用 close 方法后持久上下文将仍处于被管理状态，直到事务完成。


### EntityTransaction

EntityTransaction 接口用来管理资源层实体管理器的事务操作。通过调用实体管理器的getTransaction方法 获得其实例。

#### 常用API

>**begin方法**

*begin()*

用于启动一个事务，此后的多个数据库操作将作为整体被提交或撤消。若这时事务已启动则会抛出 IllegalStateException 异常。

>**commit方法**

*commit()*

用于提交当前事务。即将事务启动以后的所有数据库更新操作持久化至数据库中。

>**rollback方法**

*rollback()*

撤消(回滚)当前事务。即撤消事务启动后的所有数据库更新操作，从而不对数据库产生影响。

>**setRollbackOnly方法**

*setRollbackOnly()*

使当前事务只能被撤消。

>**getRollbackOnly方法**

*getRollbackOnly()*

查看当前事务是否设置了只能撤消标志。
