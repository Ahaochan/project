package com.ruyuan.process.engine.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.StringReader;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class StringXmlProcessParser extends XmlProcessParser {

    private String xmlConfig;

    public StringXmlProcessParser(String xmlConfig) {
        this.xmlConfig = xmlConfig;
    }

    @Override
    protected Document getDocument() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        StringReader reader = new StringReader(xmlConfig);
        return saxReader.read(reader);
    }
}
