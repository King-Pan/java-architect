## 第五章之JPA映射关系

>**JPA映射关系分为下列5中**

1.  映射单向多对一的关联关系
2.  映射单向一对多的关联关系
3.  映射双向多对一的关联关系
4.  映射双向一对一的关联关系
5.  映射双向多对多的关联关系

## JPA之单向多对一映射关系

客户订单是最经典的一对多映射关系,一个订单对应一个客户，但是一个客户对应多个订单.

### 特点

1. 多对一使用@ManyToOne注解,放在N端的1的引用
Order是N端，Order.customer上添加@ManyToOne注解
2. Order.customer上添加JoinColumn(name="customer_id")
指定关联外键
3. 可以指定@ManyToOne的fetch属性来设置级联查询的策略
FetchType.EAGER: 默认的加载策略,使用左外连接查询关联属性
FetchType.LAZY: 延迟加载,分2张表查询
4. 新增1和n的时候,建议先保存1的对象,这样少了n条update语句

### 持久化Bean

>**Order**

```
package club.javalearn.learn.jpa.mapping.domain.onetomany;

import javax.persistence.*;

@Entity
@Table(name = "mapping_order")
public class Order {
    private Integer id;
    private String orderName;
    private Customer customer;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "order_name")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    //映射单向多对一的映射关系
    //使用ManyToOne fetch修改关联属性的加载策略
    //使用JoinColumn来映射外键,name属性指定外键列名
    //可使用@ManyToOne的fetch属性来修改默认的关联属性的加载策略.
    //FetchType.EAGER: 默认的加载策略,使用左外连接查询关联属性
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderName='" + orderName + '\'' +
                ", customer=" + customer +
                '}';
    }
}
```

>**Customer**

```
package club.javalearn.learn.jpa.mapping.domain.manytoone;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mapping_customer")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private Integer age;
    private Date createdTime;
    private Date birth;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "last_name")
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

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdTime=" + createdTime +
                ", birth=" + birth +
                '}';
    }
}
```

### 测试

```
package test.jpa;

import club.javalearn.learn.jpa.mapping.domain.manytoone.Customer;
import club.javalearn.learn.jpa.mapping.domain.manytoone.Order;
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

public class MappingTest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init(){
        String persistenceUnit = "jpa-mapping";
        Map<String,Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql",true); // 如果有log4j配置文件则debug打印sql,程序不打印sql
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit,properties);
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

    @Test
    public void testJpa(){

    }


    /**
     * 保存一对多时，建议先保存1的一端，然后再保存n的一端,这样就不会多出额外的n条update语句
     */
    @Test
    public void testPersist(){
        Customer customer = new Customer();
        customer.setAge(21);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("cc.@qq.com");
        customer.setLastName("cc");

        entityManager.persist(customer);


        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderName("床上四件套");
        entityManager.persist(order);

        Order order2 = new Order();
        order2.setCustomer(customer);
        order2.setOrderName("菲力牛排");
        entityManager.persist(order2);
    }

    /**
     * 默认情况下,使用左外连接的方式来获取n的一端的对象和其关联的1的一端的对象.
     * 改为懒加载: @ManyToOne的fetch=FetchType.LAZY,分别查询每个表
     */
    @Test
    public void testFindManyToOne(){
        Order order = entityManager.find(Order.class,3);
        System.out.println(order.getOrderName());
        System.out.println(order.getCustomer().getLastName());
    }

    @Test
    public void testRemoveManyToOne(){
        Order order = entityManager.find(Order.class,2);
        entityManager.remove(order);
    }

    @Test
    public void testOneToManyFind(){
        Customer customer = entityManager.find(Customer.class,4);
        System.out.println(customer);
    }

    @Test
    public void testManyToOneUpdate(){
        Order order = entityManager.find(Order.class,3);
        order.setOrderName("小燕子DVD");
        order.getCustomer().setLastName("小燕子");
        entityManager.merge(order);
    }

    /**
     * 不能直接删除1对多的1，因为外键约束,
     * Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException:
     * Cannot delete or update a parent row: a foreign key constraint fails (`jpa`.`mapping_order`,
     * CONSTRAINT `FK_b863582bcd3c483aa620cc47740`
     * FOREIGN KEY (`customer_id`) REFERENCES `mapping_customer` (`customer_id`))
     */
    @Test
    public void testRemoveOneToMany(){
        Customer customer = entityManager.find(Customer.class,3);
        entityManager.remove(customer);
    }
}
```

