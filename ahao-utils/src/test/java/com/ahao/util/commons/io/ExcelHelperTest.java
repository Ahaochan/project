package com.ahao.util.commons.io;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Date;

import static com.ahao.util.commons.io.ExcelHelper.*;


public class ExcelHelperTest {

    @Test
    public void writeAndReadTest() {
        // 1. 初始化数据
        int row = 10, col = 20;
        String[][] writeData = new String[row][col];
        for(int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                writeData[i][j] = "data["+i+"]["+j+"]";
            }
        }

        // 2. 写入Excel
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File excelFile = new File(tmpDir, "test.xlsx");
        ExcelHelper.writeSheet(writeData, excelFile);
        Assert.assertTrue(excelFile.exists());
        Assert.assertTrue(excelFile.isFile());
        Assert.assertTrue(excelFile.length() > 10);

        // 3. 读取Excel
        String[][] readData = ExcelHelper.readSheet(excelFile);
        for(int i = 0, rowLen = readData.length; i < rowLen; i++) {
            for (int j = 0, colLen = readData[i].length; j < colLen; j++) {
                Assert.assertEquals(writeData[i][j], readData[i][j]);
            }
        }

        // 4. 删除excel
        boolean success = excelFile.delete();
        Assert.assertTrue(success);
    }

    @Test
    public void getCellValueTest() throws Exception {
        File file = new File("src/test/resources", "excel.xlsx");
        int sheetIndex = 0;

        try (Workbook workbook = WorkbookFactory.create(file)){
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if(sheet == null) {
                Assert.fail();
            }

            for (int r = sheet.getFirstRowNum(), rowCount = sheet.getLastRowNum(); r < rowCount; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                Cell cell0 = row.getCell(0);
                Assert.assertEquals(Integer.valueOf("100"), getInteger(cell0));
                Assert.assertEquals(String.valueOf("100"),  getString(cell0));
                Assert.assertEquals(Double.valueOf("100"),  getDouble(cell0));
                Assert.assertNull(getDate(cell0, "yyyy-MM-dd"));


                Cell cell1 = row.getCell(1);
                Assert.assertEquals(Integer.valueOf("100"),    getInteger(cell1));
                Assert.assertEquals(String.valueOf("100.00"),  getString(cell1));
                Assert.assertEquals(Double.valueOf("100.00"),  getDouble(cell1));
                Assert.assertNull(getDate(cell1, "yyyy-MM-dd"));

                Cell cell2 = row.getCell(2);
                Assert.assertNull(getInteger(cell2));
                Assert.assertEquals("测试数据",  getString(cell2));
                Assert.assertNull(getDouble(cell2));
                Assert.assertNull(getDate(cell2, "yyyy-MM-dd"));

                Cell cell3 = row.getCell(3);
                Date expect = DateUtils.parseDate("2019-01-01", "yyyy-MM-dd");
                Assert.assertEquals(Integer.valueOf((int) expect.getTime()), getInteger(cell3));
                Assert.assertEquals(String.valueOf(expect.getTime()),  getString(cell3));
                Assert.assertEquals(Double.valueOf((double) expect.getTime()),  getDouble(cell3));
                Assert.assertEquals(expect, getDate(cell3, "yyyy-MM-dd"));
            }
        }
    }
}