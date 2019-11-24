package club.javalearn.tl;

/**
 * @author king-pan
 * @date 2019/11/23 0:09
 *
 * threadLocal 存储比较小的，使用空间换取线程安全
 */
public class ThreadLocalDemo {
    static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    public void startThreadArray() {
        Thread[] runs = new Thread[3];
        for (int i = 0; i < runs.length; i++) {
            runs[i] = new Thread(new TestThread(i));
        }
        for (int i = 0; i < runs.length; i++) {
            runs[i].start();
        }
    }

    private static class TestThread implements Runnable {

        int id;

        public TestThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ":start");
            Integer s = threadLocal.get();
            s += id;
            threadLocal.set(s);
             //threadLocal.remove();
            System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());

        }
    }

    public static void main(String[] args) {
        ThreadLocalDemo demo = new ThreadLocalDemo();
        demo.startThreadArray();
    }
}
