package club.javalearn.logistical.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author king-pan
 * @date 2018/12/26
 * @Description  账户信息
 */
@Entity
@Table(name = "sys_account")
public class Account {

    private Long accountId;

    private String balance;

    private String status;

    private Long userId;
}
