package club.javalearn.pattern.singleton.lazy;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description 懒汉式： 默认加载的时候不实例化，需要使用的时候再实例化(Spring的延时加载)
 */
public class LazySingleton {
    private LazySingleton(){}

    private static LazySingleton instance ;

    public static LazySingleton getInstance(){
        if(null == instance){
            instance = new LazySingleton();
        }
        return instance;
    }
}
