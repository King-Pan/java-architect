```
package com.asiainfo.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author king-pan
 * @date 2018-01-11
 **/
public class SendMail {
    private String username = null;
    private String password = null;
    private Authenticator auth = null;
    private MimeMessage mimeMessage = null;
    private Properties pros = null;
    private Multipart multipart = null;
    private BodyPart bodypart = null;

    /**
     * 初始化账号密码并验证
     * 创建MimeMessage对象
     * 发送邮件必须的步骤:1
     *
     * @param username
     * @param password
     */
    public SendMail(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 初始化MimeMessage对象
     * 发送邮件必须的步骤:3
     */
    public void initMessage() {
        this.auth = new EmailAutherticator();
        Session session = Session.getDefaultInstance(pros, auth);
        //设置获取 debug 信息
        session.setDebug(true);
        mimeMessage = new MimeMessage(session);
    }

    /**
     * 设置email系统参数
     * 接收一个map集合key为string类型，值为String
     * 发送邮件必须的步骤:2
     *
     * @param map
     */
    public void setPros(Map<String, String> map) {
        pros = new Properties();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            pros.setProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 验证账号密码
     * 发送邮件必须的步骤
     *
     * @author Administrator
     */
    public class EmailAutherticator extends Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    /**
     * 设置发送邮件的基本参数(去除繁琐的邮件设置)
     *
     * @param sub  设置邮件主题
     * @param text 设置邮件文本内容
     * @param rec  设置邮件接收人
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void setDefaultMessagePros(String sub, String text, String rec) throws MessagingException, UnsupportedEncodingException {
        mimeMessage.setSubject(sub);
        mimeMessage.setText(text);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(rec));
        mimeMessage.setSentDate(new Date());
        mimeMessage.setFrom(new InternetAddress(username, username));
    }

    /**
     * 设置主题
     *
     * @param subject
     * @throws MessagingException
     */
    public void setSubject(String subject) throws MessagingException {
        mimeMessage.setSubject(subject);
    }

    /**
     * 设置日期
     *
     * @param date
     * @throws MessagingException
     */
    public void setDate(Date date) throws MessagingException {
        mimeMessage.setSentDate(new Date());
    }

    /**
     * 设置邮件文本内容
     *
     * @param text
     * @throws MessagingException
     */
    public void setText(String text) throws MessagingException {
        mimeMessage.setText(text);
    }

    /**
     * 设置邮件头部
     *
     * @param arg0
     * @param arg1
     * @throws MessagingException
     */
    public void setHeader(String arg0, String arg1) throws MessagingException {
        mimeMessage.setHeader(arg0, arg1);
    }

    /**
     * 设置邮件接收人地址 <单人发送>
     *
     * @param recipient
     * @throws MessagingException
     */
    public void setRecipient(String recipient) throws MessagingException {
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }

    /**
     * 设置邮件接收人地址 <多人发送>
     *
     * @param list
     * @throws MessagingException
     */
    public String setRecipients(List<String> recs) throws MessagingException {
        if (recs.isEmpty()) {
            return "接收人地址为空!";
        }
        for (String str : recs) {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
        }
        return "加入接收人地址成功!";
    }

    /**
     * 设置邮件接收人地址 <多人发送>
     *
     * @param StringBuffer<parms,parms2,parms.....>
     * @throws MessagingException
     * @throws AddressException
     */
    @SuppressWarnings("static-access")
    public String setRecipients(StringBuffer sb) throws AddressException, MessagingException {
        if (sb == null || "".equals(sb)) {
            return "字符串数据为空!";
        }
        Address[] address = new InternetAddress().parse(sb.toString());
        mimeMessage.addRecipients(Message.RecipientType.TO, address);
        return "收件人加入成功";
    }

    /**
     * 设置邮件抄送人地址 <多人发送>
     *
     * @param recs
     */
    public String setCC(List<String> recs) throws MessagingException {
        if (recs.isEmpty()) {
            return "接收人地址为空!";
        }
        for (String str : recs) {
            mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(str));
        }
        return "加入接收人地址成功!";
    }

    /**
     * 设置邮件发送人的名字
     *
     * @param from
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public void setFrom(String from) throws UnsupportedEncodingException, MessagingException {
        mimeMessage.setFrom(new InternetAddress(username, from));
    }

    /**
     * 发送邮件<单人发送>
     * return 是否发送成功
     *
     * @throws MessagingException
     */
    public String sendMessage() throws MessagingException {
        Transport.send(mimeMessage);
        return "success";
    }

    /**
     * 设置附件
     *
     * @param file 发送文件的路径
     */
    public void setMultipart(String file) throws MessagingException, IOException {
        if (multipart == null) {
            multipart = new MimeMultipart();
        }
        multipart.addBodyPart(writeFiles(file));
        mimeMessage.setContent(multipart);
    }

    /**
     * 设置附件<添加多附件>
     *
     * @param fileList<接收List集合>
     * @throws MessagingException
     * @throws IOException
     */
    public void setMultiparts(List<String> fileList) throws MessagingException, IOException {
        if (multipart == null) {
            multipart = new MimeMultipart();
        }
        for (String s : fileList) {
            multipart.addBodyPart(writeFiles(s));
        }
        mimeMessage.setContent(multipart);
    }

    /**
     * 发送文本内容，设置编码方式
     * <方法与发送附件配套使用>
     * <发送普通的文本内容请使用setText()方法>
     *
     * @param s
     * @param type
     * @throws MessagingException
     */
    public void setContent(String s, String type) throws MessagingException {
        if (multipart == null) {
            multipart = new MimeMultipart();
        }
        bodypart = new MimeBodyPart();
        bodypart.setContent(s, type);
        multipart.addBodyPart(bodypart);
        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();
    }

    /**
     * 读取附件
     *
     * @param filePath
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public BodyPart writeFiles(String filePath) throws IOException, MessagingException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在!请确定文件路径是否正确");
        }
        bodypart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(file);
        bodypart.setDataHandler(new DataHandler(dataSource));
        //文件名要加入编码，不然出现乱码
        bodypart.setFileName(MimeUtility.encodeText(file.getName()));
        return bodypart;
    }

    public static  SendMail init(){
        Map<String,String> map= new HashMap<>();
        //QQ邮箱需要开通开启服务： POP3/SMTP服务
        SendMail mail = new SendMail("xxx@qq.com","yyyyyyy");
        map.put("mail.smtp.host", "smtp.qq.com");
        map.put("mail.smtp.auth", "true");
        map.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        map.put("mail.smtp.port", "465");
        map.put("mail.smtp.socketFactory.port", "465");
        mail.setPros(map);
        mail.initMessage();
        return mail;
    }
}

```

