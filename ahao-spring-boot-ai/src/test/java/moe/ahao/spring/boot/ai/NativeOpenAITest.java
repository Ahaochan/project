package moe.ahao.spring.boot.ai;

import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class NativeOpenAITest {
    private static final ChatModel model = ChatModel.of("Qwen/QwQ-32B");
    @Autowired
    private OpenAIClient openAIClient;

    @Test
    public void chat() throws Exception {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addSystemMessage("你会直接告诉答案")
            .addUserMessage("你给我取一个中文名字，只用回答名字就好")
            .model(model)
            .temperature(0.7)           // 增加温度会使输出更随机
            .maxCompletionTokens(2000)  // 模型输出的最大的 token 数
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
    public void chatWithAssistant() throws Exception {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addSystemMessage("你会直接告诉答案")
            .addUserMessage("你给我取一个中文名字，只用回答名字就好")
            .addMessage(ChatCompletionAssistantMessageParam.builder().content("小明").build())
            .addUserMessage("你刚才给我取的名字是【小明】吗？只回答【true】或者【false】")
            .model(model)
            .build();
        ChatCompletion completion = openAIClient.chat().completions().create(params);
        String answer = completion.choices().get(0).message().content().get();

        // 验证回答是否正确
        Assertions.assertTrue(answer.contains("true"));
    }

    @Test
    public void stream() throws Exception {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addUserMessage("讲一个100字的小故事，注意控制字数")
            .model(model)
            .build();

        StringBuilder sb = new StringBuilder();
        AtomicInteger atomicInteger = new AtomicInteger(0);

        try (StreamResponse<ChatCompletionChunk> streamResponse = openAIClient.chat().completions().createStreaming(params)) {
            System.out.println("开始接收流式响应：");
            streamResponse.stream().forEach(chunk -> {
                int count = atomicInteger.incrementAndGet();
                chunk.choices().get(0).delta().content().ifPresent(c -> {
                    sb.append(c);
                    System.out.println("第" + count + "个chunk:" + c);
                });
            });
            System.out.println("流式响应结束");
        }
        System.out.println(sb);
        Assertions.assertTrue(atomicInteger.get() > 0);
    }

    @Test
    public void multiAnswer() throws Exception {
        int n = 3;
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addSystemMessage("你会直接告诉答案")
            .addUserMessage("你给我取一个中文名字，只用回答名字就好")
            .model(model)
            .n(n)
            .build();
        ChatCompletion completion = openAIClient.chat().completions().create(params);

        System.out.println("共有" + completion.choices().size() + "个答案");
        for (ChatCompletion.Choice choice : completion.choices()) {
            System.out.println("答案: " + choice.message().content().get().replace("\n", ""));
        }
        Assertions.assertEquals(n, completion.choices().size());
    }
}
