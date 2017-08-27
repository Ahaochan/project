package com.ahao.invoice.unit.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.invoice.util.ValidUtils;
import com.ahao.invoice.unit.entity.UnitDO;
import com.ahao.invoice.unit.service.UnitService;
import com.ahao.util.NumberHelper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Ahaochan on 2017/8/13.
 */
@RestController
public class UnitDataController {

    private UnitService unitService;

    @Autowired
    public UnitDataController(UnitService unitService){
        this.unitService = unitService;
    }

    @PostMapping("/invoice/unit")
    @Transactional
    public AjaxDTO add(@Valid UnitDO validUnit, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validUnit.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        unitService.insert(validUnit);

        Long id = validUnit.getId();
        return AjaxDTO.success(SpringConfig.getString("insert.success", id), id);
    }

    @DeleteMapping(value = "/invoice/units")
    @Transactional
    public AjaxDTO delete(@RequestBody MultiValueMap<String, String> formData) {
        List<String> ids = formData.get("unitIds[]");

        boolean success = unitService.deleteByKey(ids);
        int flag = NumberHelper.parse(success);
        String msg = SpringConfig.getString(success ? "delete.success" : "delete.failure");
        return AjaxDTO.get(flag, msg);
    }

    @GetMapping(value = "/invoice/units/page")
    public JSONObject getByPage(Integer page) {
        JSONObject json = new JSONObject();
        json.put("total", unitService.getAllCount());
        json.put("rows", unitService.getByPage(page));
        return json;
    }

    @PostMapping("/invoice/unit/{unitId}")
    @Transactional
    public AjaxDTO modify(@Valid UnitDO validUnit, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validUnit.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        if (validUnit.getId() == null) {
            // ID不能为空, 否则会更新全部数据
            return AjaxDTO.failure(SpringConfig.getString("update.failure", validUnit.getId()));
        }

        unitService.update(validUnit);
        return AjaxDTO.success(SpringConfig.getString("insert.success", validUnit.getId()));
    }

    @PostMapping("/invoice/unit/tax")
    public JSONObject select(@RequestParam("taxId") String taxId){
        List<UnitDO> list = unitService.selectByTaxId(taxId);
        JSONObject json = new JSONObject();
        json.put("value", list);
        return json;
    }
}
