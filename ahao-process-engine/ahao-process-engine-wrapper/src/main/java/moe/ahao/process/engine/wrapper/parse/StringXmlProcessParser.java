package moe.ahao.process.engine.wrapper.parse;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**
 * XML格式的流程配置解析器
 * 读取xml字符串
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class StringXmlProcessParser extends XmlProcessParser {
    @Setter
    private String xmlString;

    @Override
    protected Document getDocument() throws Exception {
        log.debug("加载流程配置, 从xml字符串加载流程配置: {}", xmlString);
        SAXReader saxReader = new SAXReader();
        try (StringReader reader = new StringReader(xmlString)) {
            Document document = saxReader.read(reader);
            return document;
        }
    }
}
