package club.javalearn.sn;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description 饱汉式-同步方法,线程安全，但是影响性能
 */
public class Singleton3 {

    private static Singleton3 instance;

    private Singleton3() {
    }

    public synchronized static Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}
