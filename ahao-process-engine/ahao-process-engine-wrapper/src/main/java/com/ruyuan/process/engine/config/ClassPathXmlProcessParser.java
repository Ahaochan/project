package com.ruyuan.process.engine.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class ClassPathXmlProcessParser extends XmlProcessParser {

    private final String file;

    public ClassPathXmlProcessParser(String file) {
        this.file = file.startsWith("/") ? file : "/" + file;
    }

    @Override
    protected Document getDocument() throws DocumentException {
        InputStream resourceAsStream = getClass().getResourceAsStream(file);
        SAXReader saxReader = new SAXReader();
        return saxReader.read(resourceAsStream);
    }
}
