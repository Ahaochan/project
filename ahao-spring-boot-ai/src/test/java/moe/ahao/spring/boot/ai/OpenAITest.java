package moe.ahao.spring.boot.ai;

import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class OpenAITest {
    @Autowired
    private OpenAIClient openAIClient;

    @Test
    public void chat() throws Exception {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addSystemMessage("你会直接告诉答案")
            .addUserMessage("美国的首都是哪里?")
            .model(ChatModel.of("Qwen/QwQ-32B"))
            .build();

        ChatCompletion completion = openAIClient.chat().completions().create(params);

        // 打印基本信息
        System.out.println("ID: " + completion.id());
        System.out.println("创建时间: " + completion.created());
        System.out.println("模型:" + completion.model());

        // 打印选择结果
        for (ChatCompletion.Choice choice : completion.choices()) {
            System.out.println("\n选择索引: " + choice.index());
            System.out.println("内容: " + choice.message().content().get());
            System.out.println("完成原因: " + choice.finishReason().asString());
        }

        // 打印使用统计
        System.out.println("\nToken使用统计:");
        System.out.println("提示词tokens: " + completion.usage().get().promptTokens());
        System.out.println("完成词tokens: " + completion.usage().get().completionTokens());
        System.out.println("总tokens: " + completion.usage().get().totalTokens());
    }

    @Test
    public void stream() {
        // try (StreamResponse<ChatCompletionChunk> streamResponse = openAIClient.chat().completions().createStreaming(params)) {
        //     streamResponse.stream().forEach(chunk -> {
        //         System.out.println(chunk);
        //     });
        //     System.out.println("No more chunks!");
        // }
    }
}
