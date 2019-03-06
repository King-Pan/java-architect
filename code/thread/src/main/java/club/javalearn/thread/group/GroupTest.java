package club.javalearn.thread.group;

import java.util.Arrays;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class GroupTest {

    public static void main(String[] args) {
        Thread thread = new Thread();
        thread.start();
        ThreadGroup threadGroup = thread.getThreadGroup();
        //获取线程组中存活的线程个数
        System.out.println(threadGroup.getName());
        System.out.println(threadGroup.activeCount());
        Thread[] list = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(list);
        Arrays.asList(list).forEach(System.out::println);
    }
}
