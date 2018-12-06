package club.javalearn.pattern.factory.absc;

import club.javalearn.pattern.factory.*;
import club.javalearn.pattern.factory.func.MenNiuFactory;
import club.javalearn.pattern.factory.func.TeLunSuFactory;
import club.javalearn.pattern.factory.func.YiLiFactory;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class MilkFactory extends AbstractFactory {
    @Override
    public Milk getMenNiuMilk() {
        return new MenNiuMilk();
    }

    @Override
    public Milk getYiLiMilk() {
        return new YiLiMilk();
    }

    @Override
    public Milk getSanLuMilk() {
        return new SanLuMilk();
    }

    @Override
    public Milk getTeLunSuMilk() {
        return new Telunsu();
    }
}
