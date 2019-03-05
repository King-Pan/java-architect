package club.javalearn.thread2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author king-pan
 * @date 2019/2/26
 * @Description ${DESCRIPTION}
 */
public class Command {
    public static void main(String[] args) {
        try {

            Process process = Runtime.getRuntime().exec("/bin/sh sed-file.sh /root/000000_1");
            process.waitFor();
            InputStreamReader isr = new InputStreamReader(new BufferedInputStream(process.getErrorStream()));
            BufferedReader bfr = new BufferedReader(isr);
            String line = null;
            while ((line = bfr.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
