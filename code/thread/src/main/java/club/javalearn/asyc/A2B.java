package club.javalearn.asyc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author king-pan
 * @date 2019/4/24
 * @Description ${DESCRIPTION}
 */
public class A2B {

    public static void main(String[] args) {
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始做米饭");
                try {
                    TimeUnit.SECONDS.sleep(20);
                    System.out.println("米洗好了，开始煮饭");
                    TimeUnit.SECONDS.sleep(60*1);
                    System.out.println("米饭做好了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {

                try {
                    System.out.println("准备做菜");
                    TimeUnit.SECONDS.sleep(60);
                    System.out.println("正在洗菜");
                    TimeUnit.SECONDS.sleep(60);
                    System.out.println("开始炒菜");
                    TimeUnit.SECONDS.sleep(60);
                    System.out.println("菜已经炒好了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return "鱼香肉丝";
            }
        });

        CompletableFuture f3 = f1.runAfterBoth(f2, new Runnable() {
            @Override
            public void run() {
                System.out.println("饭菜都做好了，开始吃饭");
            }
        });
        while (true){
            if(f3.isDone()){
                break;
            }
        }


    }
}
