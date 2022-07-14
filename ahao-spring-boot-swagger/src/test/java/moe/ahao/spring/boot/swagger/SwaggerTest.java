package moe.ahao.spring.boot.swagger;

import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.Swagger2MarkupExtensionRegistry;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.builder.Swagger2MarkupExtensionRegistryBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URL;
import java.nio.file.Paths;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Starter.class)
// TODO NPE
class SwaggerTest {
    @LocalServerPort
    private int port;

    private String groupName = "default";

    @Test
    void asciiDoc() throws Exception {
        MarkupLanguage language = MarkupLanguage.ASCIIDOC;
        this.generator(language);
    }

    @Test
    void markdown() throws Exception {
        MarkupLanguage language = MarkupLanguage.MARKDOWN;
        this.generator(language);
    }

    private void generator(MarkupLanguage language) throws Exception {
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
            .withMarkupLanguage(language)
            .build();

        Swagger2MarkupExtensionRegistry registry = new Swagger2MarkupExtensionRegistryBuilder()
            .build();

        String url = String.format("http://127.0.0.1:%s/v2/api-docs?group=%s", port, groupName);
        Swagger2MarkupConverter converter = Swagger2MarkupConverter.from(new URL(url))
            .withConfig(config)
            .withExtensionRegistry(registry)
            .build();

        converter.toFolder(Paths.get("src/docs/asciidoc/generated"));
        converter.toFile(Paths.get("src/docs/asciidoc/generated/all"));
    }


}
