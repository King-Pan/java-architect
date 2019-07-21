# Mysql锁

共享锁(Shared Locks) vs 排他锁(Exclusive Locks)



共享锁:

​     又称为读锁，简称S锁，顾名思义，共享锁就是多个事务对于同一数据可以共享一把锁，都能访问到数据，但是只能读不能修改；

加锁释锁方式:

​	select * from users where id = 1 lock in share modd;



​	commit/rollback;



![](./images/lock.jpg)



## 共享锁(S锁)



select * from user lock in share mood;





> Innodb行锁到底锁了什么?



* InnoDB的行锁是通过给索引上的索引项加锁来实现的。
* 只有通过索引条件进行数据索引，InnoDB才使用行级锁，否则，InnoDB将使用表锁(锁住索引的所有记录)
* 表锁: local tables xx read/write;







![](./images/mysql-lock.png)



## 执行计划



![1544606327773](./images/exctor-plan.png)

