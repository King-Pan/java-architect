package club.javalearn.pattern.factory.absc;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class AbstractFactoryTest {

    public static void main(String[] args) {
        MilkFactory factory = new MilkFactory();
        //生产牛奶的位置只有一个,对用户只提供了选择权


        //前面的工厂代码不需要改动，只需要选择生成三鹿奶粉即可
        //直观的给用户展示了新的API，符合开闭原则，对修改关闭，对扩展开放
        System.out.println(factory.getSanLuMilk());
    }
}
