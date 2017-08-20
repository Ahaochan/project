package com.ahao.invoice.unit.controller;

import com.ahao.invoice.unit.entity.UnitDO;
import com.ahao.invoice.unit.service.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ahaochan on 2017/8/13.
 */
@Controller
public class UnitViewController {
    private static final Logger logger = LoggerFactory.getLogger(UnitViewController.class);
    private UnitService unitService;

    @Autowired
    public UnitViewController(UnitService unitService) {
        this.unitService = unitService;
    }


    @GetMapping("/invoice/units")
    public String all() {
        return "invoice/unit/list";
    }

    @GetMapping("/invoice/unit")
    public String add() {
        return "invoice/unit/add";
    }

    @GetMapping("/invoice/unit/{unitId}")
    public ModelAndView modify(@PathVariable(value = "unitId") Long unitId) {
        ModelAndView mv = new ModelAndView("invoice/unit/modify");
        UnitDO user = unitService.selectByKey(unitId);
        mv.addObject(UnitDO.TAG, user);
        return mv;
    }

}
