package club.javalearn.pattern.singleton.lazy;

/**
 * @author king-pan
 * @date 2018/12/6
 *
 * Description 懒汉式： 私有静态内部类，强烈推荐使用
 *    与spring集成时，可以使用spring的单例模式
 */
public class LazySingleton3 {


    /**
     * 默认使用LazySingleton3 会先初始化内部类LazySingletonHelper
     * 如果没有使用，内部类是不加载的
     */
    private LazySingleton3(){}


    public  static LazySingleton3 getInstance() {
        return LazySingletonHelper.INSTANCE;
    }

    /**
     * 默认不加载LazySingletonHelper,也就是说默认不创建LazySingleton3对象,
     * 只有第一次调用getInstance()方法才会创建LazySingleton3对象，所以是懒汉式
     */
    private static class LazySingletonHelper{
        private static final LazySingleton3 INSTANCE = new LazySingleton3();
    }
}
