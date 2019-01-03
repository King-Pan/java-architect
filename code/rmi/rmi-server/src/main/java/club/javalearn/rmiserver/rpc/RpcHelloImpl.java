package club.javalearn.rmiserver.rpc;
/**
 * @author king-pan
 */
public class RpcHelloImpl implements IRpcHello {
    @Override
    public String sayHello(String name) {
        return "Hello :" + name;
    }
}
