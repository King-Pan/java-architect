package club.javalearn.thread.join;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class ThreadJoinDemo3 {

    public static void main(String[] args) throws InterruptedException {
        // 在线程内部调用了Thread.currentThread.join()方法，会造成线程卡死, main线程
        //000000000000000000000000000000000000000000010
        //
        System.out.println(Integer.MAX_VALUE >> 33);
        System.out.println(2<<32);
        Thread.currentThread().join();

    }
}
