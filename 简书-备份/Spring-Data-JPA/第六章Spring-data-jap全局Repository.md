## 第六章Spring-data-jap全局Repository

[spring-data-jpa 官方文档](https://docs.spring.io/spring-data/jpa/docs/1.11.6.RELEASE/reference/html/#repositories.custom-behaviour-for-all-repositories)

### 全局Repository开发步骤

>**声明一个接口, 在该接口中声明需要自定义的方法, 且该接口需要继承 Spring Data 的 Repository.**
>**提供 1) 所声明的接口的实现类. 且继承 SimpleJpaRepository, 并提供方法的实现**
>**定义 JpaRepositoryFactoryBean 的实现类, 使其生成 1) 定义的接口实现类的对象**
>**修改 <jpa:repositories /> 节点的 factory-class 属性指向 3) 的全类名**
>**注意: 全局的扩展实现类不要用 Imp 作为后缀名, 或为全局扩展接口添加 @NoRepositoryBean 注解告知  Spring Data: Spring Data 不把其作为 Repository**

### 实体Bean

```
package club.javalearn.learn.springdata.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "springdata_task")
public class Task {
    private Long id;
    private String taskName;
    private String pattern;
    private String status;
    private String log;
    private String remark;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", pattern='" + pattern + '\'' +
                ", status='" + status + '\'' +
                ", log='" + log + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
```

### 全局Repository接口

```
package club.javalearn.learn.springdata.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID> {
    boolean support(String modelType);
}
```

### 全局Repository

```
package club.javalearn.learn.springdata.repository;


import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class  BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T,ID> implements BaseRepository<T,ID>  {

    private final EntityManager entityManager;


    public BaseRepositoryImpl(JpaEntityInformation entityInformation,
                            EntityManager entityManager) {
        super(entityInformation, entityManager);

        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }
    @Override
    public boolean support(String modelType) {
        System.out.println("调用全局repository方法");
        return false;
    }

}
```

### spring配置

注意提前引入jpa命名空间

```
 <jpa:repositories 
 	base-package="club.javalearn.learn.springdata" 
 	entity-manager-factory-ref="entityManagerFactory" 
 	base-class="club.javalearn.learn.springdata.repository.BaseRepositoryImpl">
</jpa:repositories>
```

### 测试代码

```
package test.defined.repository;

import club.javalearn.learn.springdata.repository.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RepositoryTest {

    private ApplicationContext cxt = null;

    private TaskRepository taskRepository;

    @Before
    public void init(){
        cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
        taskRepository = cxt.getBean(TaskRepository.class);
    }

    @Test
    public void testAllRepositoryMethod(){
        Boolean flag = taskRepository.support("aaa");
        System.out.println(flag);
    }
}
```

### 测试结果

