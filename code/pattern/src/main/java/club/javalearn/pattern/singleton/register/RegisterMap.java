package club.javalearn.pattern.singleton.register;


import java.util.HashMap;
import java.util.Map;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description ${DESCRIPTION}
 */
public class RegisterMap {

    private RegisterMap() {
    }

    private static Map<String, Object> register = new HashMap<>(50);


    public static RegisterMap getInstance(String name) {
        if (null == name) {
            name = RegisterMap.class.getName();
        }

        if (register.get(name) == null) {
            register.put(name, new RegisterMap());
        }

        return (RegisterMap) register.get(name);
    }
}
