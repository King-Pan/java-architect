package club.javalearn.pattern.factory.simple;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class SimpleFactoryTest {

    public static void main(String[] args) {


        //简单工厂，相当于小商店
        SimpleFactory simpleFactory = new SimpleFactory();

        //从工厂中获取对于的对象，相当于从商店获取商品
        //把创建对象的过程隐藏了，创建过程用户是不清楚的。
        // 现在有三种牛奶: 特仑苏，伊利，蒙牛，如果新增一种，三鹿奶粉
        // 我们必须知道三鹿奶粉的name
        System.out.println(simpleFactory.getMilk("特仑苏"));
    }
}
