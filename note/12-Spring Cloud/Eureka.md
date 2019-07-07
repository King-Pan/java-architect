# Eureka服务发现与注册

## 实现服务端

### maven依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>club.ebuy</groupId>
    <artifactId>ebuy-eureka-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>ebuy-eureka-server</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

### 启动类配置

```java
@SpringBootApplication
@EnableEurekaServer  //标识Eureka服务端
public class EbuyEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbuyEurekaServerApplication.class, args);
    }

}
```



## 部署方法

### 单机部署

```java
server:
  port: 8761
eureka:
  client:
    # 是否要注册到其他Eureka Server实例
    register-with-eureka: false
    # 是否要从其他Eureka Server实例获取数据
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:${server.port}/eureka/
```

### 2个注册服务

```yaml
eureka:
  client:
    # 是否要注册到其他Eureka Server实例
    register-with-eureka: false
    # 是否要从其他Eureka Server实例获取数据
    fetch-registry: false
spring:
  profiles:
    active: e1
---
server:
  port: 8761
spring:
  profiles: e1
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka/,http://localhost:8763/eureka/
---
server:
  port: 8762
spring:
  profiles: e2
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8763/eureka/
```

### 三个注册服务

```yaml
eureka:
  client:
    # 是否要注册到其他Eureka Server实例
    register-with-eureka: false
    # 是否要从其他Eureka Server实例获取数据
    fetch-registry: false
spring:
  profiles:
    active: e1
---
server:
  port: 8761
spring:
  profiles: e1
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8762/eureka/,http://localhost:8763/eureka/
---
server:
  port: 8762
spring:
  profiles: e2
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8763/eureka/
---
server:
  port: 8763
spring:
  profiles: e3
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
```



## Eureka高可用

```yml
spring:
  application:
    name: eureka-server
server:
  port: 8761
eureka:
  instance:
    hostname: eureka-01
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://eureka-02:8762/eureka/,http://eureka-03:8763/eureka/
---
spring:
  profiles: dev-2
server:
  port: 8762
eureka:
  instance:
    hostname: eureka-02
  client:
    service-url:
      defaultZone: http://eureka-01:8761/eureka/,http://eureka-02:8763/eureka/
---
spring:
  profiles: dev-3
server:
  port: 8763
eureka:
  instance:
    hostname: eureka-03
  client:
    service-url:
      defaultZone: http://eureka-01:8761/eureka/,http://eureka-02:8762/eureka/
```

​		注意事项:

* 配置hosts文件

```shell
127.0.0.1 eureka-01 eureka-02 eureka-03
```

* 在配置文件中添加eureka.instance.hostname选项
* 在defaultZone中使用hostname名称
* 在配置文件中设置eureka.client.register-with-eureka为false（表示不向注册中心注册自己）
* eureka.client.fetch-registry为false(false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务)

## Eureka核心要素

> 服务注册中心

​	Eureka提供的服务端，提供服务注册与发现的功能，也就是Eureka-server

> 服务提供者

​	提供服务的应用，可以是Spring Boot应用，也可以是其他技术平台且遵循Eureka通信机制的应用，它将自己注册到Eureka注册中心上，以供其他应用发现，本身可能是服务提供者，也可以是其他服务的消费者（消费的方式有Ribbon和feign）。

> 服务消费者

​	消费者应用从服务注册中心获取服务列表，从而使消费者可以知道去何处调用其所需的服务，有两种实现方式Ribbon和Feign。



## 服务治理机制

> 服务提供者

* 服务注册

​	服务提供者再启动时会通过发送REST请求的方式把自己注册到Eureka Server上，同时带上自身服务的一些元数据信息。Eureka Server接收到这个REST请求后，将元数据存储在一个双层结构Map中，其中第一层的key时服务名，第二层的key是具体服务的实例名。

```properties
 #默认是true，改为false后将不会注册
 eureka.client.register-with-eureka=true
```

* 服务同步

  服务注册中心之间会把注册的请求转发到集群中相连的其他注册中心，从而实现注册中心之间的服务同步。通过服务同步，两个服务提供者的服务信息就可以通过集群中的任意一台服务注册中心获得。

* 服务续约

  在服务注册完成之后，服务提供者会维持一个心跳用来持续高烧Eureka Server“我还活着”，以防止Eureka Server的"剔除任务"将该服务实例从服务列表中排除出去，我们称该操作为服务续约。

  配置参数：

  ```properties
  #服务失效时间，超过该时间就会被剔除
  eureka.instance.lease-expiration-duration-in-seconds=90
  #续约任务的调用时间间隔
  eureka.instance.lease-renewal-interval-in-seconds=30
  ```

