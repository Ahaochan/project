package com.ahao.invoice.invoice.service.impl;

import com.ahao.entity.DataSet;
import com.ahao.entity.IDataSet;
import com.ahao.invoice.invoice.dao.InvoiceDAO;
import com.ahao.invoice.invoice.entity.InvoiceDO;
import com.ahao.invoice.invoice.service.InvoiceService;
import com.ahao.service.impl.PageServiceImpl;
import com.ahao.util.NumberHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by Ahaochan on 2017/7/30.
 */
@Service
public class InvoiceServiceImpl extends PageServiceImpl<InvoiceDO> implements InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);

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
    public IDataSet getForm(Long invoiceId) {
        if (invoiceId == null) {
            logger.warn("发票id为:" + null);
            return null;
        }
        return invoiceDAO.getForm(invoiceId);
    }

    @Override
    public boolean exist(String code, String number) {
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

    @Override
    public void addRelate(Long invoiceId, Long goodsId, Long number) {
        if (invoiceId == null) {
            logger.warn("发票货物表添加失败, 发票id为空");
            return;
        }
        if (goodsId == null) {
            logger.warn("发票货物表添加失败, 货物id为空");
            return;
        }
        logger.debug("添加发票货物表关联: 发票id:[" + invoiceId + "], 货物id:[" + goodsId + "]");
        invoiceDAO.addRelate(invoiceId, goodsId, number);
    }

    @Override
    public JSONObject getProfitGraph() {
        Map<String, Map<Boolean, DataSet>> data = invoiceDAO.getProfitGraph()
                .stream()
                .collect(Collectors.groupingBy(d->d.getString("date"), TreeMap::new,
                        Collectors.partitioningBy(d -> d.getBoolean("type"),
                                Collectors.reducing(null, (d1, d2) -> d2))));

        JSONObject json = new JSONObject();
        JSONArray time = new JSONArray();
        JSONArray sell = new JSONArray();
        JSONArray bought = new JSONArray();
        JSONArray profit = new JSONArray();
        for(Map.Entry<String, Map<Boolean, DataSet>> entry : data.entrySet()){
            String date = entry.getKey();

            // 想卖出的钱(成本金额+成本税额+想要获得的利润)
            double sellMoney = Optional.ofNullable(entry.getValue()).map(e -> e.get(true)).map(d -> d.getDouble("money")).orElse(0.0);
            // 卖出的税额(想要获得的利润的税额)
            double sellTax = Optional.ofNullable(entry.getValue()).map(e -> e.get(true)).map(d -> d.getDouble("tax")).orElse(0.0);
            // 想购入的价钱(成本金额)
            double boughtMoney = Optional.ofNullable(entry.getValue()).map(e -> e.get(false)).map(d -> d.getDouble("money")).orElse(0.0);
            // 购入的税额(成本税额)
            double boughtTax = Optional.ofNullable(entry.getValue()).map(e -> e.get(false)).map(d -> d.getDouble("tax")).orElse(0.0);

            time.add(date);
            sell.add(sellMoney+sellTax);
            bought.add(boughtMoney+boughtTax);
            profit.add(sellMoney-boughtMoney-boughtTax);
        }

        json.put("time", time);
        json.put("sell", sell);
        json.put("bought", bought);
        json.put("profit", profit);
        return json;
    }


    @Override
    public JSONObject getDistribution() {
        Map<String, Map<Boolean, DataSet>> data = invoiceDAO.getDistribution().stream()
                .collect(Collectors.groupingBy(
                        d -> d.getString("date"),
                        TreeMap::new,// 以年月分组
                        Collectors.partitioningBy(
                                d -> d.getBoolean("type"),
                                Collectors.reducing(
                                        new DataSet(),
                                        (left, right) -> {
                                            DataSet map = new DataSet();

                                            String leftCode = left.getString("code");
                                            String leftNumber = left.getString("number");
                                            if ("".equals(leftCode)) {
                                                map.putAll(left);
                                            } else {
                                                map.put(leftCode, leftNumber);
                                            }
                                            String rightCode = right.getString("code");
                                            String rightNumber = right.getString("number");
                                            map.put(rightCode, rightNumber);
                                            return map;
                                        }
                                ))));

        JSONObject json = new JSONObject(new TreeMap<>());
        for (Map.Entry<String, Map<Boolean, DataSet>> entry : data.entrySet()) {
            String date = entry.getKey();
            JSONObject item = new JSONObject();
            int max = 0;


            // 销贷单位的行政区划代码和数量
            JSONArray sellArray = new JSONArray();
            DataSet sellData = entry.getValue().get(true);
            for (Map.Entry<String, Object> sellEntry : sellData.entrySet()) {
                String code = sellEntry.getKey();
                int number = NumberHelper.parseInt(sellEntry.getValue());

                if (number > max) {
                    max = number;
                }

                JSONObject result = new JSONObject();
                result.put("name", code);
                result.put("value", number);

                sellArray.add(result);
            }
            item.put("sell", sellArray);

            // 购贷单位的行政区划代码和数量
            JSONArray boughtArray = new JSONArray();
            DataSet boughtData = entry.getValue().get(false);
            for (Map.Entry<String, Object> boughtEntry : boughtData.entrySet()) {
                String code = boughtEntry.getKey();
                int number = NumberHelper.parseInt(boughtEntry.getValue());

                if (number > max) {
                    max = number;
                }

                JSONObject result = new JSONObject();
                result.put("name", code);
                result.put("value", number);

                boughtArray.add(result);
            }
            item.put("bought", boughtArray);
            item.put("max", max);

            json.put(date, item);
        }

        return json;
    }

    @Override
    public Long getProfit() {
        Map<Boolean, DataSet> data = invoiceDAO.getProfit().stream()
                .collect(Collectors.toMap(d->d.getBoolean("type"), d->d));

        // 想卖出的钱(成本金额+成本税额+想要获得的利润)
        double sellMoney = Optional.ofNullable(data.get(true)).map(d -> d.getDouble("money")).orElse(0.0);
        // 卖出的税额(想要获得的利润的税额)
//        double sellTax = Optional.ofNullable(data.get(true)).map(d -> d.getDouble("tax")).orElse(0.0);
        // 想购入的价钱(成本金额)
        double boughtMoney = Optional.ofNullable(data.get(false)).map(d -> d.getDouble("money")).orElse(0.0);
        // 购入的税额(成本税额)
        double boughtTax = Optional.ofNullable(data.get(false)).map(d -> d.getDouble("tax")).orElse(0.0);

        return (long) (sellMoney - boughtMoney - boughtTax);
    }
}
