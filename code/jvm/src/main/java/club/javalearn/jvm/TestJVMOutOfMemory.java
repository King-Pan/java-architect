package club.javalearn.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author king-pan
 * @date 2019/2/28
 * @Description ${DESCRIPTION}
 */
public class TestJVMOutOfMemory {


    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            String str = "";
            for (int j = 0; j < 1000; j++) {
                str += UUID.randomUUID().toString();
            }
            list.add(str);
        }
        System.out.println("ok!!!!!!!!!!!!!!!!!!");
    }
}
