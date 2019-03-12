package com.ahao.commons.util.io;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ahaochan on 2017/11/23.
 * 简单的Excel处理工具
 */
public class ExcelHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    /**
     * 加载单个Sheet为String矩阵的形式
     * @param sheetIndex 第几个Sheet, 从0开始计数
     * @return 内容的String矩阵
     */
    public static String[][] readSheet(InputStream inputStream, int sheetIndex) {
        // 1. 创建文件对象, 同时支持Excel 2003/2007/2010
        try (Workbook workbook = WorkbookFactory.create(inputStream);){
            // 2. 获取选择的sheet
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            // 3. 获取总行数, 有n行数据则getLastRowNum()返回n-1
            int rowCount = sheet.getLastRowNum() + 1;

            // 4. 读取每行数据
            String[][] result = new String[rowCount][];
            for (int r = 0; r < rowCount; r++) {
                Row row = sheet.getRow(r);
                int cellCount = row.getLastCellNum(); //获取总列数
                // 4.1. 读取每一列, 加载为字符串
                result[r] = new String[cellCount];
                for(int c = 0; c < cellCount; c++){
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // 保证不返回null
                    cell.setCellType(CellType.STRING);
                    result[r][c] = cell.getStringCellValue();
                }
            }
            return result;
        } catch (IOException e) {
            logger.error("文件存在, 但是IO式错误: "+e.getMessage(), e);
        }
        return new String[0][0];
    }
    public static String[][] readSheet(InputStream inputStream) {
        return readSheet(inputStream, 0);
    }
    public static String[][] readSheet(File file, int sheetIndex) {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))){
            return readSheet(inputStream,sheetIndex);
        } catch (IOException e) {
            logger.error("读取excel失败", e);
        }
        return new String[0][0];
    }
    public static String[][] readSheet(File file) {
        return readSheet(file, 0);
    }
    public static String[][] readSheet(String filePath, int sheetIndex) {
        return readSheet(new File(filePath), sheetIndex);
    }
    public static String[][] readSheet(String filePath) {
        return readSheet(filePath, 0);
    }

    /**
     * 将 datas 数据源 写入 excel 中
     * @param datas 数据源集合, 一个element为一个sheet, sheet名默认为sheet+index
     * @return 写入成功返回true, 失败返回false
     */
    public static <T> boolean writeSheet(List<T[][]> datas, OutputStream os) {
        if(CollectionUtils.isEmpty(datas)) {
            return false;
        }
        // 1. 创建Excel文件, 配置默认属性
        try(Workbook workbook = new XSSFWorkbook()) {
            for (int i = 0, len = datas.size(); i < len; i++) {
                T[][] data = datas.get(i);

                // 2. 每个 T[][] 为一个 Sheet
                Sheet sheet = workbook.createSheet("sheet" + i);
                sheet.setDefaultColumnWidth(15);

                // 3. 填充数据到 Sheet
                for (int r = 0, rowLen = data.length; r < rowLen; r++) {
                    T[] row = data[r];
                    Row excelRow = sheet.createRow(r);
                    for(int c = 0, colLen = row.length; c < colLen; c++){
                        Cell cell = excelRow.createCell(c, CellType.STRING);
                        cell.setCellValue(row[c].toString());
                    }
                }
            }

            // 4. 写入文件
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            logger.error("IO错误, 写入失败", e);
            return false;
        }
        return true;
    }
    public static <T> boolean writeSheet(T[][] data, File file) {
        try (OutputStream os = new FileOutputStream(file)){
            return writeSheet(Collections.singletonList(data), os);
        } catch (FileNotFoundException e) {
            logger.error("文件不存在, 写入失败", e);
        } catch (IOException e) {
            logger.error("IO错误, 写入失败", e);
        }
        return false;

    }
    public static <T> boolean writeSheet(T[][] data, String filePath) {
        return writeSheet(data, new File(filePath));

    }
}
