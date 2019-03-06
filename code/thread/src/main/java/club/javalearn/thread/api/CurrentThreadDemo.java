package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class CurrentThreadDemo {

    public static void main(String[] args) {
        //jvm 是多线程的
        //jvm
        System.out.println("Current Thread: " + Thread.currentThread().getName());
    }
}
