package club.javalearn.jvm;

import java.util.Date;

/**
 * @author king-pan
 * @date 2019/2/27
 * @Description ${DESCRIPTION}
 */
public class WhileTest {

    public static void main(String[] args) {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("运行中:" + new Date());
        }
    }
}
