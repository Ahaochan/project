package com.ahao.spring.boot.file.download.controller;

import com.ahao.spring.boot.file.download.entity.User;
import com.ahao.util.commons.lang.StringHelper;
import com.ahao.util.web.RequestHelper;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class ExcelDownloadController {
    /**
     * 导出 Excel 2003 XLS 文件, 实现类是 {@link org.apache.poi.hssf.usermodel.HSSFWorkbook}
     */
    @GetMapping("/download.xls")
    public View xls() {
        String filename = "中文+- 测试 download.xls";
        List<User> users = User.list(100);

        return new AbstractXlsView() {
            @Override
            protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 1. 设置文件名和响应头
                String safeFilename = RequestHelper.safetyFilename(filename);
                response.setHeader("Content-Disposition", "attachment; filename=\""+safeFilename+"\"");

                // 2. 写入数据
                write(workbook, users);
            }
        };
    }

    /**
     * 导出 Excel 2007 XLSX 文件, 实现类是 {@link org.apache.poi.xssf.usermodel.XSSFWorkbook}
     */
    @GetMapping("/download.xlsx")
    public View xlsx() {
        String filename = "中文+- 测试 download.xlsx";
        List<User> users = User.list(110);

        return new AbstractXlsxView() {
            @Override
            protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 1. 设置文件名和响应头
                String safeFilename = RequestHelper.safetyFilename(filename);
                response.setHeader("Content-Disposition", "attachment; filename=\""+safeFilename+"\"");

                // 2. 写入数据
                write(workbook, users);
            }
        };
    }

    /**
     * 导出 Excel 2007 XLSX 文件, 实现类是 {@link org.apache.poi.xssf.streaming.SXSSFWorkbook}
     * 默认 100 行存在内存中, 其余写入硬盘
     */
    @GetMapping("/download_stream.xlsx")
    public View xlsxStream() {
        String filename = "中文+- 测试 download_stream.xlsx";
        List<User> users = User.list(120);

        return new AbstractXlsxStreamingView() {
            @Override
            protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
                // 1. 设置文件名和响应头
                String safeFilename = RequestHelper.safetyFilename(filename);
                response.setHeader("Content-Disposition", "attachment; filename=\""+safeFilename+"\"");

                // 2. 写入数据
                write(workbook, users);
            }
        };
    }

    private void write(Workbook workbook, List<User> users) {
        // 1. 创建 excel 配置
        Sheet sheet = workbook.createSheet();
        CellStyle cellStyle = workbook.createCellStyle();

        // 2. 写入标题行
        Row header = sheet.createRow(0);
        String[] TITLE = {"id", "name", "address", "age"};
        for (int col = 0, len = TITLE.length; col < len; col++) {
            Cell cell = header.createCell(col);
            cell.setCellValue(TITLE[col]);
            cell.setCellStyle(cellStyle);
        }

        // 3. 写入数据
        int rowCount = 1;
        for (User user : users) {
            Row userRow = sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(StringHelper.null2Empty(user.getId()));
            userRow.createCell(1).setCellValue(StringHelper.null2Empty(user.getName()));
            userRow.createCell(2).setCellValue(StringHelper.null2Empty(user.getAddress()));
            userRow.createCell(3).setCellValue(StringHelper.null2Empty(user.getAge()));
        }
    }
}