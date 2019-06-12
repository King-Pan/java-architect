package club.javalearn.jvm.mts;

/**
 * @author king-pan
 * @date 2019/6/3
 * @Description ${DESCRIPTION}
 */
public class MetatspaceTest1 {


    public static void main(String[] args) {
        System.out.println("-XX:+PrintGCDetail");
        while (true) {
            Object obj1 = new Object();

            System.out.println(obj1);
        }
    }
}