> 服务消费者

* 获取服务

  在服务注册中心注册的服务，在创建服务消费者时，它会发送一个REST请求给服务注册中心，来获取服务注册信息。为了性能考虑，Eureka Server会维护一份只读的服务清单来返回给客户端，同时该清单会每隔30秒更新一次。

  获取服务是服务消费者的基础，所以必须确保eureka.client.fetch-registry=true没有被修改为false，该值默认为true。若希望修改缓存清单的更新时间，可以通过eureka.client.registry-fetch-interval-seconds=30参数进行修改，该参数默认为30 单位为秒

* 服务调用

  服务消费者再获取服务清单后，通过服务名可以获取具体提供服务的实例名和改实例的元数据信息。因为有这些服务实例的详细信息，所以客户端可以根据自己的需要巨顶具体调用哪个实例，在Ribbon中会默认采用轮询的方式进行调用，从而实现客户端的负载均衡。

  对于访问实例的选择，Eureka中有Region和Zone的概念，一个Region中可以包含多个Zone，每个服务客户端需要被注册到一个Zone中，所以每个客户端对于一个Region和一个Zone。在进行服务调用的时候，优先访问同处一个Zone中的服务提供方，若访问不到，就访问其他Zone。

* 服务下线

  在系统运行过程中必然会面临关闭或重启服务的某个实例的情况，在服务关闭期间，我们自然不希望客户端会继续调用关闭了的实例。所以在客户端程序中，当服务实例进行正常的关闭操作时，它会触发一个服务下线的REST请求给Eureka Server，告诉服务注册中心，该服务下线了。服务端收到请求后，将该服务状态设置为下线(DOWN)，并把该下线事件传播出去。

> 服务注册中心

* 失效剔除

  有些时候，我们的服务实例并不一定会正常下线，可能由于内存溢出、网络故障等原因使得服务不能正常工作，而服务注册中心并没有收到"服务下线"的请求。为了从服务列表中将这些无法提供服务的实例剔除，Eureka Server在启动的时候会创建一个定时任务，默认每隔一段事件(默认为60秒)将当前清单中超时(默认为90秒)没有续约的服务剔除出去。

* 自我保护

  当我们在本地调试基于Eureka的程序时，基本上都会碰到这样要给问题，在服务注册中心的信息版中出现类似下面的红色告警信息:

  ```java
  EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY’RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
  ```

  ![Eureka自我保护](./images/eureka-safe.png)

  实际上，该警告就是触发了Eureka Server的自我保护机制。服务注册到Eureka Server之后，会维持一个心跳连接，告诉Eureka Server自己还活着。Eureka Server在运行期间，会统计心跳失败的比例在15分钟之内是否低于85%，如果出现低于的情况，Eureka Server会将当前的实例信息保护起来，让这些实例不会过期，尽可能保护这些注册信息。但是，在这段保护期间内实例若出现问题，那么客户端很容易拿到实际已经不存在的服务实例，会出现调用失败的情况，所以客户端必须要有容错机制，比如可以使用请求重试、断路器等机制。，

  ​	由于本地调试很容易触发注册中心的保护机制，这会使得注册中心维护的服务实例不那么准确。所以我们在本地进行开发的时候，可以使用eureka.server.enable-self-preservation=false参数来关闭自我保护机制，以确保注册中心可以将不可用的实例正确剔除。

  

  

## 源码分析

​	从上面我们得知，在Eureka开发中，主要有一下角色:

* Eureka Server服务注册中心
* Eureka 服务提供者
* Eureka 服务消费者

下面我们将从服务提供者开始源码分析

### 服务提供者

​	在Eureka服务提供者的开发过程中，我们需要做如下操作:

* 添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

* 在Spring Boot启动类上添加依赖

```java
@EnableDiscoveryClient
```

* 在application.properties中配置注册中心的地址

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka,http://localhost:8762/eureka,http://localhost:8763/eureka
```

好像我们就做了以上的操作，然后启动服务就把把服务注册到Eureka Server中，我们思考下，3步操作，添加依赖和添加配置好像都不能自动往服务注册中心发送REST请求，那么会不会时注解@EnableDiscoveryClient?

#### @EnableDiscoveryClient解析

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableDiscoveryClientImportSelector.class})
public @interface EnableDiscoveryClient {
    boolean autoRegister() default true;
}
```

