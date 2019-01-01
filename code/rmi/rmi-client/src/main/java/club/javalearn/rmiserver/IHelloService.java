package club.javalearn.rmiserver;

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
