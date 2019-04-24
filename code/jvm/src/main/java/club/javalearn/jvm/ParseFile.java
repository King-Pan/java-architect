package club.javalearn.jvm;


import java.io.*;
import java.util.Arrays;

/**
 * @author king-pan
 * @date 2019/4/22
 * @Description ${DESCRIPTION}
 */
public class ParseFile {

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\pansf\\Documents\\GitHub\\java-architect\\code\\jvm\\src\\main\\resources\\20190127_0266.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        File imgFile = new File("C:\\Users\\pansf\\Documents\\GitHub\\java-architect\\code\\jvm\\src\\main\\resources\\20190101_0142.txt");
        BufferedReader br2 = new BufferedReader(new FileReader(imgFile));
        String line = null;

        File outFile = new File("C:\\Users\\pansf\\Documents\\GitHub\\java-architect\\code\\jvm\\src\\main\\resources\\out.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

        while ((line=br.readLine())!=null){
            String[]values = line.split("\\|");
            String row = values[0]+"\t"+values[1]+"\t" + values[2]+"\t"+ values[4]+ "\t"+values[7] + "\t" + br2.readLine();
            System.out.println("长度："+values.length +"---"+Arrays.toString(values));
            System.out.println(row);
            bw.write(row);
            bw.newLine();
        }

        //长度：9---[17871254588,
        // 421202198910256232,
        // 郭国成,
        // HB.XN.02.05.16,
        // 715190127285763672,
        // 2019-01-27 15:06:37,
        // v1902289,
        // 715190127284820516_Install_R_1.jpg,715190127284820516_Install_Z_001.jpg,715190127284820516_Install_F_001.jpg,
        // Install]

    }
}
