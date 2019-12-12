## 第十章之JPA-Spring整合

>**JPA整合Spring三种方式**

* LocalEntityManagerFactoryBean：适用于那些仅使用 JPA 进行数据访问的项目，该 FactoryBean 将根据JPA PersistenceProvider 自动检测配置文件进行工作，一般从“META-INF/persistence.xml”读取配置信息，这种方式最简单，但不能设置 Spring 中定义的DataSource，且不支持 Spring 管理的全局事务
* 从JNDI中获取：用于从 Java EE 服务器获取指定的EntityManagerFactory，这种方式在进行 Spring 事务管理时一般要使用 JTA 事务管理
* LocalContainerEntityManagerFactoryBean：适用于所有环境的 FactoryBean，能全面控制 EntityManagerFactory 配置,如指定 Spring 定义的 DataSource 等等。

### 第一种整合方式

```
<!-- 配置 JPA 提供者的适配器 -->
<bean id="jpaVendorAdapter"  	class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
	<property name="databasePlatform">
		<bean class="com.atguigu.ssps.modules.persistence.Hibernates" 
			factory-method="getDialect">
				<constructor-arg ref="dataSource"></constructor-arg>
		</bean>
	</property>
</bean>
```

### 第三种整合方式(推荐使用)

```
<!-- 配置 JPA 的 EntityManager -->
<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
	<property name="dataSource" ref="dataSource"></property>
	<property name="jpaVendorAdapter" ref="jpaVendorAdapter"></property>
	<property name="packagesToScan" value="com.atuigu.crm"></property>
	<property name="jpaProperties">
		<props>
			<!-- 二级缓存相关 -->
			<prop key="hibernate.cache.region.factory_class">
				org.hibernate.cache.ehcache.EhCacheRegionFactory</prop>
			<prop key="net.sf.ehcache.configurationResourceName">
				ehcache-hibernate.xml</prop>
			<!-- 生成的数据表的列的映射策略 -->
			<prop key="hibernate.ejb.naming_strategy">
				org.hibernate.cfg.ImprovedNamingStrategy</prop>
			<!-- hibernate 基本属性 -->
			<prop key="hibernate.show_sql">true</prop>
			<prop key="hibernate.format_sql">true</prop>
			<prop key="hibernate.hbm2ddl.auto">update</prop>
		</props>
	</property>
</bean>
```


报表关键信息维护功能测试	100%	报表ID/报表名称只能通过手动填写	目前持久化框架开发效率低下，测试是否能集成优秀的spring-data-jpa框架，提高持久化部分开发效率
报表关键信息维护功能优化点1: 期望报表数据到达日期精确到小时	100%		
报表关键信息维护新增页面优化,必填字段增加提示,页面布局美观调整	100%		
列表选择增加checkbox,并且优化选择事件	100%		
自定义报表查询中的下拉框排序	100%		
