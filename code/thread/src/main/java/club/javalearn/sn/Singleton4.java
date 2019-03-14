package club.javalearn.sn;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 饱汉式-懒加载，性能高，线程安全，但是可能引起空指针异常,编译优化,指令重排序
 *
 * 线程A，线程B 两个线程调用Singleton4.getInstance()方法
 */
public class Singleton4 {

    private static Singleton4 instance;

    private Singleton4() {
    }

    public static Singleton4 getInstance() {
        if (instance == null) {
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }
}
