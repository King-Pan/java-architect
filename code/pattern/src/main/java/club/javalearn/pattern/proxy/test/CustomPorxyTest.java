package club.javalearn.pattern.proxy.test;

import club.javalearn.pattern.proxy.Tenementable;
import club.javalearn.pattern.proxy.custom.CustomLinkHome;
import club.javalearn.pattern.proxy.staticed.Lodget;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-09
 * Time: 11:59
 * Description: 手写动态代理实现--测试类
 */
public class CustomPorxyTest {

    public static void main(String[] args) {
        try {
            Tenementable target = new Lodget();
            Tenementable proxy = (Tenementable) new CustomLinkHome().getInstance(target);
            System.out.println(proxy.getClass());
            proxy.tenement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
