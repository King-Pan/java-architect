package club.javalearn.pattern.prototype.simple;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-07
 * Time: 23:04
 * Description:
 */
@Data
public class CloneTarget implements Cloneable {

    private String name;
    private CloneTarget target;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
