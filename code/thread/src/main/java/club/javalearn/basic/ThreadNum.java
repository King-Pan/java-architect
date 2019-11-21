package club.javalearn.basic;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author king-pan
 * @date 2019/11/21 23:38
 */
public class ThreadNum {


    /**
     * [6]Monitor Ctrl-Break    JetBrain IJ来Run这个程序，结果会多这样一个线程Monitor Ctrl-Break
     * [5]Attach Listener  Attach Listener线程是负责接收到外部的命令，而对该命令进行执行的并且吧结果返回给发送者。
     * 通常我们会用一些命令去要求jvm给我们一些反 馈信息，如：java -version、jmap、jstack等等。如果该线程在jvm启动的时候没有初始化，那么，则会在用户第一次执行jvm命令时，得到启动。
     * [4]Signal Dispatcher 前面我们提到第一个Attach Listener线程的职责是接收外部jvm命令，
     * 当命令接收成功后，会交给signal dispather线程去进行分发到各个不同的模块处理命令，并且返回处理结果。
     * signal dispather线程也是在第一次接收外部jvm命令时，进行初始化工作。
     * [3]Finalizer 这个线程也是在main线程之后创建的，其优先级为10，主要用于在垃圾收集前，调用对象的finalize()方法；关于Finalizer线程的几点：
     * [2]Reference Handler VM在创建main线程后就创建Reference Handler线程，其优先级最高，为10，它主要用于处理引用对象本身（软引用、弱引用、虚引用）的垃圾回收问题。
     * [1]main  主方法
     * @param args
     */
    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false,false);
        for (ThreadInfo info : threadInfos) {
            System.out.println("[" + info.getThreadId() + "]" + info.getThreadName());
        }
        System.out.println("******active threads count:" + Thread.activeCount());
    }
}