## JPA之一对多映射关系

客户订单是最经典的一对多映射关系,一个订单对应一个客户，但是一个客户对应多个订单.

### 特点

1. 一对多使用@OneToMany,放在1端的n的引用(Set<Order> orders)
Customer是1端，Customer.orders上添加@OneToMany注解
2. Customer.orders上添加JoinColumn(name="customer_id")
指定关联外键
3. 可以指定@OneToMany的fetch属性来设置级联查询的策略
FetchType.EAGER: 默认的加载策略,使用左外连接查询关联属性
FetchType.LAZY: 延迟加载,分2张表查询
4. @OneToMany的cascade(指定parent-child关系,1的一端是parent)
CascadeType.REMOVE: 级联删除
CascadeType.PERSIST: 级联持久化（保存）操作（持久保存拥有方实体时，也会持久保存该实体的所有相关数据。）
CascadeType.MERGE: 级联更新（合并）操作（将分离的实体重新合并到活动的持久性上下文时，也会合并该实体的所有相关数据。）
CascadeType.REFRESH: 级联刷新操作 （只会查询获取操作）
CascadeType.DETACH: 没有搞明白(游离)
CascadeType.ALL: 包含以上全部级联操作


### 持久化Bean

>**Order**

```
package club.javalearn.learn.jpa.mapping.domain.onetomany;

import javax.persistence.*;

@Entity
@Table(name = "mapping_order")
public class Order {
    private Integer id;
    private String orderName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "order_name")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderName='" + orderName + '\'' +
                '}';
    }
}
```

>**Customer**

```
package club.javalearn.learn.jpa.mapping.domain.onetomany;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "mapping_customer")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private Integer age;
    private Date createdTime;
    private Date birth;
    private Set<Order> orders;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "last_name")
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

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    //映射单向1-n的关联关系
    //可使用OneToMany注解来映射1-n的关联关系
    //使用@JoinColumn lai 来映射外键列的名称
    @JoinColumn(name = "customer_id")
    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE})
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdTime=" + createdTime +
                ", birth=" + birth +
                ", orders=" + orders +
                '}';
    }
}
```

### 测试

```
package test.jpa;

import club.javalearn.learn.jpa.mapping.domain.onetomany.Customer;
import club.javalearn.learn.jpa.mapping.domain.onetomany.Order;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.*;

public class OneToManyTest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init(){
        String persistenceUnit = "jpa-mapping";
        Map<String,Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql",true); // 如果有log4j配置文件则debug打印sql,程序不打印sql
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit,properties);
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

    @Test
    public void testJpa(){

    }

    @Test
    public void testOneToManyPersist(){
        Customer customer = new Customer();
        customer.setAge(18);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("ccc@qq.com");
        customer.setLastName("ccc");

        //建立连接
        Set<Order> orders = new HashSet<Order>();
        Order order1 = new Order();
        order1.setOrderName("ccc-01-1");

        Order order2 = new Order();
        order2.setOrderName("ccc-02-02");
        orders.add(order1);
        orders.add(order2);
        customer.setOrders(orders);

        //执行保存操作
        entityManager.persist(customer);
        entityManager.persist(order1);
        entityManager.persist(order2);

    }

    @Test
    public void testOneToManyFind(){
        Customer customer = entityManager.find(Customer.class,3);
        System.out.println(customer.getLastName());
        System.out.println(customer.getOrders().size());
    }

    @Test
    public void testOneToManyUpdate(){
        Customer customer = entityManager.find(Customer.class,1);
        customer.setLastName("hahaha");
        Order order = customer.getOrders().iterator().next();
        order.setOrderName("修改名称");
        entityManager.merge(customer);
    }

    /**
     * 默认情况下,若删除1的一端,则会先吧关联的n的一端外键设置为null,
     *
     * 可以通过@OneToMany的cascade属性来修改级联策略.
     */
    @Test
    public void testOneToManyRemove(){
        Customer customer = entityManager.find(Customer.class,2);
        entityManager.remove(customer);
    }

}
```

