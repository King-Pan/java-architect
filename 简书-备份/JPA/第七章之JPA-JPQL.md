## 第七章之JPA-JPQL

JPQL: java persistence query language(jpa查询语言)

JPQL语言，即 Java Persistence Query Language 的简称。

JPQL 是一种和 SQL 非常类似的中间性和对象化查询语言，它最终会被编译成针对不同底层数据库的 SQL 查询，从而屏蔽不同数据库的差异。
JPQL语言的语句可以是 select 语句、update 语句或delete语句，
它们都通过 Query 接口封装执行

### 持久化Bean

>**User**

```
package club.javalearn.learn.jpa.jpql.domain;

import javax.persistence.*;
import java.util.Date;

@NamedQuery(name = "test_name_query",query = "FROM User c where c.id = ? ")
@Entity
@Table(name = "jpql_user")
public class User {
    private Integer id;
    private String name;
    private String password;
    private String salt;
    private String locked;
    private Date lastLoginDate;

    public User() {
    }

    public User(Integer id,Date lastLoginDate) {
        this.id = id;
        this.lastLoginDate = lastLoginDate;
    }


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

>**Student**

```
package club.javalearn.learn.jpa.jpql.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jqpl_student")
public class Student {
    private Integer id;
    private String name;
    private Integer age;
    private Set<Score> scores = new HashSet<>();

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @OneToMany(mappedBy = "student",cascade = {CascadeType.REMOVE},fetch = FetchType.LAZY)
    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", scores=" + scores +
                '}';
    }
}
```

>**Score**

```
package club.javalearn.learn.jpa.jpql.domain;

import javax.persistence.*;

@Table(name = "jpql_score")
@Entity
public class Score {
    private Integer id;
    private String name;
    private Double score;
    private Student student;

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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
```

### JPQL Query

>**查询整个对象**

```
@Test
public void testJPQLQuery(){
    String jpql = "FROM User u WHERE 1 = ?";
    Query query = entityManager.createQuery(jpql);
    query.setParameter(1,1);
    List<User> users = query.getResultList();
    System.out.println(users.size());
}
```

>**查询部分字段**

```
/**
 * 查询部分属性:
 *   1. 查询一个字段 返回一个Object类型/Object类型的List
 *   2. 查询多个字段 返回一个List<Object[]>
 *   3. 如果需要返回对象,则需要在jpql语句中使用new User(u.id,u.lastLoginDate)方式,并且在user类中提供对应的构造器
 */
@Test
public void testFieldQuery(){
    //String jpql = " SELECT new User(u.id,u.lastLoginDate) FROM User u where u.id < ?";
    String jpql = "SELECT new User(u.id,u.lastLoginDate) FROM User u where u.id < ?";
    Query query = entityManager.createQuery(jpql);
    query.setParameter(1,100);
    List<User> users = query.getResultList();
    System.out.println(users.get(0));
}
```

>**NamedQuery**


NamedQuery与Query唯一的差别是把sql通过@NamedQuery注解放在实体类的Bean上.

```
//createNamedQuery  适用于实体类前@NamedQuery标记的SQL
@Test
public void testNameQuery(){
    Query query = entityManager.createNamedQuery("test_name_query");
    query.setParameter(1,2);
    List result = query.getResultList();
    System.out.println(result.size());
}
```

>**NativeQuery**

NativeQuery是和一些复杂的本地SQL，不能通过JPQL来查询的SQL.

```
//createNativeQuery 适用于本地SQL(原生的SQL)
@Test
public void testNaviteQuery(){
    String sql = "select id from jpql_user where id = ?";
    Query query = entityManager.createNativeQuery(sql);
    query.setParameter(1,2);
    Object result = query.getSingleResult();
    System.out.println(result);
}
```

### 查询缓存

查询缓存使用步骤:

1. 需要开启jpa缓存
2. 配置查询缓存: hibernate.cache.use_query_cache
3. 在查询时设置查询缓存为true: createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE,true);
4. 再查询时如果匹配上缓存中的数据,就不用从数据库中查询.

```
//使用hibernate的查询缓存
//persistence.xml 配置了启动查询缓存:hibernate.cache.use_query_cache
@Test
public void testQueryCache(){
    String jpql = "FROM User u WHERE 1 = ?";
    Query query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE,true);
    query.setParameter(1,1);
    List<User> users = query.getResultList();
    System.out.println(users.size());

