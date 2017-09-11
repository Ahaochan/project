package com.ahao.invoice.unit.service.impl;

import com.ahao.entity.DataSet;
import com.ahao.invoice.unit.dao.UnitDAO;
import com.ahao.invoice.unit.entity.UnitDO;
import com.ahao.invoice.unit.service.UnitService;
import com.ahao.service.impl.PageServiceImpl;
import com.ahao.util.NumberHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by Ahaochan on 2017/8/13.
 */
@Service
public class UnitServiceImpl extends PageServiceImpl<UnitDO> implements UnitService {
    private static final Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);

    private UnitDAO unitDAO;

    @Autowired
    public UnitServiceImpl(UnitDAO unitDAO) {
        this.unitDAO = unitDAO;
    }

    @Override
    protected Mapper<UnitDO> dao() {
        return unitDAO;
    }

    @Override
    protected Class<UnitDO> clazz() {
        return UnitDO.class;
    }

    @Override
    protected Collection<UnitDO> getByPage(int start, int pageSize, String sort, String order) {
        return unitDAO.selectPage(start, pageSize, sort, order);
    }

    @Override
    public List<UnitDO> selectByTaxId(String taxId) {
        Example example = new Example(UnitDO.class);
        example.selectProperties("id", "taxId", "name", "address", "tel", "account")
                .createCriteria().andLike("taxId", taxId + "%");
        return unitDAO.selectByExample(example);
    }

    @Override
    public JSONObject getDistribution() {
        Map<String, Map<Boolean, DataSet>> data = unitDAO.getDistribution().stream()
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
}
