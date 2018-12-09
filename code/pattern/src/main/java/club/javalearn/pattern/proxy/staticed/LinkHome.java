package club.javalearn.pattern.proxy.staticed;

import club.javalearn.pattern.proxy.Tenementable;

/**
 * @author king-pan
 * Date: 2018-12-08
 * Time: 23:31
 * Description: LinkHome租房中介公司
 */
public class LinkHome implements Tenementable {

    private Tenementable target;

    public LinkHome(Tenementable target) {
        this.target = target;
    }

    @Override
    public void tenement() {
        System.out.println("欢迎选择链家,请告诉您的诉求");
        target.tenement();
        System.out.println("正在紧急给你查找房源......请稍后");
        System.out.println("已为您找到房源，请签订合同");
    }
}