    query = entityManager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE,true);
    query.setParameter(1,1);
    users = query.getResultList();
    System.out.println(users.size());
}
```

### JPQL ORDER BY

```
@Test
public void testOrderBy(){
    String jpql = "SELECT new User(u.id,u.lastLoginDate) FROM User u where u.id < ? order by u.lastLoginDate desc";
    Query query = entityManager.createQuery(jpql);
    query.setParameter(1,100);
    List<User> users = query.getResultList();
    System.out.println(users.get(0));
}
```
>**查询SQL**

```
Hibernate: 
    select
        user0_.id as col_0_0_,
        user0_.last_login_date as col_1_0_ 
    from
        jpql_user user0_ 
    where
        user0_.id<? 
    order by
        user0_.last_login_date desc
```

### JPQL GROUP BY

```
@Test
public void testGroupBy(){
   String jpql = "SELECT s.student from Score s group by s.student having sum(s.score)>160";
    Query query = entityManager.createQuery(jpql);
    //query.setParameter(1,100);
    List users = query.getResultList();
    System.out.println(users.get(0));
}
```
>**查询SQL**

```
Hibernate: 
    select
        student1_.id as id1_2_,
        student1_.age as age2_2_,
        student1_.name as name3_2_ 
    from
        jpql_score score0_ 
    inner join
        jqpl_student student1_ 
            on score0_.student_id=student1_.id 
    group by
        score0_.student_id 
    having
        sum(score0_.score)>160
```

### JQPL连接查询

>**JPQL语句中包含fetch**


```
@Test
public void testOutJoinQuery(){
    String jpql = "SELECT s.scores FROM Student s where s.id = ?";
    List<Score> scores = entityManager.createQuery(jpql).setParameter(1,1).getResultList();
    System.out.println(scores.size());
}
```

```
@Test
public void testOutJoinQuery2(){
    String jpql = "FROM Student s where s.id = ?";
    Student student = (Student) entityManager.createQuery(jpql).setParameter(1,1).setMaxResults(1).getSingleResult();
    Assert.assertNotNull(student);
}
```

```
//使用fetch直接返回Student对象，并且使用做外连接查询出了score对象
@Test
public void testOutJoinQuery3(){
    String jpql = "FROM Student s left outer join fetch s.scores where s.id = ?";
    Student student = (Student) entityManager.createQuery(jpql).
            setParameter(1,1).
            setMaxResults(1).getSingleResult();
    Assert.assertNotNull(student);
}
```

>**JPQL语句中不包含fetch**


```
//如果不用fetch，这分别返回了Student，Score对象,
@Test
public void testOutJoinQuery4(){
    String jpql = "FROM Student s left outer join s.scores where s.id = ?";
    List<Object[]> result = (List<Object[]>) entityManager.createQuery(jpql).setParameter(1,1).getResultList();
    Assert.assertNotNull(result);
    System.out.println(result.get(0).getClass().getName());
    for(Object obj:result.get(0)){
        System.out.println(obj);
    }
    System.out.println(Arrays.toString(result.get(0)));
}
```

```
Hibernate: 
    select
        student0_.id as id1_2_0_,
        scores1_.id as id1_0_1_,
        student0_.age as age2_2_0_,
        student0_.name as name3_2_0_,
        scores1_.name as name2_0_1_,
        scores1_.score as score3_0_1_,
        scores1_.student_id as student_4_0_1_ 
    from
        jqpl_student student0_ 
    left outer join
        jpql_score scores1_ 
            on student0_.id=scores1_.student_id 
    where
        student0_.id=?
