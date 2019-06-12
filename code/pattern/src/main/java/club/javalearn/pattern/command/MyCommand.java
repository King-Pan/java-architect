package club.javalearn.pattern.command;

/**
 * @author king-pan
 * @date 2019/5/27
 * @Description ${DESCRIPTION}
 */
public class MyCommand implements Command {

    public MyCommand(Reciver reciver){
        this.reciver = reciver;
    }

    private Reciver reciver;

    @Override
    public String exe() {
        reciver.action();
        return "士兵执行完命令";
    }
}
