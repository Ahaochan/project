package com.ahao.invoice.unit.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.unit.net.BankApi;
import com.ahao.util.StringHelper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Ahaochan on 2017/8/14.
 */
@RestController
public class BankDataController {

    @GetMapping("/invoice/bank/{account}")
    public AjaxDTO parseCode(@PathVariable("account") String account){
        String bank = BankApi.getCode(account);
        if(StringHelper.isEmpty(bank)){
            return AjaxDTO.failure(SpringConfig.getString("invoice.unit.account.illegal"));
        }

        JSONObject json = new JSONObject();
        json.put("bank", bank);
        json.put("imgUrl", "/invoice/bank/"+bank+"/img");
        return AjaxDTO.success(json);
    }

    @GetMapping("/invoice/bank/{code}/img")
    public void getBankImage(@PathVariable("code") String code,
                             HttpServletResponse response) throws IOException {
        byte[] imgByte = BankApi.getImage(code);

        response.setContentType("image/png");
        OutputStream stream = response.getOutputStream();
        stream.write(imgByte);
        stream.flush();
        stream.close();
    }
}
