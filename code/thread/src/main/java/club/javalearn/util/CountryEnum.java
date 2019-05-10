package club.javalearn.util;

import lombok.Getter;

/**
 * @author king-pan
 * @date 2019/5/8
 * @Description ${DESCRIPTION}
 */
public enum CountryEnum {

    ONE(1, "齐国"), TWO(2, "楚国"), THREE(3, "燕过"), FOUR(4, "赵国"), FIVE(5, "韩国"), SIX(6, "魏");

    @Getter
    private Integer code;
    @Getter
    private String msg;

    CountryEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static CountryEnum forEachCountry(int index) {
        CountryEnum[] arrys = CountryEnum.values();
        CountryEnum result = null;
        for (CountryEnum element : arrys) {
            if (index == element.getCode()) {
                result = element;
                break;
            }
        }
        return result;
    }

}
