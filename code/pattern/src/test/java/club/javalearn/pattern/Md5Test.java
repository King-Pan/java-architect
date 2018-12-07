package club.javalearn.pattern;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author king-pan
 * @date 2018/12/7
 * @Description ${DESCRIPTION}
 */
public class Md5Test {



    @Test
    public void test(){

        String str = "a264a295e1f58099"+ "0b1a3162" + "20181205155242";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes());
        String md5=new BigInteger(1, md.digest()).toString(16);

        System.out.println(md5.length()==32?md5:fillMD5("0"+md5));
    }

    public static String fillMD5(String md5){
        return md5.length()==32?md5:fillMD5("0"+md5);
    }
}
