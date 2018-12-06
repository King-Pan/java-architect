package club.javalearn.pattern.factory.func;

import club.javalearn.pattern.factory.Milk;
import club.javalearn.pattern.factory.Telunsu;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class TeLunSuFactory implements Factory {
    @Override
    public Milk getMilk() {
        return new Telunsu();
    }
}
