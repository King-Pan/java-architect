package club.javalearn.pattern.factory.func;

import club.javalearn.pattern.factory.MenNiuMilk;
import club.javalearn.pattern.factory.Milk;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class MenNiuFactory implements Factory {
    @Override
    public Milk getMilk() {
        return new MenNiuMilk();
    }
}
