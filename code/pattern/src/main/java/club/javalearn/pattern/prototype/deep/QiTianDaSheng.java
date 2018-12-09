package club.javalearn.pattern.prototype.deep;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018-12-07
 * Time: 23:10
 * Description: No Description
 */
@Getter
@Setter
public class QiTianDaSheng extends Monkey implements Cloneable, Serializable {

    private JinGuBang jinGuBang;

    public QiTianDaSheng() {
        this.birthday = new Date();
        jinGuBang = new JinGuBang();
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public Object deepClone() {

        Object result = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            QiTianDaSheng qiTianDaSheng = (QiTianDaSheng) ois.readObject();
            qiTianDaSheng.setBirthday(new Date());
            result = qiTianDaSheng;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    public QiTianDaSheng copy(QiTianDaSheng target) {
        QiTianDaSheng qiTianDaSheng = new QiTianDaSheng();

        qiTianDaSheng.setBirthday(new Date());
        qiTianDaSheng.setJinGuBang(new JinGuBang());
        qiTianDaSheng.jinGuBang.setD(target.jinGuBang.getD());
        qiTianDaSheng.jinGuBang.setHeight(target.jinGuBang.getHeight());
        qiTianDaSheng.setHeight(target.getHeight());
        qiTianDaSheng.setWeight(target.getWeight());
        return qiTianDaSheng;
    }
}
