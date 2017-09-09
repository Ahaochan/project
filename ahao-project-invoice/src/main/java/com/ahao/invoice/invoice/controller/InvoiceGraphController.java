package com.ahao.invoice.invoice.controller;

import com.ahao.entity.AjaxDTO;
import com.ahao.entity.DataSet;
import com.ahao.invoice.invoice.service.InvoiceService;
import com.ahao.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public AjaxDTO getDistribution(@RequestParam("type") Boolean type){
        Map<Integer, Map<Integer, List<DataSet>>> result = invoiceService.getDistribution(type);
        if(CollectionHelper.isEmpty(result)){
            return AjaxDTO.failure("没有查询到数据");
        }
        return AjaxDTO.success("查询数据成功", result);
    }

    @PostMapping("/invoice/graph/profit")
    public AjaxDTO getMoneyTax(){
        return AjaxDTO.success(invoiceService.getProfitGraph());
    }
}
