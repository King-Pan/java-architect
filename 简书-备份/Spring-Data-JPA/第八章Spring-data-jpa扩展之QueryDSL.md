## 第八章Spring-data-jpa扩展之QueryDSL

使用过spring data jpa的同学，都很清楚，对于复杂的sql查询，处理起来还是比较复杂的，而本文中的QueryDSL就是用来简化JPA操作的。
Querydsl定义了一种常用的静态类型语法，用于在持久域模型数据之上进行查询。
JDO和JPA是Querydsl的主要集成技术。本文旨在介绍如何使用Querydsl与JPA组合使用。JPA的Querydsl是JPQL和Criteria查询的替代方法。QueryDSL仅仅是一个通用的查询框架，专注于通过Java API构建类型安全的SQL查询。

### Maven依赖

spring-data-jpa中使用QueryDSL需要额外的添加依赖

```
<querydsl.version>4.1.4</querydsl.version>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-apt</artifactId>
    <version>${querydsl.version}</version>
</dependency>
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-jpa</artifactId>
    <version>${querydsl.version}</version>
</dependency>
```

### 实体类

>**City**

```
package club.javalearn.learn.springdata.extend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "extend_city")
public class City {
    private int id;
    private String name;
    private String state;
    private String country;
    private String map;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
```

>**Hotel**

```
package club.javalearn.learn.springdata.extend.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "extend_hotel")
public class Hotel {
    private int id;
    private String name;
    private String address;
    private Integer city;//保存着城市的id主键

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city=" + city +
                '}';
    }
}
```

### Repository

```
package club.javalearn.learn.springdata.extend.repository;

import club.javalearn.learn.springdata.extend.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface CityRepository extends JpaRepository<City,Integer>,CityDao,JpaSpecificationExecutor<City>,QueryDslPredicateExecutor<City> {

}
```

>**CityDao**

```
package club.javalearn.learn.springdata.extend.repository;

import club.javalearn.learn.springdata.extend.domain.City;

import java.util.List;

public interface CityDao {
    List<City> getPageList();
}
```

>**CityRepositoryImpl**

```
package club.javalearn.learn.springdata.extend.repository;

import club.javalearn.learn.springdata.extend.domain.City;
import club.javalearn.learn.springdata.extend.domain.QCity;
import club.javalearn.learn.springdata.extend.domain.QHotel;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class CityRepositoryImpl  implements CityDao {

    @Autowired
    private EntityManager entityManager;

    /**
     * 使用QueryDSL实现查询,建议在Repository层实现，也可以在service层实现
     * 使用springdata jpa Criteria查询在service层实现
     * @return
     */
    public List<City> getPageList() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<City> jpaQuery = queryFactory.select(QCity.city).from(QCity.city).leftJoin(QHotel.hotel).on(QCity.city.id.intValue().eq(QHotel.hotel.city.intValue()));
        Predicate predicate = QCity.city.name.like("武汉");
        jpaQuery.where(predicate);
        return jpaQuery.fetch();
    }

}
```

### Service

```
package club.javalearn.learn.springdata.extend.service;

import club.javalearn.learn.springdata.extend.domain.City;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CityService {
    void test(String userName);

    Page<City> getPageList();
    List<City> getList();
    List<City> getList2();
}
```

### Service实现类

```
package club.javalearn.learn.springdata.extend.service;

import club.javalearn.learn.springdata.extend.domain.City;
import club.javalearn.learn.springdata.extend.domain.QCity;
import club.javalearn.learn.springdata.extend.repository.CityRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;

    public void test(String userName) {
        QCity qCity = QCity.city;
        Predicate predicate = qCity.name.eq(userName).and(qCity.id.gt(100));
        cityRepository.findAll(predicate);
    }

    public Page<City> getPageList() {

        QCity qCity = QCity.city;
        Predicate predicate = qCity.state.eq("Y").and(qCity.name.like("zs"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC,"id"));

        PageRequest pageRequest = new PageRequest(0,10,sort);

        Page<City> tCityPage = cityRepository.findAll(predicate,pageRequest);

        return  tCityPage;
    }

    @Override
    public List<City> getList() {
        return cityRepository.getPageList();
    }
    @Override
    public List<City> getList2() {
        Pageable pageable = new PageRequest(1,10);
        Page<City> page = cityRepository.findAll(new Specification<City>(){
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<City> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> namePath = root.get("name");
                Path<String> countryPath = root.get("country");
                //这里可以设置任意条查询条件
                query.where(cb.like(namePath, "%李%"), cb.like(countryPath, "%武汉%"));
                //这种方式使用JPA的API设置了查询条件，所以不需要再返回查询条件Predicate给Spring Data Jpa，故最后return null;即可。
                return null;
            }
        },pageable);
        return page.getContent();
    }
}
```

