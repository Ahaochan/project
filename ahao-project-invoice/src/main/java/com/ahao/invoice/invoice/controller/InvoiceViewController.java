package com.ahao.invoice.invoice.controller;

import com.ahao.invoice.invoice.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Avalon on 2017/6/7.
 */
@Controller
public class InvoiceViewController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceViewController.class);
    private InvoiceService invoiceService;

    @Autowired
    public InvoiceViewController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public String all() {
        return "invoice/invoice_list";
    }

    @GetMapping("/invoice")
    public String add() {
        return "invoice/invoice_add";
    }

//    @GetMapping("/invoice/{invoiceId}")
//    public ModelAndView modify(@PathVariable(value = "invoiceId") Long invoiceId) {
//        ModelAndView mv = new ModelAndView("invoice/invoice_modify");
//        InvoiceDO auth = invoiceService.selectByKey(invoiceId);
//        mv.addObject(InvoiceDO.TAG, auth);
//        return mv;
//    }
}
