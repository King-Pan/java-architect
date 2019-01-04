# Zookeeper实践之watcher源码分析

https://www.cnblogs.com/shangxiaofei/p/7171882.html

## Watcher源码入口

```java
 @Test
    public void watcherTest() {

        try {
            //1. 创建节点
            zooKeeper.create("/watcher-demo", "watcher-demo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            //2. 绑定监听事件 getData、exists、getChildren

            //zooKeeper.exists(path,boolean) 通过默认的监听事件监听
            Stat stat = zooKeeper.exists("/watcher-demo", new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println(event.getType() + "->" + event.getPath());
                }
            });
            //修改节点，path，value，版本号，乐观锁，如果版本号小于当前版本号，修改失败
            stat = zooKeeper.setData("/watcher-demo","watcher-demo-modify".getBytes(),stat.getVersion());
            Thread.sleep(1000);
            // 删除节点: path和版本号
            zooKeeper.delete("/watcher-demo",stat.getVersion());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```

zooKeeper.exists()方法

```java
 public Stat exists(final String path, Watcher watcher)
        throws KeeperException, InterruptedException
    {
        final String clientPath = path;
        PathUtils.validatePath(clientPath);

        // the watch contains the un-chroot path
        WatchRegistration wcb = null;
        if (watcher != null) {
            wcb = new ExistsWatchRegistration(watcher, clientPath);
        }

        final String serverPath = prependChroot(clientPath);
		
		//构建请求头
        RequestHeader h = new RequestHeader();
        h.setType(ZooDefs.OpCode.exists);
        //构建exists请求体
        ExistsRequest request = new ExistsRequest();
        request.setPath(serverPath);
        request.setWatch(watcher != null);
        SetDataResponse response = new SetDataResponse();
        ReplyHeader r = cnxn.submitRequest(h, request, response, wcb);
        if (r.getErr() != 0) {
            if (r.getErr() == KeeperException.Code.NONODE.intValue()) {
                return null;
            }
            throw KeeperException.create(KeeperException.Code.get(r.getErr()),
                    clientPath);
        }

        return response.getStat().getCzxid() == -1 ? null : response.getStat();
    }
```

org.apache.zookeeper.ClientCnxn#submitRequest

```java
public ReplyHeader submitRequest(RequestHeader h, Record request,
            Record response, WatchRegistration watchRegistration)
            throws InterruptedException {
        ReplyHeader r = new ReplyHeader();
        Packet packet = queuePacket(h, r, request, response, null, null, null,
                    null, watchRegistration);
        synchronized (packet) {
            while (!packet.finished) {
                packet.wait();
            }
        }
        return r;
    }
```

org.apache.zookeeper.ClientCnxn#queuePacket(组装packet，加入到outgoingQueue)

```java
Packet queuePacket(RequestHeader h, ReplyHeader r, Record request,
            Record response, AsyncCallback cb, String clientPath,
            String serverPath, Object ctx, WatchRegistration watchRegistration)
    {
        Packet packet = null;

        // Note that we do not generate the Xid for the packet yet. It is
        // generated later at send-time, by an implementation of ClientCnxnSocket::doIO(),
        // where the packet is actually sent.
        synchronized (outgoingQueue) {
            //把请求参数封装到Packet中
            packet = new Packet中(h, r, request, response, watchRegistration);
            packet.cb = cb;
            packet.ctx = ctx;
            packet.clientPath = clientPath;
            packet.serverPath = serverPath;
            if (!state.isAlive() || closing) {
                conLossPacket(packet);
            } else {
                // If the client is asking to close the session then
                // mark as closing
                if (h.getType() == OpCode.closeSession) {
                    closing = true;
                }
                //把请求参数放入到输出队列
                outgoingQueue.add(packet);
            }
        }
    	//触发selector的执行，基于NIO的异步通信
        sendThread.getClientCnxnSocket().wakeupCnxn();
        return packet;
    }
```

org.apache.zookeeper.ClientCnxnSocketNIO#wakeupCnxn

```java
@Override
synchronized void wakeupCnxn() {
    selector.wakeup();
}
```







org.apache.zookeeper.ZooKeeper#ZooKeeper(java.lang.String, int, org.apache.zookeeper.Watcher, boolean) Zookeeper构造器,

初始化了ClientCnxn并且调用了start()方法

```java
 public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher,
            boolean canBeReadOnly)
        throws IOException
    {
        LOG.info("Initiating client connection, connectString=" + connectString
                + " sessionTimeout=" + sessionTimeout + " watcher=" + watcher);

        watchManager.defaultWatcher = watcher;

        ConnectStringParser connectStringParser = new ConnectStringParser(
                connectString);
        HostProvider hostProvider = new StaticHostProvider(
                connectStringParser.getServerAddresses());
        //
        cnxn = new ClientCnxn(connectStringParser.getChrootPath(),
                hostProvider, sessionTimeout, this, watchManager,
                getClientCnxnSocket(), canBeReadOnly);
        cnxn.start();
    }
```

