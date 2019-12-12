### 表单登录

Spring Security 默认的登录窗口默认
现在我们需要表单登录那么我们就需要修改Spring Security的默认配置

```
package club.javalearn.crm.security.browser;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * crm-parent
 *
 * @author king-pan
 * @date 2017-12-28
 **/
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic()默认的basic窗口登录
        http.formLogin() //表单登录
            .and().authorizeRequests().anyRequest().authenticated(); //任何请求都需要身份认证
    }
}
```

> 默认的表单登录页面

![表单登录默认页面](http://upload-images.jianshu.io/upload_images/6331401-0adbdd9554e5deb3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 自定义登录页面

