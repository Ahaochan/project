package com.ahao.util.commons.io;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ahaochan on 2017/11/25.
 * 文件操作工具类
 */
public class FileHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

    public static String readString(String path){
        try {
            return FileUtils.readFileToString(new File(path), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("读取文件"+path+"错误:", e);
        }
        return null;
    }
}