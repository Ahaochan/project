package moe.ahao.spring.boot.file.download.controller;

import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import moe.ahao.spring.boot.file.download.entity.User;
import moe.ahao.util.web.RequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractPdfStamperView;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class PdfDownloadController {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 下载 PDF 文件
     */
    @GetMapping("/download.pdf")
    public View pdf() {
        String filename = "中文+- 测试 download.pdf";
        List<User> users = User.list(100);

        return new AbstractPdfView() {
            @Override
            protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request) throws DocumentException {
                super.prepareWriter(model, writer, request);
                writer.setPdfVersion(PdfWriter.VERSION_1_4); // 设置 PDF 版本
                writer.setEncryption("userPassword".getBytes(StandardCharsets.UTF_8), "ownerPassword".getBytes(StandardCharsets.UTF_8), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128); // 设置密码
            }

            @Override
            protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
                document.addTitle("Hello Title");
                document.addSubject("Hello Subject");
                document.addKeywords("Hello Keywords");
                document.addAuthor("Hello Author");
                document.addCreator("Hello Creator");
                document.addProducer();
                document.addCreationDate();
                Rectangle pageSize = new Rectangle(PageSize.A4.rotate()); // 设置页面 A4 大小
                pageSize.setBackgroundColor(Color.LIGHT_GRAY);            // 设置页面背景色灰色
                document.setPageSize(pageSize);
                document.setMargins(10, 20, 30, 40); // 设置页边距
            }

            @Override
            protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 1. 设置文件名和响应头
                String safeFilename = RequestHelper.safetyFilename(filename);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + safeFilename + "\"");

                // 2. 设置字体, 默认的不能显示中文
                BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                Font chineseFont = new Font(baseFont, 14, Font.NORMAL);

                // 2. 写入数据
                for (User user : users) {
                    document.add(new Paragraph(user.toString(), chineseFont));
                }
                document.newPage();
                document.add(new Paragraph("总数:" + users.size(), chineseFont));
            }
        };
    }

    /**
     * 通过 PDF 模版, 生成动态的 PDF
     * @see <a href="https://helpx.adobe.com/cn/acrobat/using/pdf-form-field-basics.html">PDF 表单域基础知识</a>
     */
    @GetMapping("/download_stamper.pdf")
    public View pdfStamper(@RequestParam(defaultValue = "name") String name,@RequestParam(defaultValue = "addr") String addr) {
        String filename = "中文+- 测试 download_stamper.pdf";
        return new AbstractPdfStamperView() {
            // 重写避免 applicationContext 为 null
            protected PdfReader readPdfResource() throws IOException {
                // return new PdfReader(obtainApplicationContext().getResource(url).getInputStream());
                return new PdfReader(applicationContext.getResource("classpath:/template.pdf").getInputStream());
            }

            @Override
            protected void mergePdfDocument(Map<String, Object> model, PdfStamper stamper, HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 1. 设置文件名和响应头
                String safeFilename = RequestHelper.safetyFilename(filename);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + safeFilename + "\"");

                // 2. 写入数据
                AcroFields form = stamper.getAcroFields();
                form.setField("name", name);
                form.setField("addr", addr);

                stamper.setFormFlattening(true);
                stamper.setFreeTextFlattening(true);
            }
        };
    }
}
