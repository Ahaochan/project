package com.ahao.invoice.invoice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Avalon on 2017/6/7.
 */
@Controller
public class InvoiceViewController {
    @GetMapping("/admin/invoice")
    public String addInvoice(@PathVariable(value = "invoiceId", required = false) Long invoiceId){
        return "invoice/invoice_add";
    }
}
