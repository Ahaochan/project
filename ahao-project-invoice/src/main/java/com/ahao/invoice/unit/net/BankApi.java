package com.ahao.invoice.unit.net;

import com.ahao.net.HttpClientHelper;
import com.ahao.net.convert.JSONConvert;

/**
 * Created by Ahaochan on 2017/8/14.
 */
public class BankApi {
    public static final String URL_CODE = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json";
    public static final String URL_IMG = "https://apimg.alipay.com/combo.png";

    public static String getCode(String account){
        return HttpClientHelper.get(URL_CODE)
                .addParam("_input_charset", "utf-8")
                .addParam("cardNo", account)
                .addParam("cardBinCheck", "true")
                .execute()
                .convert(JSONConvert.class)
                .getString("bank");
    }

    public static byte[] getImage(String code){
        return HttpClientHelper.get(URL_IMG)
                .addParam("d", "cashier")
                .addParam("t", code)
                .execute()
                .getByte();
    }
}
