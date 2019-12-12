## 第三章Spring-data-jpa方法规范

>**在Repository子接口中声明select方法如果没有@Query注解那么就必须遵守一定的规范:**

1. 查询必须以: find|read|get
2. 涉及条件查询时,条件的属性用条件关键字连接
3. 要注意的是条件首字母大写
4. 支持属性的级联查询,首先匹配本类的属性,默认查询本类的属性,若当前类有符合条件的属性,则优先使用,而不使用级别属性. 若需要使用级联属性,则属性之间使用_进行连接
5. spring-data-jpa 不支持insert操作,可以使用JpaRepository提供的save方法

### 实体Bean

>**User**

```
package club.javalearn.learn.springdata.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "springdata_user")
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

>**Person**

```
package club.javalearn.learn.springdata.domain;

import javax.persistence.*;

@Entity(name = "springdata_person")
public class Person {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private Address address;
    private Long addressId;

    @Column(name = "add_id")
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @JoinColumn(name = "address_id")
    @ManyToOne
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}
```

>**Address**

```
package club.javalearn.learn.springdata.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "springdata_address")
public class Address {
    private Long id;
    private String procince;
    private Set<Person> persons = new HashSet<>();

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcince() {
        return procince;
    }

    public void setProcince(String procince) {
        this.procince = procince;
    }

    @OneToMany
    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", procince='" + procince + '\'' +
                ", persons=" + persons +
                '}';
    }
}
```

### select(查询方法)规范命名

直接在接口中定义查询方法，如果是符合规范的，可以不用写实现，目前支持的关键字写法如下：

![关键字一](http://upload-images.jianshu.io/upload_images/6331401-04254026a8d35ec4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![关键字二](http://upload-images.jianshu.io/upload_images/6331401-a1d859c595098fe7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 测试代码

```
package club.javalearn.learn.springdata.repository;

import club.javalearn.learn.springdata.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 1. Repository 是一个空接口,即是一个标记接口
 * 2. 若我们定义的接口继承了Repository,则该接口会被IOC容器识别为一个Repository Bean. 纳入到IOC容器中,进而可以在该接口中定义满足一定规范的方法.
 * 3. 也可以通过@RepositoryDefintion 注解来替代继承Repository接口
 * 在Repository子接口中声明方法
 * 1. 不是随便声明的,而需要符合一定的规范
 * 2. 查询方法以find | read | get 开头
 * 3. 涉及条件查询时,条件的属性用条件关键字连接,要注意的是以首字母大写
 * 4. 支持属性的级联查询,首先匹配本类的属性,默认查询本类的属性,若当前类有符合条件的属性,则优先使用,而不使用级别属性. 若需要使用级联属性,则属性之间使用_进行连接
 *
 * JPA方法规范优缺点:
 *
 */
public interface UserRepository extends Repository<User,Long> {
    //根据userName 来获取对应的User
    // WHERE user_name = ?
    User getByUserName(String userName);

    // WHERE user_name like ?
    // 需要在参数前后加上%_匹配符
    List<User> getByUserNameLike(String userName);

    // WHERE user_name = ? and password = ?
    User findByUserNameAndPassword(String userName,String password);

    // WHERE user_name = ? or id = ?
    User findByUserNameOrId(String userName,Long id);

    // WHERE id < ?
    List<User> findByIdLessThan(Long id);

    // WHERE id <= ?
    List<User> findByIdLessThanEqual(Long id);

    // WHERE id > ?
    List<User> findByIdIsGreaterThan(Long id);

    // WHERE id >= ?
    List<User> findByIdGreaterThanEqual(Long id);
}
```

### @Query(查询方法)不规范命名

select(查询方法)不规范命名那么就必须使用@Query注解或者更复杂的查询使用自定义的Repository方法,这儿我们只介绍@Query方法


#### 测试代码

```
// 使用@Query 注解可以自定义JPQL 语句以实现更灵活的查询
// @Query注解传递参数 ?1,?2 顺序传递
@Query("select u from User u where  u.locked = ?1")
List<User> findUsedUser(String locked);

//为@Query传递参数
//1. 占位符 ?1,?2 按照顺序
@Query("select u from User  u where u.userName=?1 and u.password =?2")
User login(String userName,String password);

//2. 命名参数
@Query("select u from User  u where u.userName=:userName and u.password =:password")
User login2(@Param("password") String password, @Param("userName")String userName);

@Query("select u from User u where u.userName like ?1 and u.password like ?2")
// 使用@Query注解时,如果使用Like 模糊查询, name参数前后需要使用%
List<User> searchUserList(String userName,String password);


//可以在占位符前后加% 模糊查询
@Query("select u from User u where u.userName like %?1 and u.password like ?2%")
List<User> searchUserList2(String userName,String password);


@Query("select u from User u where u.userName like %:userName and u.password like :password%")
List<User> searchUserList3(@Param("password") String password,@Param("userName") String userName);

// 一般情况下老板的员工编号是最小的,所以通过查询最小ID的员工信息来找到Boss
@Query("select u from User u where u.id = (select min(u2.id) from User u2)")
User findBigBoss();

@Query("select count(1) from User ")
Long getCount();

@Query(value = "select count(1) from springdata_user",nativeQuery = true)
Long getCount2();
```

### update/delete方法


>**update/delete方法开发步骤:**

* update/delete方法必须使用@Query注解标注sql
* 添加@Modifying注解
* 添加Service层，添加@Transactional 事务注解,因为spring-data-jpa默认开启的是只读事务.

容易出现异常，参考[第九章Spring-data-jpa常见异常](http://www.jianshu.com/p/8819c5c7ca73)

#### 测试代码

```
@Query("UPDATE User u set u.password = :password where u.id = :id")
@Modifying
//可以通过自定义的JPQL语句完成UPDATE和Delete操作,注意： JPQL不支持使用insert
// 在@Query注解中编写JPQL语句但必须使用@Modifying进行修饰，以通知springdata-jpa这是一个update或者delete操作
//update 或者Delete操作需要使用事务,此时需要定义Service层,在Service层的方法上添加事务操作@Transactional
// 默认情况下springdata的方法上有事务，但是都是只读的
void updatePassword(@Param("id") Long id,@Param("password") String password);
```
