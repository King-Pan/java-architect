package club.javalearn.rmiclient.rpc;

import club.javalearn.rmiserver.rpc.IRpcHello;

/**
 * @author king-pan
 */
public class ClientDemo {

    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy();

        IRpcHello hello = rpcClientProxy.clientProxy
                (IRpcHello.class, "localhost", 8088);
        System.out.println(hello.sayHello("mic"));

    }
}
