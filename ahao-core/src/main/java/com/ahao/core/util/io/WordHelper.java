package com.ahao.core.util.io;

import com.ahao.core.util.lang.StringHelper;
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

            String[] rows = StringHelper.split(text, "\n");

            for(String row : rows){
                XWPFParagraph p = doc.createParagraph();
                XWPFRun r = p.createRun();
                if(row.contains("答案")){
                    r.setBold(true);
                    r.setColor("ff0000");
                }
                r.setText(row);
            }
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
