package com.ahao.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ahaochan on 2017/8/14.
 */
public class IOHelper {
    private static final Logger logger = LoggerFactory.getLogger(IOHelper.class);

    public static byte[] toByte(InputStream inputStream) {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO异常" + e.getMessage());
        }
        return null;
    }
}
