package club.javalearn.pattern.prototype.test;

import club.javalearn.pattern.prototype.simple.CloneTarget;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-07
 * Time: 23:05
 * Description: 实现Cloneable 接口实现的复制，都是浅复制.
 */
public class CloneTargetTest {


    public static void main(String[] args) {
        CloneTarget target = new CloneTarget();
        target.setName("king");
        target.setTarget(new CloneTarget());

        try {
            CloneTarget cloneTarget = (CloneTarget) target.clone();
            System.out.println(target.getTarget() == cloneTarget.getTarget());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
