package club.javalearn.pattern.singleton.test;

import club.javalearn.pattern.singleton.register.RegisterEnum;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class RegisterEnumTest {

    public static void main(String[] args) {
        RegisterEnum.INSTANCE.getInstance();
    }
}
