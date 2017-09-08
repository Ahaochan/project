package com.ahao.invoice.invoice.controller;

import com.ahao.entity.IDataSet;
import com.ahao.invoice.invoice.service.InvoiceService;
import com.ahao.invoice.product.goods.sevice.GoodsService;
import com.ahao.invoice.unit.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Avalon on 2017/6/7.
 */
@Controller
public class InvoiceViewController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceViewController.class);
    private InvoiceService invoiceService;
    private GoodsService goodsService;
    private UnitService unitService;

    @Autowired
    public InvoiceViewController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Autowired
    public void setGoodsService(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @Autowired
    public void setUnitService(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/invoices")
    public String all() {
        return "invoice/invoice_list";
    }

    @GetMapping("/invoice")
    public String add() {
        return "invoice/invoice_add";
    }

    @GetMapping("/invoice/{invoiceId}")
    public ModelAndView modify(@PathVariable("invoiceId") Long invoiceId) {
        ModelAndView mv = new ModelAndView("invoice/invoice_modify");
        IDataSet data = invoiceService.getForm(invoiceId);
        mv.addObject("data", data);

        return mv;
    }
}
