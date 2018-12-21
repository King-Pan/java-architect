# SpringMvc相关面试题

## SpringMvc是什么

​	什么是Spring MVC ？简单介绍下你对springMVC的理解?

​	Spring MVC是一个基于MVC架构的用来简化web应用程序开发的应用开发框架，它是Spring的一个模块,无需中间整合层来整合 ，它和Struts2一样都属于表现层的框架。在web模型中，MVC是一种很流行的框架，通过把Model，View，Controller分离，把较为复杂的web应用分成逻辑清晰的几部分，简化开发，减少出错，方便组内开发人员之间的配合。

## SpringMvc线程安全

​	对于使用过SpringMVC和Struts2的人来说，大家都知道SpringMVC是基于方法的拦截，而Struts2是基于类的拦截。

 	对于Struts2来说，因为每次处理一个请求，struts就会实例化一个对象；这样就不会有线程安全的问题了;

​	而Spring的controller默认是Singleton的，这意味着每一个request过来，系统都会用原有的instance去处理，这样导致两个结果：

​	一是我们不用每次创建Controller，二是减少了对象创建和垃圾收集的时间;由于只有一个Controller的instance，当多个线程调用它的时候，它里面的instance变量就不是线程安全的了，会发生窜数据的问题。

 	当然大多数情况下，我们根本不需要考虑线程安全的问题，比如dao,service等，除非在bean中声明了实例变量。因此，我们在使用spring mvc 的contrller时，应避免在controller中定义实例变量。

 

>  解决方案：



* 在Controller中使用ThreadLocal变量
* 在spring配置文件Controller中声明 scope="prototype"，每次都创建新的controller
  所在在使用spring开发web 时要注意，默认Controller、Dao、Service都是单例的。