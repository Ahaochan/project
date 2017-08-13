package com.ahao.invoice.invoice.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.invoice.util.ProvinceUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ahaochan on 2017/7/30.
 */
@RestController
public class InvoiceDataController {
    private static final String[] LANGUAGE = new String[]{"",
            "invoice.language.chinese", // 中文
            "invoice.language.english", // 中英文
            "invoice.language.tibetan", // 藏汉文
            "invoice.language.uyghur"}; // 维汉文
    private static final String[] MONEY_VERSION = new String[]{"",
            "invoice.money-version.10_000",         // 万元版
            "invoice.money-version.100_000",        // 十万元版
            "invoice.money-version.1_000_000",      // 百万元版
            "invoice.money-version.10_000_000"};    // 千万元版

    @PostMapping("/parseCode")
    public AjaxDTO parseCode(@RequestParam("code") String code) {
        if (code.length() != 10) {
            return AjaxDTO.failure(SpringConfig.getString("invoice.code.illegal"));
        }

        JSONObject json = new JSONObject();
        json.put("city", ProvinceUtils.getCity(code.substring(0, 4) + "00"));
        json.put("year", code.substring(4, 6));
        json.put("print", code.charAt(6));

        boolean isPro = (code.charAt(7) - '0') == 6;
        json.put("type", SpringConfig.getString(isPro ? "invoice.value-added.pro" : "invoice.value-added.normal"));
        if (isPro) {
            json.put("language", SpringConfig.getString(LANGUAGE[code.charAt(7) - '0']));
        }

        json.put("num", SpringConfig.getString("invoice.num", code.charAt(8)));


        int code_9 = code.charAt(9) - '0';
        if (code_9 == 0) {
            json.put("computer", true);
        } else {
            json.put("moneyVersion", MONEY_VERSION[code_9]);
        }

        return AjaxDTO.success(json);
    }
}
