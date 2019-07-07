# Ribbon实现客户端负载均衡

下面的组件都可以使用了Ribbon作为客户端负载均衡的实现：

* RestTemplate
* Fegion
* Zuul



​		Ribbon三大功能：服务发现、服务 选择规则、服务监听



​		Ribbon的组件: ServerList、IRule、ServerListFilter




ServerList 然后过滤





## Ribbon的工作流程

​		

		1. choose(serviceId) 通过ILoadBalancer(BaseBalancer)去Eureka Server查找服务
  		2. 对服务列表进行过滤
    		3. 然后默认使用轮询的规则做负载均衡



## 自定义Ribbon负载均衡规则



```yml
service-provider-user:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

其中service-provider-user是服务应用名称