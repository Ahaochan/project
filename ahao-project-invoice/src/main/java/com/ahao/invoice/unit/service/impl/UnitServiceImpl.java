package com.ahao.invoice.unit.service.impl;

import com.ahao.invoice.unit.dao.UnitDAO;
import com.ahao.invoice.unit.entity.UnitDO;
import com.ahao.invoice.unit.service.UnitService;
import com.ahao.service.impl.PageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

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

}
