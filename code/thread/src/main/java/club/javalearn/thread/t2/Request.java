package club.javalearn.thread.t2;

/**
 * @author king-pan
 * @date 2019/2/15
 * @Description ${DESCRIPTION}
 */
public class Request {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                '}';
    }

}
