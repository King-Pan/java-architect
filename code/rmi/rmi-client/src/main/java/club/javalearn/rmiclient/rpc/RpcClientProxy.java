package club.javalearn.rmiclient.rpc;

import java.lang.reflect.Proxy;

/**
 * @author king-pan
 */
public class RpcClientProxy {
    /**
     * 创建客户端的远程代理。通过远程代理进行访问
     *
     * @param interfaceCls
     * @param host
     * @param port
     * @param <T>
     * @return
     */
    public <T> T clientProxy(final Class<T>
                                     interfaceCls,
                             final String host, final int port) {
        //使用到了动态代理。
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class[]{interfaceCls}, new RemoteInvocationHandler(host, port));
    }

}
