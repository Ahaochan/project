# 简介
使用`ControllerAdvice`拦截请求方法, 统一处理校验异常.
执行单元测试即可.

```java
@ControllerAdvice("moe.ahao")
public class ValidatorExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AjaxDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 1. 找到 BindingResult 参数
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();

        // 2. 生成错误信息
        Map<String, String> fieldErrors = errors.stream()
            .filter(e -> e instanceof FieldError)
            .map(e -> (FieldError) e)
            .collect(Collectors.toMap(FieldError::getField, e -> ObjectUtils.defaultIfNull(e.getDefaultMessage(), "")));

        AjaxDTO response = AjaxDTO.failure("校验失败", fieldErrors);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<AjaxDTO> bindException(BindException ex) {
        // 1. 找到 BindingResult 参数
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();

        // 2. 生成错误信息
        Map<String, String> fieldErrors = errors.stream()
            .filter(e -> e instanceof FieldError)
            .map(e -> (FieldError) e)
            .collect(Collectors.toMap(FieldError::getField, e -> ObjectUtils.defaultIfNull(e.getDefaultMessage(), "")));

        AjaxDTO response = AjaxDTO.failure("校验失败", fieldErrors);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AjaxDTO> bindException(ConstraintViolationException ex) {
        // 1. 生成错误信息
        Map<String, String> fieldErrors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            String path = String.valueOf(constraintViolation.getPropertyPath());
            String field = path.substring(Math.max(path.indexOf('.') + 1, 0)); // 排除第一个节点
            String message = constraintViolation.getMessage();
            fieldErrors.put(field, message);
        }

        AjaxDTO response = AjaxDTO.failure("校验失败", fieldErrors);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> ex(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
```
