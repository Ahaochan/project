package com.ahao.invoice.invoice.controller;

import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.invoice.service.InvoiceService;
import com.ahao.util.CollectionHelper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ahaochan on 2017/9/8.
 */
@RestController
public class InvoiceGraphController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceGraphController(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @PostMapping("/invoice/graph/distribution")
    public AjaxDTO getDistribution(){
        JSONObject result = invoiceService.getDistribution();
        if(CollectionHelper.isEmpty(result)){
            return AjaxDTO.failure("没有查询到数据");
        }
        return AjaxDTO.success("查询数据成功", result);
    }

    @PostMapping("/invoice/graph/profit")
    public AjaxDTO getMoneyTax(){
        return AjaxDTO.success(invoiceService.getProfitGraph());
    }

    @PostMapping("/invoice/count")
    public AjaxDTO getCount(){
        return AjaxDTO.success(invoiceService.getAllCount());
    }

    @PostMapping("/invoice/profit")
    public AjaxDTO getAllProfit(){
        return AjaxDTO.success(invoiceService.getProfit());
    }
}
