package club.javalearn.basic;

/**
 * @author king-pan
 * @date 2019/11/22 0:07
 * 如何中断线程
 *
 * volatile boolean runFlag = false;标识, 如果线程中有阻塞，响应不及时
 *
 * while(runFlag || !isInterrupter()){//中断线程
 *
 * }
 *
 * 方法会抛出InterruptedException异常时，线程的中断标志位重置位false
 */
public class EndThread2 {

    private static class UseThread2 extends Thread{
        public UseThread2(String name){
            super((name));
        }

        public UseThread2(){}

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            while (!isInterrupted()){
                try {
                    Thread.sleep(99);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+" interrupter flag is " + isInterrupted());
                    e.printStackTrace();
                    //再次中断
                    interrupt();
                }
                System.out.println(threadName + " is running.");
            }

            System.out.println(threadName+ " interrupt flag is " + isInterrupted());
            interrupted();
            System.out.println(threadName+ " interrupt flag is " + isInterrupted());

        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread endThread = new UseThread2("endThread");
        endThread.start();
        //interrupt方法，中断标志位设置为true
        //java线程是协作的
        Thread.sleep(500);
        endThread.interrupt();
        //isInterrupted()判断当前线程是否处于中断状态
        // static方法interrupted()判断当前线程是否处于中断状态，终端标志为改为false
    }
}
