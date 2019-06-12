package club.javalearn.pattern.command;

/**
 * @author king-pan
 * @date 2019/5/27
 * @Description 命令模式的调用者
 */
public class Invoker {

    private Command command;
    public Invoker(Command command){
        this.command = command;
    }

    public void action(){
        System.out.println(command.exe());
    }
}
