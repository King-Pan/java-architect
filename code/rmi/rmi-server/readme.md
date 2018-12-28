

## 报错Connection refused to host: 127.0.0.1; nested exception is:


```java
java.rmi.ConnectException: Connection refused to host: 127.0.0.1; nested exception is:
	java.net.ConnectException: Connection refused: connect
	at sun.rmi.transport.tcp.TCPEndpoint.newSocket(TCPEndpoint.java:619)
	at sun.rmi.transport.tcp.TCPChannel.createConnection(TCPChannel.java:216)
	at sun.rmi.transport.tcp.TCPChannel.newConnection(TCPChannel.java:202)
	at sun.rmi.server.UnicastRef.newCall(UnicastRef.java:342)
	at sun.rmi.registry.RegistryImpl_Stub.rebind(Unknown Source)
	at java.rmi.Naming.rebind(Naming.java:177)
	at club.javalearn.rmiserver.Server.main(Server.java:23)
Caused by: java.net.ConnectException: Connection refused: connect
	at java.net.DualStackPlainSocketImpl.connect0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketConnect(DualStackPlainSocketImpl.java:79)
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350)
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206)
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188)
	at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:172)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
	at java.net.Socket.connect(Socket.java:589)
	at java.net.Socket.connect(Socket.java:538)
	at java.net.Socket.<init>(Socket.java:434)
	at java.net.Socket.<init>(Socket.java:211)
	at sun.rmi.transport.proxy.RMIDirectSocketFactory.createSocket(RMIDirectSocketFactory.java:40)
	at sun.rmi.transport.proxy.RMIMasterSocketFactory.createSocket(RMIMasterSocketFactory.java:148)
	at sun.rmi.transport.tcp.TCPEndpoint.newSocket(TCPEndpoint.java:613)
	... 6 more

```


> 解决方法

由于某些操作修改了默认状态，所以恢复下：

1、单击“开始”，运行中输入cmd。
2、然后输入命令 netsh winsock reset。
3、.重启计算机。(如果检测下可以，就不用重启)