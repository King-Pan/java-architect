## JPA基础注解(补充)

### @TableGenerator

@TableGenerator 与@GeneratedValue(strategy = GenerationType.TABLE)配合使用

#### 定义
```
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface TableGenerator {
	/**
	 * (Required) A unique generator name that can be referenced
	 * by one or more classes to be the generator for id values.
	 */
	String name();

	/**
	 * (Optional) Name of table that stores the generated id values.
	 * <p/>
	 * Defaults to a name chosen by persistence provider.
	 */
	String table() default "";

	/**
	 * (Optional) The catalog of the table.
	 * <p/>
	 * Defaults to the default catalog.
	 */
	String catalog() default "";

	/**
	 * (Optional) The schema of the table.
	 * <p/>
	 * Defaults to the default schema for user.
	 */
	String schema() default "";

	/**
	 * (Optional) Name of the primary key column in the table.
	 * <p/>
	 * Defaults to a provider-chosen name.
	 */
	String pkColumnName() default "";

	/**
	 * (Optional) Name of the column that stores the last value generated.
	 * <p/>
	 * Defaults to a provider-chosen name.
	 */
	String valueColumnName() default "";

	/**
	 * (Optional) The primary key value in the generator table
	 * that distinguishes this set of generated values from others
	 * that may be stored in the table.
	 * <p/>
	 * Defaults to a provider-chosen value to store in the
	 * primary key column of the generator table
	 */
	String pkColumnValue() default "";

	/**
	 * (Optional) The initial value to be used to initialize the column
	 * that stores the last value generated.
	 */
	int initialValue() default 0;

	/**
	 * (Optional) The amount to increment by when allocating id
	 * numbers from the generator.
	 */
	int allocationSize() default 50;

	/**
	 * (Optional) Unique constraints that are to be placed on the
	 * table. These are only used if table generation is in effect.
	 * These constraints apply in addition to primary key constraints.
	 * <p/>
	 * Defaults to no additional constraints.
	 */
	UniqueConstraint[] uniqueConstraints() default { };
}
```
#### 实例

> **数据库表**

```
DROP TABLE IF EXISTS `sys_table_id`;
CREATE TABLE `sys_table_id` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pk_name` varchar(50) DEFAULT NULL,
  `id_val` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `sys_table_id` VALUES ('1', 'teacher_id', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
```

>**Java持久化Bean**

```
package club.javalearn.learn.annotation.domain;

import javax.persistence.*;

@Entity
@Table(name = "jpa_teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "table_id")
    @TableGenerator(name = "table_id",
            table = "sys_table_id",
            pkColumnName = "pk_name",
            pkColumnValue = "teacher_id",
            valueColumnName = "id_val",
            allocationSize = 1,
            initialValue = 1
    )
    private Long id;
    private String name;
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
```

#### 属性详解

> **name**

    属性表示该主键生成策略的名称，它被引用在@GeneratedValue中设置的generator 值中

> **table**

    属性表示表生成策略所持久化的表名

> **catalog**
   
   数据库实例名

> **schema**

   参考catalog

> **pkColumnName**

   属性的值表示在持久化表中，该主键生成策略所对应键值的名称
  
> **valueColumnName**

  属性的值表示在持久化表中，该主键当前所生成的值，它的值将会随着每次创建累加

> **pkColumnValue**
   
   属性的值表示在持久化表中，该生成策略所对应的主键

> **initialValue**

 valueColumnName 对应的字段的初始化值

> **allocationSize**
  
    表示每次主键值增加的大小, 默认值为 50  

> **uniqueConstraints**

   表示表中的唯一约束

### @SequenceGenerator

>**(本机没有装Oracle，下次有机会补上)**
>**或者万能的网友自行补上**

### @Temporal

```
         在核心的 Java API 中并没有定义 Date 类型的精度(temporal precision).  而在数据库中,
表示 Date 类型的数据有 DATE, TIME, 和 TIMESTAMP 三种精度(即单纯的日期,时间,或者两
者兼备). 在进行属性映射时可使用@Temporal注解来调整精度.
```

>**@Temporal(TemporalType.DATE)**

 @Temporal(TemporalType.DATE) 对应java.sql.Date类 ，数据库的DATE
 
参考值: 2017-08-08

>**@Temporal(TemporalType.TIME)**

 @Temporal(TemporalType.TIME)对应java.sql.Time类 ，数据库的TIME
  
 参考值: 9:00:32

>**@Temporal(TemporalType.TIMESTAMP)**

 @Temporal(TemporalType.TIMESTAMP)对应java.sql.TimeStamp类，数据库的TIMESTAMP
 
 参考值: 2017-08-08 9:00:32:000

#### 定义

```
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Temporal {
	/**
	 * The type used in mapping <code>java.util.Date</code> or <code>java.util.Calendar</code>.
	 */
	TemporalType value();
}
```

#### 属性详解

>**value**
   
   value: 属性指定date精度,精度值有DATE,TIME,TIMESTAMP
