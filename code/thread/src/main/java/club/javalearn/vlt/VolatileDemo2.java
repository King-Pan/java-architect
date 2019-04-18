package club.javalearn.vlt;


import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/4/12
 * @Description ${DESCRIPTION}
 *
 * 1. 验证volatile的可见性
 * 1.1 假设int num = 0;num变量没有添加volatile，没有可见性
 * 1.2 添加volatile关键字，解决可见性
 *
 * 2. 验证volatile不保证原子性
 * 2.1 原子性是什么意思？ 不可分割，完整性，也即是线程正在执行某个操作，这个操作不可以中断，加塞等，要么不做，要么做完。
 * 2.2 是否可以保证原子性,volatile不保证原子性
 */
public class VolatileDemo2 {
    public static void main(String[] args) {
        MyData2 myData = new MyData2();

        for (int i =0;i<20;i++){
            new Thread(() -> {
               for (int j=0;j<1000;j++){
                   myData.addPlusPlus();
               }
            },"线程"+(i)).start();
        }
        // main线程
        // 后台gc线程
        while (Thread.activeCount()> 2) {
            Thread.yield();
        }
        //1. volatile如果保证原子性: 那么结果就是20000
        //2. 实际结果可能小于20000
        System.out.println(Thread.currentThread().getName() + "\t value=" + myData.num);
    }
}


class MyData2 {
    //内存不可见
   volatile int num;

    public void addPlusPlus() {
        num++;
    }
}