package com.ahao.invoice.unit.service;

import com.ahao.invoice.unit.entity.UnitDO;
import com.ahao.service.PageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ahaochan on 2017/8/13.
 */
@Service
public interface UnitService extends PageService<UnitDO> {

    public List<UnitDO> selectByTaxId(String taxId);

}
