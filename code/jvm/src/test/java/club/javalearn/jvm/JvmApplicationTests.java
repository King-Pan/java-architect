package club.javalearn.jvm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;


public class JvmApplicationTests {

    @Test
    public void contextLoads() {

        String str = "20190402142640|20190402 14:27:14.859|2019-04-02 14:28:02|dacp::Nlkkh5Y1Pjp9UBnJ5Ovdpw==|28731|112707329";
        System.out.println(Arrays.toString(str.split("\\|")));
    }

}
