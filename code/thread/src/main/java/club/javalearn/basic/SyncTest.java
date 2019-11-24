package club.javalearn.basic;

/**
 * @author king-pan
 * @date 2019/11/22 23:45
 */
public class SyncTest {

    private int age = 100000;

    private static class TestThread extends Thread {
        private SyncTest syncTest;

        public TestThread(SyncTest syncTest, String name) {
            super(name);
            this.syncTest = syncTest;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                syncTest.test();
            }
        }
    }

    public void test() {
        age++;
    }
}
