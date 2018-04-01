package com.ahao.forum.guitar.module.index.controller;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.index.service.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private IndexService indexService;
    public IndexController(IndexService indexService){
        this.indexService = indexService;
    }

    @GetMapping({"/index", "/"})
    public String index(Model model) {
        List<IDataSet> categories = indexService.getCategories();
        for (IDataSet category : categories) {
            long categoryId = category.getLong("id");
            List<IDataSet> forums = indexService.getForums(categoryId);
            category.put("forums", forums);
        }
        model.addAttribute("categories", categories);
        return "index";
    }

    @GetMapping("/test")
    public String test(){
        return "category/manager-category-list";
    }
}
