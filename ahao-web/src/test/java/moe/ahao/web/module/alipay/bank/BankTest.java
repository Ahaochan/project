package moe.ahao.web.module.alipay.bank;


import moe.ahao.commons.http.HttpClientHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void parseCard() {
        Bank bank = Bank.parseCard("6222020903001483077");
        Assertions.assertNotNull(bank);
        Assertions.assertEquals("ICBC", bank.getCode());
    }

    @Test
    public void getBankLogo() {
        String ICBC_LOGO = Bank.ICBC.getBankLogo();
        int status = HttpClientHelper.get(ICBC_LOGO).execute(true).getStatusCode();
        Assertions.assertEquals(200, status);

        int error = HttpClientHelper.get("http://localhost").execute().getStatusCode();
        Assertions.assertNotEquals(200, error);
    }
}
