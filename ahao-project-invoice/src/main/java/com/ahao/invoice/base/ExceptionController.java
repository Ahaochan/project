package com.ahao.invoice.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Ahaochan on 2017/9/16.
 */
@Controller
public class ExceptionController {

    @GetMapping("/403")
    public String status403(){
        return "exception/403";
    }
}
