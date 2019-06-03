package com.ahao.spring.boot.file.download.controller;

import com.ahao.spring.boot.file.download.entity.User;
import com.ahao.spring.boot.file.download.view.AbstractDocxView;
import com.ahao.util.web.RequestHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class WordDownloadController {
    /**
     * 导出 Word 2007 docx 文件, 实现类是 {@link org.apache.poi.xwpf.usermodel.XWPFDocument}
     */
    @GetMapping("/download.docx")
    public View docx() {
        String filename = "中文+- 测试 download.docx";
        List<User> users = User.list(100);

        return new AbstractDocxView() {
            @Override
            protected void buildWordDocument(Map<String, Object> model, XWPFDocument document, HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 1. 设置文件名和响应头
                String safeFilename = RequestHelper.safetyFilename(filename);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + safeFilename + "\"");

                // 2. 写入数据
                write(document, users);
            }
        };
    }


    private void write(XWPFDocument workbook, List<User> users) {
        // 1. 创建 excel 配置
        for (User user : users) {
            XWPFParagraph paragraph = workbook.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(user.toString());
        }
    }
}