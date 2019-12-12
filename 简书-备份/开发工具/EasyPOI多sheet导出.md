## 测试实体类
```java
package test;

import cn.afterturn.easypoi.excel.annotation.Excel;


public class EasyPOIModel {
    @Excel(name = "序号")
    private String id;
    @Excel(name = "班级")
    private String name;
    private User user;

    public EasyPOIModel(String id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
class User{
    public User(String name, String sex, Integer age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    @Excel(name = "姓名")
    private String name;
    @Excel(name = "性别")
    private String sex;
    @Excel(name = "年龄")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

```

## 测试类

```
package test;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.asiainfo.audit.exception.BusinessException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.*;

/**
 * audit-parent
 *
 * @author king-pan
 * @create 2017-10-01
 **/
public class ImportTest2 {

    public static void main(String[] args) {
       Workbook workbook = getBook();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream ds=null;
        File file = new File("a.xls");
        try {
            ds=new DataOutputStream(new FileOutputStream(file));
            ds.write(os.toByteArray());
            ds.flush();
        }catch (Exception e) {
            throw new BusinessException("文件处理错误");
        }
    }

    private static Workbook getBook(){
        // 查询数据,此处省略
        List list = new ArrayList<>();
        int count1 = 0 ;
        EasyPOIModel easyPOIModel11 = new EasyPOIModel(String.valueOf(count1++),"信科",new User("张三","男",20)) ;
        EasyPOIModel easyPOIModel12 = new EasyPOIModel(String.valueOf(count1++),"信科",new User("李四","男",17)) ;
        EasyPOIModel easyPOIModel13 = new EasyPOIModel(String.valueOf(count1++),"信科",new User("淑芬","女",34)) ;
        EasyPOIModel easyPOIModel14 = new EasyPOIModel(String.valueOf(count1++),"信科",new User("仲达","男",55)) ;
        list.add(easyPOIModel11) ;
        list.add(easyPOIModel12) ;
        list.add(easyPOIModel13) ;
        list.add(easyPOIModel14) ;
        List list1 = new ArrayList<>();
        int count2 = 0 ;
        EasyPOIModel easyPOIModel21 = new EasyPOIModel(String.valueOf(count2++),"软件",new User("德林","男",22)) ;
        EasyPOIModel easyPOIModel22 = new EasyPOIModel(String.valueOf(count2++),"软件",new User("智勇","男",28)) ;
        EasyPOIModel easyPOIModel23 = new EasyPOIModel(String.valueOf(count2++),"软件",new User("廉贞","女",17)) ;
        list1.add(easyPOIModel21) ;
        list1.add(easyPOIModel22) ;
        list1.add(easyPOIModel23) ;
        // 设置导出配置
        // 获取导出excel指定模版
        // 创建参数对象（用来设定excel得sheet得内容等信息）
        ExportParams params1 = new ExportParams() ;
        // 设置sheet得名称
        params1.setSheetName("表格1"); ;
        ExportParams params2 = new ExportParams() ;
        params2.setSheetName("表格2") ;
        // 创建sheet1使用得map
        Map dataMap1 = new HashMap<>();
        // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
        dataMap1.put("title",params1) ;
        // 模版导出对应得实体类型
        dataMap1.put("entity",EasyPOIModel.class) ;
        // sheet中要填充得数据
        dataMap1.put("data",list) ;
        // 创建sheet2使用得map
        Map dataMap2 = new HashMap<>();
        dataMap2.put("title",params2) ;
        dataMap2.put("entity",EasyPOIModel.class) ;
        dataMap2.put("data",list1) ;
        // 将sheet1和sheet2使用得map进行包装
        List sheetsList = new ArrayList<>() ;
        sheetsList.add(dataMap1);
        sheetsList.add(dataMap2);
        // 执行方法
        return ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF) ;
    }

}

```

## 注意事项

>** 目前好像只支持低版本导出，
