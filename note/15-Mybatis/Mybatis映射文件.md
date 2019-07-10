# Mybatis映射文件

​		Mabatis的映射文件中，主要有一下标签。本章主要是下列标签的使用实例

* cache – 给定命名空间的缓存配置
*  cache-ref – 其他命名空间缓存配置的引用
*  resultMap – 是最复杂也是最强大的元素，用来描述如何从数据库结果集中来加载对象
*  sql – 可被其他语句引用的可重用语句块
*  insert – 映射插入语句
*  update – 映射更新语句
*  delete – 映射删除语句
*  select – 映射查询语句

## select元素

| 元素          | 说明                                                         | 备注                                                         |
| ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| id            | 它和Mapper的命名空间组合起来是唯一的，提供给MyBatis调用      | 如果命名空间和id组合起来不唯一，会抛出异常                   |
| parameterType | 传入参数的类型;可以给出类全名，也可以给出类别名，使用别名必须是MyBatis内部定义或自定义的；<br/>基本数据类型：int , String , long , date(不知是sql.date 还是 util.date)<br/>复杂数据类型：类 和 Map | 可以选择JavaBean，Map等复杂的参数类型传递给SQL               |
| resultType    | 从这条语句中返回的期望类型的类的完全限定名或别名。<br/>注意如果是集合情形，那应该是集合可以包含的类型，而不能是集合本身。<br/>使用 resultType 或 resultMap，但不能同时使用<br/>定义类的全路径，在允许自动匹配的情况下，结果集将通过JavaBean的规范映射；<br/>或者定义为int,double,float等参数...<br/>也可以使用别名，但是要符合别名规范，不能和resultMap同时使用。 | 它是我们常用的参数之一，比如我们总计总条数就可以把它的值设为int |
| resultMap     | 外部 resultMap 的命名引用。使用 resultMap 或 resultType，但不能同时使用；<br/>它是映射集的引用，将执行强大的映射功能，我们可以使用resultType或者resultMap其中的一个，resultMap可以给予我们自定义映射规则的机会 | 它是MyBatis最复杂的元素，可以配置映射规则，级联，typeHandler等 |
| flushCache    | 它的作用是在调用SQL后，是否要求MyBatis清空之前查询的本地缓存和二级缓存 | true/false，默认为false                                      |
| useCache      | 启动二级缓存开关，是否要求MyBatis将此次结果缓存              | true/false，默认为true                                       |
| timeout       | 设置超时时间，超时之后抛出异常，秒                           | 默认值为数据库厂商提供的JDBC驱动所设置的秒数                 |
| fetchSize     | 获取记录的总条数设定                                         | 默认值是数据库厂商提供的JDBC驱动所设的条数                   |

### 自动映射

* 前提：SQL列名和JavaBean的属性是一致的；
* 自动映射等级autoMappingBehavior设置为PARTIAL,需要谨慎使用FULL;
* 使用resultType
* 如果列表和JavaBean不一致，但列名服务单词下划线分割，Java是驼峰命名法，则mapUnderscoreToCamelCase可设置为true(下划线转换驼峰)

### 手动映射

* 可以使用resultType和resultMap
* 使用resultType时，SQL列名和JavaBean的属性不一致且不符合驼峰命名法，需要在查询SQL中使用别名或者使用resultMap手动映射字段关系。
* 符合驼峰命名法的，需要在全局设置中设置下划线转驼峰(mapUnderscoreToCamelCase可设置为true)

### select多参数查询

* 使用map传参(杜绝使用【可读性和可扩展性差】)
* 使用注解传递参数，@Param("paramName")，建议参数个数小于5个的使用使用
* 使用JavaBean大的方式传递参数；当参数大于5个时，强制使用JavaBean的方式

### resultMap元素

