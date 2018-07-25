package com.ahao.commons.util.io;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract class WordHelper {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WordHelper.class);

    private WordHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static void write(String text, String path) {
        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(path)) {

            String[] rows = StringUtils.split(text, "\n");

            for(String row : rows){
                XWPFParagraph p = doc.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(row);
            }
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
