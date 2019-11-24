package club.javalearn.basic;

import java.time.temporal.Temporal;

/**
 * @author king-pan
 * @date 2019/11/22 23:37
 * synchronized java内置锁
 * 对象锁 synchronized(obj){}
 * 类锁 public static synchronized void test(){}
 * synchronized(Object.class){}
 */
public class SynchronizedTest {
    private static class SynClass extends Thread{

    }

    private synchronized void instance(){
        //SleepTools
    }

    private static class InstanceSyn implements Runnable{
        private SynchronizedTest synchronizedTest;
        public InstanceSyn(SynchronizedTest synchronizedTest){
            this.synchronizedTest = synchronizedTest;
        }

        @Override
        public void run() {
            System.out.println("synchronizedTest is running ... "+ synchronizedTest);
            //synchronizedTest
        }
    }

}
