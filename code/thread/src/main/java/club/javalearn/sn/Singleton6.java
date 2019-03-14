package club.javalearn.sn;

import java.util.stream.IntStream;

/**
 * @author king-pan
 * @date 2019/3/7
 * @Description ${DESCRIPTION}
 */
public class Singleton6 {
    private Singleton6() {
    }

    private enum SingletonEnum {
        INSTANCE;
        private final Singleton6 instance;

        SingletonEnum() {
            instance = new Singleton6();
        }

        public Singleton6 getInstance() {
            return instance;
        }
    }

    public static Singleton6 getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

    public static void main(String[] args) {
        IntStream.range(1, 100).forEach(i -> new Thread(String.valueOf(i)) {
            @Override
            public void run() {
                System.out.println(Singleton6.getInstance());
            }
        }.start());
    }

}
