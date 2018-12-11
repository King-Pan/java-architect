package club.javalearn.pattern.proxy.test;

import club.javalearn.pattern.proxy.Tenementable;
import club.javalearn.pattern.proxy.jkd.JDKLinkHome;
import club.javalearn.pattern.proxy.staticed.Lodget;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author king-pan
 * Date: 2018-12-08
 * Time: 23:52
 * Description: JDK动态代理测试类
 */
public class JDKLinkHomeTest {

    public static void main(String[] args) {
        Tenementable tenementable = (Tenementable) new JDKLinkHome().getInstance(new Lodget());
        System.out.println("JDK动态代理的类名称: " + tenementable.getClass());
        tenementable.tenement();


        //原理：
        //1、拿到被代理对象的引用，并且获取到它的所有的接口，反射获取
        //2、JDK Proxy类重新生成一个新的类、同时新的类要实现被代理类所有实现的所有的接口
        //3、动态生成Java代码，把新加的业务逻辑方法由一定的逻辑代码去调用（在代码中体现）
        //4、编译新生成的Java代码.class
        //5、再重新加载到JVM中运行
        //以上这个过程就叫字节码重组

        //JDK中有个规范，只要要是$开头的一般都是自动生成的

        //通过反编译工具可以查看源代码
        byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{Tenementable.class});
        FileOutputStream os = null;
        try {
            os = new FileOutputStream("./$Proxy0.class");
            os.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
