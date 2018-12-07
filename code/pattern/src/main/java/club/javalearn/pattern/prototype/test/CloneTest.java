package club.javalearn.pattern.prototype.test;

import club.javalearn.pattern.prototype.simple.Prototype;

/**
 * @author king-pan
 * @date 2018/12/7
 * @Description  浅复制，新创建一个对象，对象的属性引用地址相同
 */
public class CloneTest {

    public static void main(String[] args) {
        Prototype prototype = new Prototype();
        prototype.setName("Tom");
        prototype.getList().add(1);

        try {
            Prototype pro =  (Prototype) prototype.clone();
            System.out.println(pro.getList());
            System.out.println(pro.getList() == prototype.getList());
            System.out.println(pro.getName() == prototype.getName());
            System.out.println(pro == prototype);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
