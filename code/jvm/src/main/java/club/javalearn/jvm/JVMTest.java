package club.javalearn.jvm;

/**
 * @author king-pan
 * @date 2019/2/27
 * @Description ${DESCRIPTION}
 */
public class JVMTest {

    public static void main(String[] args) {
        String str = System.getProperty("str");
        if (str == null) {
            System.out.println("str is null");
        } else {
            System.out.println(str);
        }
    }
}
