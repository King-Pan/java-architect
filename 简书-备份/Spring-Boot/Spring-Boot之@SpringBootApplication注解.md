# @SpringBootApplication注解

```
@SpringBootApplication(scanBasePackages = 
{"com.example.web",
"com.example.jpa",
"com.example.models"})
public class JpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}
}
```

这里主要关注@SpringBootApplication注解，它包括三个注解：

@Configuration：表示将该类作用springboot配置文件类。

@EnableAutoConfiguration:表示程序启动时，自动加载springboot默认的配置。

@ComponentScan:表示程序启动是，自动扫描当前包及子包下所有类。

> **注意**
> **如果需要扫描其他包，请配置@SpringBootApplication的scanBasePackages属性**
