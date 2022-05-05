package moe.ahao.spring.boot.validator;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@RestController
@Validated
public class HelloController {

    @GetMapping("/path-{id}-{age}")
    public Integer path(@Valid @PathVariable @Max(value = 5, message = "超过 id 的范围了") Integer id,
                        @Valid @PathVariable @Max(value = 5, message = "超过 age 的范围了") Integer age) {
        return id + age;
    }

    @GetMapping("/get1")
    public User get1(@Valid User user1) {
        return user1;
    }

    @GetMapping("/get2")
    public Integer get1(@Valid @RequestParam @Max(value = 5, message = "超过 id 的范围了") Integer id,
                        @Valid @RequestParam @Max(value = 5, message = "超过 age 的范围了") Integer age) {
        return id + age;
    }

    @PostMapping("/post1")
    public User post1(@Valid @RequestBody User user) {
        return user;
    }

    @PostMapping("/post2")
    public NestedObj post1(@Valid @RequestBody NestedObj obj) {
        return obj;
    }
}
