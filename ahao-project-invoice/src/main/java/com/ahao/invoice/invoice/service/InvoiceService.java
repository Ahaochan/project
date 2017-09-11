package com.ahao.invoice.invoice.service;

import com.ahao.entity.IDataSet;
import com.ahao.invoice.invoice.entity.InvoiceDO;
import com.ahao.service.PageService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Created by Ahaochan on 2017/7/30.
 */
@Service
public interface InvoiceService extends PageService<InvoiceDO> {

    /**
     * 获取单个发票信息, 用于编辑发票信息页面的显示
     * @param invoiceId 发票id
     * @return 发票信息
     */
    IDataSet getForm(Long invoiceId);

    /**
     * 根据 发票代码 和 发票号码 判断是否有相同的发票
     * @param code 发票代码
     * @param number 发票号码
     * @return 是否存在发票
     */
    boolean exist(String code, String number);

    /**
     * 对发票货物表进行关联
     * @param invoiceId 发票id
     * @param goodsId 货物id
     * @param number 货物数量
     */
    void addRelate(Long invoiceId, Long goodsId, Long number);


    /**
     * 返回 时间、进项价税合计、销项价税合计、纯利润
     */
    JSONObject getProfitGraph();

    /**
     * 根据发票类型获取发票的地理分布
     */
    JSONObject getDistribution();

}
