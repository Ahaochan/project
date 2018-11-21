package com.ahao.commons.util.io;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Ahaochan on 2017/11/23.
 * 简单的Excel处理工具
 */
public class ExcelHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    public static String[][] readSingleSheet(String path) {
        return readSingleSheet(path, 0);
    }

    /**
     * 加载单个Sheet为String矩阵的形式
     * @param path 文件路径
     * @param sheetIndex 第几个Sheet, 从0开始计数
     * @return 内容的String矩阵
     */
    public static String[][] readSingleSheet(String path, int sheetIndex) {
        try {
            //同时支持Excel 2003、2007
            File excelFile = new File(path); //创建文件对象
            Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFile)); //这种方式 Excel 2003/2007/2010 都是可以处理的
            // int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
            //遍历每一行
            String[][] result = new String[rowCount][];
            for (int r = 0; r < rowCount; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                int cellCount = row.getPhysicalNumberOfCells(); //获取总列数
                result[r] = new String[cellCount];
                for (int c = 0; c < cellCount; c++) {
                    result[r][c] = getCellValue(row, c);
                }
            }
            return result;
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            logger.warn("Excel解析失败:", e);
        }
        return null;
    }

    private static String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }


    public static void main(String[] args) {

        for(int i = 1; i <= 200; i++) {
            for (int j = 0; j <= 200; j++){
                if((6*i+17*j)==200) {
                    System.out.println(i+","+j+","+(6*i+17*j)+","+(6*i)+","+(17*j));
                }
            }
        }
    }
}