# 分布式架构-RPC

## 什么是RPC

​	RPC(Remote Procedure Call)远程服务调用，一般用来实现部署在不同的机器上的系统之间的方法调用，是的程序能够像访问本地方法一样，通过网络传输去访问远程端系统资源；对于客户端来说，传输层使用什么协议，序列化和反序列化都是透明的。

​	Dubbo是基于RPC通信的。

## RPC框架原理

​	

## 了解JAVA RMI

​	JAVA RMI （JAVA Remote Method Invocation）是基于java的远程方法调用，是纯java的网络分布式应用系统的核心解决方案之一。

​	RMI目前使用Java远程消息交换协议JRMP(JAVA Remote Message protocol)进行通信，由于JRMP是专门为Java对象制定的，是纯Java分布式应用系统的解决方案。

​	RMI是专为java语言设计的远程调用框架，所有对其他语言支持不足。

## 基于JAVA RMI实践

### 服务端代码

> 远程调用接口-IHelloService

```java
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author king-pan
 * @date 2018/12/28
 * @Description 远程调用接口,需要继承Remote
 */
public interface IHelloService extends Remote {

    String sayHello(String name) throws RemoteException;
}
```

> 远程调用实现类-HelloServiceImpl

```java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * @author king-pan
 * @date 2018/12/28
 * @Description 远程调用实现,需要继承UnicastRemoteObject，并且构造器上必须抛出RemoteException异常
 */
public class HelloServiceImpl extends UnicastRemoteObject implements IHelloService {

    protected HelloServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        return "Hello " + name + ",now:" + new Date();
    }
}
```

构造器上必须抛出异常的原因：父类无参构造器中抛出了RemoteException异常

```java
 protected UnicastRemoteObject() throws RemoteException
 {
    this(0);
 }
```

> 发布服务-Server

```java
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author king-pan
 * @date 2018/12/28
 * @Description 发布服务
 */
public class Server {

    public static void main(String[] args) {
        try {
            //创建需要发布的远程对象
            IHelloService helloService = new HelloServiceImpl();
            //注册端口-必须是1099
            LocateRegistry.createRegistry(1099);
            //注册中心,key-value形式
            Naming.rebind("rmi://127.0.0.1/Hello", helloService);

            System.out.println("服务启动成功");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
```

### 客户端实现

> 客户端代码-ClientDemo

```java
import club.javalearn.rmiserver.IHelloService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author king-pan
 * @date 2018/12/28
 * @Description 客户端测试代码
 */
public class ClientDemo {
    public static void main(String[] args) {
        try {
            IHelloService helloService = (IHelloService) Naming.lookup("rmi://127.0.0.1/Hello");
            System.out.println(helloService.sayHello("King-pan"));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
```



## RMI通信原理分析



sun.rmi.server.UnicastServerRef#exportObject(java.rmi.Remote, java.lang.Object, boolean)



```java
 public Remote exportObject(Remote var1, Object var2, boolean var3) throws RemoteException {
        Class var4 = var1.getClass();

        Remote var5;
        try {
            //对HelloServiceImpl进行代理
            var5 = Util.createProxy(var4, this.getClientRef(), this.forceStubUse);
        } catch (IllegalArgumentException var7) {
            throw new ExportException("remote object implements illegal remote interface", var7);
        }

        if (var5 instanceof RemoteStub) {
            this.setSkeleton(var1);
        }
		//包装成一个暴露在TCP端口上的对象
        Target var6 = new Target(var1, this, var5, this.ref.getObjID(), var3);
        this.ref.exportObject(var6);
        this.hashToMethod_Map = (Map)hashToMethod_Maps.get(var4);
        return var5;
    }
```

> 服务端启动Registry

## 实现自己的RPC框架