## JPA之双向多对一映射关系

### 特点

* 双向多对一映射关系,默认情况下如果先保存1端,这会多出N条update语句,如果先保存n端,这多出2N条update语句
* 1端放弃维护关联关系,n端维护关联关系,不会多出n条update语句,提高性能
* 注意:若在1端的@OneToMany中使用mapperby属性放弃维护关联关系,则OneToMany端就不能再使用@JoinColumn注解



### 持久化Bean

>**Customer**

```
package club.javalearn.learn.jpa.mapping.domain.dmo;


import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mapping_customer")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private Integer age;
    private Date createdTime;
    private Date birth;
    private Set<Order> orders = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "last_name")
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

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    //映射单向1-n的关联关系
    //可使用OneToMany注解来映射1-n的关联关系
    //使用@JoinColumn lai 来映射外键列的名称
    //@JoinColumn(name = "customer_id")
    //注意:若在1端的@OneToMany中使用mapperby属性,则OneToMany端就不能再使用@JoinColumn注解
    @OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE},mappedBy = "customer")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdTime=" + createdTime +
                ", birth=" + birth +
                ", orders=" + orders +
                '}';
    }
}
```

>**Order**

```
package club.javalearn.learn.jpa.mapping.domain.dmo;

import javax.persistence.*;

@Entity
@Table(name = "mapping_order")
public class Order {
    private Integer id;
    private String orderName;
    private Customer customer;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "order_name")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderName='" + orderName + '\'' +
                ", customer=" + customer +
                '}';
    }
}
```

### 测试

```
package test.jpa;

import club.javalearn.learn.jpa.mapping.domain.dmo.Customer;
import club.javalearn.learn.jpa.mapping.domain.dmo.Order;
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

public class DoubleManyToOneTest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init(){
        String persistenceUnit = "jpa-mapping";
        Map<String,Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql",true); // 如果有log4j配置文件则debug打印sql,程序不打印sql
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit,properties);
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

    @Test
    public void testJpa(){

    }

    //单向1对多时,关联保存时,一定会多出update语句
    //因为n的一端在插入时不会同时插入外键列

    //若是双向1-n的关联关系，执行保存时，若先保存多的1端，再保存1的一端默认情况下会多出2n条update语句
    //因为2边都维护关联关系,
    //若先保存1的一端,则会多出n条update语句
    //在进行双向1-n关联关系时,建议使用多的一方维护关联关系，而一的一方不维护关联关系,这样会有效的减少SQL语句(update)
    //习近平 和13亿人民,13亿人民知道习近平，习近平不知道13亿人
    //在1的一方@OneToMany注解的mappedBy = "customer",放弃维护关联关系,但是不能定义@JoinColumn了
    @Test
    public void testPersist(){
        Customer customer = new Customer();
        customer.setAge(24);
        customer.setBirth(new Date());
        customer.setCreatedTime(new Date());
        customer.setEmail("吴京.@qq.com");
        customer.setLastName("吴京");
        

        Order order = new Order();
        order.setOrderName("T2000坦克");

        Order order2 = new Order();
        order2.setOrderName("AK47");

        customer.getOrders().add(order);
        customer.getOrders().add(order2);
        order.setCustomer(customer);
        order2.setCustomer(customer);

        entityManager.persist(customer);
        entityManager.persist(order);
        entityManager.persist(order2);
    }
}
```

