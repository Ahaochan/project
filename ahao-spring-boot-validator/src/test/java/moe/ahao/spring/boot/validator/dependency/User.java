package moe.ahao.spring.boot.validator.dependency;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class User {
    @NotBlank(message = "名称不能为空")
    private String name;

    @Max(value = 100, message = "年龄不大于100")
    private Integer age;

    @Pattern(regexp = "^1([34578])\\d{9}$",message = "手机号码格式错误")
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @Email(message = "邮箱格式错误")
    private String email;

}
