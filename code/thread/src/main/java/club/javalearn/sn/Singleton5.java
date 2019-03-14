package club.javalearn.sn;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 饱汉式-懒加载，性能高，线程安全，但是可能引起空指针异常,编译优化,指令重排序
 *
 * 线程A，线程B 两个线程调用Singleton4.getInstance()方法
 */
public class Singleton5 {

    /**
     * volatile：遵循happend before原则，禁止jvm编译优化和CPU的指令重排序
     *
     */
    private  volatile static Singleton5 instance;

    private Singleton5() {
    }

    public static Singleton5 getInstance() {
        if (instance == null) {
            synchronized (Singleton5.class) {
                if (instance == null) {
                    instance = new Singleton5();
                }
            }
        }
        return instance;
    }
}
