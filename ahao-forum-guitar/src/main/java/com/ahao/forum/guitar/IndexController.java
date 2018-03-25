package com.ahao.forum.guitar;

import com.ahao.forum.guitar.module.category.service.CategoryService;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private CategoryService categoryService;
    public IndexController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping({"/index", "/"})
    public String index(Model model) {
        JSONArray catagory = categoryService.getCategoryAndSub();
        model.addAttribute("category", catagory);
        return "index";
    }

    @GetMapping("/test")
    public String test(){
        return "admin/pane/pane-category-group";
    }
}