com.netflix.discovery.DiscoveryClient

```java
/**
 * The class that is instrumental for interactions with <tt>Eureka Server</tt>.
 *
 * <p>
 * <tt>Eureka Client</tt> is responsible for a) <em>Registering</em> the
 * instance with <tt>Eureka Server</tt> b) <em>Renewal</em>of the lease with
 * <tt>Eureka Server</tt> c) <em>Cancellation</em> of the lease from
 * <tt>Eureka Server</tt> during shutdown
 * <p>
 * d) <em>Querying</em> the list of services/instances registered with
 * <tt>Eureka Server</tt>
 * <p>
 *
 * <p>
 * <tt>Eureka Client</tt> needs a configured list of <tt>Eureka Server</tt>
 * {@link java.net.URL}s to talk to.These {@link java.net.URL}s are typically amazon elastic eips
 * which do not change. All of the functions defined above fail-over to other
 * {@link java.net.URL}s specified in the list in the case of failure.
 * </p>
 *
 * @author Karthik Ranganathan, Greg Kim
 * @author Spencer Gibb
 *
 */
@Singleton
public class DiscoveryClient implements EurekaClient {
}
```

类注释大概意思:

这个类用于帮助与Eureka Server互相协作。

Eureka Client负责下面的任务

- 向Eureka Server注册服务实例
- 向Eureka Server服务租约
- 当服务关闭期间，向Eureka Server取消续约
- 查询Eureka Server中的服务实例列表

Eureka Client还需要配置一个Eureka Server的URL列表。



com.netflix.discovery.endpoint.EndpointUtils#getServiceUrlsFromConfig

```
/**
 * Get the list of all eureka service urls from properties file for the eureka client to talk to.
 *
 * @param clientConfig the clientConfig to use
 * @param instanceZone The zone in which the client resides
 * @param preferSameZone true if we have to prefer the same zone as the client, false otherwise
 * @return The list of all eureka service urls for the eureka client to talk to
 */
public static List<String> getServiceUrlsFromConfig(EurekaClientConfig clientConfig, String instanceZone, boolean preferSameZone) {
    List<String> orderedUrls = new ArrayList<String>();
    String region = getRegion(clientConfig);
    String[] availZones = clientConfig.getAvailabilityZones(clientConfig.getRegion());
    if (availZones == null || availZones.length == 0) {
        availZones = new String[1];
        availZones[0] = DEFAULT_ZONE;
    }
    logger.debug("The availability zone for the given region {} are {}", region, availZones);
    int myZoneOffset = getZoneOffset(instanceZone, preferSameZone, availZones);

    List<String> serviceUrls = clientConfig.getEurekaServerServiceUrls(availZones[myZoneOffset]);
    if (serviceUrls != null) {
        orderedUrls.addAll(serviceUrls);
    }
    int currentOffset = myZoneOffset == (availZones.length - 1) ? 0 : (myZoneOffset + 1);
    while (currentOffset != myZoneOffset) {
        serviceUrls = clientConfig.getEurekaServerServiceUrls(availZones[currentOffset]);
        if (serviceUrls != null) {
            orderedUrls.addAll(serviceUrls);
        }
        if (currentOffset == (availZones.length - 1)) {
            currentOffset = 0;
        } else {
            currentOffset++;
        }
    }

    if (orderedUrls.size() < 1) {
        throw new IllegalArgumentException("DiscoveryClient: invalid serviceUrl specified!");
    }
    return orderedUrls;
}
```

在上面的函数中，可以发现客户端依次加载了两个内容，第一个时Region，第二个是Zone，从其加载逻辑上我们可以判断它们之间的关系：

​	通过getRegion函数，我们可以看到它从配置中读取一个region返回，所以一个微服务应用只可以属于一个Region，如果不特别配置，默认为default。若我们自己设置，可以通过eureka.client.region属性来定义。

​	通过getAvailabilityZones函数可以知道当我们没有特别为Region配置Zone的时候，将默认采用defaultZone，这也是我们之前配置参数eureka.client.serviceUrl.defaultZone的由来。若要为应用指定Zone，可以通过eureka.client.availabiltity-zones属性来进行设置。从该函数的return内容，我们可以知道zone可以设置多个，并且通过逗号分割。由此我们可以知道Region和zone的关系是一对多的关系。

​	serviceUrls

​	在获取了Region和Zone的信息之后，才开始真正加载Eureka Server的具体地址。它根据传入的参数按照一定算法确定加载位于哪一个Zone配置的serviceUrls。

