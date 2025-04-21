package moe.ahao.spring.boot.feishu.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageCardURL {
    @JsonProperty("url")
    private String url;
    @JsonProperty("android_url")
    private String androidUrl;
    @JsonProperty("ios_url")
    private String iosUrl;
    @JsonProperty("pc_url")
    private String pcUrl;

    public MessageCardURL(String url) {
        this.url = url;
    }
}
