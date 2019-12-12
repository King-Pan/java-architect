### 第一步： 创建日志注解类:

```
/**
 * 自定义注解,拦截log操作
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogService {
    //模块
    String module()  default "";
    //说明
    String description()  default "";
}
```

### 第二步: 创建日志切面

```
@Aspect
@Component
public class LogAspect {
    //定义切面
    @Pointcut("@annotation(com.asiainfo.audit.log.LogService)")
    public void serviceAspect() { }

    @Before("serviceAspect()")
    public  void doBefore(JoinPoint joinPoint) {
        //前置通知
        logger.debug("Before...");
        AuditInvocationContext context=AuditInvocationContext.getContext();
        context.setStartTime(new Date());
    }

    
    @AfterReturning(pointcut = "serviceAspect()",returning = "rvt")
    public Object  doAfterReturning(JoinPoint joinPoint,Object rvt){
        logger.debug("AfterReturning...");
      
        try {
           //记录日志
        }catch (Exception e){
            logger.error("记录日志出错",e);
        }
        return rvt;
    }*/

    @Around(value = "serviceAspect()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object rvt;
        logger.debug("around...");
        try {
           rvt = pjp.proceed();
           //记录成功
        } catch (Throwable throwable) {
           //记录失败
            throw throwable;
        }
       
        return rvt;
    }

    @After("serviceAspect()")
    public  void doAfter(JoinPoint joinPoint) {
        logger.debug("After...");
    }
}
```

### 第三步:  使用注解记录日志

```
    @RequestMapping(value = "getImportMsg",method = RequestMethod.POST)
    @LogService(module = "模块编码",description = "xx信息预导入")
    @ResponseBody
    public Object getImportMsg(String resultId){
        Map<String,Object> result = new HashMap<>();
        return  result;
    }
```