## JPA之双向一对一映射关系

### 特点

Department: 是一对一关系维护端
Manager: 不维护关系

* 保存时，先保存不维护关系的一端,这样没有update语句,否则多一条update语句
* 查询时,最好获取关系维护的一端(Department),默认使用左外连接查询其关联的对象,并且可以通过@OneToOne的fetch属性修改加载策略
* 不建议通过查询不维护关系的一端(Manager)来获取维护关系的一端(Department)[不建议通过Manager获取Department].

### 持久化Bean

>**Department**

```
package club.javalearn.learn.jpa.mapping.domain.onetoone;


import javax.persistence.*;

@Table(name = "mapping_department")
@Entity
public class Department {

    private Integer id;
    private String deptName;
    private Manager mgr;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "dept_name",length = 50)
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    //使用@OneToOne来映射1-1关联关系
    //若需要在当前数据库表中添加主键则需要使用@JoinColumn来进行映射. 注意1-1关联关系,所有需要添加unique=true
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mgr_id",unique = true)
    public Manager getMgr() {
        return mgr;
    }

    public void setMgr(Manager mgr) {
        this.mgr = mgr;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", deptName='" + deptName + '\'' +
                ", mgr=" + mgr +
                '}';
    }
}
```


>**Manager**

```
package club.javalearn.learn.jpa.mapping.domain.onetoone;


import javax.persistence.*;

@Entity
@Table(name = "mapping_manager")
public class Manager {
    private Integer id;
    private String mgrName;
    private Department dept;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "mgr_name", length = 100)
    public String getMgrName() {
        return mgrName;
    }

    public void setMgrName(String mgrName) {
        this.mgrName = mgrName;
    }
    //mappedBy等于另外1的一端的属性名
    //对于不维护关联关系,没有外键的一方,使用@OneToOne来进行映射,建议设置mappedBy来指定维护关联关系的属性名
    @OneToOne(mappedBy = "mgr")
    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", mgrName='" + mgrName + '\'' +
                ", dept=" + dept +
                '}';
    }
}
```

### 测试

```
package test.jpa;

import club.javalearn.learn.jpa.mapping.domain.onetoone.Department;
import club.javalearn.learn.jpa.mapping.domain.onetoone.Manager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class OneToOneTest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init(){
        String persistenceUnit = "jpa-mapping";
        Map<String,Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql",true); // 如果有log4j配置文件则debug打印sql,程序不打印sql
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit,properties);
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

    @Test
    public void testJpa(){

    }

    /**
     * 先保存没有外键的一方,则没有update语句
     * 先保存有外键的一方，则会多出一条update语句
     */
    @Test
    public void testOneToOnePersist(){
        Manager manager = new Manager();
        manager.setMgrName("IT部经理");
        Department department = new Department();
        department.setDeptName("IT部门");

        //设置关联关系
        manager.setDept(department);
        department.setMgr(manager);

        //执行保存操作
        entityManager.persist(manager);
        entityManager.persist(department);

    }

    /**
     * 默认情况下:
     *   1. 若获取维护关联关系的一方,则会使用左外连接获取其关联的对象(默认)
     *   2. 可以通过@OneToOne的fetch修改加载策略,
     *   FetchType.LAZY: 在使用到关联 属性时再通过左外连接查询
     */
    @Test
    public void testOneToOneFind(){
        Department department = entityManager.find(Department.class,1);
        System.out.println(department.getDeptName());
        System.out.println(department.getMgr().getClass());
        System.out.println(department.getMgr().getMgrName());
    }

    /**
     * 默认情况下:
     *    若获取不维护关联关系的一方，则也会通过左外连接获取其关联的对象
     *    可以通过@OntToOne的fetch属性来修改加载策略，但依然会再发送SQL语句来初始化其关联对象
     *    这说明在不维护关联关系的一方,不建议修改fetch属性
     */
    @Test
    public void testOneToOneFind2(){
        Manager manager = entityManager.find(Manager.class,1);
        System.out.println(manager.getMgrName());
        System.out.println(manager.getDept().getClass().getName());
    }
}
```

