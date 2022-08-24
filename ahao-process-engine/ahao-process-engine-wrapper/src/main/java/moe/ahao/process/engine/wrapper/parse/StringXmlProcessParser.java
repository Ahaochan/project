package moe.ahao.process.engine.wrapper.parse;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**
 * XML格式的流程配置解析器
 * 读取xml字符串
 */
@Slf4j
public class StringXmlProcessParser extends XmlProcessParser {
    private final String xmlConfig;
    public StringXmlProcessParser(String xmlConfig) {
        this.xmlConfig = xmlConfig;
    }

    @Override
    protected Document getDocument() throws DocumentException {
        log.debug("加载流程配置, 从xml字符串加载流程配置: {}", xmlConfig);
        SAXReader saxReader = new SAXReader();
        try (StringReader reader = new StringReader(xmlConfig)) {
            Document document = saxReader.read(reader);
            return document;
        }
    }
}
