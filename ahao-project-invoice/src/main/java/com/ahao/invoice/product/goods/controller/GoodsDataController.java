package com.ahao.invoice.product.goods.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.invoice.util.ValidUtils;
import com.ahao.invoice.product.goods.entity.GoodsDO;
import com.ahao.invoice.product.goods.sevice.GoodsService;
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
 * Created by Ahaochan on 2017/8/22.
 */
@RestController
public class GoodsDataController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsDataController.class);

    private GoodsService goodsService;

    @Autowired
    public GoodsDataController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/product/good")
    @Transactional
    public AjaxDTO add(@Valid GoodsDO validGoods, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validGoods.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        goodsService.insert(validGoods);
        Long id = validGoods.getId();
        return AjaxDTO.success(SpringConfig.getString("insert.success", id), id);
    }

    @DeleteMapping("/product/goods")
    @Transactional
    public AjaxDTO delete(@RequestBody MultiValueMap<String, String> formData) {
        List<String> ids = formData.get("goodIds[]");

        boolean success = goodsService.deleteByKey(ids);
        int flag = NumberHelper.parseInt(success);
        String msg = SpringConfig.getString(success ? "delete.success" : "delete.failure");
        return AjaxDTO.get(flag, msg);
    }

    @GetMapping("/product/goods/page")
    public JSONObject getByPage(Integer page) {
        JSONObject json = new JSONObject();
        json.put("total", goodsService.getAllCount());
        json.put("rows", goodsService.getByPage(page));
        return json;
    }

    @PostMapping("/product/good/{goodId}")
    @Transactional
    public AjaxDTO modify(@Valid GoodsDO validGoods, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validGoods.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        if (validGoods.getId() == null) {
            // ID不能为空, 否则会更新全部数据
            return AjaxDTO.failure(SpringConfig.getString("update.failure", validGoods.getId()));
        }

        goodsService.update(validGoods);
        return AjaxDTO.success(SpringConfig.getString("insert.success", validGoods.getId()));
    }

    @PostMapping("/product/good/searchByName")
    public JSONObject getCategory(@RequestParam("name") String name) {
        List<GoodsDO> list = goodsService.selectByName(name);
        JSONObject json = new JSONObject();
        json.put("value", list);
        return json;
    }
}
