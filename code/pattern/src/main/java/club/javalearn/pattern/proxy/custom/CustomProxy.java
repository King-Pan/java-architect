package club.javalearn.pattern.proxy.custom;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-09
 * Time: 11:18
 * Description: 手动首先动态代理: 模拟java.lang.reflect.Proxy
 */
public class CustomProxy {

    private static final String LN = "\r\n";

    public static Object newProxyInstance(CustomClassLoader classLoader, Class<?>[] interfaces, CustomInvocationHandler invocationHandler) {

        FileWriter fw = null;
        Object result = null;
        StandardJavaFileManager manage = null;
        try {
            //1. 动态生成源代码
            String src = generateSrc(interfaces);
            //2. Java文件输出到磁盘
            String filePath = CustomProxy.class.getResource("").getPath();
            System.out.println(filePath);
            File f = new File(filePath + "$Proxy0.java");
            fw = new FileWriter(f);
            fw.write(src);
            fw.flush();
            //3. 把生成的Java文件编译成.class文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            manage = compiler.getStandardFileManager(null, null, null);
            Iterable iterable = manage.getJavaFileObjects(f);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manage, null, null, null, iterable);
            task.call();
            manage.close();

            //4. 把编译生成的.class文件加载到JVM中来
            Class<?> proxyClass = classLoader.findClass("$Proxy0");
            Constructor c = proxyClass.getConstructor(CustomInvocationHandler.class);
            boolean delFlag = f.delete();
            if (delFlag) {
                System.out.println("删除文件成功");
            } else {
                System.out.println("删除文件失败");
            }

            //5. 返回字节码重组以后哦生成新的代理对象
            result = c.newInstance(invocationHandler);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
                if (manage != null) {
                    manage.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String generateSrc(Class<?>[] interfaces) {
        StringBuilder sb = new StringBuilder();
        sb.append("package club.javalearn.pattern.proxy.custom;").append(LN);

        sb.append("import club.javalearn.pattern.proxy.Tenementable;").append(LN);
        sb.append("import club.javalearn.pattern.proxy.custom.CustomInvocationHandler;").append(LN);
        sb.append("import java.lang.reflect.Method;").append(LN);
        sb.append("public class $Proxy0 implements ").append(interfaces[0].getName()).append("{").append(LN);
        sb.append("CustomInvocationHandler h;").append(LN);
        sb.append("public $Proxy0(CustomInvocationHandler h) { ").append(LN);
        sb.append("this.h = h;");
        sb.append("}").append(LN);

        for (Method m : interfaces[0].getMethods()) {
            sb.append("public ").append(m.getReturnType()).append(" ").append(m.getName()).append("() {").append(LN);
            sb.append("try{").append(LN);
            sb.append("Method  m = ").append(interfaces[0].getName()).append(".class.getMethod(\"").append(m.getName()).append("\",new Class[]{});");
            sb.append(LN);
            sb.append("try{" + LN);
            sb.append("this.h.invoke(this,m,null);").append(LN);
            sb.append("}catch(Throwable e){").append(LN);
            sb.append("e.printStackTrace();").append(LN);
            sb.append("}");
            sb.append("}");
        }

        sb.append("}" + LN);

        return sb.toString();


    }
}
