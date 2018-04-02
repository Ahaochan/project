package com.ahao.core.util.io;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ahaochan on 2017/8/14.
 *
 * 对IO的工具类
 */
public abstract class IOHelper {
    private static final Logger logger = LoggerFactory.getLogger(IOHelper.class);

    private IOHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    public static String toString(InputStream inputStream) {
        try {
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            logger.warn("InputStream转换String时发生IO异常:", e);
        }
        return null;
    }

    public static byte[] toByte(InputStream inputStream) {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            logger.warn("InputStream转换byte[]时发生IO异常:", e);
        }
        return null;
    }

    public static boolean writeJpg(RenderedImage image, OutputStream output) {
        try {
            return ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            logger.warn("写出图片发生IO异常:", e);
        }
        return false;
    }

    public static void close(Closeable closeable) {
        IOUtils.closeQuietly(closeable);
    }
}