​		resultMap 元素是 MyBatis 中最重要最强大的元素。它可以让你从 90% 的 JDBC ResultSets 数据提取代码中解放出
来,在对复杂语句进行联合映射的时候，它很可能可以代替数千行的同等功能的代码。
​		ResultMap 的设计思想是，简单的语句不需要明确的结果映射，而复杂一点的语句只需要描述它们的关系就行了。

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| id          | 当前命名空间中的一个唯一标识，用于标识一个result map.        |
| type        | 类的完全限定名, 或者一个类型别名 (内置的别名可以参考上面的表格). |
| autoMapping | 如果设置这个属性，MyBatis将会为这个ResultMap开启或者关闭自动映射。这个属性会覆盖全局的属性 autoMappingBehavior。默认值为：unset。 |

#### resultMap子元素

* constructor - 用于在实例化类时，注入结果到构造方法中
  * idArg - ID 参数;标记出作为 ID 的结果可以帮助提高整体性能
  * arg - 将被注入到构造方法的一个普通结果

* id  – 一个 ID 结果;标记出作为 ID 的结果可以帮助提高整体性能
*  result – 注入到字段或 JavaBean 属性的普通结果
*  association – 一个复杂类型的关联;许多结果将包装成这种类型
* 嵌套结果映射 – 关联可以指定为一个 resultMap 元素，或者引用一个
* collection – 一个复杂类型的集合
  * 嵌套结果映射 – 集合可以指定为一个 resultMap 元素，或者引用一个
* discriminator – 使用结果值来决定使用哪个 resultMap
* case – 基于某些值的结果映射
  * 嵌套结果映射 – 一个 case 也是一个映射它本身的结果,因此可以包含很多相 同
    的元素，或者它可以参照一个外部的 resultMap

#### id与result

* id 和 result 都将一个列的值映射到一个简单数据类型(字符串,整型,双精度浮点数,日期等)的属性或字段
*  两者之间的唯一不同是， id 表示的结果将是对象的标识属性，这会在比较对象实例时用到。 这样可以
  提高整体的性能，尤其是缓存和嵌套结果映射(也就是联合映射)的时候

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| property    | property 映射到列结果的字段或属性。如果用来匹配的 JavaBeans 存在给定名字的属性，那么它将会被使用。否则 MyBatis将会寻找给定名称 property 的字段。 无论是哪一种情形，你都可以使用通常的点式分隔形式进行复杂属性导航。 |
| column      | 数据库中的列名,或者是列的别名。一般情况下，这和 传递给 resultSet.getString(columnName) 方法的参数一样。 |
| javaType    | 一个 Java 类的完全限定名,或一个类型别名(参考上面内建类型别名 的列表) 。如果你映射到一个 JavaBean,MyBatis通常可以断定类型。 然而,如果你映射到的是 HashMap,那么你应该明确地指定 javaType 来保证期望的行为。 |
| jdbcType    | JDBC 类型，所支持的 JDBC 类型参见这个表格之后的“支持的 JDBC 类型”。 只需要在可能执行插入、更新和删除的允许空值的列上指定 JDBC 类型。这是 JDBC 的要求而非 MyBatis 的要求。如果你直接面向 JDBC 编程,你需要对可能为 null 的值指定这个类型。 |
| typeHandler | 使用这个属性,你可以覆盖默 认的类型处理器。这个属性值是一个类型处理 器实现类的完全限定名，或者是类型别名。 |

#### constructor 

* 适合使用不可变类。 构造方法注入允许你在初始化时 为类设置属性的值，而不用暴露出公有方法,
  constructor 元素就是为此而生的;
* 为了通过名称来引用构造方法参数，你可以添加 @Param 注解,指定参数名称的前提下，以任意顺序编写
  arg 元素

> 实例

```xml
<constructor>
    <idArg column="id" javaType="int" name="id" />
    <arg column="age" javaType="_int" name="age" />
    <arg column="username" javaType="String" name="username" />
</constructor>
```

## insert、update和delete

