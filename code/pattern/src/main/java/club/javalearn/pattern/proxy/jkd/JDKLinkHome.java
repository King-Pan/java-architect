package club.javalearn.pattern.proxy.jkd;

import club.javalearn.pattern.proxy.Tenementable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author king-pan
 * Date: 2018-12-08
 * Time: 23:46
 * Description: 静态代理相当于是个人中介，一对一服务，现实中都是一个门店服务很多个租房客.
 * <p>
 * 这种情况下就需要用到了动态代理.
 * 1. 动态代理的应用: 标准化
 * 2.
 */
public class JDKLinkHome implements InvocationHandler {

    private Tenementable target;


    public Object getInstance(Tenementable target) {
        this.target = target;
        Class<?> clz = target.getClass();
        //用来生成一个新的对象（字节码重组来实现）
        return Proxy.newProxyInstance(clz.getClassLoader(), clz.getInterfaces(), this);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("LinkHome: 欢迎来到LinkHome，我们竭诚为您服务");
        System.out.println("LinkHome: 请提供您的要求");
        method.invoke(this.target, args);
        System.out.println("LinkHome：为您找到了合适的房源");
        System.out.println("LinkHome: 期待下次为您服务");
        return null;
    }
}
