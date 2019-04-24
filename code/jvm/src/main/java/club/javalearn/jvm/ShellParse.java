package club.javalearn.jvm;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author king-pan
 * @date 2019/4/22
 * @Description ${DESCRIPTION}
 */
public class ShellParse {

    static BASE64Decoder decoder = new BASE64Decoder();   //解密

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\pansf\\Documents\\GitHub\\java-architect\\code\\jvm\\src\\main\\resources\\20190101_0142.txt";
        File file = new File(fileName);

        //临时文件
        File tempFile = File.createTempFile(file.getName(), null, new File("C:\\Users\\pansf\\Documents\\GitHub\\java-architect\\code\\jvm\\src\\main\\resources\\temp"));

        System.out.println(tempFile.getAbsolutePath());

        OutputStream fos = new FileOutputStream(tempFile);


        String value = FileUtils.readFileToString(file, "utf-8");
        byte[] bys = decoder.decodeBuffer(value);
        fos.write(bys);
        fos.flush();
        fos.close();

        LineIterator it = FileUtils.lineIterator(tempFile, "UTF-8");
        String line;
        String[] values;
        try {
            while (it.hasNext()) {
                line = it.nextLine();
                values = line.split("\\|");
                System.out.println(Arrays.toString(values));
                System.out.println(values[0]+"\t"+values[1]+"\t"+values[2]);

            }
        } finally {
            it.close();
        }

    }
}
