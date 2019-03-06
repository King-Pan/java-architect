package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class StackThread {

    private static int counter = 1;

    public static void main(String[] args) {

        Thread t1 = new Thread(null, new Runnable() {

            @Override
            public void run() {
                try {
                    add(1);
                } catch (Error e) {
                    System.out.println(counter);
                }

            }

            private void add(int i) {
                counter++;
                add(i + 1);
            }
        }, "stack-size-test", 1 << 24);

        t1.start();
    }
}