开启服务：
POP3/SMTP服务 ([如何使用 Foxmail 等软件收发邮件？](http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=371))



使用案列:

```
 @Override
    void doTask() throws Exception {
        List<FpfUser> userList = fpfUserService.querySendMailList();
        logger.info("工作日下午5-6点需要发送邮件提醒的用户如下：");

        if(userList!=null && userList.size()>0){
            SendMail mail = SendMail.init();
            List<String> list = new ArrayList<>();
            for (FpfUser user:userList) {
                if(StringUtils.isNotBlank(user.getEmail()) && checkEmail(user.getEmail())){
                    list.add(user.getEmail());
                }else {
                    logger.error(">>>>>>>>>>>>>>>>>>错误的邮箱:" + user.getUsername() +","+user.getEmail()+"<<<<<<<<<<<<<<<<<<<<<<<<");
                }

            }
            if(!list.isEmpty()){
                mail.setRecipients(list);
                mail.setSubject("请及时填写工时，http://39.108.75.212/pm");
                mail.setDate(new Date());
                mail.setFrom("工时管理系统-管理员");
                String context = "工时系统: http://39.108.75.212/pm 请点击:<a href='http://39.108.75.212/pm' style='color:red;font-size:20px;'>工时管理系统</a>";
                mail.setContent(context, "text/html; charset=UTF-8");
                logger.info(list+": 工时提醒发送 - ["+mail.sendMessage()+"]");
            }

        }
    }
```

```
@Override
	void doTask() throws Exception {
		logger.info("发送周报：");
		String savePath = worktimeService.getFilePath(1);
		logger.info("发送周报的保存位置： " + savePath);
		SendMail mail = SendMail.init2();
		mail.setRecipient("zhuzy@asiainfo.com");
		List<String> ccList = new ArrayList<>();
		ccList.add("dongyong@asiainfo.com");
		ccList.add("fupc@asiainfo.com");
		ccList.add("wanghang@asiainfo.com");
		ccList.add("lizj5@asiainfo.com");
		ccList.add("wangzj7@asiainfo.com");
		mail.setCC(ccList);
		mail.setSubject("前台组周报");
		mail.setDate(new Date());
		mail.setMultipart(savePath);
		mail.setFrom("工时管理系统-管理员");
		String context = "您好，附件为前台组本周周报，请查收，谢谢！";
		mail.setContent(context, "text/html; charset=UTF-8");
        mail.sendMessage();
		logger.info("发送周报成功");

	}
```

```
@Override
	void doTask() throws Exception {
		logger.info("发送月报：");
		String savePath = worktimeService.getFilePath(2);
		logger.info("发送月报的保存位置： " + savePath);
		SendMail mail = SendMail.init();
		mail.setRecipient("lizj5@asiainfo.com");
		List<String> ccList = new ArrayList<>();
		mail.setCC(ccList);
		mail.setSubject("前台组月报");
		mail.setDate(new Date());
		mail.setMultipart(savePath);
		mail.setFrom("工时管理系统-管理员");
		String context = "您好，附件为前台组本月月报，请查收，谢谢！";
		mail.setContent(context, "text/html; charset=UTF-8");
		mail.sendMessage();
		logger.info("发送月报成功");
	}
```