### Maven插件

建立好实体类后使用Maven命令: mvn clean complie

会在target/generated-sources/java生成Q+实体类名的查询类型,把查询类型拷贝到项目中即可 

```
<build>
    <plugins>
        <plugin>
            <groupId>com.mysema.maven</groupId>
            <artifactId>apt-maven-plugin</artifactId>
            <version>1.1.3</version>
            <executions>
                <execution>
                    <goals>
                        <goal>process</goal>
                    </goals>
                    <configuration>
                        <outputDirectory>target/generated-sources/java</outputDirectory>
                        <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 生成的查询类型(Q+实体类名)

>**QCity**

```
package club.javalearn.learn.springdata.extend.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QCity is a Querydsl query type for City
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCity extends EntityPathBase<City> {

    private static final long serialVersionUID = 928606154L;

    public static final QCity city = new QCity("city");

    public final StringPath country = createString("country");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath map = createString("map");

    public final StringPath name = createString("name");

    public final StringPath state = createString("state");

    public QCity(String variable) {
        super(City.class, forVariable(variable));
    }

    public QCity(Path<? extends City> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCity(PathMetadata metadata) {
        super(City.class, metadata);
    }

}
```

>**QHotel**

```
package club.javalearn.learn.springdata.extend.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QHotel is a Querydsl query type for Hotel
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QHotel extends EntityPathBase<Hotel> {

    private static final long serialVersionUID = -1273184459L;

    public static final QHotel hotel = new QHotel("hotel");

    public final StringPath address = createString("address");

    public final NumberPath<Integer> city = createNumber("city", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QHotel(String variable) {
        super(Hotel.class, forVariable(variable));
    }

    public QHotel(Path<? extends Hotel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHotel(PathMetadata metadata) {
        super(Hotel.class, metadata);
    }

}
```

### 测试代码

```
package test.springdata.extend;

import club.javalearn.learn.springdata.extend.service.CityService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QueryDSLTest {

    private ApplicationContext cxt = null;

    private CityService cityService;

    @Before
    public void init() {
        cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
        cityService = cxt.getBean(CityService.class);
    }

    @Test
    public void testGetPageList2(){
        cityService.getList2();
    }
}
```

### 测试结果

```
/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/bin/java -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=50395:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar:/Applications/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit-rt.jar:/Applications/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit5-rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/tools.jar:/Users/king-pan/git_repository/springdata-parent/springdata-extend/target/test-classes:/Users/king-pan/git_repository/springdata-parent/springdata-extend/target/classes:/Users/king-pan/.m2/repository/com/querydsl/querydsl-apt/4.1.4/querydsl-apt-4.1.4.jar:/Users/king-pan/.m2/repository/com/querydsl/querydsl-codegen/4.1.4/querydsl-codegen-4.1.4.jar:/Users/king-pan/.m2/repository/com/mysema/codegen/codegen/0.6.8/codegen-0.6.8.jar:/Users/king-pan/.m2/repository/org/eclipse/jdt/core/compiler/ecj/4.3.1/ecj-4.3.1.jar:/Users/king-pan/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar:/Users/king-pan/.m2/repository/org/reflections/reflections/0.9.9/reflections-0.9.9.jar:/Users/king-pan/.m2/repository/com/google/code/findbugs/annotations/2.0.1/annotations-2.0.1.jar:/Users/king-pan/.m2/repository/javax/jdo/jdo-api/3.0.1/jdo-api-3.0.1.jar:/Users/king-pan/.m2/repository/javax/transaction/jta/1.1/jta-1.1.jar:/Users/king-pan/.m2/repository/com/querydsl/querydsl-jpa/4.1.4/querydsl-jpa-4.1.4.jar:/Users/king-pan/.m2/repository/com/querydsl/querydsl-core/4.1.4/querydsl-core-4.1.4.jar:/Users/king-pan/.m2/repository/com/google/guava/guava/18.0/guava-18.0.jar:/Users/king-pan/.m2/repository/com/google/code/findbugs/jsr305/1.3.9/jsr305-1.3.9.jar:/Users/king-pan/.m2/repository/com/mysema/commons/mysema-commons-lang/0.2.4/mysema-commons-lang-0.2.4.jar:/Users/king-pan/.m2/repository/com/infradna/tool/bridge-method-annotation/1.13/bridge-method-annotation-1.13.jar:/Users/king-pan/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:/Users/king-pan/.m2/repository/junit/junit/4.11/junit-4.11.jar:/Users/king-pan/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/king-pan/.m2/repository/org/springframework/data/spring-data-jpa/1.11.1.RELEASE/spring-data-jpa-1.11.1.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/data/spring-data-commons/1.13.1.RELEASE/spring-data-commons-1.13.1.RELEASE.jar:/Users/king-pan/.m2/repository/org/aspectj/aspectjrt/1.8.10/aspectjrt-1.8.10.jar:/Users/king-pan/.m2/repository/org/slf4j/jcl-over-slf4j/1.7.24/jcl-over-slf4j-1.7.24.jar:/Users/king-pan/.m2/repository/org/springframework/spring-webmvc/4.3.10.RELEASE/spring-webmvc-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-expression/4.3.10.RELEASE/spring-expression-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-context/4.3.10.RELEASE/spring-context-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-beans/4.3.10.RELEASE/spring-beans-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-web/4.3.10.RELEASE/spring-web-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-jdbc/4.3.10.RELEASE/spring-jdbc-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-aop/4.3.10.RELEASE/spring-aop-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-tx/4.3.10.RELEASE/spring-tx-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-core/4.3.10.RELEASE/spring-core-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar:/Users/king-pan/.m2/repository/org/springframework/spring-orm/4.3.10.RELEASE/spring-orm-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-core/5.2.10.Final/hibernate-core-5.2.10.Final.jar:/Users/king-pan/.m2/repository/org/jboss/logging/jboss-logging/3.3.0.Final/jboss-logging-3.3.0.Final.jar:/Users/king-pan/.m2/repository/org/hibernate/javax/persistence/hibernate-jpa-2.1-api/1.0.0.Final/hibernate-jpa-2.1-api-1.0.0.Final.jar:/Users/king-pan/.m2/repository/org/javassist/javassist/3.20.0-GA/javassist-3.20.0-GA.jar:/Users/king-pan/.m2/repository/antlr/antlr/2.7.7/antlr-2.7.7.jar:/Users/king-pan/.m2/repository/org/jboss/spec/javax/transaction/jboss-transaction-api_1.2_spec/1.0.1.Final/jboss-transaction-api_1.2_spec-1.0.1.Final.jar:/Users/king-pan/.m2/repository/org/jboss/jandex/2.0.3.Final/jandex-2.0.3.Final.jar:/Users/king-pan/.m2/repository/com/fasterxml/classmate/1.3.0/classmate-1.3.0.jar:/Users/king-pan/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:/Users/king-pan/.m2/repository/org/hibernate/common/hibernate-commons-annotations/5.0.1.Final/hibernate-commons-annotations-5.0.1.Final.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-entitymanager/5.2.10.Final/hibernate-entitymanager-5.2.10.Final.jar:/Users/king-pan/.m2/repository/net/bytebuddy/byte-buddy/1.6.6/byte-buddy-1.6.6.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-ehcache/5.2.10.Final/hibernate-ehcache-5.2.10.Final.jar:/Users/king-pan/.m2/repository/net/sf/ehcache/ehcache/2.10.3/ehcache-2.10.3.jar:/Users/king-pan/.m2/repository/mysql/mysql-connector-java/5.1.35/mysql-connector-java-5.1.35.jar:/Users/king-pan/.m2/repository/com/alibaba/druid/1.1.2/druid-1.1.2.jar:/Users/king-pan/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar:/Users/king-pan/.m2/repository/javax/servlet/jstl/1.2/jstl-1.2.jar" com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit4 test.springdata.extend.QueryDSLTest,testPageList
objc[2981]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/bin/java (0x10bc694c0) and /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/libinstrument.dylib (0x10bd454e0). One of the two will be used. Which one is undefined.
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
八月 15, 2017 10:13:09 下午 org.hibernate.jpa.internal.util.LogHelper logPersistenceUnitInformation
INFO: HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
八月 15, 2017 10:13:09 下午 org.hibernate.Version logVersion
INFO: HHH000412: Hibernate Core {5.2.10.Final}
八月 15, 2017 10:13:09 下午 org.hibernate.cfg.Environment <clinit>
INFO: HHH000206: hibernate.properties not found
八月 15, 2017 10:13:09 下午 org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
八月 15, 2017 10:13:10 下午 org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.MySQL5InnoDBDialect
八月 15, 2017 10:13:12 下午 org.hibernate.hql.internal.QueryTranslatorFactoryInitiator initiateService
INFO: HHH000397: Using ASTQueryTranslatorFactory
Hibernate: 
    select
        city0_.id as id1_0_,
        city0_.country as country2_0_,
        city0_.map as map3_0_,
        city0_.name as name4_0_,
        city0_.state as state5_0_ 
    from
        extend_city city0_ 
    where
        city0_.state=? 
        and (
            city0_.name like ? escape '!'
        ) 
    order by
        city0_.id asc limit ?
SLF4J: Failed to load class "org.slf4j.impl.StaticMDCBinder".
SLF4J: Defaulting to no-operation MDCAdapter implementation.

SLF4J: See http://www.slf4j.org/codes.html#no_static_mdc_binder for further details.

Process finished with exit code 0
```

