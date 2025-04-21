package moe.ahao.spring.boot.feishu.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "notify.feishu") // 指定配置前缀
public class NotifyFeishuProperties {
    private Map<String, FeishuConfig> scene = new HashMap<>();

    public FeishuConfig getScene(SceneEnum sceneEnum) {
        return scene.get(sceneEnum.name);
    }

    @Getter
    @AllArgsConstructor
    public enum SceneEnum {
        SEND_ALARM("send-alarm");
        public final String name;
    }

    @Data
    public static class FeishuConfig {
        private String token;
        private String templateId;
        private String version;
    }
}