```java
int myZoneOffset = getZoneOffset(instanceZone, preferSameZone, availZones);

String zone = availZones[myZoneOffset];
List<String> serviceUrls = clientConfig.getEurekaServerServiceUrls(zone);
```

​	具体获取serviceUrls的实现，我们可以详细查看getEurekaServerServiceUrls函数的具体实现类EurekaClientConfigBean，该类是EurekaClientConfig和EurekaConstants接口的实现，用来加载配置文件中的内容，这里有许多有用的信息，我们先说一下此处我们关系的，关于defaultZone的信息，通过搜索defaultZone，我们可以很容易的找到了这个函数，它具体实现了如何解析该参数的过程，通过此内容，我们就知道，eureka.client.serviceUrl.defaultZone属性可以配置多个，并且需要通过逗号分割。

org.springframework.cloud.netflix.eureka.EurekaClientConfigBean#getEurekaServerServiceUrls

```java
@Override
public List<String> getEurekaServerServiceUrls(String myZone) {
    String serviceUrls = this.serviceUrl.get(myZone);
    if (serviceUrls == null || serviceUrls.isEmpty()) {
        serviceUrls = this.serviceUrl.get(DEFAULT_ZONE);
    }
    if (!StringUtils.isEmpty(serviceUrls)) {
        final String[] serviceUrlsSplit = StringUtils
            .commaDelimitedListToStringArray(serviceUrls);
        List<String> eurekaServiceUrls = new ArrayList<>(serviceUrlsSplit.length);
        for (String eurekaServiceUrl : serviceUrlsSplit) {
            if (!endsWithSlash(eurekaServiceUrl)) {
                eurekaServiceUrl += "/";
            }
            eurekaServiceUrls.add(eurekaServiceUrl.trim());
        }
        return eurekaServiceUrls;
    }

    return new ArrayList<>();
}
```

​	在微服务中使用Ribbon来实现微服务调用时，对于Zone的设置可以在负载均衡时实现区域亲和性:Ribbon 的默认策略会优先访问同客户端处于一个Zone中的服务端实例，只有当同一个Zone中没有可用服务端实例的时候才会访问其他Zone中的实例。所以通过Zone属性的定义，配合实际部署的物理结构，我们就可以有效地设计出对区域性故障的容错集群。

> 服务注册源码分析

com.netflix.discovery.DiscoveryClient#initScheduledTasks初始化Eureka 客户端的所有的定时任务。

```java
 /**
     * Initializes all scheduled tasks.
     */
private void initScheduledTasks() {
    //从服务注册中心获取客户端列表
    if (clientConfig.shouldFetchRegistry()) {
        // registry cache refresh timer
        int registryFetchIntervalSeconds = clientConfig.getRegistryFetchIntervalSeconds();
        int expBackOffBound = clientConfig.getCacheRefreshExecutorExponentialBackOffBound();
        scheduler.schedule(
            new TimedSupervisorTask(
                "cacheRefresh",
                scheduler,
                cacheRefreshExecutor,
                registryFetchIntervalSeconds,
                TimeUnit.SECONDS,
                expBackOffBound,
                new CacheRefreshThread()
            ),
            registryFetchIntervalSeconds, TimeUnit.SECONDS);
    }
	//如果client可以向Eureka Server注册，执行下面的逻辑
    if (clientConfig.shouldRegisterWithEureka()) {
        int renewalIntervalInSecs = instanceInfo.getLeaseInfo().getRenewalIntervalInSecs();
        int expBackOffBound = clientConfig.getHeartbeatExecutorExponentialBackOffBound();
        logger.info("Starting heartbeat executor: " + "renew interval is: {}", renewalIntervalInSecs);

        // Heartbeat timer 服务续约
        scheduler.schedule(
            new TimedSupervisorTask(
                "heartbeat",
                scheduler,
                heartbeatExecutor,
                renewalIntervalInSecs,
                TimeUnit.SECONDS,
                expBackOffBound,
                new HeartbeatThread()
            ),
            renewalIntervalInSecs, TimeUnit.SECONDS);

        // InstanceInfo replicator 服务注册
        instanceInfoReplicator = new InstanceInfoReplicator(
            this,
            instanceInfo,
            clientConfig.getInstanceInfoReplicationIntervalSeconds(),
            2); // burstSize

        statusChangeListener = new ApplicationInfoManager.StatusChangeListener() {
            @Override
            public String getId() {
                return "statusChangeListener";
            }

            @Override
            public void notify(StatusChangeEvent statusChangeEvent) {
                if (InstanceStatus.DOWN == statusChangeEvent.getStatus() ||
                    InstanceStatus.DOWN == statusChangeEvent.getPreviousStatus()) {
                    // log at warn level if DOWN was involved
                    logger.warn("Saw local status change event {}", statusChangeEvent);
                } else {
                    logger.info("Saw local status change event {}", statusChangeEvent);
                }
                instanceInfoReplicator.onDemandUpdate();
            }
        };

        if (clientConfig.shouldOnDemandUpdateStatusChange()) {
            applicationInfoManager.registerStatusChangeListener(statusChangeListener);
        }

        instanceInfoReplicator.start(clientConfig.getInitialInstanceInfoReplicationIntervalSeconds());
    } else {
        logger.info("Not registering with Eureka server per configuration");
    }
}
```





