package club.javalearn.thread.t2;

/**
 * @author king-pan
 * @date 2019/2/15
 * @Description ${DESCRIPTION}
 */
public class Demo {
    PrintProcessor printProcessor;

    public Demo() {
        SaveProcessor saveProcessor=new SaveProcessor();
        saveProcessor.start();
        printProcessor=new PrintProcessor(saveProcessor);
        printProcessor.start();
    }

    public static void main(String[] args) {
        Request request=new Request();
        request.setName("mic");
        new Demo().doTest(request);
    }

    public void doTest(Request request){

        printProcessor.processorRequest(request);

    }
}
