### 1. 启用过滤器拦截器

```
@ServletComponentScan
@SpringBootApplication
```

### 2. 过滤器

过滤器：所谓过滤器顾名思义是用来过滤的，在java web中，你传入的request,response提前过滤掉一些信息，或者提前设置一些参数，然后再传入servlet或者struts的action进行业务逻辑，比如过滤掉非法url（不是login.do的地址请求，如果用户没有登陆都过滤掉）,或者在传入servlet或者struts的action前统一设置字符集，或者去除掉一些非法字符（聊天室经常用到的，一些骂人的话）。filter 流程是线性的， url传来之后，检查之后，可保持原来的流程继续向下执行，被下一个filter, servlet接收等.

```
package club.javalearn.crm.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * crm-parent
 *
 * @author king-pan
 * @date 2018-01-10
 **/
@WebFilter(filterName = "xFrameFilter",urlPatterns = "/*")
@Slf4j
public class XframeFilter  implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化XframeFilter配置");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("设置请求头x-frame-options='SAMEORIGIN'");
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.addHeader("x-frame-options","SAMEORIGIN");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        log.info("销毁XframeFilter");
    }
}
```

### 3. 监听器

监听器：这个东西在c/s模式里面经常用到，他会对特定的事件产生产生一个处理。监听在很多模式下用到。比如说观察者模式，就是一个监听来的。又比如struts可以用监听来启动。Servlet监听器用于监听一些重要事件的发生，监听器对象可以在事情发生前、发生后可以做一些必要的处理。

#### 3.1 简介

Listener 用于监听 java web程序中的事件，例如创建、修改、删除Session、request、context等，并触发响应的事件

 Listener 对应观察者模式，事件发生的时候会自动触发该事件对应的Listeer。 Listener 主要用于对 Session、request、context 进行监控。servlet2.5 规范中共有 8 种Listener 

#### 3.2 监听器的实现

监听 Session、request、context 的创建于销毁，分别为  

HttpSessionLister、ServletContextListener、ServletRequestListener下面主要举例：是ServletContextListener、HttpSessionLister

#### 3.3 HttpSessionLister监听器

```
package club.javalearn.crm.web.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * crm-parent
 *
 * @author king-pan
 * @date 2018-01-10
 **/
@WebListener
public class TestHttpSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session创建");

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session销毁");
    }
}
```
#### 3.3 ServletContextListener监听器

```
package club.javalearn.crm.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * crm-parent
 *
 * @author king-pan
 * @date 2018-01-10
 **/
@WebListener
public class TestServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ServletContext初始化");
        System.out.println(sce.getServletContext().getServerInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext销毁");
    }
}
```
