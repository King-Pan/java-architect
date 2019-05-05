package club.javalearn.collection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author king-pan
 * @date 2019/5/5
 * @Description HashSet 是线程不安全的
 * hashset底层就是hashmap, hashmap key-value
 *
 * hashset中的hashmap 只关心key，value是一个常量。private static final Object PRESENT = new Object();
 */
public class SetDemo {


    public static void main(String[] args) {

        //Set<String> set = new HashSet<>(50);
        //Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }
}
