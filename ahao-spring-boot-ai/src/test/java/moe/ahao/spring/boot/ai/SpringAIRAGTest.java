package moe.ahao.spring.boot.ai;

import io.micrometer.observation.ObservationRegistry;
import lombok.Data;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.observation.DefaultAdvisorObservationConvention;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.observation.DefaultVectorStoreObservationConvention;
import org.springframework.ai.vectorstore.observation.VectorStoreObservationConvention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
public class SpringAIRAGTest {
    @Autowired
    private ChatClient.Builder builder;
    @Autowired
    private OpenAiEmbeddingModel openAiEmbeddingModel;

    @Test
    public void testQuestionAnswerAdvisorSuccess() throws Exception {
        ChatClient chatClient = builder
            .defaultSystem("你会从知识库里获取答案，当知识库不存在的时候直接拒绝回复")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        String content = chatClient.prompt()
            .advisors(new QuestionAnswerAdvisor(this.initVectorStore(), this.initSearchRequest()))
            .user("给我推荐一款手机，并告诉我推荐它的理由")
            .call()
            .content();
        System.out.println("回答内容: " + content);
    }

    @Test
    public void testQuestionAnswerAdvisorNotFound() throws Exception {
        ChatClient chatClient = builder
            .defaultSystem("你会从知识库里获取答案，当知识库不存在的时候直接拒绝回复")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        String content = chatClient.prompt()
            .advisors(new QuestionAnswerAdvisor(this.initVectorStore(), this.initSearchRequest()))
            .user("给我推荐一款电视，并告诉我推荐它的理由")
            .call()
            .content();
        System.out.println("回答内容: " + content);
    }
    @Test
    public void testRetrievalAugmentationAdvisor() throws Exception{
        ChatClient chatClient = builder
            .defaultSystem("你会从知识库里获取答案，当知识库不存在的时候直接拒绝回复")
            .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .build();

        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
            .queryTransformers(RewriteQueryTransformer.builder()
                .chatClientBuilder(builder.build().mutate())
                .build())
            .documentRetriever(VectorStoreDocumentRetriever.builder()
                .similarityThreshold(0.50)
                .vectorStore(this.initVectorStore())
                .build())
            .build();

        String content = chatClient.prompt()
            .advisors(retrievalAugmentationAdvisor)
            .user("给我推荐一款手机，并告诉我推荐它的理由")
            .call()
            .content();
        System.out.println("回答内容: " + content);
    }

    private VectorStore initVectorStore() {
        VectorStore vectorStore = SimpleVectorStore.builder(openAiEmbeddingModel)
            .batchingStrategy(new TokenCountBatchingStrategy()) // 当累积的 token 数量达到一定的阈值时，就会触发批量处理
            .observationRegistry(ObservationRegistry.create())  // 监控, 用于收集和报告关于向量存储操作的观测数据
            .customObservationConvention(new DefaultVectorStoreObservationConvention()) // 定义了如何命名和标记观测数据
            .build();

        List<Document> documents = List.of(
            new Document("小米手机15", Map.of("source", "小米公司", "type", "手机")),
            new Document("红米手机k80", Map.of("source", "小米公司", "type", "手机")),
            new Document("华为手机Mate60", Map.of("source", "华为公司", "type", "手机")),
            new Document("华为手机Nova11", Map.of("source", "华为公司", "type", "手机")),
            new Document("小米电视", Map.of("source", "华为公司", "type", "电视")),
            new Document("华为电视", Map.of("source", "华为公司", "type", "电视"))
        );
        vectorStore.add(documents);
        return vectorStore;
    }

    public SearchRequest initSearchRequest() {
        return SearchRequest.builder()
            .filterExpression("source == '小米公司'") // 仅适用于Document的元数据键值对
            // .query("")
            .topK(3) // 设置返回的相似度最高的文档数量
            .similarityThreshold(0.5) // 设置相似度阈值，用于过滤掉相似度低于阈值的文档
            .build();
    }
}