```
/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/bin/java -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=65375:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar:/Applications/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit-rt.jar:/Applications/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit5-rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/lib/tools.jar:/Users/king-pan/git_repository/springdata-parent/springdata-defined-repository/target/test-classes:/Users/king-pan/git_repository/springdata-parent/springdata-defined-repository/target/classes:/Users/king-pan/.m2/repository/junit/junit/4.11/junit-4.11.jar:/Users/king-pan/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/king-pan/.m2/repository/org/springframework/data/spring-data-jpa/1.11.1.RELEASE/spring-data-jpa-1.11.1.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/data/spring-data-commons/1.13.1.RELEASE/spring-data-commons-1.13.1.RELEASE.jar:/Users/king-pan/.m2/repository/org/aspectj/aspectjrt/1.8.10/aspectjrt-1.8.10.jar:/Users/king-pan/.m2/repository/org/slf4j/slf4j-api/1.7.24/slf4j-api-1.7.24.jar:/Users/king-pan/.m2/repository/org/slf4j/jcl-over-slf4j/1.7.24/jcl-over-slf4j-1.7.24.jar:/Users/king-pan/.m2/repository/org/springframework/spring-webmvc/4.3.10.RELEASE/spring-webmvc-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-expression/4.3.10.RELEASE/spring-expression-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-context/4.3.10.RELEASE/spring-context-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-beans/4.3.10.RELEASE/spring-beans-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-web/4.3.10.RELEASE/spring-web-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-jdbc/4.3.10.RELEASE/spring-jdbc-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-aop/4.3.10.RELEASE/spring-aop-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-tx/4.3.10.RELEASE/spring-tx-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/springframework/spring-core/4.3.10.RELEASE/spring-core-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar:/Users/king-pan/.m2/repository/org/springframework/spring-orm/4.3.10.RELEASE/spring-orm-4.3.10.RELEASE.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-core/5.2.10.Final/hibernate-core-5.2.10.Final.jar:/Users/king-pan/.m2/repository/org/jboss/logging/jboss-logging/3.3.0.Final/jboss-logging-3.3.0.Final.jar:/Users/king-pan/.m2/repository/org/hibernate/javax/persistence/hibernate-jpa-2.1-api/1.0.0.Final/hibernate-jpa-2.1-api-1.0.0.Final.jar:/Users/king-pan/.m2/repository/org/javassist/javassist/3.20.0-GA/javassist-3.20.0-GA.jar:/Users/king-pan/.m2/repository/antlr/antlr/2.7.7/antlr-2.7.7.jar:/Users/king-pan/.m2/repository/org/jboss/spec/javax/transaction/jboss-transaction-api_1.2_spec/1.0.1.Final/jboss-transaction-api_1.2_spec-1.0.1.Final.jar:/Users/king-pan/.m2/repository/org/jboss/jandex/2.0.3.Final/jandex-2.0.3.Final.jar:/Users/king-pan/.m2/repository/com/fasterxml/classmate/1.3.0/classmate-1.3.0.jar:/Users/king-pan/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:/Users/king-pan/.m2/repository/org/hibernate/common/hibernate-commons-annotations/5.0.1.Final/hibernate-commons-annotations-5.0.1.Final.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-entitymanager/5.2.10.Final/hibernate-entitymanager-5.2.10.Final.jar:/Users/king-pan/.m2/repository/net/bytebuddy/byte-buddy/1.6.6/byte-buddy-1.6.6.jar:/Users/king-pan/.m2/repository/org/hibernate/hibernate-ehcache/5.2.10.Final/hibernate-ehcache-5.2.10.Final.jar:/Users/king-pan/.m2/repository/net/sf/ehcache/ehcache/2.10.3/ehcache-2.10.3.jar:/Users/king-pan/.m2/repository/mysql/mysql-connector-java/5.1.35/mysql-connector-java-5.1.35.jar:/Users/king-pan/.m2/repository/com/alibaba/druid/1.1.2/druid-1.1.2.jar:/Users/king-pan/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar:/Users/king-pan/.m2/repository/javax/servlet/jstl/1.2/jstl-1.2.jar" com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit4 test.defined.repository.RepositoryTest,testAllRepositoryMethod
objc[2513]: Class JavaLaunchHelper is implemented in both /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/bin/java (0x106e724c0) and /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/libinstrument.dylib (0x107ef04e0). One of the two will be used. Which one is undefined.
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
八月 15, 2017 9:07:48 下午 org.hibernate.jpa.internal.util.LogHelper logPersistenceUnitInformation
INFO: HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
八月 15, 2017 9:07:48 下午 org.hibernate.Version logVersion
INFO: HHH000412: Hibernate Core {5.2.10.Final}
八月 15, 2017 9:07:48 下午 org.hibernate.cfg.Environment <clinit>
INFO: HHH000206: hibernate.properties not found
八月 15, 2017 9:07:48 下午 org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
八月 15, 2017 9:07:49 下午 org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.MySQL5InnoDBDialect
调用全局repository方法
false

Process finished with exit code 0
```
