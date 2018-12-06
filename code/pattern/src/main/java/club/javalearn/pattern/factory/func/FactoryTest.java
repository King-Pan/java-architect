package club.javalearn.pattern.factory.func;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class FactoryTest {
    public static void main(String[] args) {

        // 必须修改客户端代码
        Factory factory = new SanLuFactory();

        // 增加了复杂度，必须要知道牛奶的具体工厂，才能生产具体的牛奶
        // 3中牛奶，3个工厂，如果10个牛奶就需要十个工厂   10 .. 100 .. 1000.....
        // 现在有三种牛奶: 特仑苏，伊利，蒙牛，如果新增一种，三鹿奶粉
        // 我们必须修改客户端代码Factory factory = new SanLuFactory()才能生产三鹿奶粉
        System.out.println(factory.getMilk().getName());

    }
}
