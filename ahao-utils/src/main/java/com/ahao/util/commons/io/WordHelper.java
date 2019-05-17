package com.ahao.util.commons.io;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;

public class WordHelper {
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
