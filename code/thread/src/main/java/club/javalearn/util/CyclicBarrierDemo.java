package club.javalearn.util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author king-pan
 * @date 2019/5/8
 * @Description ${DESCRIPTION}
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        //CyclicBarrier
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("董事会成员到齐，开始开会");
        });

        for(int i=0;i<7;i++){
            new Thread(()->{
                System.out.println("第"+Thread.currentThread().getName()+"位董事会成员到达会议室");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
