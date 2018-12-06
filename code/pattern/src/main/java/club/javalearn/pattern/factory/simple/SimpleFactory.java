package club.javalearn.pattern.factory.simple;

import club.javalearn.pattern.factory.*;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class SimpleFactory {

    public Milk getMilk(String name){
        Milk milk = null;
        if("特仑苏".equals(name)){
            milk = new Telunsu();
        }else if("蒙牛".equals(name)){
            milk = new MenNiuMilk();
        }else if("伊利".equals(name)){
            milk = new YiLiMilk();
        }else if("三鹿".equals(name)){
            milk = new SanLuMilk();
        }
        return milk;
    }
}
