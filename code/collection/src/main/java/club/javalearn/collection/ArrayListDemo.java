package club.javalearn.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author king-pan
 * @date 2019/5/5
 * @Description
 *
 * ArrayList多线程环境下常见的异样:
 *
 *
 *  Vector: 是1.0 版本,加锁，可以保证线程安全，但是性能低
 *  ArrayList: 是1.2版本
 *  ArrayList: 初始化时最好设置初始化因子
 *
 * 1. 故障现象
 *  java.util.ConcurrentModificationException  并发修改异常
 *
 * 2. 导致原因
 *  java.util.ConcurrentModificationException  并发修改异常
 *
 * 3. 解决方案
 *  3.1 new Vector<>()</>
 *  3.2 Collections.synchronizedList(new ArrayList<>());
 *  3.3 new CopyOnWriteArrayList<>();
 *  3.3.1:
 *      原理: 写时复制，读写分离的思想
 *
 *
 * 4. 优化建议(同样的错误不犯第二次)
 *
 */
public class ArrayListDemo {

    public static void main(String[] args) {
        //集合类线程不安全问题
        //Collections集合工具类
        //List<String> list = Collections.synchronizedList(new ArrayList<>());
        //List<String> list = new Vector<>();
        //List<String> list = new ArrayList<>();
        List<String> list = new CopyOnWriteArrayList<>();
        List<String> listStr = Arrays.asList("a","b","c");
        listStr.forEach(System.out::println);

        for (int i=0;i<30;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
