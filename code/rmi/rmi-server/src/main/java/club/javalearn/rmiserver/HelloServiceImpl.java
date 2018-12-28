package club.javalearn.rmiserver;

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
