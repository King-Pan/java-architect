package club.javalearn.pattern.factory.simple;

import club.javalearn.pattern.factory.Telunsu;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class Test {

    /**
     *   原始社会，男耕女织，日出而作日落而息
     *
     *   衣食住行都是自产自销,吃的粮食是自己种植的，映射到java中，所以的对象都是自己new出来的
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(new Telunsu().getName());
    }
}
