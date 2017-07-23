package com.ahao.invoice.test;

import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.admin.auth.entity.AuthDO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Ahaochan on 2017/7/20.
 * <p>
 * // TODO 测试Controller, 实际生产环境删除
 */
@Controller
public class TestController {

    @GetMapping("/test")
    public ModelAndView test() {
        System.out.println("打开");
        ModelAndView mv = new ModelAndView("test/test_add");
        mv.addObject(AuthDO.TAG, new AuthDO());
        return mv;
    }

    @PostMapping("/test")
    public ModelAndView test(@Valid AuthDO authDO, BindingResult result) {
        System.out.println("保存");
        ModelAndView mv = new ModelAndView("test/test_add");
        if (result.hasErrors()) {
            mv.addObject("tip", AjaxDTO.failure("添加失败"));
        } else {
            mv.addObject("tip", AjaxDTO.success("添加成功"));
        }
        return mv;
    }

    @GetMapping(value = "/json", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getJSON() {
        return "你好,hello";
    }


}
