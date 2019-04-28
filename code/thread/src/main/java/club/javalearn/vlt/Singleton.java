package club.javalearn.vlt;

/**
 * @author king-pan
 * @date 2019/4/28
 * @Description ${DESCRIPTION}
 */
public class Singleton {

    private static Singleton instance;

    private Singleton(){}

    public static Singleton getInstance(){
        if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
