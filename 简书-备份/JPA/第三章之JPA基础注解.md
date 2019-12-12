## JPA基础注解

本文部分内容引用
[JPA之@GeneratedValue注解](http://blog.csdn.net/u012493207/article/details/50846616)

JPA基础注解都在javax.persistence包下

>**JPA中主要包含以下几个注解**
* @Entity
* @Table
* @Id
* @GeneratedValue
* @Basic
* @Column
* @Transient

这些注解大体可以分为2类，持久化Bean注解和持久化属性注解

### JPA持久化Bean注解

>**@Entity和@Table属于类注解**

#### @Entity

>**定义**

```
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Entity {

	/**
	 * (Optional) The entity name. Defaults to the unqualified
	 * name of the entity class. This name is used to refer to the
	 * entity in queries. The name must not be a reserved literal
	 * in the Java Persistence query language.
	 */
	String name() default "";
}
```
>**属性详解**

* name: 唯一的名称，在使用持久化查询语言的时候使用(不需要这个属性,默认使用类名来查询)

#### @Table

>**定义**

```
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {
	/**
	 * (Optional) The name of the table.
	 * <p/>
	 * Defaults to the entity name.
	 */
	String name() default "";

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
	 * (Optional) Unique constraints that are to be placed on
	 * the table. These are only used if table generation is in
	 * effect. These constraints apply in addition to any constraints
	 * specified by the <code>Column</code> and <code>JoinColumn</code>
	 * annotations and constraints entailed by primary key mappings.
	 * <p/>
	 * Defaults to no additional constraints.
	 */
	UniqueConstraint[] uniqueConstraints() default { };
}
```

>**属性详解**

* name: 数据库表名称
* catalog: 数据库实例名称
1. 当catalog属性不指定时，新创建的表将出现在url指定的数据库实例中 
2. 当catalog属性设置名称时，若数据库存在和指定名称一致的实例，新创建的表将出现在该实例中
3. 当catalog属性设置名称不存在时,报异常:ERROR: HHH000388: Unsuccessful: create table xxx.xxxx .....
* schema: 参考catalog
* uniqueConstraints: 用于设定约束条件
1. @Table(name="jpa_person",uniqueConstraints = {@UniqueConstraint(columnNames = {"idCard","name"})})
2. 可以指定多组约束uniqueConstraints={@UniqueConstraint(columnNames={"idCard","name"}),@UniqueConstraint(columnNames={"name","age"})}

### JPA持久化属性注解

#### @Id

>**@Id注解只能注解方法和字段**

>**定义**

```
@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface Id {
}
```

>**属性详解**

@Id没有属性,是一个空注解,标识该字段是一个主键,具体的主键策略需要配置@GeneratedValue注解完成.

#### @GeneratedValue

>**定义**

```
@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface GeneratedValue {

    /**
     * (Optional) The primary key generation strategy
     * that the persistence provider must use to
     * generate the annotated entity primary key.
     */
    GenerationType strategy() default AUTO;

    /**
     * (Optional) The name of the primary key generator
     * to use as specified in the {@link SequenceGenerator}
     * or {@link TableGenerator} annotation.
     * <p> Defaults to the id generator supplied by persistence provider.
     */
    String generator() default "";
}
```

>**属性详解**

* strategy: 主键生成策略，具体参考(GenerationType)
* generator: 主要配合GenerationType.Table和GenerationType.SEQUENCE主键生成策略使用

>**主键生成策略**

>**GenerationType.AUTO:**

 JPA自动选择合适的策略(默认选项)

>**GenerationType.IDENTITY:**

 采用数据库 ID自增长的方式来自增主键字段，Oracle 不支持这种方式

>**GenerationType.SEQUENCE:**

通过序列产生主键，通过 @SequenceGenerator 注解指定序列名，MySql 不支持这种方式

```
@Id  
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuSeq")  
@SequenceGenerator(name = "menuSeq", initialValue = 1, allocationSize = 1, sequenceName = "MENU_SEQUENCE")  
private Integer id;  
```

>**GenerationType.TABLE:**

通过表产生主键，框架借由表模拟序列产生主键，使用该策略可以使应用更易于数据库移植

```
@Id  
@GeneratedValue(strategy = GenerationType.IDENTITY)  
private Integer id;  
```

#### @Basic

>**定义**

```
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Basic {

    /**
     * (Optional) Defines whether the value of the field or property should
     * be lazily loaded or must be eagerly fetched. The <code>EAGER</code>
     * strategy is a requirement on the persistence provider runtime
     * that the value must be eagerly fetched.  The <code>LAZY</code>
     * strategy is a hint to the persistence provider runtime.
     * If not specified, defaults to <code>EAGER</code>.
     */
    FetchType fetch() default EAGER;

    /**
     * (Optional) Defines whether the value of the field or property may be null.
     * This is a hint and is disregarded for primitive types; it may
     * be used in schema generation.
     * If not specified, defaults to <code>true</code>.
     */
    boolean optional() default true;
}
```

>**属性详解**

* fetch: 表示该属性的读取策略,有 EAGER 和 LAZY 两种,分别表示主支抓取和延迟加载,默认为 EAGER
* optional: 表示该属性是否允许为null, 默认为true

#### @Column

>**定义**

```
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column {

    /**
     * (Optional) The name of the column. Defaults to
     * the property or field name.
     */
    String name() default "";

    /**
     * (Optional) Whether the column is a unique key.  This is a
     * shortcut for the <code>UniqueConstraint</code> annotation at the table
     * level and is useful for when the unique key constraint
     * corresponds to only a single column. This constraint applies
     * in addition to any constraint entailed by primary key mapping and
     * to constraints specified at the table level.
     */
    boolean unique() default false;

    /**
     * (Optional) Whether the database column is nullable.
     */
    boolean nullable() default true;

    /**
     * (Optional) Whether the column is included in SQL INSERT
     * statements generated by the persistence provider.
     */
    boolean insertable() default true;

    /**
     * (Optional) Whether the column is included in SQL UPDATE
     * statements generated by the persistence provider.
     */
    boolean updatable() default true;

    /**
     * (Optional) The SQL fragment that is used when
     * generating the DDL for the column.
     * <p> Defaults to the generated SQL to create a
     * column of the inferred type.
     */
    String columnDefinition() default "";

    /**
     * (Optional) The name of the table that contains the column.
     * If absent the column is assumed to be in the primary table.
     */
    String table() default "";

    /**
     * (Optional) The column length. (Applies only if a
     * string-valued column is used.)
     */
    int length() default 255;

    /**
     * (Optional) The precision for a decimal (exact numeric)
     * column. (Applies only if a decimal column is used.)
     * Value must be set by developer if used when generating
     * the DDL for the column.
     */
    int precision() default 0;

    /**
     * (Optional) The scale for a decimal (exact numeric) column.
     * (Applies only if a decimal column is used.)
     */
    int scale() default 0;
}
```

>**属性详解**

* name: 指定数据库列名
* unique: 是否为唯一标识,默认为false
* nullable: 是否可为空,默认为true
* insertable: 表示在使用”INSERT”语句插入数据时，是否需要插入该字段的值
* updateable: 属性表示在使用”UPDATE”语句插入数据时，是否需要更新该字段的值
* columnDefinition: 表示创建表时，该字段创建的SQL语句，一般用于通过Entity生成表定义时使用 
* table: 定义了包含当前字段的表名
* length: 表示字段的长度，当字段的类型为varchar时，该属性才有效，默认为255个字符
* precision: 当字段类型为double时，precision表示数值的总长度，scale表示小数点所占的位数
* scale: 当字段类型为double时，precision表示数值的总长度，scale表示小数点所占的位数
* precision属性和scale属性一起表示精度,mysql中没有效果，可以使用*columnDefinition="double(5,2) default '0.00'"这种方式创建*

#### @Transient

>**定义**

```
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Transient {
}
```

>**属性详解**

@Transient注解是一个空注解,标识该属性不持久化,让JPA忽略该属性.
