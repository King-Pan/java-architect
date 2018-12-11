package club.javalearn.pattern.prototype.test;

import club.javalearn.pattern.prototype.deep.QiTianDaSheng;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-07
 * Time: 23:18
 * Description: Spring原码中的原型大多数使用的是反射
 * <p>
 * spring原码中的原型模式:
 * <p>
 * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
 * doCreateBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])
 */
public class DeepTest {

    public static void main(String[] args) {
        QiTianDaSheng qiTianDaSheng = new QiTianDaSheng();
        try {
            QiTianDaSheng cloneObj = (QiTianDaSheng) qiTianDaSheng.clone();
            System.out.println(qiTianDaSheng.getJinGuBang() == cloneObj.getJinGuBang());


            QiTianDaSheng obj2 = (QiTianDaSheng) qiTianDaSheng.deepClone();

            System.out.println(qiTianDaSheng.getJinGuBang() == obj2.getJinGuBang());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


        QiTianDaSheng q1 = new QiTianDaSheng();

        QiTianDaSheng q2 = q1.copy(q1);

        System.out.println(q1 == q2);

        System.out.println(q1.getJinGuBang() == q2.getJinGuBang());


    }

}
