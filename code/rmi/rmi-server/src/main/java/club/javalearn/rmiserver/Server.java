package club.javalearn.rmiserver;

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
