package moe.ahao.spring.boot.feishu;

import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.feishu.config.NotifyFeishuProperties;
import moe.ahao.spring.boot.feishu.dto.MyCardReq;
import moe.ahao.spring.boot.feishu.feign.FeishuFeignClient;
import moe.ahao.spring.boot.feishu.feign.dto.FeishuCardTemplateSendReq;
import moe.ahao.spring.boot.feishu.feign.dto.FeishuMessageCardURL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class FeishuMessageTest {
    @Autowired
    private FeishuFeignClient feishuFeignClient;
    @Autowired
    private NotifyFeishuProperties feishuNotifyProperties;

    @Test
    public void cardMessage() throws Exception {
        MyCardReq myCardReq = new MyCardReq();
        myCardReq.setTitle("服务发生异常");
        myCardReq.setAlarmMessage("系统发生了宕机");
        myCardReq.setErrorMessage("日志写入失败，磁盘IO 100%");
        myCardReq.setDownloadUrl(new FeishuMessageCardURL("https://open.feishu.cn"));
        myCardReq.setTableRawArray(new ArrayList<>());
        for (int i = 0; i < 20; i++) {
            MyCardReq.MyTableRow row = new MyCardReq.MyTableRow();
            row.setNo("运单" + i);
            row.setStatus("状态" + i);
            myCardReq.getTableRawArray().add(row);
        }

        NotifyFeishuProperties.FeishuConfig feishuConfig = feishuNotifyProperties.getScene(NotifyFeishuProperties.SceneEnum.SEND_ALARM);
        FeishuCardTemplateSendReq.Template<MyCardReq> template = new FeishuCardTemplateSendReq.Template<>();
        template.setTemplateId(feishuConfig.getTemplateId());
        template.setTemplateVersionName(feishuConfig.getVersion());
        template.setTemplateVariable(myCardReq);

        FeishuCardTemplateSendReq<MyCardReq> req = new FeishuCardTemplateSendReq<>(template);

        String resp = feishuFeignClient.sendMessage(feishuConfig.getToken(), req);
        System.out.println(resp);
    }
}