org.apache.zookeeper.ZooKeeper#getClientCnxnSocket

从zoo.cfg配置中来选择实例化对象，如果没有配置zookeeper.clientCnxnSocket，则创建

ClientCnxnSocket clientCnxnSocket = new ClientCnxnSocketNIO();

如果配置了则通过反射创建配置的实例

//老版本，是没有

ClientCnxnSocket clientCnxnSocket = new ClientCnxnSocketNetty();

```java
private ClientCnxnSocket getClientCnxnSocket() throws IOException {
        String clientCnxnSocketName = getClientConfig().getProperty(
                ZKClientConfig.ZOOKEEPER_CLIENT_CNXN_SOCKET);
        if (clientCnxnSocketName == null) {
            clientCnxnSocketName = ClientCnxnSocketNIO.class.getName();
        }
        try {
            Constructor<?> clientCxnConstructor = Class.forName(clientCnxnSocketName).getDeclaredConstructor(ZKClientConfig.class);
            ClientCnxnSocket clientCxnSocket = (ClientCnxnSocket) clientCxnConstructor.newInstance(getClientConfig());
            return clientCxnSocket;
        } catch (Exception e) {
            IOException ioe = new IOException("Couldn't instantiate "
                    + clientCnxnSocketName);
            ioe.initCause(e);
            throw ioe;
        }
    }
```



org.apache.zookeeper.ClientCnxn#start

```java

public void start() {
     sendThread.start();
     eventThread.start();
 }
```

org.apache.zookeeper.ClientCnxn.SendThread#run

