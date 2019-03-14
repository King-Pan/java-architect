package club.javalearn.thread.producter;

/**
 * @author king-pan
 * @date 2019/3/12
 * @Description ${DESCRIPTION}
 */
public class PCData {
    private final int intData;

    public PCData(int d) {
        intData = d;
    }

    public PCData(String d) {
        intData = Integer.valueOf(d);
    }

    public int getData() {
        return intData;
    }

    @Override
    public String toString() {
        return "data:" + intData;
    }
}
