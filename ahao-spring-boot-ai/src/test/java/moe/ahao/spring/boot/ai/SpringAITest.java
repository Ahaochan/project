package moe.ahao.spring.boot.ai;

import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.Data;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class SpringAITest {
    @Autowired
    private ChatClient.Builder builder;

    @Test
    public void chatResponse() throws Exception {
        ChatClient chatClient = builder
            .defaultSystem("你会直接告诉答案")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        ChatResponse chatResponse = chatClient.prompt().user("你给我取一个中文名字，只用回答名字就好").call().chatResponse();
        // String content = chatClient.prompt().user("你给我取一个中文名字，只用回答名字就好").call().content();
        // System.out.println(content);
        // 打印基本信息
        System.out.println("ID: " + chatResponse.getMetadata().getId());
        System.out.println("创建时间: " + chatResponse.getMetadata().get("created"));
        System.out.println("模型:" + chatResponse.getMetadata().getModel());

        // 打印选择结果
        for (Generation generation : chatResponse.getResults()) {
            // System.out.println("\n选择索引: " + generation.index());
            System.out.println("内容: " + generation.getOutput().getText());
            System.out.println("完成原因: " + generation.getMetadata().getFinishReason());
        }

        // 打印使用统计
        System.out.println("\nToken使用统计:");
        System.out.println("提示词tokens: " + chatResponse.getMetadata().getUsage().getPromptTokens());
        System.out.println("完成词tokens: " + chatResponse.getMetadata().getUsage().getCompletionTokens());
        System.out.println("总tokens: " + chatResponse.getMetadata().getUsage().getTotalTokens());
    }

    @Test
    public void chatEntity() throws Exception {
        ChatClient chatClient = builder
            .defaultSystem("你会直接告诉答案")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        UserDTO user = chatClient.prompt().user("你给我取一个中文名字，只用回答名字就好").call().entity(new ParameterizedTypeReference<UserDTO>() {});
        System.out.println(user.getName());
    }

    @Test
    public void chatWithAssistant() throws Exception {
        ChatClient chatClient = builder
            .defaultSystem("你会直接告诉答案")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())) // 自带记忆, 不用手动传递assistant
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        String name1 = chatClient.prompt("你给我取一个中文名字，只用回答名字就好").call().content();
        String name2 = chatClient.prompt("你刚才给我取的名字是什么?").call().content();
        // 验证回答是否正确
        Assertions.assertEquals(name1, name2);
    }

    @Test
    public void stream() throws Exception {
        ChatClient chatClient = builder
            .defaultSystem("你会直接告诉答案")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        Flux<String> flux = chatClient.prompt()
            .user("讲一个100字的小故事，注意控制字数")
            .stream()
            .content();

        AtomicBoolean done = new AtomicBoolean(false);
        flux.subscribe(
            element -> System.out.print(element), // 处理每个元素
            error -> System.err.println("发生错误: " + error), // 处理错误
            () -> done.set(true) // 处理完成事件
        );
        while (!done.get()) {}
        System.out.println();
        System.out.println("处理完成");
    }

    @Test
    public void multiAnswer() throws Exception {
        int n = 3; // ds只支持输出一个答案
        ChatClient chatClient = builder
            .defaultSystem("你会直接告诉答案")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .defaultOptions(OpenAiChatOptions.builder()
                .N(n)
                .build())
            .build();

        ChatResponse chatResponse = chatClient.prompt().user("你给我取一个中文名字，只用回答名字就好").call().chatResponse();

        System.out.println("共有" + chatResponse.getResults().size() + "个答案");
        for (Generation generation : chatResponse.getResults()) {
            System.out.println("答案: " + generation.getOutput().getText());
        }
        Assertions.assertEquals(n, chatResponse.getResults().size());
    }

    @Data
    public static class UserDTO {
        private String name;
    }
}


