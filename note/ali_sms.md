## 短信验证码



```
// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
static final String accessKeyId = "LTAIsU4u7y975Kq7";
static final String accessKeySecret = "8zajvrMGXtXmhU05iE7ZXblf9c8EcT";


//发送短信方法templatecode为短信的模板，phone为发送的手机号码，result为验证码，storename为店铺名，ordernum为订单号
    public static SendSmsResponse sendSms(String phone,String result,String templatecode,String storename,String ordernum) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("今朝药链");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templatecode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //审核店铺的通知短信
        if (templatecode.equals("SMS_160860417")||templatecode.equals("SMS_160855380") || templatecode.equals("SMS_160860459") || templatecode.equals("SMS_160855386")) {
        	
			 request.setTemplateParam("{\"dianpumingcheng\":\""+storename+"\"}");
			 
		}else if (templatecode.equals("SMS_159230537")) {
			//修改手机号验证码短信
			request.setTemplateParam("{'code':"+result+"}");
			
		}else if (templatecode.equals("SMS_160855877")) {
			 request.setTemplateParam("{'dingdanhao':"+ordernum+"}");
		}else{
	        //验证码短信
	        request.setTemplateParam("{'yanzhengma':"+result+"}");
		}
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        System.out.println(sendSmsResponse.getCode());
        return sendSmsResponse;
    }
```