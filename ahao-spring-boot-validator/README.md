# 简介
使用`AOP`拦截请求方法, 统一处理校验异常.
执行单元测试即可.

```java
@Aspect
@Component
public class ValidatorAOP {

    @Around("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 1. 找到 BindingResult 参数
        BindingResult bindingResult = null;
        for (Object arg : pjp.getArgs()) {
            if (arg instanceof BindingResult) {
                bindingResult = (BindingResult) arg;
            }
        }
        if(bindingResult == null) {
            return pjp.proceed();
        }

        // 2. 判断有没有错误
        List<ObjectError> errors = bindingResult.getAllErrors();
        if(CollectionUtils.isEmpty(errors)) {
            return pjp.proceed();
        }

        // 3. 生成错误信息
        Map<String, String> fieldErrors = errors.stream()
            .filter(e -> e instanceof FieldError)
            .map(e -> (FieldError) e)
            .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

//        Map<String, String> objectErrors = errors.stream()
//            .filter(e -> !(e instanceof FieldError))
//            .collect(Collectors.toMap(e->e.getObjectName(), e->e.toString()));
        return AjaxDTO.failure("校验失败", fieldErrors);
    }
}
```
