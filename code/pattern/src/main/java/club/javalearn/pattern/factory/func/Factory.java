package club.javalearn.pattern.factory.func;

import club.javalearn.pattern.factory.Milk;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description 工厂方法模式： 接口
 */
public interface Factory {

    public Milk getMilk();
}
