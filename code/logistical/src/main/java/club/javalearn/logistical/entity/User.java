package club.javalearn.logistical.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author king-pan
 * @date 2018/12/26
 * @Description 用户信息表
 */
@Entity
@Table(name = "sys_user")
public class User {

    @Id
    @GeneratedValue
    private Long userId;
    private String userName;
    private String nickName;
    private String password;
    private String status;

}
