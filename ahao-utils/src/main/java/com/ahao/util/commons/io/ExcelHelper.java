package com.ahao.util.commons.io;

import com.ahao.util.commons.lang.StringHelper;
import com.ahao.util.commons.lang.math.NumberHelper;
import com.ahao.util.commons.lang.time.DateHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahaochan on 2017/11/23.
 * 简单的Excel处理工具
 * @see <a href="http://poi.apache.org/components/spreadsheet/quick-guide.html#CellContents">CellContents</a>
 */
public class ExcelHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    /**
     * 加载单个Sheet为String矩阵的形式, InputStream 会消耗更多内存
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
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // 保证不返回null、
                    result[r][c] = getString(cell);
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

    public static String getString(Cell cell) {
        if (cell == null) {
            return "";
        }
        String result;

        // 如果是公式则获取缓存值, https://stackoverflow.com/a/7609587
        DataFormatter dataFormatter = new DataFormatter();
        CellType cellType = cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        switch (cellType) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    result = date == null ? "" : DateHelper.getString(date, DateHelper.yyyyMMdd_hhmmssSSS);
                } else {
                    result = dataFormatter.formatCellValue(cell);
                }
                break;
            default:      result = dataFormatter.formatCellValue(cell); break;
        }

        return StringHelper.null2Empty(result);
    }
    public static Date getDate(Cell cell, String dateFormat) {
        if (cell == null) {
            return null;
        }
        Date result;

        // 如果是公式则获取缓存值, https://stackoverflow.com/a/7609587
        CellType cellType = cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        switch (cellType) {
            case STRING:
                String string = cell.getRichStringCellValue().getString();
                result = DateHelper.getDate(string, dateFormat);
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    result = new Date((long) cell.getNumericCellValue());
                }
                break;
            default: result = null;
        }
        return result;
    }
    public static Integer getInteger(Cell cell) {
        if (cell == null) {
            return null;
        }
        Integer result;
        // 如果是公式则获取缓存值, https://stackoverflow.com/a/7609587
        CellType cellType = cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        switch (cellType) {
            case STRING:
                String string = cell.getRichStringCellValue().getString();
                result = NumberHelper.isNumber(string) ? Integer.valueOf(string) : null;
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    result = date != null ? (int) date.getTime() : null;
                } else {
                    result = (int) cell.getNumericCellValue();
                }
                break;
            case BOOLEAN: result = cell.getBooleanCellValue() ? 1 : 0; break;
            default:      result = null; break;
        }

        return result;
    }
    public static Double getDouble(Cell cell) {
        if (cell == null) {
            return null;
        }
        Double result;
        // 如果是公式则获取缓存值, https://stackoverflow.com/a/7609587
        CellType cellType = cell.getCellType() == CellType.FORMULA ? cell.getCachedFormulaResultType() : cell.getCellType();
        switch (cellType) {
            case STRING:
                String string = cell.getRichStringCellValue().getString();
                result = NumberHelper.isNumber(string) ? Double.valueOf(string) : null;
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    result = date != null ? (double) date.getTime() : null;
                } else {
                    result = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN: result = cell.getBooleanCellValue() ? 1d : 0d; break;
            default:      result = null; break;
        }
        return result;
    }

    public static boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(), len = row.getLastCellNum(); i < len; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }
}
