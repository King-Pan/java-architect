需求描述:

项目组需要做一个自动部署的工具(很lower，为了解决远程部署的问题，不太适合叫自动部署)

192.168.1.9 ftp 服务器

192.168.1.10    autodeploy(工具部署的主机，可以外网访问，只开放了80端口)

80端口部署了一个nginx   

部署了一个tomcat 8080 

192.168.1.11-190 (服务器主机，内网访问，与10网络互通,只能通过ftp拷贝下载文件，不能通过SCP，不要问我为什么，我拿了刀问了客户了的)

把项目的war包上传192.168.1.10:8080上，然后上传到ftp上，最后分发到11-190的服务器上，

难点： 

自动部署，自动两个字，autodeploy项目中维护了11-190服务器信息：
主机IP、服务名、用户、密码、tomcat(公司只用tomcat)根目录，

通过autodeploy 项目的上传页面把war把上传到ftp服务器上
然后执行自动部署程序:
1. 备份原来的程序
2. 停止服务(备份，清除日志)
3. 下载最新的war到tomcat的webapps
4. 启动服务
5. 自动部署完成


下面的代码是其中的一段： 第三步骤: ssh远程到11-190的服务器上，然后执行ftp命令

```
package club.javalearn.crm.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * crm-parent
 *
 * @author king-pan
 * @date 2018-02-01
 **/
public class SshCommandExecutor {
    private Logger logger = LoggerFactory.getLogger(SshCommandExecutor.class);
    private String ipAddress;

    private String username;

    private String password;

    private static final int DEFAULT_SSH_PORT = 22;

    private Vector<String> stdout;

    public SshCommandExecutor(final String ipAddress, final String username, final String password) {
        this.ipAddress = ipAddress;
        this.username = username;
        this.password = password;
        stdout = new Vector<>();
    }

    public int execute(final String command) {
        int returnCode = 0;
        JSch jsch = new JSch();
        SshUserInfo userInfo = new SshUserInfo();

        try {
            // Create and connect session.
            Session session = jsch.getSession(username, ipAddress, DEFAULT_SSH_PORT);
            session.setPassword(password);
            session.setUserInfo(userInfo);
            session.connect();

            // Create and connect channel.
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            BufferedReader input = new BufferedReader(new InputStreamReader(channel
                    .getInputStream()));

            channel.connect();
            logger.info("The remote command is: " + command);

            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
            input.close();

            if (channel.isClosed()) {
                returnCode = channel.getExitStatus();
            }

            channel.disconnect();
            session.disconnect();
        } catch (JSchException e) {
            logger.error("执行脚本失败:" + command,e);
            e.printStackTrace();
            throw new RuntimeException("执行脚本失败:" + command);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("执行脚本失败:" + command,e);
            throw new RuntimeException("执行脚本失败:" + command);
        }
        return returnCode;
    }

    private Vector<String> getStandardOutput() {
        return stdout;
    }

    public static void main(final String [] args) {
        SshCommandExecutor sshExecutor = new SshCommandExecutor("192.168.1.200", "yh", "yh");
        String cmd = "ftp -n << EOF \n" +
                "open 192.168.1.200 \n" +
                "user jfftp 1Qaz#edc \n" +
                "bin \n" +
                "lcd /home/yh/soft \n" +
                "cd /home/jfftp/deploy/2018-02-01\n" +
                "get 12.zip\n" +
                "bye\n" +
                "EOF\n";
        long start = System.currentTimeMillis();
        sshExecutor.execute(cmd);
        long end  = System.currentTimeMillis();
        System.out.println("共耗时: " + (end-start)/1000+"秒");

        Vector<String> stdout = sshExecutor.getStandardOutput();
        for (String str : stdout) {
            System.out.println(str);
        }
    }
}

```

```
package club.javalearn.crm.utils;

import com.jcraft.jsch.UserInfo;

/**
 * crm-parent
 *
 * @author king-pan
 * @date 2018-02-01
 **/
public class SshUserInfo implements UserInfo {

    private String password;

    private String passphrase;

    @Override
    public String getPassphrase() {
        System.out.println("SshUserInfo.getPassphrase()");
        return null;
    }

    @Override
    public String getPassword() {
        System.out.println("MyUserInfo.getPassword()");
        return null;
    }

    @Override
    public boolean promptPassphrase(final String arg0) {
        System.out.println("MyUserInfo.promptPassphrase()");
        System.out.println(arg0);
        return false;
    }

    @Override
    public boolean promptPassword(final String arg0) {
        System.out.println("MyUserInfo.promptPassword()");
        System.out.println(arg0);
        return false;
    }

    @Override
    public boolean promptYesNo(final String arg0) {
        System.out.println("MyUserInfo.promptYesNo()");
        System.out.println(arg0);
        if (arg0.contains("The authenticity of host")) {
            return true;
        }
        return false;
    }

    @Override
    public void showMessage(final String arg0) {
        System.out.println("MyUserInfo.showMessage()");
    }
}
```


![服务器信息](http://upload-images.jianshu.io/upload_images/6331401-e24ef9a355391a58.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![部署操作](http://upload-images.jianshu.io/upload_images/6331401-59373a6272239bbe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



