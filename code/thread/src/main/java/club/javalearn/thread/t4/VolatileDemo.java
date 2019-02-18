package club.javalearn.thread.t4;

import javafx.scene.paint.Stop;

import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/2/18
 * @Description ${DESCRIPTION}
 */
public class VolatileDemo {

    private static volatile boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            int i =0;
            while (!stop){
                i++;
            }
        },"volatileThread");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        stop = true;
    }
}
