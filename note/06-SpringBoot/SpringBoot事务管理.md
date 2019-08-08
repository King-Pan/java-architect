# SpringBoot事务管理



1. 开启事务

   ```java
   @EnableTransactionManagement
   ```

   

2. 在使用的方法或者类上加上

   ```java
   @Transactional(rollbackFor = RuntimeException.class)
   ```

   

