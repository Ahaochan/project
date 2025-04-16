package moe.ahao.spring.boot.ai.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.ai.autoconfigure.openai.OpenAiConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenAIConfig {

    @Bean(destroyMethod = "close")
    public OpenAIClient client(OpenAiConnectionProperties properties) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
            .baseUrl(properties.getBaseUrl())
            .apiKey(properties.getApiKey())
            .organization(properties.getOrganizationId())
            .project(properties.getProjectId())
            .build();
        return client;
    }
}
