package club.javalearn.jvm.gc;

/**
 * @author king-pan
 * @date 2019/5/11
 * @Description ${DESCRIPTION}
 *
 * 可以作为GC Root的对象有
 *  1.虚拟机栈(栈帧中的本地变量表)中的引用
 *  2.方法区中的静态属性引用的对象(jdk8方法区改为永久代)
 *  3.方法区中的常量引用的对象
 *  4.本地方法栈中JNI(native方法)中引用的对象
 */
public class GCRootDemo {

    private byte[]btAry = new byte[100*1024*1025];

    public static void main(String[] args) {
        m1();
    }

    public static void m1(){
        GCRootDemo t1 = new GCRootDemo();
        System.gc();
        System.out.println("第一次GC结束");
    }
}