com.netflix.discovery.InstanceInfoReplicator#run

```java
public void run() {
    try {
        discoveryClient.refreshInstanceInfo();

        Long dirtyTimestamp = instanceInfo.isDirtyWithTime();
        if (dirtyTimestamp != null) {
            //服务提供者往服务注册中心注册服务.
            discoveryClient.register();
            instanceInfo.unsetIsDirty(dirtyTimestamp);
        }
    } catch (Throwable t) {
        logger.warn("There was a problem with the instance info replicator", t);
    } finally {
        Future next = scheduler.schedule(this, replicationIntervalSeconds, TimeUnit.SECONDS);
        scheduledPeriodicRef.set(next);
    }
}
```



服务注册

com.netflix.discovery.DiscoveryClient#register

```
/**
 * Register with the eureka service by making the appropriate REST call.
 */
boolean register() throws Throwable {
    logger.info(PREFIX + "{}: registering service...", appPathIdentifier);
    EurekaHttpResponse<Void> httpResponse;
    try {
    	//通过Rest请求把服务注册到服务注册中心。传入参数instanceInfo服务的元数据
        httpResponse = eurekaTransport.registrationClient.register(instanceInfo);
    } catch (Exception e) {
        logger.warn(PREFIX + "{} - registration failed {}", appPathIdentifier, e.getMessage(), e);
        throw e;
    }
    if (logger.isInfoEnabled()) {
        logger.info(PREFIX + "{} - registration status: {}", appPathIdentifier, httpResponse.getStatusCode());
    }
    return httpResponse.getStatusCode() == Status.NO_CONTENT.getStatusCode();
}
```

服务注册地址: serviceUrl+"apps/"+InstanceInfo.getAppName();

服务获取

```java
 private void initScheduledTasks() {
        if (clientConfig.shouldFetchRegistry()) {
            // registry cache refresh timer
            int registryFetchIntervalSeconds = clientConfig.getRegistryFetchIntervalSeconds();
            int expBackOffBound = clientConfig.getCacheRefreshExecutorExponentialBackOffBound();
            scheduler.schedule(
                    new TimedSupervisorTask(
                            "cacheRefresh",
                            scheduler,
                            cacheRefreshExecutor,
                            registryFetchIntervalSeconds,
                            TimeUnit.SECONDS,
                            expBackOffBound,
                            new CacheRefreshThread()
                    ),
                    registryFetchIntervalSeconds, TimeUnit.SECONDS);
        }
     //......省略
 }
```

服务续约

com.netflix.discovery.DiscoveryClient#initScheduledTasks

```java
 if (clientConfig.shouldRegisterWithEureka()) {
     int renewalIntervalInSecs = instanceInfo.getLeaseInfo().getRenewalIntervalInSecs();
     int expBackOffBound = clientConfig.getHeartbeatExecutorExponentialBackOffBound();
     logger.info("Starting heartbeat executor: " + "renew interval is: {}", renewalIntervalInSecs);

     // 服务续约定时器，HeartbeatThread的run方法时服务续约的逻辑
     scheduler.schedule(
         new TimedSupervisorTask(
             "heartbeat",
             scheduler,
             heartbeatExecutor,
             renewalIntervalInSecs,
             TimeUnit.SECONDS,
             expBackOffBound,
             new HeartbeatThread()
         ),
         renewalIntervalInSecs, TimeUnit.SECONDS);
     //省略其他代码
 }
```

com.netflix.discovery.DiscoveryClient.HeartbeatThread

```
private class HeartbeatThread implements Runnable {

    public void run() {
        if (renew()) {
            lastSuccessfulHeartbeatTimestamp = System.currentTimeMillis();
        }
    }
}
```

