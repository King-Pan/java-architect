package club.javalearn.pattern.proxy.test;

import club.javalearn.pattern.proxy.staticed.LinkHome;
import club.javalearn.pattern.proxy.staticed.Lodget;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-08
 * Time: 23:38
 * Description: 静态代理测试类
 */
public class StaticedTest {


    public static void main(String[] args) {
        //租房客
        Lodget lodget = new Lodget();

        //租房客找到链家找房子
        //这儿的中介可能有很多家，我们不需要修改租房客的代码，又可以扩展，
        //【符合开闭原则，对修改关闭，对扩展开放】
        LinkHome linkHome = new LinkHome(lodget);
        //链家给租房客查找房源


        linkHome.tenement();
    }
}
