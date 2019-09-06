package moe.ahao.spring.boot.validator.aop;

import com.ahao.domain.entity.AjaxDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
