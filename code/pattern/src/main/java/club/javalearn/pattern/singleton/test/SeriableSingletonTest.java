package club.javalearn.pattern.singleton.test;

import club.javalearn.pattern.singleton.seriable.SeriableSingleton;

import java.io.*;

/**
 * @author king-pan
 * @date 2018/12/6
 * @Description  单例模式在对象序列化后，不能保证唯一性
 */
public class SeriableSingletonTest {

    public static void main(String[] args) {
        SeriableSingleton s1 = null;
        SeriableSingleton s2 = SeriableSingleton.getInstance();


        try {
            FileOutputStream fos = new FileOutputStream("SeriableSingleton.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s2);
            oos.flush();
            oos.close();


            FileInputStream fis = new FileInputStream("SeriableSingleton.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            s1 = (SeriableSingleton) ois.readObject();
            ois.close();
            fis.close();

            System.out.println(s1 == s2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
