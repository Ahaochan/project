package com.ahao.invoice.invoice.service;

import com.ahao.entity.IDataSet;
import com.ahao.invoice.invoice.entity.InvoiceDO;
import com.ahao.service.PageService;
import org.springframework.stereotype.Service;

/**
 * Created by Ahaochan on 2017/7/30.
 */
@Service
public interface InvoiceService extends PageService<InvoiceDO> {


    IDataSet getForm(Long invoiceId);

    boolean existName(String code, String number);

    void addRelate(Long invoiceId, Long goodsId, Long number);

}
