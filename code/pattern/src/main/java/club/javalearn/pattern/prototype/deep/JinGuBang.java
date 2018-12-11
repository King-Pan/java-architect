package club.javalearn.pattern.prototype.deep;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-07
 * Time: 23:11
 * Description: No Description
 */
@Data
public class JinGuBang implements Serializable {

    private float height = 100;

    private float d = 10;

    public void big() {
        this.height *= 2;
        this.d *= 2;
    }


    public void small() {
        this.d /= 2;
        this.height /= 2;
    }
}
