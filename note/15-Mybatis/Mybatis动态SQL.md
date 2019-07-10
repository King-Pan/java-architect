# Mybatis动态SQL

​		Mybatis的动态SQL实现：通过标签的方式，如果标签中的条件表达式成立，则把标签中的内容以sql片段的形式拼接成一个完整的SQL，从而实现Mybatis的动态SQL。

| 元素                      | 作用                  | 备注                                       |
| ------------------------- | --------------------- | ------------------------------------------ |
| if                        | 判断语句              | 单条件分支判断                             |
| choose（when、otherwise） | 相当于java的case when | 多条件分支判断                             |
| trim(where、set)          | 辅助元素              | 用于处理sql拼装问题                        |
| foreach                   | 循环语句              | 在in语句等列举条件常用，常用于实现批量操作 |
|                           |                       |                                            |

## if

​		if中的test表达式成立则把if标签中的内容拼接到sql中，从而实现动态sql

```XML
<select id="selectById" resultMap="BaseResultMap"	parameterType="TUser">
    select
    <include refid="Base_Column_List"/>
    from t_user
    where 1=1
    <if test="userName !=null and userName != ''">
        and user_name = #{userName,jdbcType=VARCHAR}
    </if>
    <if test="realName !=null and realName != ''">
        and real_name like CONCAT('%',#{realName,jdbcType=VARCHAR},'%') 
    </if>
</select>
```



## where

```xml
<select id="selectById" resultMap="BaseResultMap"	parameterType="TUser">
    select
    <include refid="Base_Column_List"/>
    from t_user
    where 1=1
    <where>
        <if test="userName !=null and userName != ''">
            and user_name = #{userName,jdbcType=VARCHAR}
        </if>
        <if test="realName !=null and realName != ''">
            and real_name like CONCAT('%',#{realName,jdbcType=VARCHAR},'%')
        </if>
    </where>
</select>
```

## set

* 当在update语句中使用if标签时，如果前面的if没有执行，则或导致逗号多余错误。使用set标签可以将动态的配置SET 关键字，和剔除追加到条件末尾的任何不相关的逗号。
* 在insert、update去除逗号

没有使用if标签时，如果有一个参数为null，都会导致错误，如下示例：

```xml
<update id="update" parameterType="TUser">
    update t_user
    real_name = #{realName,jdbcType=VARCHAR},
    user_name = #{userName,jdbcType=VARCHAR}
    where id = #{id,jdbcType=INTEGER}
</update>
```

需要解决更新字段后面的，如果是最后的一个更新字段不能有","的问题

> 使用set前

```xml
<update id="update" parameterType="TUser">
    update t_user set
    <if test="realName != null and realName != ''">
        real_name = #{realName,jdbcType=VARCHAR},
    </if>
    <if test="userName != null and userName != ''">
        user_name = #{userName,jdbcType=VARCHAR},
    </if>
    id = #{id}
    where id = #{id,jdbcType=INTEGER}
</update>
```

> 使用set后

```xml
<update id="update" parameterType="TUser">
    update t_user
    <set>
        <if test="realName != null and realName != ''">
            real_name = #{realName,jdbcType=VARCHAR},
        </if>
        <if test="userName != null and userName != ''">
            user_name = #{userName,jdbcType=VARCHAR},
        </if>
    </set>
    where id = #{id,jdbcType=INTEGER}
</update>
```

## trim

> trim实现where

```xml
<select id="selectByUser" resultMap="BaseResultMap"	parameterType="TUser">
    select
    <include refid="Base_Column_List"/>
    from t_user
    <trim prefix="where" prefixOverrides="and | or">
        <if test="userName !=null and userName != ''">
            and user_name = #{userName,jdbcType=VARCHAR}
        </if>
        <if test="realName !=null and realName != ''">
            and real_name like CONCAT('%',#{realName,jdbcType=VARCHAR},'%')
        </if>
    </trim>
</select>
```

> trim实现set

