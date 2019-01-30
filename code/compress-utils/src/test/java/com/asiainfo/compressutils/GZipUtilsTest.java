package com.asiainfo.compressutils;

import com.asiainfo.compress.utils.GZipUtils;
import org.junit.Test;

/**
 * @author king-pan
 * @date 2019/1/24
 * @Description ${DESCRIPTION}
 */
public class GZipUtilsTest {


    @Test
    public void test() {
        try {
            // GZipUtils.compress(new File("e:\\readme.txt"),false);
            GZipUtils.decompress("d:\\readme.txt.gz", "F:\\", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
