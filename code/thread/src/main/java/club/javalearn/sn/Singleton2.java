package club.javalearn.sn;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 饱汉式
 */
public class Singleton2 {

    private static Singleton2 instance;

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}
