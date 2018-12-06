package club.javalearn.pattern.singleton.test;

import club.javalearn.pattern.singleton.lazy.LazySingleton3;
import club.javalearn.pattern.singleton.lazy.LazySingleton4;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description 是线程安全的，synchronized消耗性能
 */
public class LazySaftTest4 {

    public static void main(String[] args) {


        Class clz = LazySingleton4.class;
       Constructor[] constructors = clz.getDeclaredConstructors();
       for (Constructor c:constructors){
           c.setAccessible(true);
           try {
              //Object o =  c.newInstance();
             Object o =  c.newInstance(new Object(){});
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }
}
