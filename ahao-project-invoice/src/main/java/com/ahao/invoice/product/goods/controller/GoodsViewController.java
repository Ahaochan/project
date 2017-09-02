package com.ahao.invoice.product.goods.controller;

import com.ahao.invoice.product.goods.entity.GoodsDO;
import com.ahao.invoice.product.goods.sevice.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ahaochan on 2017/8/22.
 */
@Controller
public class GoodsViewController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsViewController.class);
    private GoodsService goodsService;

    @Autowired
    public GoodsViewController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }


    @GetMapping("/product/goods")
    public String all() {
        return "product/goods/list";
    }

    @GetMapping("/product/good")
    public String add() {
        return "product/goods/add";
    }

    @GetMapping("/product/good/{goodId}")
    public ModelAndView modify(@PathVariable(value = "goodId") Long goodId) {
        ModelAndView mv = new ModelAndView("product/goods/modify");
        GoodsDO goods = goodsService.selectByKey(goodId);
        goods.setTaxRate(goods.getTaxRate());
        mv.addObject(GoodsDO.TAG, goods);
        mv.addObject("category", goodsService.selectCategoryByKey(goodId));
        return mv;
    }
}
