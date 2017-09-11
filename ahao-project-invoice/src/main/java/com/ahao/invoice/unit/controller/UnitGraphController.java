package com.ahao.invoice.unit.controller;

import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.unit.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ahaochan on 2017/9/10.
 */
@RestController
public class UnitGraphController {

    private UnitService unitService;

    @Autowired
    public UnitGraphController(UnitService unitService){
        this.unitService = unitService;
    }


    @PostMapping("/invoice/unit/graph/distribution")
    public AjaxDTO getDistribution(){
        return AjaxDTO.success(unitService.getDistribution());
    }
}