[Ljava.lang.Object;
Hibernate: 
    select
        scores0_.student_id as student_4_2_1_,
        scores0_.id as id1_0_1_,
        scores0_.id as id1_0_0_,
        scores0_.name as name2_0_0_,
        scores0_.score as score3_0_0_,
        scores0_.student_id as student_4_0_0_ 
    from
        jpql_score scores0_ 
    where
        scores0_.student_id=?
Student{id=1, name='zs', age=21, scores=[Score{id=1, name='语文', score=77.5}, Score{id=2, name='数学', score=46.5}]}
Score{id=1, name='语文', score=77.5}
[Student{id=1, name='zs', age=21, scores=[Score{id=1, name='语文', score=77.5}, Score{id=2, name='数学', score=46.5}]}, Score{id=1, name='语文', score=77.5}]
```


>**有无fetch的区别**

* 有fetch返回的是持久化对象,没有fetch返回的是Object[]数组
* 有fetch只用连接查询一次,没有fetch,还需要另外的select查询该持久化对象中的其他对象


### JPQL 子查询

JPQL也支持子查询，在 where 或 having 子句中可以包含另一个查询。当子查询返回多于 1 个结果集时，它常出现在 any、all、exist s表达式中用于集合匹配查询。它们的用法与SQL语句基本相同。

>**测试用例**

```
 @Test
public void testChildQuery(){
    //查询 student_id = 1 的score
    String jpql = "SELECT s FROM Score s where s.student in(SELECT st from Student st where st.name=?)";
    Query query = entityManager.createQuery(jpql).setParameter(1,"马云");
    List<Score> scores = query.getResultList();
    System.out.println(scores.size());
}
```
>**查询SQL**

```
Hibernate: 
    select
        score0_.id as id1_0_,
        score0_.name as name2_0_,
        score0_.score as score3_0_,
        score0_.student_id as student_4_0_ 
    from
        jpql_score score0_ 
    where
        score0_.student_id in (
            select
                student1_.id 
            from
                jqpl_student student1_ 
            where
                student1_.name=?
        )
```

### JPQL 内建函数


JPQL提供了以下一些内建函数，包括字符串处理函数、算术函数和日期函数。
>**字符串处理函数主要有：**

*  concat(String s1, String s2)：字符串合并/连接函数。
*  substring(String s, int start, int length)：取字串函数。
*  trim([leading|trailing|both,] [char c,] String s)：从字符串中去掉首/尾指定的字符或空格。
*  lower(String s)：将字符串转换成小写形式。
*  upper(String s)：将字符串转换成大写形式。
*  length(String s)：求字符串的长度。
*  locate(String s1, String s2[, int start])：从第一个字符串中查找第二个字符串(子串)出现的位置。若未找到则返回0。

算术函数主要有 abs、mod、sqrt、size 等。Size 用于求集合的元素个数。
日期函数主要为三个，即 current_date、current_time、current_timestamp，它们不需要参数，返回服务器上的当前日期、时间和时戳。

>**测试用例**

```
@Test
public void testFunction(){
    String jpql = "SELECT upper(u.name) from User u where u.id = ?";
    String name = (String) entityManager.createQuery(jpql).setParameter(1,1).getSingleResult();
    System.out.println(name);
}
```

>**测试结果**

```
Hibernate: 
    select
        upper(user0_.name) as col_0_0_ 
    from
        jpql_user user0_ 
    where
        user0_.id=?
MAYUN
```

### JPQL UPDATE/DELETE

update语句用于执行数据更新操作。主要用于针对单个实体类的批量更新

delete语句用于执行数据更新操作。

#### JPQL UPDATE

>**测试用例**

把分数小于50的成绩加上10

```
@Test
public void testUpdate(){
    String jqpl ="UPDATE Score s SET s.score = s.score + 10 where s.score<=50";
    entityManager.createQuery(jqpl).executeUpdate();
}
```

>**更新SQL**

```
Hibernate: 
    update
        jpql_score 
    set
        score=score+10 
    where
        score<=50
```


#### JPQL UPDATE

>**测试用例**

把分数小于60的Score删除

```
@Test
public void testDelete(){
    String jqpl ="DELETE FROM Score s  where s.score<60";
    entityManager.createQuery(jqpl).executeUpdate();
}
```

>**删除SQL**

```
Hibernate: 
    delete 
    from
        jpql_score 
    where
        score<60
```