服务续约则复杂一些，会根据是否是第一次获取发起不同的REST请求和相应的处理。

```java
/**
     * Renew with the eureka service by making the appropriate REST call
     */
    boolean renew() {
        EurekaHttpResponse<InstanceInfo> httpResponse;
        try {
            httpResponse = eurekaTransport.registrationClient.sendHeartBeat(instanceInfo.getAppName(), instanceInfo.getId(), instanceInfo, null);
            logger.debug(PREFIX + "{} - Heartbeat status: {}", appPathIdentifier, httpResponse.getStatusCode());
            if (httpResponse.getStatusCode() == Status.NOT_FOUND.getStatusCode()) {
                REREGISTER_COUNTER.increment();
                logger.info(PREFIX + "{} - Re-registering apps/{}", appPathIdentifier, instanceInfo.getAppName());
                long timestamp = instanceInfo.setIsDirtyWithTime();
                boolean success = register();
                if (success) {
                    instanceInfo.unsetIsDirty(timestamp);
                }
                return success;
            }
            return httpResponse.getStatusCode() == Status.OK.getStatusCode();
        } catch (Throwable e) {
            logger.error(PREFIX + "{} - was unable to send heartbeat!", appPathIdentifier, e);
            return false;
        }
    }
```



> 在客户端注册服务到注册中心后，有一个状态查看，如果使用的是机器名，可能不能正常查看服务状态，需要如下修改:

```yml
eureka:
  instance:
    prefer-ip-address: true
    ip-address: 127.0.0.1
```





### 服务注册中心

com.netflix.eureka.resources.ApplicationResource#addInstance

```java
@POST
@Consumes({"application/json", "application/xml"})
public Response addInstance(InstanceInfo info,
                            @HeaderParam(PeerEurekaNode.HEADER_REPLICATION) String isReplication) {
    logger.debug("Registering instance {} (replication={})", info.getId(), isReplication);
    // validate that the instanceinfo contains all the necessary required fields
    if (isBlank(info.getId())) {
        return Response.status(400).entity("Missing instanceId").build();
    } else if (isBlank(info.getHostName())) {
        return Response.status(400).entity("Missing hostname").build();
    } else if (isBlank(info.getIPAddr())) {
        return Response.status(400).entity("Missing ip address").build();
    } else if (isBlank(info.getAppName())) {
        return Response.status(400).entity("Missing appName").build();
    } else if (!appName.equals(info.getAppName())) {
        return Response.status(400).entity("Mismatched appName, expecting " + appName + " but was " + info.getAppName()).build();
    } else if (info.getDataCenterInfo() == null) {
        return Response.status(400).entity("Missing dataCenterInfo").build();
    } else if (info.getDataCenterInfo().getName() == null) {
        return Response.status(400).entity("Missing dataCenterInfo Name").build();
    }

    // handle cases where clients may be registering with bad DataCenterInfo with missing data
    DataCenterInfo dataCenterInfo = info.getDataCenterInfo();
    if (dataCenterInfo instanceof UniqueIdentifier) {
        String dataCenterInfoId = ((UniqueIdentifier) dataCenterInfo).getId();
        if (isBlank(dataCenterInfoId)) {
            boolean experimental = "true".equalsIgnoreCase(serverConfig.getExperimental("registration.validation.dataCenterInfoId"));
            if (experimental) {
                String entity = "DataCenterInfo of type " + dataCenterInfo.getClass() + " must contain a valid id";
                return Response.status(400).entity(entity).build();
            } else if (dataCenterInfo instanceof AmazonInfo) {
                AmazonInfo amazonInfo = (AmazonInfo) dataCenterInfo;
                String effectiveId = amazonInfo.get(AmazonInfo.MetaDataKey.instanceId);
                if (effectiveId == null) {
                    amazonInfo.getMetadata().put(AmazonInfo.MetaDataKey.instanceId.getName(), info.getId());
                }
            } else {
                logger.warn("Registering DataCenterInfo of type {} without an appropriate id", dataCenterInfo.getClass());
            }
        }
    }

    registry.register(info, "true".equals(isReplication));
    return Response.status(204).build();  // 204 to be backwards compatible
}
```

从Eureka Server的服务注册方法中可以知道，服务提供者进行服务注册时必须提供一下元数据:

* id
* hostname
* address
* appName不能和服务注册中心的appName一致
* dataCenterInfo
* dataCenterInfo.name

