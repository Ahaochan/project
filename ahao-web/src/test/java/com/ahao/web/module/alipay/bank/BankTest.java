package com.ahao.web.module.alipay.bank;


import com.ahao.commons.http.HttpClientHelper;
import org.junit.Assert;
import org.junit.Test;

public class BankTest {

    @Test
    public void parseCard() {
        Bank bank = Bank.parseCard("6222020903001483077");
        Assert.assertNotNull(bank);
        Assert.assertEquals("ICBC", bank.getCode());
    }

    @Test
    public void getBankLogo() {
        String ICBC_LOGO = Bank.ICBC.getBankLogo();
        int status = HttpClientHelper.get(ICBC_LOGO).execute(true).getStatusCode();
        Assert.assertEquals(200, status);

        int error = HttpClientHelper.get("http://localhost").execute().getStatusCode();
        Assert.assertNotEquals(200, error);
    }
}