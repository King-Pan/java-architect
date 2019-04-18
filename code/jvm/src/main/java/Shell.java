import java.io.*;

/**
 * @author king-pan
 * @date 2019/4/18
 * @Description ${DESCRIPTION}
 * <p>
 * #!/bin/sh
 * TOM_HOME=$(cd `dirname $0`;cd ..;pwd)
 * ps -ef|grep $TOM_HOME|grep -v grep|grep -v kill
 * if [ $? -eq 0 ];then
 * kill -9 `ps -ef|grep $TOM_HOME|grep -v grep|grep -v kill|awk '{print $2}'`
 * else
 * echo $TOM_HOME' No Found Process'
 * fi
 * <p>
 * <p>
 * ps -ef|grep |grep -v grep
 */
public class Shell {

    public static void main(String[] args) throws Exception {
        //callShell(args[0]);
        String cmd = "ps -ef|grep java|grep -v grep";
        System.out.println("需要执行的命令:" + cmd);
        while (true) {
            if (ps(cmd)) {
                break;
            } else {
                System.out.println("进程还存在:" + cmd);
            }
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("脚本执行完毕");
    }


    public static boolean ps(String cmd) throws IOException {
        BufferedReader br = null;
        BufferedWriter wr = null;
        boolean isFish = true;
        try {
            System.out.println("开始执行命令:" + cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            int status = process.waitFor();
            if(status!=0){
                System.out.println("执行失败");
            }else{
                System.out.println("执行成功");
            }

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            wr = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            String inline;
            while ((inline = br.readLine()) != null) {
                isFish = false;
                System.out.println(inline);
            }
            br.close();
            //错误信息
            br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((inline = br.readLine()) != null) {
                System.out.println(inline);
            }
        } catch (Throwable e) {
            System.out.println("call shell failed. " + e.getMessage());
        } finally {
            if (br != null) {
                br.close();
            }
            if (wr != null) {
                wr.close();
            }
        }
        return isFish;
    }

    public static void callShell(String shellString) {
        try {
            Process process = Runtime.getRuntime().exec(shellString);
            System.out.println("等待shell脚本返回执行结果");
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                System.out.println("call shell failed. error code is :" + exitValue);
            } else {
                System.out.println("等待shell脚本返回执行结果");
            }
        } catch (Throwable e) {
            System.out.println("call shell failed. " + e.getMessage());
        }
    }
}
