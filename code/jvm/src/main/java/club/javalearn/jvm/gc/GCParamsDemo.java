package club.javalearn.jvm.gc;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/11
 * @Description ${DESCRIPTION}
 */
public class GCParamsDemo {

    public static void main(String[] args) {
        //gc XX参数

        System.out.println("-------------hello GC");
        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
