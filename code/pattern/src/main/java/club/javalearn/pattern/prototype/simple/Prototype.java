package club.javalearn.pattern.prototype.simple;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author king-pan
 * @date 2018/12/7
 * @Description 原型类
 */
@Data
public class Prototype implements Cloneable{

    private String name;

    private ArrayList list = new ArrayList();


    @Override
    public Object clone() throws CloneNotSupportedException {
        System.out.println("克隆开始");
        return super.clone();
    }
}
