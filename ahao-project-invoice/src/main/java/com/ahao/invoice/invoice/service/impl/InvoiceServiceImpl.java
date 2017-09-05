package com.ahao.invoice.invoice.service.impl;

import com.ahao.entity.DataSet;
import com.ahao.invoice.invoice.dao.InvoiceDAO;
import com.ahao.invoice.invoice.entity.InvoiceDO;
import com.ahao.invoice.invoice.service.InvoiceService;
import com.ahao.service.impl.PageServiceImpl;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;

/**
 * Created by Ahaochan on 2017/7/30.
 */
@Service
public class InvoiceServiceImpl extends PageServiceImpl<InvoiceDO> implements InvoiceService {

    private InvoiceDAO invoiceDAO;

    @Autowired
    public InvoiceServiceImpl(InvoiceDAO invoiceDAO) {
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
    protected Collection<DataSet> getByPage(int start, int pageSize, String sort, String order) {
        return invoiceDAO.selectPage(start, pageSize, sort, order);
    }


    @Override
    public boolean existName(String code, String number) {
        if (StringHelper.isNullOrEmptyString(code) || StringHelper.isNullOrEmptyString(number)) {
            return true;
        }
        if (code.length() != 10 && number.length() != 8) {
            return true;
        }

        Example example = new Example(InvoiceDO.class);
        example.createCriteria()
                .andEqualTo("code", code)
                .andEqualTo("number", number);
        int count = invoiceDAO.selectCountByExample(example);
        return count > 0;
    }
}
