package club.javalearn.thread.api;

/**
 * @author king-pan
 * @date 2019/3/6
 * @Description ${DESCRIPTION}
 */
public class MyRunnable implements Runnable{

    private String name;

    public MyRunnable(String name){
        this.name = name;
    }


    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Thread start : " + this.name + ",i=" + i);
        }
    }
}
