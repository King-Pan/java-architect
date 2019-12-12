## Java基础类型

### Java除了8个基础类型和枚举类型外，其他的都是引用类型

> **整型(默认值0)**

基础类型 | 封装类型(java.lang包下)  | 长度(单位:字节) | 范围
---------|--------------------------|-------|------
byte     | Byte                     | 1     |[-2^8, 2^8)
short    | Short                    | 2     |[-2^16, 2^16)
int      | Integer                  | 4     |[-2^32, 2^32)
long     | Long                     | 8     |[-2^64, 2^64)

> **浮点型(默认值0.0)**

基础类型 | 封装类型(java.lang包下)  | 长度(单位:字节) | 范围
---------|--------------------------|-------|------
float    | Float                    | 4     |[-3.4E38~3.4E38)
double   | Double                   | 8     |[-1.7E308~1.7E308)

> **字符型(默认值'\u0000')**

基础类型 | 封装类型(java.lang包下)  | 长度(单位:字节) | 范围
---------|--------------------------|-------|------
char     | Character                | 2     |[0,2^16)

> **布尔型(默认false)**

基础类型 | 封装类型(java.lang包下)  | 长度(单位:字节) | 范围
---------|--------------------------|-------|------
boolean  | Boolean                  | 1     |true/false

### 基础类型详情

基础类型 | 封装类型(java.lang包下)  | 长度(单位:字节) | 范围
---------|--------------------------|-------|------
byte     | Byte                     | 1     |[-2^8, 2^8)
boolean  | Boolean                  | 1     |true/false
short    | Short                    | 2     |[-2^16, 2^16)
char     | Character                | 2     |[0,2^16)
int      | Integer                  | 4     |[-2^32, 2^32)
float    | Float                    | 4     |[-3.4E38~3.4E38)
long     | Long                     | 8     |[-2^64, 2^64)
double   | Double                   | 8     |[-1.7E308~1.7E308)


> **java自动拆装箱**

* 自动装箱: Integer i = 100;
* 自动拆箱: int n = i;
* 其他类型同理

```
Integer i = 100; //自动装箱,实际上执行了Integer i = Integer.valueOf(100);
int n = i; //自动拆箱,实际上执行int n = i.intValue();
```

上面的代码调用了Intger的两个方法valueOf()和intValue(),我们来看下它们的源代码

```
private final int value;
public int intValue() {
    return value;
}
```

intValue()方法没有什么内容,那么我们来看下valueOf()

```
public static Integer valueOf(int i) {
    assert IntegerCache.high >= 127;
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

valueOf方法很有意思这儿有两种生成Integer对象的方式,一种是直接new Integer(i)
另外一种是通过IntegerCache.cache的静态成员变量中获取,下面我们看下IntegerCache类的定义

```
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];

    static {
        // high value may be configured by property
        int h = 127;
        String integerCacheHighPropValue =
            sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            int i = parseInt(integerCacheHighPropValue);
            i = Math.max(i, 127);
            // Maximum array size is Integer.MAX_VALUE
            h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
        }
        high = h;

        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);
    }

    private IntegerCache() {}
}
```

* 在IntegerCache类的静态方法中初始化了high,这个初始化有点复杂,暂定high =127
* IntegerCache.cache数组初始化了high - low + 1 个,值是从low到high
* 也就是说Integer的值在[-128,127]之间,直接获取缓存里面的Integer不创建新的,如果不在该范围，则new Integer(i)
* Integer i = 100; Integer n = Integer(100); i和n引用的是同一个对象,n==i等于true


### 面试题

> **问题1:下面代码的输出结果是?,为什么?**

```
Integer i1 = new Integer(100);
Integer i2 = new Integer(100);
Integer i3 = 100;
int i4 = 100;
System.out.println(i1==i2);   //输出?
System.out.println(i2==i3);   //输出?
System.out.println(i3==i4);   //输出?
```

* i1 == i2; new了2个对象,地址值肯定不一致,返回false
* i2 == i3; i2是new出来的,i3是从常量池中取出来的,地址值不一致,返回false
* i3 == i4; i4是int类型,i3自动拆箱成int然后比较值

> **问题2: 下面两段代码是否正确,为什么**

第一段代码:

```
short s1 = 1;
s1 = s1+ 1;
```
第二段代码:

```
short s1 = 1;
s1 += 1;
```

* 第一段代码编译错误,第二段代码编译正常
* s1 = s1 + 1;   s1 + 1后的结果是int类型的2,  int值不能直接赋值给short类型的s1
* s1 += 1;   java编译器在编译阶段做了操作: s1 = (short)(s1+1),把int强转成short类型

> **问题3: float f = 3.4 有问题吗**

* float f = 3.4; 在语法上是错误的,编译不通过
* float f = 3.4; 将双精度型（double）赋值给浮点型（float）属于下转型,精度缺失，需要强制转换
* float f = (float) 3.4 或者float f = 3.4F/3.4f;
* float f = 3;编译有问题吗,为什么?


> **问题3: Math.round(11.5) 等于多少？Math.round(-11.5)等于多少**

* Math.round(11.5) 等于12
* Math.round(-11.5) 等于-11
* Math.round(double d)方法的原理是对d+0.5 向下取整,代码如下

```
public static long round(double a) {
    if (a != 0x1.fffffffffffffp-2) // greatest double value less than 0.5
        return (long)floor(a + 0.5d);
    else
        return 0;
}
```

#### 注意

* 如果java基础类型和基础类型的封装类型作运算时,name封装类型就会自动拆箱,然后进行运算
* java的基础类型只有8种,其他的都是枚举类型和引用类型
