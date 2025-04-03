package moe.ahao.spring.boot.ai.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import moe.ahao.spring.boot.ai.config.properties.OpenAIProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenAIConfig {

    @Bean(destroyMethod = "close")
    public OpenAIClient client(OpenAIProperties geminiProperties) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
            .baseUrl(geminiProperties.getBaseUrl())
            .apiKey(geminiProperties.getApiKey())
            .organization(geminiProperties.getOrganization())
            .project(geminiProperties.getProject())
            .build();
        return client;
    }
}
