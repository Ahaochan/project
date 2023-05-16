package moe.ahao.process.engine.wrapper.parse;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * XML格式的流程配置解析器
 * 读取指定路径下的XML文件
 */
@Slf4j
public class ClassPathXmlProcessParser extends XmlProcessParser {
    private final String file;

    public ClassPathXmlProcessParser(String file) {
        this.file = file.startsWith("/") ? file : "/" + file;
    }

    @Override
    protected Document getDocument() throws Exception {
        log.debug("加载流程配置, 从文件路径加载流程配置: {}", file);
        try (InputStream resourceAsStream = this.getClass().getResourceAsStream(file);) {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(resourceAsStream);
            return document;
        } catch (IOException e) {
            throw new DocumentException(e);
        }
    }
}
