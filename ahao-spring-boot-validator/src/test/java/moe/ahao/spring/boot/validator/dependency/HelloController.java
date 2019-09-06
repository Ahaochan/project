package moe.ahao.spring.boot.validator.dependency;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello(@Validated User user, BindingResult bindingResult) {
        return user;
    }
}
