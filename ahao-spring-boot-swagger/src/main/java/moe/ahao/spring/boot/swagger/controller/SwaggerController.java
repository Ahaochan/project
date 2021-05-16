package moe.ahao.spring.boot.swagger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
//@Api(hidden = true) // 无效
@ApiIgnore
public class SwaggerController {

    @GetMapping("/swagger")
    public String swagger() {
        // return "redirect:/swagger-ui.html";
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/doc")
    public String doc() {
        return "redirect:/doc.html";
    }
}
