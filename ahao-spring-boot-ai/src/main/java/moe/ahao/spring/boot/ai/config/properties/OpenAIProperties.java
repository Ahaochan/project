package moe.ahao.spring.boot.ai.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.ai.openai")
public class OpenAIProperties {
    /**
     * 基础URL
     */
    private String baseUrl;
    /**
     * API密钥
     */
    private String apiKey;
    /**
     * 组织ID
     */
    private String organization;
    /**
     * 项目ID
     */
    private String project;
}