## JPA之双向多对多映射关系

### 特点

* 多对多映射关系,需要一端放弃维护关联关系(@ManyToMany(mappedBy = "categories")) 指向维护端的关联对象属性
* 多对多映射关系,每端的查询执行SQL没有差别
* 维护关系的一端使用使用@JoinTable来映射中间表
1. name: 中间表名
2. joinColumns 映射当前类所在表在中间表中的外键
3. joinColumns.name 指定外键列列名
4. joinColumns.referencedColumnName 指定外键列关联当前表的哪一列
5. inverseJoinColumns 映射关联类所在中间表的外键
6. inverseJoinColumns.name 指定外键列列名
7. inverseJoinColumns.referencedColumnName 指定外键列关联不维护关联关系表的哪一列

### 持久化Bean

>**Category**

```
package club.javalearn.learn.jpa.mapping.domain.manytomany;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "mapping_category")
@Entity
public class Category {
    private Integer id;
    private String categoryName;
    private Set<Item> items = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @ManyToMany(mappedBy = "categories")
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
```

>**Item**

```
package club.javalearn.learn.jpa.mapping.domain.manytomany;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mapping_item")
public class Item {

    private Integer id;
    private String itemName;
    private Set<Category> categories = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "item_name")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    //使用@ManyToMany注解来映射多对多关联关系
    //使用@JoinTable来映射中间表
    //name: 中间表名
    //joinColumns 映射当前类所在表在中间表中的外键
    //1. joinColumns.name 指定外键列列名
    //2. joinColumns.referencedColumnName 指定外键列关联当前表的哪一列

    //inverseJoinColumns 映射关联类所在中间表的外键

    @ManyToMany
    @JoinTable(name = "item_category",joinColumns = {@JoinColumn(name = "item_Id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "categoory_id", referencedColumnName = "ID")})
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
```

### 测试


```
package test.jpa;

import club.javalearn.learn.jpa.mapping.domain.manytomany.Category;
import club.javalearn.learn.jpa.mapping.domain.manytomany.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.Map;

public class ManyToManyTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @Before
    public void init(){
        String persistenceUnit = "jpa-mapping";
        Map<String,Object> properties = new HashMap<>();
        properties.put("hibernate.show_sql",true); // 如果有log4j配置文件则debug打印sql,程序不打印sql
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit,properties);
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

    @Test
    public void testJpa(){

    }

    @Test
    public void testManyToManyPersist(){
        Item item1 = new Item();
        item1.setItemName("小米手机");

        Item item2 = new Item();
        item2.setItemName("苹果电脑");

        Category category1 = new Category();
        category1.setCategoryName("手机");

        Category category2 = new Category();
        category2.setCategoryName("电脑");

        //设置关联关系

        item1.getCategories().add(category1);
        item1.getCategories().add(category2);

        item2.getCategories().add(category1);
        item2.getCategories().add(category2);

        category1.getItems().add(item1);
        category1.getItems().add(item2);

        category2.getItems().add(item1);
        category2.getItems().add(item2);

        //执行保存

        entityManager.persist(item1);
        entityManager.persist(item2);

        entityManager.persist(category1);
        entityManager.persist(category2);


    }


    //对于关联的对象默认使用了懒加载的策略
    //使用维护关联关系的还是使用不维护关联关系的一方，sql语句相同
    @Test
    public void testFind(){
        Item item = entityManager.find(Item.class,1);
        System.out.println(item.getItemName());
        System.out.println(item.getCategories().size());
    }

    @Test
    public void testFind2(){
        Category category = entityManager.find(Category.class,1);
        System.out.println(category.getCategoryName());
        System.out.println(category.getItems().size());
    }
}
```

