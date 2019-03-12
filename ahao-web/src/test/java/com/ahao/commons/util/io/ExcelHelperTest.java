package com.ahao.commons.util.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

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
}