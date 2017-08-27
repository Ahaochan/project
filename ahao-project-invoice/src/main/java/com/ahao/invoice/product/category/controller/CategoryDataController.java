package com.ahao.invoice.product.category.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.admin.auth.controller.AuthDataController;
import com.ahao.invoice.invoice.util.ValidUtils;
import com.ahao.invoice.product.category.entity.CategoryDO;
import com.ahao.invoice.product.category.sevice.CategoryService;
import com.ahao.util.NumberHelper;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Ahaochan on 2017/8/21.
 */
@RestController
public class CategoryDataController {
    private static final Logger logger = LoggerFactory.getLogger(AuthDataController.class);

    private CategoryService categoryService;

    @Autowired
    public CategoryDataController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/product/category")
    @Transactional
    public AjaxDTO add(@Valid CategoryDO validCategory, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validCategory.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        categoryService.insert(validCategory);
        Long id = validCategory.getId();
        return AjaxDTO.success(SpringConfig.getString("insert.success", id), id);
    }

    @DeleteMapping("/product/categorys")
    @Transactional
    public AjaxDTO delete(@RequestBody MultiValueMap<String, String> formData) {
        List<String> ids = formData.get("categoryIds[]");

        boolean success = categoryService.deleteByKey(ids);
        int flag = NumberHelper.parse(success);
        String msg = SpringConfig.getString(success ? "delete.success" : "delete.failure");
        return AjaxDTO.get(flag, msg);
    }

    @GetMapping(value = "/product/categorys/page")
    public JSONObject getByPage(Integer page) {
        JSONObject json = new JSONObject();
        json.put("total", categoryService.getAllCount());
        json.put("rows", categoryService.getByPage(page));
        return json;
    }

    @PostMapping("/product/category/{categoryId}")
    @Transactional
    public AjaxDTO modify(@Valid CategoryDO validCategory, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validCategory.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        if (validCategory.getId() == null) {
            // ID不能为空, 否则会更新全部数据
            return AjaxDTO.failure(SpringConfig.getString("update.failure", validCategory.getId()));
        }

        categoryService.update(validCategory);
        return AjaxDTO.success(SpringConfig.getString("insert.success", validCategory.getId()));
    }

    @PostMapping("/product/category/searchByName")
    public JSONObject getCategory(@RequestParam("name") String name){
        List<CategoryDO> list = categoryService.selectByName(name);
        JSONObject json = new JSONObject();
        json.put("value", list);
        return json;
    }
}
