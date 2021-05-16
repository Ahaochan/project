package moe.ahao.spring.boot.file.download.view;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * copy from {@link org.springframework.web.servlet.view.document.AbstractXlsView}
 */
public abstract class AbstractDocxView extends AbstractView {

    public AbstractDocxView() {
        setContentType("application/msword");
    }


    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. 创建一个文档
        XWPFDocument document = createWorkbook(model, request);

        // 2. 写入数据
        buildWordDocument(model, document, request, response);

        // 3. 设置 content type.
        response.setContentType(getContentType());

        // 4. 输出到 Response
        renderWorkbook(document, response);
    }

    protected XWPFDocument createWorkbook(Map<String, Object> model, HttpServletRequest request) {
        return new XWPFDocument();
    }


    protected void renderWorkbook(XWPFDocument document, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        document.write(out);
        document.close();
    }

    protected abstract void buildWordDocument(
            Map<String, Object> model, XWPFDocument document, HttpServletRequest request, HttpServletResponse response)
            throws Exception;
}
