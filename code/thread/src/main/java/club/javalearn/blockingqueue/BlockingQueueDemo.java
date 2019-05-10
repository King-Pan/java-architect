package club.javalearn.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author king-pan
 * @date 2019/5/9
 * @Description ${DESCRIPTION}
 */
public class BlockingQueueDemo {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        error();


//        System.out.println(blockingQueue.offer("a"));
//        System.out.println(blockingQueue.offer("b"));
//        System.out.println(blockingQueue.offer("c"));
//        System.out.println(blockingQueue.offer("d"));
//
//        System.out.println(blockingQueue.peek());
//
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.poll());
//
//        System.out.println(blockingQueue.peek());


        try {
//            blockingQueue.put("a");
//            blockingQueue.put("b");
//            blockingQueue.put("c");
//            System.out.println("================1======================");
//            blockingQueue.put("d");
//            System.out.println("================2======================");


//            blockingQueue.put("a");
//            blockingQueue.put("b");
//            blockingQueue.put("c");
//
//            System.out.println(blockingQueue.take());
//            System.out.println(blockingQueue.take());
//            System.out.println(blockingQueue.take());
//            System.out.println(blockingQueue.take());
            System.out.println(blockingQueue.offer("a", 2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.offer("b", 2L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.offer("c", 2L, TimeUnit.SECONDS));
            System.out.println("=====================================");
            System.out.println(blockingQueue.offer("d", 2L, TimeUnit.SECONDS));


            System.out.println(blockingQueue.poll(5L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.poll(5L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.poll(5L, TimeUnit.SECONDS));
            System.out.println("--------------------------------------");
            System.out.println(blockingQueue.poll(5L, TimeUnit.SECONDS));
            System.out.println(blockingQueue.poll(5L, TimeUnit.SECONDS));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ;
    }


    private static void error(){
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        System.out.println("=============add元素=============");
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
        System.out.println("=============add元素:队列已满=============");
        System.out.println(blockingQueue.add("d"));

        System.out.println("=============element元素:=============");
        System.out.println(blockingQueue.element());

        System.out.println("=============remove元素:=============");
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println("=============remove元素队列为空:=============");
        System.out.println(blockingQueue.remove());
        System.out.println("=============element元素队列为空:=============");
        System.out.println(blockingQueue.element());
    }
}
