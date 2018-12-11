package club.javalearn.pattern.proxy.staticed;

import club.javalearn.pattern.proxy.Tenementable;

/**
 * @author king-pan
 * Date: 2018-12-08
 * Time: 23:33
 * Description: 租房房客,自己租房子麻烦，找黑心的LinkHome，把要求告诉LinkHome
 */
public class Lodget implements Tenementable {


    @Override
    public void tenement() {
        System.out.println("我的要求: 一室一厅，独门独户，距离【光谷广场地铁】一公里以内，价格不超过1000元");
    }
}