```java
@Override
public void run() {
    clientCnxnSocket.introduce(this,sessionId);
    clientCnxnSocket.updateNow();
    clientCnxnSocket.updateLastSendAndHeard();
    int to;
    long lastPingRwServer = System.currentTimeMillis();
    final int MAX_SEND_PING_INTERVAL = 10000; //10 seconds
    //存活，循环
    while (state.isAlive()) {
        try {
            //zookeeper状态不是connected，重连
            if (!clientCnxnSocket.isConnected()) {
                if(!isFirstConnect){
                    try {
                        Thread.sleep(r.nextInt(1000));
                    } catch (InterruptedException e) {
                        LOG.warn("Unexpected exception", e);
                    }
                }
                // don't re-establish connection if we are closing
                if (closing || !state.isAlive()) {
                    break;
                }
                startConnect();
                clientCnxnSocket.updateLastSendAndHeard();
            }

            if (state.isConnected()) {
                // determine whether we need to send an AuthFailed event.
                if (zooKeeperSaslClient != null) {
                    boolean sendAuthEvent = false;
                    if (zooKeeperSaslClient.getSaslState() == ZooKeeperSaslClient.SaslState.INITIAL) {
                        try {
                            zooKeeperSaslClient.initialize(ClientCnxn.this);
                        } catch (SaslException e) {
                            LOG.error("SASL authentication with Zookeeper Quorum member failed: " + e);
                            state = States.AUTH_FAILED;
                            sendAuthEvent = true;
                        }
                    }
                    KeeperState authState = zooKeeperSaslClient.getKeeperState();
                    if (authState != null) {
                        if (authState == KeeperState.AuthFailed) {
                            // An authentication error occurred during authentication with the Zookeeper Server.
                            state = States.AUTH_FAILED;
                            sendAuthEvent = true;
                        } else {
                            if (authState == KeeperState.SaslAuthenticated) {
                                sendAuthEvent = true;
                            }
                        }
                    }

                    if (sendAuthEvent == true) {
                        eventThread.queueEvent(new WatchedEvent(
                            Watcher.Event.EventType.None,
                            authState,null));
                    }
                }
                
                to = readTimeout - clientCnxnSocket.getIdleRecv();
            } else {
                to = connectTimeout - clientCnxnSocket.getIdleRecv();
            }
			//to小于0表示超时
            if (to <= 0) {
                String warnInfo;
                warnInfo = "Client session timed out, have not heard from server in "
                    + clientCnxnSocket.getIdleRecv()
                    + "ms"
                    + " for sessionid 0x"
                    + Long.toHexString(sessionId);
                LOG.warn(warnInfo);
                throw new SessionTimeoutException(warnInfo);
            }
            if (state.isConnected()) {
                //1000(1 second) is to prevent race condition missing to send the second ping
                //also make sure not to send too many pings when readTimeout is small 
                int timeToNextPing = readTimeout / 2 - clientCnxnSocket.getIdleSend() - 
                    ((clientCnxnSocket.getIdleSend() > 1000) ? 1000 : 0);
                //send a ping request either time is due or no packet sent out within MAX_SEND_PING_INTERVAL
                if (timeToNextPing <= 0 || clientCnxnSocket.getIdleSend() > MAX_SEND_PING_INTERVAL) {
                    sendPing();
                    clientCnxnSocket.updateLastSend();
                } else {
                    if (timeToNextPing < to) {
                        to = timeToNextPing;
                    }
                }
            }

            // If we are in read-only mode, seek for read/write server
            if (state == States.CONNECTEDREADONLY) {
                long now = System.currentTimeMillis();
                int idlePingRwServer = (int) (now - lastPingRwServer);
                if (idlePingRwServer >= pingRwTimeout) {
                    lastPingRwServer = now;
                    idlePingRwServer = 0;
                    pingRwTimeout =
                        Math.min(2*pingRwTimeout, maxPingRwTimeout);
                    pingRwServer();
                }
                to = Math.min(to, pingRwTimeout - idlePingRwServer);
            }
			//从上面
            clientCnxnSocket.doTransport(to, pendingQueue, outgoingQueue, ClientCnxn.this);
        } catch (Throwable e) {
            if (closing) {
                if (LOG.isDebugEnabled()) {
                    // closing so this is expected
                    LOG.debug("An exception was thrown while closing send thread for session 0x"
                              + Long.toHexString(getSessionId())
                              + " : " + e.getMessage());
                }
                break;
            } else {
                // this is ugly, you have a better way speak up
                if (e instanceof SessionExpiredException) {
                    LOG.info(e.getMessage() + ", closing socket connection");
                } else if (e instanceof SessionTimeoutException) {
                    LOG.info(e.getMessage() + RETRY_CONN_MSG);
                } else if (e instanceof EndOfStreamException) {
                    LOG.info(e.getMessage() + RETRY_CONN_MSG);
                } else if (e instanceof RWServerFoundException) {
                    LOG.info(e.getMessage());
                } else {
                    LOG.warn(
                        "Session 0x"
                        + Long.toHexString(getSessionId())
                        + " for server "
                        + clientCnxnSocket.getRemoteSocketAddress()
                        + ", unexpected error"
                        + RETRY_CONN_MSG, e);
                }
                cleanup();
                if (state.isAlive()) {
                    eventThread.queueEvent(new WatchedEvent(
                        Event.EventType.None,
                        Event.KeeperState.Disconnected,
                        null));
                }
                clientCnxnSocket.updateNow();
                clientCnxnSocket.updateLastSendAndHeard();
            }
        }
    }
    cleanup();
    clientCnxnSocket.close();
    if (state.isAlive()) {
        eventThread.queueEvent(new WatchedEvent(Event.EventType.None,
                                                Event.KeeperState.Disconnected, null));
    }
    ZooTrace.logTraceMessage(LOG, ZooTrace.getTextTraceLevel(),
                             "SendThread exited loop for session: 0x"
                             + Long.toHexString(getSessionId()));
}
```

org.apache.zookeeper.ClientCnxn

org.apache.zookeeper.ClientCnxn#ClientCnxn(java.lang.String, org.apache.zookeeper.client.HostProvider, int, org.apache.zookeeper.ZooKeeper, org.apache.zookeeper.ClientWatchManager, org.apache.zookeeper.ClientCnxnSocket, long, byte[], boolean)

```java
public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher,
            long sessionId, byte[] sessionPasswd, boolean canBeReadOnly)
        throws IOException
    {
        LOG.info("Initiating client connection, connectString=" + connectString
                + " sessionTimeout=" + sessionTimeout
                + " watcher=" + watcher
                + " sessionId=" + Long.toHexString(sessionId)
                + " sessionPasswd="
                + (sessionPasswd == null ? "<null>" : "<hidden>"));

        watchManager.defaultWatcher = watcher;

        ConnectStringParser connectStringParser = new ConnectStringParser(
                connectString);
        HostProvider hostProvider = new StaticHostProvider(
                connectStringParser.getServerAddresses());
        //这儿调用了getClientCnxnSocket()方法来获取ClientCnxnSocket实例
        cnxn = new ClientCnxn(connectStringParser.getChrootPath(),
                hostProvider, sessionTimeout, this, watchManager,
                getClientCnxnSocket(), sessionId, sessionPasswd, canBeReadOnly);
        cnxn.seenRwServerBefore = true; // since user has provided sessionId
        cnxn.start();
    }
```

