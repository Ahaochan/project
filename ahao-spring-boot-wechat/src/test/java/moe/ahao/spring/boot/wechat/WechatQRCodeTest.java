package moe.ahao.spring.boot.wechat;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.wechat.mp.feign.dto.WechatGetUnlimitedQRCodeQuery;
import moe.ahao.spring.boot.wechat.mp.service.WeChatQRCodeComponent;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
public class WechatQRCodeTest {
    @Autowired
    private WeChatQRCodeComponent weChatQRCodeComponent;

    @Test
    public void testGetUnlimitedQRCode() throws Exception {
        String accessToken = "";

        WechatGetUnlimitedQRCodeQuery query = new WechatGetUnlimitedQRCodeQuery();
        query.setScene("pageId=");
        query.setPage("pages/new/index");
        // query.setCheckPath();
        query.setEnvVersion(WechatGetUnlimitedQRCodeQuery.EnvVersion.RELEASE.getCode());
        // query.setWidth();
        // query.setAutoColor();
        // query.setLineColor();
        query.setIsHyaline(false);

        byte[] unlimitedQRCode = weChatQRCodeComponent.getUnlimitedQRCode(accessToken, query);
        FileUtils.writeByteArrayToFile(new File("123.jpg"), unlimitedQRCode);
        System.out.println(unlimitedQRCode);
    }
}