| 属性             | 描述                                                         | 范围           |
| ---------------- | ------------------------------------------------------------ | -------------- |
| id               | 命名空间中的唯一标识符，可被用来代表这条语句。               |                |
| parameterType    | 将要传入语句的参数的完全限定类名或别名。这个属性是可选的，因为 MyBatis 可以通过 TypeHandler 推断出具体传入语句的参数，默认值为 unset。 |                |
| flushCache       | 将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：true（对应插入、更新和删除语句）。 |                |
| timeout          | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为 unset（依赖驱动）。 |                |
| statementType    | STATEMENT，PREPARED 或 CALLABLE 的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或CallableStatement，默认值：PREPARED。 |                |
| useGeneratedKeys | （仅对 insert 和 update 有用）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系数据库管理系统的自动递增字段），默认值：false。 | insert、update |
| keyProperty      | （仅对 insert 和 update 有用）唯一标记一个属性，MyBatis 会通过 getGeneratedKeys 的返回值或者通过 insert语句的 selectKey 子元素设置它的键值，默认：unset。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 | insert、update |
| keyColumn        | （仅对 insert 和 update 有用）通过生成的键值设置表中的列名，这个设置仅在某些数据库（像 PostgreSQL）是必须的，当主键列不是表中的第一列的时候需要设置。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 | insert、update |
| databaseId       | 如果配置了 databaseIdProvider，MyBatis 会加载所有的不带 databaseId 或匹配当前 databaseId 的语句；如果带或者不带的语句都有，则不带的会被忽略。 |                |

### selectKey元素

| 属性          | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| keyProperty   | selectKey 语句结果应该被设置的目标属性。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |
| keyColumn     | 匹配属性的返回结果集中的列名称。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |
| resultType    | 结果的类型。MyBatis 通常可以推算出来，但是为了更加确定写上也不会有什么问题。MyBatis 允许任何简单类型用作主键的类型，包括字符串。如果希望作用于多个生成的列，则可以使用一个包含期望属性的 Object 或一个 Map。 |
| order         | 这可以被设置为 BEFORE 或 AFTER。如果设置为 BEFORE，那么它会首先选择主键，设置 keyProperty 然后执行插入语句。如果设置为 AFTER，那么先执行插入语句，然后是 selectKey 元素 - 这和像 Oracle 的数据库相似，在插入语句内部可能有嵌入索引调用。 |
| statementType | 与前面相同，MyBatis 支持 STATEMENT，PREPARED 和 CALLABLE 语句的映射类型，分别代表PreparedStatement 和 CallableStatement 类型。 |



## sql

​	sql元素：用来定义可重用的 SQL 代码段，可以包含在其他语句中；

> 实例

```xml
<sql id="Base_Column_List">
    id, user_name, real_name, sex, mobile, email, note,
    position_id
</sql>
<select id="selectById" resultMap="BaseResultMap"	parameterType="java.lang.Integer">
    select
    <include refid="Base_Column_List"/>
    from t_user
    where id = #{id,jdbcType=INTEGER}
</select>
```

## 参数

* 预编译 #{}：将传入的数据都当成一个字符串，会对自动传入的数据加一个双引号，能够很大程度防止
  sql注入；
*  传值 ${}：传入的数据直接显示生成在sql中，无法防止sql注入；
*  排序时使用order by和in操作， 用$而不是#

## 注解方式配置

​		注解方式就是将SQL语句直接写在接口上，对于需求比较简单的系统，效率较高。缺点在于，每次修改sql语句都要编译代码，对于复杂的sql语句可编辑性和可读性都差，一般不建议使用这种配置方式；

*  @Select
*  @Results
*  @Insert
*  @Update
*  @Delete

> 实例

```java
@Results(id = "UserResultMap", value = {
    @Result(property = "id", column = "id", id = true),
    @Result(property = "realName", column = "user_name"),
    @Result(property = "realName", column = "real_name"),
    @Result(property = "sex", column = "sex"),
    @Result(property = "mobile", column = "mobile"),
    @Result(property = "email", column = "email"),
    @Result(property = "note", column = "note"),
    @Result(property = "positionId", column = "position_id")
})
@Select(value = "select id, user_name , real_name , sex, mobile, email, note,position_id positionId from t_user")
List<TUser> findAll();
```



## cache 



## cache-ref

