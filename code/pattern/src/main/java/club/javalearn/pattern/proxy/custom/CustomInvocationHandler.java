package club.javalearn.pattern.proxy.custom;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-09
 * Time: 11:23
 * Description: 手写动态代理实现--模拟JDKInvocationHandler接口
 */
public interface CustomInvocationHandler {
    Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
