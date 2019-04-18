# Volatile关键字



## Volatile简介

Volatile是java虚拟机提供的轻量级的同步机制:

* 保证内存可见性
* 不保证原子性
* 禁止指令重排





DCL(双端检锁) 机制不一定线程安全，原因是有指令重排序的存在，加入volatile可以禁止指令重排

原因在与某一个线程执行到第一次检测，读取到instance不为null时，instance的引用对象可能没有完成初始化。 
instance = new Singleton();可以分为以下三步完成

memory = allocate();//1.分配对象内存空间
instance(memory);   //2. 初始化对象
instance = memory;  //3. 设置instance指向刚分配的内存地址，此时instance!=null

步骤2和步骤3不存在数据依赖关系，而且无论重排还是重排后程序的执行结果在单线程中并没有改变，因此这种重排优化时允许的。
memory = allocate();   //1. 分配对象内存空间