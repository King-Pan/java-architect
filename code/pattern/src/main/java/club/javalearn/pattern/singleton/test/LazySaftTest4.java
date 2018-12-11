package club.javalearn.pattern.singleton.test;

import club.javalearn.pattern.singleton.lazy.LazySingleton3;
import club.javalearn.pattern.singleton.lazy.LazySingleton4;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

/**
 * @author king-pan
 * @date 2018/12/6
 * <p>
 * Description 是线程安全的，synchronized消耗性能
 */
public class LazySaftTest4 {

    public static void main(String[] args) {


        Class<?> clz = LazySingleton4.class;
        Constructor constructor;
        try {
            constructor = clz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object o1 = constructor.newInstance();
            constructor = clz.getDeclaredConstructor();
            constructor.setAccessible(true);

            Object o2 = constructor.newInstance();
            System.out.println(o1 == o2);
            System.out.println(o1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
