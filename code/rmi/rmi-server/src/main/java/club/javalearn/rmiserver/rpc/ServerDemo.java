package club.javalearn.rmiserver.rpc;
/**
 * @author king-pan
 */
public class ServerDemo {

    public static void main(String[] args) {
        //创建一个远程调用对象
        IRpcHello rpcHello = new RpcHelloImpl();
        //创建一个发布远程调用服务的对象
        RpcServer rpcServer = new RpcServer();
        //把远程对象发布出去
        rpcServer.publisher(rpcHello,8088);
    }
}
