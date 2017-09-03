package com.ahao.invoice.invoice.service.impl;

import com.ahao.invoice.invoice.dao.InvoiceDAO;
import com.ahao.invoice.invoice.entity.InvoiceDO;
import com.ahao.invoice.invoice.service.InvoiceService;
import com.ahao.service.impl.PageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * Created by Ahaochan on 2017/7/30.
 */
@Service
public class InvoiceServiceImpl extends PageServiceImpl<InvoiceDO> implements InvoiceService {

    private InvoiceDAO invoiceDAO;

    @Autowired
    public InvoiceServiceImpl(InvoiceDAO invoiceDAO){
        this.invoiceDAO = invoiceDAO;
    }

    @Override
    protected Mapper<InvoiceDO> dao() {
        return invoiceDAO;
    }

    @Override
    protected Class<InvoiceDO> clazz() {
        return InvoiceDO.class;
    }

    @Override
    protected Collection<InvoiceDO> getByPage(int start, int pageSize, String sort, String order) {
        return invoiceDAO.selectPage(start, pageSize, sort, order);
    }
}
