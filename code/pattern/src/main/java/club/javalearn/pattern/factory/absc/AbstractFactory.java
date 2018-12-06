package club.javalearn.pattern.factory.absc;

import club.javalearn.pattern.factory.Milk;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description 抽象工厂模式:  把公共的逻辑写在一起，方便统一管理
 *
 *  Spring中使用抽象工厂模式
 */
public abstract class AbstractFactory {

    /**
     * 生产蒙牛牛奶
     * @return
     */
    public abstract Milk getMenNiuMilk();

    /**
     * 生产伊利牛奶
     * @return
     */
    public abstract Milk getYiLiMilk();


    /**
     * 生产特仑苏牛奶
     * @return
     */
    public abstract Milk getTeLunSuMilk();


    /**
     * 生成三鹿奶粉
     * @return
     */
    public abstract Milk getSanLuMilk();
}
