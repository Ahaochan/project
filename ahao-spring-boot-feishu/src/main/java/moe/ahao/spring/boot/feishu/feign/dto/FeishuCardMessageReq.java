package moe.ahao.spring.boot.feishu.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lark.oapi.card.model.MessageCard;
import com.lark.oapi.service.im.v1.enums.MsgTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeishuCardMessageReq {
    @SerializedName("msg_type")
    private String msgType;
    private MessageCard card;
}
