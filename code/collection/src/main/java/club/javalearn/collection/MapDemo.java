package club.javalearn.collection;

import java.util.*;

/**
 * @author king-pan
 * @date 2019/5/5
 * @Description ${DESCRIPTION}
 */
public class MapDemo {

    public static void main(String[] args) {
        //Map<String,String> map = new HashMap<>(50);
        Map<String,String> map = new Hashtable<>();
        //Map<String,String> map = Collections.synchronizedMap(new HashMap<>());
        Map<String,String> map = new Hashtable<>();
        for (int i=0;i<30;i++){
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            }).start();
        }
    }
}
