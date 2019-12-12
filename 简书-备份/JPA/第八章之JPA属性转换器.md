## 第八章之JPA属性转换器


### 定义枚举类
```
public enum StatusEnum {
    ENABLE(1, "启用"), DISABLE(-1, "禁用"), DELETED(-2, "已删除");

    private Integer value;
    private String description;

    private StatusEnum(Integer value,String description){
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "StatusEnum{" +
                "value=" + value +
                ", description='" + description + '\'' +
                '}';
    }
}
```

### 定义转换器

```
public class StatusAttributeConverter implements AttributeConverter<String,Integer> {


    /**
     * 把前台传递的字符串转换成数据库存储的数字
     * @param status
     * @return
     */
    @Override
    public Integer convertToDatabaseColumn(String status) {

        try {
            return Integer.parseInt(status);    //如果是数字，则直接返回（这里可以遍历StatusEnum的value来进一步验证）
        } catch (NumberFormatException e) {
            for (StatusEnum type : StatusEnum.values()) {    //如果不是数字，则通过StatusEnum来找到描述对应的数字

                if (status.equals(type.getDescription())) {
                    return type.getValue();
                }
            }
        }
        throw new RuntimeException("Unknown StatusEnum: " + status);
    }

    /**
     * 把数据库中的数字转换成前台展示的描述
     * @param value
     * @return
     */
    @Override
    public String convertToEntityAttribute(Integer value) {
        for (StatusEnum type : StatusEnum.values()) {    //将数字转换为描述
            if (value.equals(type.getValue())) {
                return type.getDescription();
            }
        }
        throw new RuntimeException("Unknown database value: " + value);
    }
}
```


### 定义实体类


在实体类属性上加上@Convert注解,指定转换器

```
@Entity
@Table(name = "sys_demo")
public class Demo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = StatusAttributeConverter.class)
    private String status;

    @Transient
    private Integer statusValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        for (StatusEnum statusEnum:StatusEnum.values()){
            if(status.equals(statusEnum.getDescription())){
                statusValue = statusEnum.getValue();
            }
        }
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", statusValue=" + statusValue +
                '}';
    }
}
```

### Jpa接口

```
public interface DemoRepository  extends JpaRepository<Demo,Long>{
}
```


### 测试

```
@Autowired
private DemoRepository demoRepository;

@RequestMapping("/hello")
public String hello(){
    System.out.println(demoRepository.findAll());
    return  "Hello Spring Boot";
}
```
