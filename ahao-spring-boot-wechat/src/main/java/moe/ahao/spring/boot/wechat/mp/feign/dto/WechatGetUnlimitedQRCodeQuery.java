package moe.ahao.spring.boot.wechat.mp.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 获取不限制的小程序码: https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
 */
@Data
public class WechatGetUnlimitedQRCodeQuery {
    private String scene;
    private String page;
    @JsonProperty("check_path")
    private Boolean checkPath = true;
    @JsonProperty("env_version")
    private String envVersion = EnvVersion.RELEASE.getCode();
    private Integer width = 430;
    @JsonProperty("auto_color")
    private Boolean autoColor = false;
    @JsonProperty("line_color")
    private RGB lineColor = new RGB(0, 0, 0);
    @JsonProperty("is_hyaline")
    private Boolean isHyaline = false;

    @Getter
    @AllArgsConstructor
    public enum EnvVersion {
        RELEASE("release", "正式版"),
        TRIAL("trial", "体验版"),
        DEVELOP("develop", "开发版"),
        ;
        private final String code;
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public static class RGB {
        private Integer r;
        private Integer g;
        private Integer b;
    }

}
