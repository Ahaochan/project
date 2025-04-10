package moe.ahao.spring.boot.feishu;

import com.lark.oapi.card.enums.MessageCardHeaderTemplateEnum;
import com.lark.oapi.card.model.*;
import com.lark.oapi.service.im.v1.enums.MsgTypeEnum;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import com.lark.oapi.service.im.v1.model.ext.MessageCardLayoutMarkdown;
import moe.ahao.spring.boot.Starter;
import moe.ahao.spring.boot.feishu.feign.FeishuFeignClient;
import moe.ahao.spring.boot.feishu.feign.dto.FeishuCardMessageReq;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class FeishuMessageTest {
    public static final String token = "";
    @Autowired
    private FeishuFeignClient feishuFeignClient;

    @Test
    public void cardMessage() throws Exception {
        List<String> messages = Arrays.asList("测试1", "测试2", "测试3", "测试4", "测试5", "测试6", "测试7", "测试8", "测试9", "测试10");

        MessageCardConfig config = MessageCardConfig.newBuilder().wideScreenMode(true).build();
        MessageCardHeader header = MessageCardHeader.newBuilder()
            .title(MessageCardPlainText.newBuilder().content("我是标题内容").build())
            .template(MessageCardHeaderTemplateEnum.RED)
            .build();

        MessageCardField[] messageCardFields = new MessageCardField[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            String content = String.format("**%s**\t%s\t%s", messages.get(i), "待完成", "已完成");
            // String content = String.format("**%s**", messages.get(i));

            MessageCardField messageCardField = MessageCardField.newBuilder()
                .isShort(true)
                .text(MessageCardLarkMd.newBuilder().content(content).build())
                .build();
            messageCardFields[i] = messageCardField;
        }
        MessageCardDiv element = MessageCardDiv.newBuilder()
            .fields(messageCardFields)
            .build();

        MessageCard card = MessageCard.newBuilder().config(config)
            .header(header)
            .elements(new MessageCardElement[]{element})
            .build();

        FeishuCardMessageReq req = FeishuCardMessageReq.builder()
            .msgType(MsgTypeEnum.MSG_TYPE_INTERACTIVE.getValue())
            .card(card).build();

        CreateMessageResp resp = feishuFeignClient.sendMessage(token, req);
        Assertions.assertEquals(0, resp.getCode());
    }
}
