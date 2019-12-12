## Maven依赖

```
<dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
<!-- spring core -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>2.5.5</version>
        </dependency>

        <!-- spring beans -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>2.5.5</version>
        </dependency>

        <!-- spring context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>2.5.5</version>
        </dependency>

        <!-- spring web -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>2.5.5</version>
        </dependency>

        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.1</version>
        </dependency>

        <dependency>
            <groupId>javax.xml</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.1</version>
            <type>pom</type>
        </dependency>

        <dependency>
            <groupId>javax.xml</groupId>
            <artifactId>jaxb-impl</artifactId>
            <version>2.1</version>
        </dependency>

        <dependency>
            <groupId>xfire</groupId>
            <artifactId>saaj-api</artifactId>
            <version>1.3</version>
        </dependency>

        <dependency>
            <groupId>xfire</groupId>
            <artifactId>saaj-impl</artifactId>
            <version>1.3</version>
        </dependency>

        <dependency>
            <groupId>wsdl4j</groupId>
            <artifactId>wsdl4j</artifactId>
            <version>1.6.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-frontend-jaxws</artifactId>
            <version>2.2.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-transports-http</artifactId>
            <version>2.2.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-rt-transports-http-jetty</artifactId>
            <version>2.2.3</version>
        </dependency>
  </dependencies>
```

## 接口

```
package com.asiainfo.service;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author test
 * @create 2013-11-26下午10:21:13
 */
@WebService
public interface HelloWorld {

    String sayHello(@WebParam(name = "userName") String userName);

}
```

## 接口实现类

```
package com.asiainfo.service;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author test
 * @create 2013-11-26下午10:22:53
 */
@WebService(serviceName = "HelloWorld")
public class HelloWorldImpl implements HelloWorld {
    
	public String sayHello(@WebParam(name = "userName") String userName) {
        // TODO Auto-generated method stub
        System.out.println("客户端提交信息： " + userName);
        return "say Hello " + userName;
    }
}
```

## Java命令启动

```
package com.asiainfo.service;

import javax.xml.ws.Endpoint;

public class TestServer {
	public static void main(String[] args) {
        System.out.println("web service start");
        HelloWorldImpl implementor= new HelloWorldImpl();
        String address="http://localhost:8080/helloWorld";
        Endpoint.publish(address, implementor);
        System.out.println("web service started");
}
}
```

## applicationContext.xml配置

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:jaxws="http://cxf.apache.org/jaxws"
	xsi:schemaLocation="
                       http://www.springframework.org/schema/beans

                       http://www.springframework.org/schema/beans/spring-beans.xsd
                       http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd">

	<import resource="classpath:META-INF/cxf/cxf.xml" />
	<import resource="classpath:META-INF/cxf/cxf-extension-soap.xml" />
	<import resource="classpath:META-INF/cxf/cxf-servlet.xml" />
	<bean id="helloworld" class="com.asiainfo.service.HelloWorldImpl" />

	<jaxws:server address="/helloWorld"
		serviceClass="com.asiainfo.service.HelloWorld">
		<jaxws:serviceBean>
			<ref bean="helloworld" />
		</jaxws:serviceBean>
	</jaxws:server>
</beans>
```

## web.xml配置

```
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >
<web-app>
	<welcome-file-list>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:applicationContext.xml</param-value>
	</context-param>
	<listener>
		<listener-class>
			org.springframework.web.context.ContextLoaderListener
		</listener-class>
	</listener>

	<servlet>
		<servlet-name>CXFServlet</servlet-name>
		<display-name>CXFServlet</display-name>
		<servlet-class>
			org.apache.cxf.transport.servlet.CXFServlet
		</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>CXFServlet</servlet-name>
		<url-pattern>/webservice/*</url-pattern>
	</servlet-mapping>
</web-app>
```
