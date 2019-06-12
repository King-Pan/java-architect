package club.javalearn.pattern.command;

/**
 * @author king-pan
 * @date 2019/5/27
 * @Description ${DESCRIPTION}
 */
public class CommandTest {

    public static void main(String[] args) {
        //1. 士兵，执行命令
        Reciver reciver = new Reciver();
        //2. 把命令传递给士兵
        Command command = new MyCommand(reciver);
        //3. 司令发出命令
        Invoker invoker = new Invoker(command);
        invoker.action();
    }
}
