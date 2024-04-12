package moe.ahao.unidbg.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class TempFileUtils {
    public static File getTempFile(String classPathFileName) throws IOException {
        File soLibFile = new File(System.getProperty("java.io.tmpdir"), classPathFileName);
        if (!soLibFile.exists()) {
            FileUtils.copyInputStreamToFile(new ClassPathResource(classPathFileName).getInputStream(), soLibFile);
        }
        return soLibFile;
    }
}
