package club.javalearn.sn;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 静态内部类:
 */
public class Singleton {

    private Singleton() {
    }

    public static Singleton getInstance() {
        return SingletonHepper.INSTANCE;
    }

    private static class SingletonHepper {
        private final static Singleton INSTANCE = new Singleton();
    }
}
