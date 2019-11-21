package club.javalearn.basic;

/**
 * @author king-pan
 * @date 2019/11/22 0:07
 * 如何中断线程
 */
public class EndThread {

    private static class UseThread extends Thread{
        public UseThread(String name){
            super((name));
        }

        public UseThread(){}

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            while (!isInterrupted()){
                System.out.println(threadName + " is running.");
            }

            System.out.println(threadName+ " interrupt flag is " + isInterrupted());
            interrupted();
            System.out.println(threadName+ " interrupt flag is " + isInterrupted());

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread endThread = new UseThread("endThread");
        endThread.start();
        //interrupt方法，中断标志位设置为true
        //java线程是协作的
        Thread.sleep(20);
        endThread.interrupt();
        //isInterrupted()判断当前线程是否处于中断状态
        // static方法interrupted()判断当前线程是否处于中断状态，终端标志为改为false
    }
}
