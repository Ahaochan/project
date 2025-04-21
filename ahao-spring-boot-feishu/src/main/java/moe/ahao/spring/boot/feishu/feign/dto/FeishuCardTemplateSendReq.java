package moe.ahao.spring.boot.feishu.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lark.oapi.card.model.MessageCard;
import com.lark.oapi.service.performance.v2.model.Template;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

@Data
@NoArgsConstructor
public class FeishuCardTemplateSendReq<T> {
    @JsonProperty("msg_type")
    private String msgType;
    private Card<T> card;

    public FeishuCardTemplateSendReq(Template<T> data) {
        this.msgType = "interactive";
        this.card = new Card<>(data);
    }

    @Data
    @NoArgsConstructor
    public static class Card<T> {
        private String type;
        private Template<T> data;

        public Card(Template<T> data) {
            this.type = "template";
            this.data = data;
        }
    }

    @Data
    public static class Template<T> {
        @JsonProperty("template_id")
        private String templateId;
        @JsonProperty("template_version_name")
        private String templateVersionName;
        @JsonProperty("template_variable")
        private T templateVariable;
    }
}
