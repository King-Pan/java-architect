package club.javalearn.basic;

/**
 * @author king-pan
 * @date 2019/11/22 0:26
 *
 * 守护线程:
 * start()之前设置daemon(true);设置守护线程
 * 守护线程的finally代码块不一定会执行.
 */
public class DaemonThread {
    private static class UseThread extends Thread{
        @Override
        public void run() {
            try {
                while (!isInterrupted()){
                    System.out.println("run ......");
                }
            }finally {
                System.out.println("daemon thread finally 代码块不一定执行");
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new  UseThread();
        //t1.setDaemon(true);
        t1.start();
        try {
            Thread.sleep(200);
            t1.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
