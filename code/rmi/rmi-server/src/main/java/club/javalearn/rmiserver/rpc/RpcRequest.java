package club.javalearn.rmiserver.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * @author king-pan
 */
@Data
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String className;
    private String methodName;
    private Object[] parameters;
}
