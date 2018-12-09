package club.javalearn.pattern.proxy.custom;

import club.javalearn.pattern.proxy.Tenementable;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-09
 * Time: 12:00
 * Description: No Description
 */
public class CustomLinkHome implements CustomInvocationHandler {

    private Tenementable target;

    public Object getInstance(Tenementable target) {
        this.target = target;

        Class<?> clazz = target.getClass();
        //用来生成一个新的对象（字节码重组来实现）
        return CustomProxy.newProxyInstance(new CustomClassLoader(), clazz.getInterfaces(), this);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("手写动态代理实现：欢迎光临本地");
        System.out.println("手写动态代理实现: 请问您有什么需求");

        Object obj = method.invoke(this.target, args);

        System.out.println("手写动态代理实现: 欢迎下次光临");

        return obj;

    }
}
