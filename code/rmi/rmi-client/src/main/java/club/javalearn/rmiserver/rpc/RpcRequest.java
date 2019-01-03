package club.javalearn.rmiserver.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * @author king-pan
 * 传输对象,可以把服务端的该类提取出来公用
 */
@Data
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String className;
    private String methodName;
    private Object[] parameters;

}
