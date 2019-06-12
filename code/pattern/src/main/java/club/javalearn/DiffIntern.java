package club.javalearn;

/**
 * @author king-pan
 * @date 2019/5/31
 * @Description ${DESCRIPTION}
 */
public class DiffIntern {

    public static void main(String[] args) {
        jdk6plusT2();
    }


    public static void jdk6plusT1(){
        String str1 = "a";
        String str2 =str1.intern();
        System.out.println(str1==str2);
    }
    public static void jdk6plusT2(){
        String str1 = "a";
        String str3 = new String("a");
        String str4 = str3.intern();
        String str2 =str1.intern();
        System.out.println(str1==str2);
        System.out.println(str1==str3);
        System.out.println(str1==str4);
        System.out.println(str2==str3);
        System.out.println(str2==str4);
        System.out.println(str3==str4);
    }

    public static void diff(){
        String s1 = new String("a");
        String s= s1.intern();

        String s2 = "a";
        System.out.println(s==s2);
        //false
        System.out.println(s1==s2);

        String s3 = new String("a")+new String("a");
        s3.intern();
        String s4 = "aa";
        //true
        System.out.println(s3==s4);
    }
}
