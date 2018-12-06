package club.javalearn.pattern.singleton.lazy;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description 懒汉式： 私有静态内部类，强烈推荐使用
 *    与spring集成时，可以使用spring的单例模式
 */
public class LazySingleton4 {

    private static boolean initialized = false;

    /**
     * 默认使用LazySingleton3 会先初始化内部类LazySingletonHelper
     * 如果没有使用，内部类是不加载的
     */
    private LazySingleton4(){
        synchronized (LazySingleton4.class){

            if(initialized == false){
                initialized = !initialized;
            }else{
                throw new RuntimeException("单例已被侵犯");
            }
        }
    }


    public static synchronized LazySingleton4 getInstance(){
        return LazySingletonHelper.INSTANCE;
    }

    private static class LazySingletonHelper{
        private static final LazySingleton4 INSTANCE = new LazySingleton4();
    }
}
