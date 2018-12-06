package club.javalearn.pattern.singleton.hungry;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class HungrySingleton {

    /**
     * 单例模式:
     *      1. 私有构造方法(在该类外部不能使用new创建该对象)
     *      2. 提供一个私有的静态属性
     *      2. 对外提供一个静态方法返回该类实例(私有构造器只能对外提供静态方法)
     *      3. 不管调用多少次静态方法，返回的实例都是唯一
     */
    private HungrySingleton(){}

    //先静态，后动态
    //先属性，后方法
    //先上后下

    private static final HungrySingleton instance = new HungrySingleton();

    public static HungrySingleton getInstance(){
        return instance;
    }
}
