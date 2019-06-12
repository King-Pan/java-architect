package com.javalearn.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author king-pan
 * @date 2019/5/23
 * @Description ${DESCRIPTION}
 */
public class MapTest {

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>(10);
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");
        map.keySet().iterator();
    }
}

