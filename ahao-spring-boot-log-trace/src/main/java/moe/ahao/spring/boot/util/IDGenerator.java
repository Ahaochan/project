package moe.ahao.spring.boot.util;

import com.ahao.util.commons.lang.RandomHelper;
import com.ahao.util.commons.lang.time.DateHelper;

public class IDGenerator {
    public static String generateID(String prefix) {
        return prefix + DateHelper.getNow("yyyyMMddHHmmssSSS") + RandomHelper.getString(6, RandomHelper.DIST_NUMBER);
    }
}
