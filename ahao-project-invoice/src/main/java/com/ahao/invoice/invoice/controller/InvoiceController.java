package com.ahao.invoice.invoice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Avalon on 2017/6/7.
 */
@Controller
public class InvoiceController {
    @RequestMapping(value = "/admin/invoice/{invoiceId}", method = RequestMethod.GET)
    public String addInvoice(@PathVariable(value = "invoiceId", required = false) Long invoiceId){
        return "invoice/invoice_add";
    }
}
