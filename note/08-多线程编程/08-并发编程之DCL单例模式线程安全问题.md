# 08-并发编程之DCL单例模式线程安全模式





```java
/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 饱汉式-懒加载，性能高，线程安全，但是可能引起空指针异常,编译优化,指令重排序
 *
 */
public class Singleton {

    /**
     * volatile：遵循happend before原则，禁止jvm编译优化和CPU的指令重排序
     *
     */
    private  volatile static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```



​		DCL(双端检索)机制不一定线程安全，原因是有指令重排序的存在，加入volatile可以禁止指令重排序

原因子啊与某一时刻执行第一次检测，读取到的instance不为null时，instance的引用对象可能没有完成初始化。

 instance = new Singleton()可以分为一下三步CPU指令完成。

* memory = allocate()//1. 分配对象内存空间
* instance(memory);//2. 初始化对象
* instance = memory; //3. 设置instance指向刚分配的内存地址，此时instance!=null;

步骤2和步骤3不存在数据依赖关系，而且无论重排前还是重排后程序的执行结果在单线程中并没有改变，因此这种重排优化是允许的。

memory= allocate();//1. 分配对象内存空间
instance = memory;//3 . 设置instance指向刚分配的内存地址，此时instance!=null，但是对象没有初始化完成。

instance(memory);//2. 初始化对象